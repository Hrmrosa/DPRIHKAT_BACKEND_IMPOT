/**
 * Script pour la gestion du dashboard en temps réel
 * Utilise SockJS et STOMP pour les communications WebSocket
 */

// Variables globales
let stompClient = null;
let dashboardData = {};
let chartInstances = {};

// Initialisation de la connexion WebSocket
function connectWebSocket() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    
    stompClient.connect({}, function(frame) {
        console.log('Connecté au serveur WebSocket: ' + frame);
        
        // S'abonner au topic des mises à jour du dashboard
        stompClient.subscribe('/topic/dashboard', function(response) {
            const data = JSON.parse(response.body);
            console.log('Mise à jour du dashboard reçue:', data);
            dashboardData = data;
            updateDashboardUI(data);
        });
        
        // S'abonner aux mises à jour spécifiques
        stompClient.subscribe('/topic/updates/contribuable', function(response) {
            const data = JSON.parse(response.body);
            console.log('Mise à jour de contribuable reçue:', data);
            updateContribuableSection(data);
        });
        
        stompClient.subscribe('/topic/updates/taxation', function(response) {
            const data = JSON.parse(response.body);
            console.log('Mise à jour de taxation reçue:', data);
            updateTaxationSection(data);
        });
        
        stompClient.subscribe('/topic/updates/paiement', function(response) {
            const data = JSON.parse(response.body);
            console.log('Mise à jour de paiement reçue:', data);
            updatePaiementSection(data);
        });
        
        // Demander une mise à jour initiale
        requestDashboardUpdate();
    }, function(error) {
        console.error('Erreur de connexion WebSocket:', error);
        // Tentative de reconnexion après 5 secondes
        setTimeout(connectWebSocket, 5000);
    });
}

// Demander une mise à jour du dashboard
function requestDashboardUpdate() {
    if (stompClient && stompClient.connected) {
        stompClient.send("/app/dashboard/refresh", {}, JSON.stringify({}));
    } else {
        console.warn('Client WebSocket non connecté, impossible de demander une mise à jour');
    }
}

// Mettre à jour l'interface utilisateur du dashboard
function updateDashboardUI(data) {
    // Mettre à jour les statistiques générales
    updateStatistiques(data.statistiques);
    
    // Mettre à jour les graphiques
    updateGraphiques(data.graphiques);
    
    // Mettre à jour les listes de données récentes
    updateRecentData(data);
}

// Mettre à jour la section des statistiques
function updateStatistiques(stats) {
    if (!stats) return;
    
    // Mettre à jour les compteurs
    document.getElementById('total-contribuables').textContent = stats.total_contribuables || 0;
    document.getElementById('total-proprietes').textContent = stats.total_proprietes || 0;
    document.getElementById('total-vehicules').textContent = stats.total_vehicules || 0;
    document.getElementById('total-utilisateurs').textContent = stats.total_utilisateurs || 0;
    
    // Mettre à jour les montants
    document.getElementById('montant-taxations').textContent = formatMontant(stats.montant_total_taxations);
    document.getElementById('montant-paiements').textContent = formatMontant(stats.montant_total_paiements);
    document.getElementById('taux-recouvrement').textContent = (stats.taux_recouvrement || 0).toFixed(2) + '%';
}

// Mettre à jour les graphiques
function updateGraphiques(graphiques) {
    if (!graphiques) return;
    
    // Mettre à jour le graphique des taxations par mois
    updateChartData('taxations-chart', graphiques.taxations_par_mois, 'Taxations par mois');
    
    // Mettre à jour le graphique des paiements par mois
    updateChartData('paiements-chart', graphiques.paiements_par_mois, 'Paiements par mois');
    
    // Mettre à jour le graphique des propriétés par type
    updatePieChartData('proprietes-pie', graphiques.proprietes_par_type, 'Propriétés par type');
    
    // Mettre à jour le graphique des contribuables par type
    updatePieChartData('contribuables-pie', graphiques.contribuables_par_type, 'Contribuables par type');
}

