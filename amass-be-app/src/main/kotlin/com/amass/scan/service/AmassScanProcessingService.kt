package com.amass.scan.service

import com.amass.scan.model.entity.AmassScan
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AmassScanProcessingService(
    private val initiateScanService: InitiateScanService,
    private val amassScanPersistenceService: AmassScanPersistenceService
) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AmassScanProcessingService::class.java)
    }

    fun process(domain: String): AmassScan {
        logger.info("Starting scan processing for domain: {}", domain)

        val initialScan = amassScanPersistenceService.save(domain)
        logger.info("Initial scan saved for domain: {}", domain)

        initiateScanService.initiateScan(initialScan)

        return initialScan
    }
}