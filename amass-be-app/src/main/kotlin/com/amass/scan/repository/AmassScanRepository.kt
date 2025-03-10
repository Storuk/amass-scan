package com.amass.scan.repository

import com.amass.scan.model.entity.AmassScan
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*


@Repository
interface AmassScanRepository : JpaRepository<AmassScan, Long> {

    @Query("SELECT a FROM AmassScan a WHERE a.domain = :domain AND a.version = (SELECT max(b.version) from AmassScan b WHERE b.domain = :domain)")
    fun findLatestByDomain(domain: String): Optional<AmassScan>

    fun findAmassScanByDomainAndVersion(domain: String, version: Int): AmassScan

    @Query("SELECT a FROM AmassScan a WHERE a.status = 'PROCESSING' AND a.startTime <= :threeHoursAgo")
    fun findProcessingScans(@Param("threeHoursAgo") threeHoursAgo: LocalDateTime): List<AmassScan>
}