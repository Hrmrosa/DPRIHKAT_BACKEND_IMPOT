package com.DPRIHKAT.controller;

import com.DPRIHKAT.entity.enums.Role;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ViewController {

    @GetMapping("/dashboard")
    public String getDashboardView(Authentication authentication, Model model) {
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .orElse("CONTRIBUABLE");
        
        model.addAttribute("userRole", role);
        return "dashboard/" + role.toLowerCase() + "-dashboard";
    }

    @GetMapping("/reports")
    public String getReportsView(Authentication authentication, Model model) {
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .orElse("CONTRIBUABLE");
        
        model.addAttribute("userRole", role);
        model.addAttribute("reportTypes", List.of("TAXATION", "PAIEMENT", "APUREMENT", "VIGNETTE", "CERTIFICAT"));
        return "reports/report-view";
    }
}
