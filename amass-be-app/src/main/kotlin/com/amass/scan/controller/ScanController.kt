package com.amass.scan.controller

import com.amass.scan.model.entity.AmassScan
import com.amass.scan.service.AmassScanPersistenceService
import com.amass.scan.service.AmassScanProcessingService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/scans")
class ScanController(
    private val amassScanProcessingService: AmassScanProcessingService,
    private val amassScanPersistenceService: AmassScanPersistenceService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun initiateScan(@RequestParam domain: String): AmassScan {
        return amassScanProcessingService.process(domain)
    }

    @GetMapping("/domain/{domain}/version/{version}")
    fun getScanByDomainAndVersion(@PathVariable domain: String, @PathVariable version: Int): AmassScan {
        return amassScanPersistenceService.findByDomainAndVersion(domain, version)
    }

    @GetMapping("/health")
    fun health(): String {
        return "up1"
    }

    @GetMapping
    fun getAllScans(): List<AmassScan> {
        return amassScanPersistenceService.findAll()
    }
}