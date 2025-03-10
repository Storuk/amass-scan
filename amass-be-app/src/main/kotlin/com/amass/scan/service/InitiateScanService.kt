package com.amass.scan.service

import com.amass.scan.enums.ScanStatus
import com.amass.scan.exception.AmassScanException
import com.amass.scan.mapper.OutputMapper
import com.amass.scan.model.OutputDTO
import com.amass.scan.model.entity.AmassScan
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class InitiateScanService(
    private val scanPersistenceService: AmassScanPersistenceService,
    private val dockerService: DockerService,
    private val outputMapper: OutputMapper
) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(InitiateScanService::class.java)
    }

    @Async
    fun initiateScan(initialScan: AmassScan) {
        val domain = initialScan.domain
        logger.info("Initiating scan for domain: {}", domain)
        try {
            val output = dockerService.runAmassScan(domain)
            logger.info("Scan completed for domain: {}", domain)

            if (hasNoDiscoveries(output)) {
                logger.info("No assets or names were discovered for domain: {}", domain)
                scanPersistenceService.update(initialScan, OutputDTO(), ScanStatus.NOT_FOUND)
            } else {
                val outputDto = outputMapper.extractDomainData(output)
                scanPersistenceService.update(initialScan, outputDto, ScanStatus.COMPLETED)
                logger.info("Scan results persisted for domain: {}", domain)
            }
        } catch (e: AmassScanException) {
            logger.error(e.message)
            scanPersistenceService.update(initialScan, OutputDTO(), ScanStatus.FAILED)
            logger.info("Recorded scan failure for domain: {}", domain)
        }
    }

    private fun hasNoDiscoveries(output: String): Boolean {
        return output.contains("No names were discovered") ||
                output.contains("No assets were discovered")
    }
}