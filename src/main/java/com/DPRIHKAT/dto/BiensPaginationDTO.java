package com.DPRIHKAT.dto;

import com.DPRIHKAT.entity.Propriete;
import org.springframework.data.domain.Page;

public class BiensPaginationDTO {
    private int currentPage;
    private int totalPages;
    private long totalItems;
    private Iterable<Propriete> biens;

    // Getters and Setters
    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }
    
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    
    public long getTotalItems() { return totalItems; }
    public void setTotalItems(long totalItems) { this.totalItems = totalItems; }
    
    public Iterable<Propriete> getBiens() { return biens; }
    public void setBiens(Iterable<Propriete> biens) { this.biens = biens; }
}
