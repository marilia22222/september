package com.mycompany.festival.controller;

import com.mycompany.festival.objects.PerformanceO;
import com.mycompany.festival.service.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/performances")
public class PerformanceController {

    @Autowired
    private PerformanceService performanceService;

    @PostMapping
    @PreAuthorize("hasRole('ARTIST')")
    public ResponseEntity<PerformanceO> createPerformance(@RequestBody @Valid PerformanceO performanceO) {
        PerformanceO createdPerformance = performanceService.createPerformance(performanceO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPerformance);
    }
}
