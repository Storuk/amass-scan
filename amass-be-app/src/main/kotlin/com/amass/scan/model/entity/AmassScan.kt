package com.amass.scan.model.entity

import com.amass.scan.enums.ScanStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "amass_scan")
data class AmassScan(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val domain: String,
    val startTime: LocalDateTime,
    var endTime: LocalDateTime? = null,
    @Enumerated(EnumType.STRING)
    var status: ScanStatus,
    val version: Int,

    @ElementCollection
    @CollectionTable(name = "amass_scan_subdomains", joinColumns = [JoinColumn(name = "amass_scan_id")])
    @Column(name = "subdomain")
    var subdomains: MutableSet<String> = mutableSetOf(),

    @ElementCollection
    @CollectionTable(name = "amass_scan_ip_addresses", joinColumns = [JoinColumn(name = "amass_scan_id")])
    @Column(name = "ip_address")
    var ipAddresses: MutableSet<String> = mutableSetOf(),

    @ElementCollection
    @CollectionTable(name = "amass_scan_email_servers", joinColumns = [JoinColumn(name = "amass_scan_id")])
    @Column(name = "email_server")
    var emailServers: MutableSet<String> = mutableSetOf(),

    @ElementCollection
    @CollectionTable(name = "amass_scan_dns_servers", joinColumns = [JoinColumn(name = "amass_scan_id")])
    @Column(name = "dns_server")
    var dnsServers: MutableSet<String> = mutableSetOf()
)