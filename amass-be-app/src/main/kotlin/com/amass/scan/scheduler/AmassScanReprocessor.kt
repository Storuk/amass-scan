package com.amass.scan.scheduler

import com.amass.scan.model.entity.AmassScan
import com.amass.scan.repository.AmassScanRepository
import com.amass.scan.service.InitiateScanService
import lombok.RequiredArgsConstructor
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
@RequiredArgsConstructor
class AmassScanReprocessor(
    private val amassScanRepository: AmassScanRepository,
    private val initiateScanService: InitiateScanService,
    @Value("\${scan.scheduler.reprocess-duration-hours}") private val retryDurationHours: Long
) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AmassScanReprocessor::class.java)
    }

    /**
     * Scheduled task that retries processing scans that were previously marked as "in progress".
     * This will reprocess scans if the scan status is still in progress and the scan hasn't been updated to completed or failed.
     *
     * The retry process is protected with a scheduler lock to prevent multiple instances
     * of the task from running concurrently.
     */
    @Scheduled(fixedRateString = "\${scan.scheduler.fixed-rate-ms}")
    @SchedulerLock(
        name = "AmassScanReprocessor_retryProcessingScans",
        lockAtLeastFor = "PT5M",
        lockAtMostFor = "PT1H"
    )
    fun retryProcessingScans() {
        val threeHoursAgo = LocalDateTime.now().minusHours(retryDurationHours)
        val processingScans: List<AmassScan> = amassScanRepository.findProcessingScans(threeHoursAgo)

        if (processingScans.isEmpty()) {
            logger.info("No processing scans found for retry.")
            return
        }

        logger.info("Retrying {} processing scans", processingScans.size)

        processingScans.forEach { scan ->
            logger.info("Reprocessing scan for domain: {}", scan.domain)
            initiateScanService.initiateScan(scan)
        }
    }
}