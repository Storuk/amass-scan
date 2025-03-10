package com.amass.scan.model

data class OutputDTO(
    val subdomains: MutableSet<String> = mutableSetOf(),
    val ipAddresses: MutableSet<String> = mutableSetOf(),
    val emailServers: MutableSet<String> = mutableSetOf(),
    val dnsServers: MutableSet<String> = mutableSetOf()
)