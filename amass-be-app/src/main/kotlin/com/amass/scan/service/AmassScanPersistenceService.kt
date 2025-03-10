package com.amass.scan.service

import com.amass.scan.enums.ScanStatus
import com.amass.scan.model.entity.AmassScan
import com.amass.scan.model.OutputDTO
import com.amass.scan.repository.AmassScanRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
@RequiredArgsConstructor
class AmassScanPersistenceService(
    private val amassScanRepository: AmassScanRepository
) {

    fun update(amassScan: AmassScan, output: OutputDTO, status: ScanStatus) {
        amassScan.endTime = LocalDateTime.now()
        amassScan.status = status
        amassScan.subdomains = output.subdomains
        amassScan.ipAddresses = output.ipAddresses
        amassScan.dnsServers = output.dnsServers
        amassScan.emailServers = output.emailServers

        amassScanRepository.saveAndFlush(amassScan)
    }

    fun save(domain: String): AmassScan {
        val newVersion = getNewVersion(domain)

        val amassScan = AmassScan(
            null,
            domain,
            LocalDateTime.now(),
            null,
            ScanStatus.PROCESSING,
            newVersion
        )

        return amassScanRepository.saveAndFlush(amassScan)
    }

    fun findAll(): List<AmassScan> {
        return amassScanRepository.findAll()
    }

    fun findByDomainAndVersion(domain: String, version: Int): AmassScan {
        return amassScanRepository.findAmassScanByDomainAndVersion(domain, version)
    }

    private fun getNewVersion(domain: String): Int {
        return amassScanRepository.findLatestByDomain(domain)
            .map { obj: AmassScan -> obj.version }
            .map { version: Int -> version + 1 }
            .orElse(1)
    }
}