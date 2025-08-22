# Haut-Katanga Tax API

This is a Spring Boot application that provides a REST API for managing various provincial taxes in Haut-Katanga, Democratic Republic of Congo. The API supports tele-declaration, agent taxation, geolocation (PostGIS), penalty management, payments, printed item issuance, fiscal control, and user/role management according to local fiscal laws and regulations.

## Features

- **Tax Management**:
  - Impôt Foncier (IF)
  - Impôt sur les Revenus Locatifs (IRL)
  - Retenue sur les Loyers (RL)
  - Impôt Réel sur les Véhicules (IRV)
  - Impôt sur les Concessions Minières (ICM)

- **Tele-declaration**: Contributors can declare their taxes online between January 2nd and February 1st
- **Agent Taxation**: Tax agents can create tax assessments
- **Geolocation**: PostGIS integration for property location tracking (mandatory for IF, IRL, RL, ICM, IRV)
- **Penalty Management**: Automated penalty calculation (2% per month for late payments, 25% for non-declaration)
- **Recovery Procedures**: Multi-stage debt recovery process
- **Payment Processing**: Payment tracking and reconciliation with bank slips
- **Printed Items**: Integration with external stock API for vignettes, certificates, and plates
- **Fiscal Control**: Monitoring and control mechanisms
- **User/Role Management**: Role-based access control with comprehensive role hierarchy
- **Audit Logging**: Complete history of all actions in the system
- **Role-based Reports**: Custom reports for each user role
- **Role-based Dashboards**: Custom dashboards for each user role
- **JWT Authentication**: Secure authentication with refresh token support

## User Roles

The system implements a comprehensive role-based access control with the following roles:

- **Taxateur**: Manages contributors, properties, and taxation
- **Chef de bureau**: Views all information on contributors and taxation in their office
- **Chef de division**: Views all movements in their division
- **Directeur**: Views all movements across the organization
- **Chef de cellules**: Views taxation and contributors in their assigned office
- **Vérificateur**: Views contributors and taxation that have been processed
- **Controlleur**: Views problematic notes (incorrectly declared, missing declarations, overdue notes...)
- **Apureur**: Views taxation and validates payments in the system
- **Receveur des impôts**: Views overdue notes, payments, and files in recovery procedures
- **Admin**: Full administrative access
- **Informaticien**: Manages users
- **Contribuable**: Submits tax declarations online

## Technical Stack

- **Backend**: Spring Boot 3.3.2 with Java 17
- **Database**: PostgreSQL with PostGIS extension
- **ORM**: Hibernate Spatial for geolocation data
- **Security**: Spring Security with JWT authentication
- **API Documentation**: Springdoc OpenAPI
- **Build Tool**: Maven

## Project Structure

```
src/main/java/com/DPRIHKAT/
├── config/           # Configuration classes
├── controller/       # REST controllers
├── entity/           # JPA entities
├── repository/       # Spring Data JPA repositories
├── service/          # Business logic services
└── util/             # Utility classes
```

## API Endpoints

### Authentication Endpoints

- `POST /api/auth/login` - Authenticate user with login and password
- `POST /api/auth/changer-mot-de-passe` - Change password on first login

### Declaration Endpoints

- `POST /api/declarations/soumettre` - Submit a tax declaration online (Contribuable only)
- `POST /api/declarations/manuelle` - Record a manual declaration (Taxateur, Receveur des impôts)
- `GET /api/declarations` - List all declarations with pagination

### Plaque Management Endpoints

- `POST /api/plaques` - Create a new plate
- `POST /api/plaques/assigner` - Assign a plate to a property
- `GET /api/plaques` - List all plates with pagination
- `GET /api/plaques/disponibles` - List available plates with pagination

### Penalty Management Endpoints

- `POST /api/penalites/calculer` - Automatically calculate penalties for a late declaration
- `PUT /api/penalites/ajuster` - Manually adjust penalties (Chef de bureau, Chef de division, Directeur)

### User Management Endpoints

- `POST /api/users` - Create a new user
- `POST /api/users/bloquer` - Block a user account
- `GET /api/users` - List all users with pagination

## Security

The application implements role-based access control:

- **Contribuable** can only submit online declarations
- **Taxateur** and **Receveur des impôts** can record manual declarations
- **Chef de bureau**, **Chef de division**, and **Directeur** can adjust penalties
- **Admin** and **Directeur** can block user accounts

## Tax Rules

### Impôt Foncier (IF)

Rates are loaded from `taux_if.json`:
- Villas: 0.50-2.00 USD/m²
- Apartments: 30-150 USD
- Commercial: 5-25 USD
- Domestic: 5-20 USD (based on rank and contributor type)