// Mettre à jour un graphique en ligne
function updateChartData(chartId, data, label) {
    const ctx = document.getElementById(chartId);
    if (!ctx) return;
    
    // Préparer les données pour le graphique
    const labels = data ? data.map(item => item.mois) : [];
    const values = data ? data.map(item => item.montant) : [];
    
    // Si le graphique existe déjà, mettre à jour les données
    if (chartInstances[chartId]) {
        chartInstances[chartId].data.labels = labels;
        chartInstances[chartId].data.datasets[0].data = values;
        chartInstances[chartId].update();
    } else {
        // Sinon, créer un nouveau graphique
        chartInstances[chartId] = new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: label,
                    data: values,
                    backgroundColor: 'rgba(54, 162, 235, 0.2)',
                    borderColor: 'rgba(54, 162, 235, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    }
}

// Mettre à jour un graphique en camembert
function updatePieChartData(chartId, data, label) {
    const ctx = document.getElementById(chartId);
    if (!ctx) return;
    
    // Préparer les données pour le graphique
    const labels = data ? data.map(item => item.type) : [];
    const values = data ? data.map(item => item.count) : [];
    
    // Générer des couleurs pour chaque segment
    const colors = generateColors(labels.length);
    
    // Si le graphique existe déjà, mettre à jour les données
    if (chartInstances[chartId]) {
        chartInstances[chartId].data.labels = labels;
        chartInstances[chartId].data.datasets[0].data = values;
        chartInstances[chartId].data.datasets[0].backgroundColor = colors;
        chartInstances[chartId].update();
    } else {
        // Sinon, créer un nouveau graphique
        chartInstances[chartId] = new Chart(ctx, {
            type: 'pie',
            data: {
                labels: labels,
                datasets: [{
                    label: label,
                    data: values,
                    backgroundColor: colors,
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true
            }
        });
    }
}

// Générer des couleurs aléatoires pour les graphiques
function generateColors(count) {
    const colors = [];
    for (let i = 0; i < count; i++) {
        const hue = (i * 137) % 360; // Répartition uniforme des teintes
        colors.push(`hsl(${hue}, 70%, 60%)`);
    }
    return colors;
}

// Mettre à jour les données récentes
function updateRecentData(data) {
    // Mettre à jour la liste des taxations récentes
    updateTableData('taxations-recentes-table', data.taxations_recentes, [
        { key: 'dateTaxation', label: 'Date' },
        { key: 'contribuable', label: 'Contribuable' },
        { key: 'montant', label: 'Montant', format: formatMontant }
    ]);
    
    // Mettre à jour la liste des paiements récents
    updateTableData('paiements-recents-table', data.paiements_recents, [
        { key: 'datePaiement', label: 'Date' },
        { key: 'contribuable', label: 'Contribuable' },
        { key: 'montant', label: 'Montant', format: formatMontant },
        { key: 'reference', label: 'Référence' }
    ]);
    
    // Mettre à jour la liste des derniers utilisateurs
    updateTableData('derniers-utilisateurs-table', data.derniers_utilisateurs, [
        { key: 'nomComplet', label: 'Nom' },
        { key: 'login', label: 'Login' },
        { key: 'role', label: 'Rôle' }
    ]);
}

// Mettre à jour une table de données
function updateTableData(tableId, data, columns) {
    const table = document.getElementById(tableId);
    if (!table || !data) return;
    
    // Vider le tableau
    const tbody = table.querySelector('tbody');
    if (!tbody) return;
    tbody.innerHTML = '';
    
    // Ajouter les nouvelles lignes
    data.forEach(item => {
        const row = document.createElement('tr');
        
        columns.forEach(column => {
            const cell = document.createElement('td');
            const value = item[column.key];
            cell.textContent = column.format ? column.format(value) : (value || '-');
            row.appendChild(cell);
        });
        
        tbody.appendChild(row);
    });
}

// Formater un montant en devise
function formatMontant(montant) {
    if (montant === undefined || montant === null) return '-';
    return new Intl.NumberFormat('fr-CD', {
        style: 'currency',
        currency: 'CDF'
    }).format(montant);
}

// Mettre à jour la section des contribuables
function updateContribuableSection(data) {
    // Demander une mise à jour complète du dashboard
    requestDashboardUpdate();
}

// Mettre à jour la section des taxations
function updateTaxationSection(data) {
    // Demander une mise à jour complète du dashboard
    requestDashboardUpdate();
}

// Mettre à jour la section des paiements
function updatePaiementSection(data) {
    // Demander une mise à jour complète du dashboard
    requestDashboardUpdate();
}

// Initialiser le dashboard en temps réel
document.addEventListener('DOMContentLoaded', function() {
    console.log('Initialisation du dashboard en temps réel');
    connectWebSocket();
    
    // Configurer le rafraîchissement automatique toutes les 30 secondes
    setInterval(requestDashboardUpdate, 30000);
});