### Impôt sur les Concessions Minières (ICM)

Rates are loaded from `taux_icm.json`:
- Research: 0.03-0.12 USD/ha
- Exploitation: 0.03-0.12 USD/ha

### Impôt sur les Revenus Locatifs (IRL)

- Total rate: 22% (20% advance payment, 2% balance due by February 1st of the following year)

### Retenue sur les Loyers (RL)

- Same rates as IRL

### Impôt Réel sur les Véhicules (IRV)

- Based on engine capacity/tonnage (e.g., 54 USD + 19 USD TSCR for <2.5T for individuals)

## Penalty Rules

- **Late payments**: 2% per month
- **Non-declaration**: 25% penalty

## Database Configuration

The application uses PostgreSQL with PostGIS extension for geolocation data. Configuration is in `application.properties`.

## JSON Tax Rate Files

Tax rates are loaded from JSON files in `src/main/resources`:
- `taux_if.json` - IF tax rates
- `taux_icm.json` - ICM tax rates

## Reference JSON Endpoints

Read-only endpoints loaded from `src/main/resources/communes.json` and `src/main/resources/voiture.json` (case-insensitive lookups; no database):

- Communes
  - `GET /api/ref/communes` — list communes
  - `GET /api/ref/communes/{commune}/quartiers` — list quartiers of a commune
  - `GET /api/ref/communes/{commune}/quartiers/{quartier}/avenues` — list avenues of a quartier
- Vehicles
  - `GET /api/ref/voitures/marques` — list brands
  - `GET /api/ref/voitures/marques/{marque}/models` — list models for a brand

Examples:
```bash
curl http://localhost:8080/api/ref/communes
curl http://localhost:8080/api/ref/communes/kamalondo/quartiers
curl http://localhost:8080/api/ref/communes/kamalondo/quartiers/kitumaini/avenues

curl http://localhost:8080/api/ref/voitures/marques
curl http://localhost:8080/api/ref/voitures/marques/BMW/models
```

## Response Format

All API responses follow a standardized format:

```json
{
  "success": true,
  "data": {...},
  "error": null,
  "meta": {
    "timestamp": "2025-08-20T07:52:37.123456",
    "version": "1.0.0"
  }
}
```

Error responses:

```json
{
  "success": false,
  "data": null,
  "error": {
    "code": "ERROR_CODE",
    "message": "Error message",
    "details": "Detailed error information"
  },
  "meta": {
    "timestamp": "2025-08-20T07:52:37.123456",
    "version": "1.0.0"
  }
}
```

## Pagination

All list endpoints support pagination using standard Spring Data JPA parameters:
- `page` - Page number (0-based)
- `size` - Page size
- `sort` - Sort field and direction (e.g., `date,desc`)

Example: `GET /api/declarations?page=0&size=10&sort=date,desc`

## Example Usage

### Authenticate a user

```bash
curl -X POST "http://localhost:8080/api/auth/login" \
  -d "login=john_doe" \
  -d "motDePasse=secure_password"
```

### Submit a tax declaration

```bash
curl -X POST "http://localhost:8080/api/declarations/soumettre" \
  -H "Authorization: Bearer JWT_TOKEN" \
  -d "proprieteId=123e4567-e89b-12d3-a456-426614174000" \
  -d "typeImpot=IF"
```

### List declarations

```bash
curl -X GET "http://localhost:8080/api/declarations?page=0&size=10&sort=date,desc" \
  -H "Authorization: Bearer JWT_TOKEN"
```

## Legal Compliance

This application complies with the following legal frameworks:
- Constitution of the DRC (February 18, 2006, revised January 20, 2011)
- Law No. 11/002 (January 20, 2011)
- Ordinance-Law No. 69-006 (February 10, 1969)
- Law No. 04/2003 (March 13, 2003)
- Law No. 83/004 (February 23, 1983)
- Ministerial Order No. 004/ICAB/MINFIN/HKAT-KATANGA/2018 (August 10, 2018)

## Development

### Prerequisites

- Java 17
- Maven 3.8+
- PostgreSQL 16 with PostGIS extension

### Setup

1. Create a PostgreSQL database with PostGIS extension
2. Update `application.properties` with your database credentials
3. Run the application: `mvn spring-boot:run`

### Building

```bash
mvn clean package
```

### Running

```bash
mvn spring-boot:run
```

Or:

```bash
java -jar target/impots-0.0.1-SNAPSHOT.jar
```

## Testing

Run unit and integration tests:

```bash
mvn test
```

## Documentation

API documentation is available at:
- `http://localhost:8080/swagger-ui.html`
- `http://localhost:8080/v3/api-docs`
