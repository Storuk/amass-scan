package com.amass.scan.mapper

import com.amass.scan.model.OutputDTO
import org.springframework.stereotype.Component

@Component
class OutputMapper {
    companion object {
        private const val FQDN_MARKER = "(FQDN)"
        private const val IP_ADDRESS_MARKER = "(IPAddress)"
        private const val NODE_RELATIONSHIP = "node"
        private const val A_RECORD = "a_record"
        private const val AAAA_RECORD = "aaaa_record"
        private const val MX_RECORD = "mx_record"
        private const val NS_RECORD = "ns_record"
        private const val CNAME_RECORD = "cname_record"
        private const val RELATIONSHIP_SEPARATOR = "-->"
        private const val EXPECTED_PARTS_COUNT = 3
    }

    fun extractDomainData(text: String): OutputDTO {
        val subdomains = mutableSetOf<String>()
        val ipAddresses = mutableSetOf<String>()
        val emailServers = mutableSetOf<String>()
        val dnsServers = mutableSetOf<String>()
        val nodeRelations = mutableSetOf<String>()

        text.lineSequence()
            .filter { it.isNotBlank() }
            .forEach { line ->
                val parts = parseLine(line)
                if (parts.size < EXPECTED_PARTS_COUNT) return@forEach

                processForSubdomains(parts, subdomains)
                processForIpAddresses(parts, ipAddresses)
                processForEmailServers(parts, emailServers)
                processForDnsServers(parts, dnsServers)
                processForNodes(parts, nodeRelations)
            }

        processNodeRelations(nodeRelations, subdomains, dnsServers, emailServers)

        return OutputDTO(subdomains, ipAddresses, emailServers, dnsServers)
    }

    /**
     * Splits a line into its component parts.
     */
    private fun parseLine(line: String): List<String> {
        return line.trim().split(RELATIONSHIP_SEPARATOR).map { it.trim() }
    }

    /**
     * Processes a parsed line to extract subdomains (CNAME records).
     */
    private fun processForSubdomains(
        parts: List<String>,
        subdomains: MutableSet<String>
    ) {
        if (isCnameRecord(parts)) {
            extractNameFromMarker(parts[2], FQDN_MARKER)?.let { subdomains.add(it) }
        }
    }

    /**
     * Processes a parsed line to extract IP addresses (both IPv4 and IPv6).
     */
    private fun processForIpAddresses(parts: List<String>, ipAddresses: MutableSet<String>) {
        if (isIpAddressRecord(parts)) {
            extractNameFromMarker(parts[2], IP_ADDRESS_MARKER)?.let { ipAddresses.add(it) }
        }
    }

    /**
     * Processes a parsed line to extract email servers (MX records).
     */
    private fun processForEmailServers(parts: List<String>, emailServers: MutableSet<String>) {
        if (isMxRecord(parts)) {
            extractNameFromMarker(parts[2], FQDN_MARKER)?.let { emailServers.add(it) }
        }
    }

    /**
     * Processes a parsed line to extract DNS servers (NS records).
     */
    private fun processForDnsServers(parts: List<String>, dnsServers: MutableSet<String>) {
        if (isNsRecord(parts)) {
            extractNameFromMarker(parts[2], FQDN_MARKER)?.let { dnsServers.add(it) }
        }
    }

    /**
     * Processes a parsed line to extract node relationships.
     */
    private fun processForNodes(parts: List<String>, nodeRelations: MutableSet<String>) {
        if (isNodeRelationship(parts)) {
            extractNameFromMarker(parts[2], FQDN_MARKER)?.let { nodeRelations.add(it) }
        }
    }

    /**
     * Extracts a domain from a part containing a marker.
     */
    private fun extractNameFromMarker(part: String, marker: String): String? {
        return part.substringBefore(" $marker").trim().takeIf { it.isNotBlank() }
    }

    /**
     * Processes node relations to categorize them as subdomains if they're not
     * already categorized as DNS or email servers.
     */
    private fun processNodeRelations(
        nodeRelations: Set<String>,
        subdomains: MutableSet<String>,
        dnsServers: Set<String>,
        emailServers: Set<String>
    ) {
        nodeRelations.asSequence()
            .filter { node -> !dnsServers.contains(node) && !emailServers.contains(node) }
            .forEach { node -> subdomains.add(node) }
    }

    /**
     * Determines if the relationship represents a CNAME record.
     */
    private fun isCnameRecord(parts: List<String>): Boolean {
        return parts[1].contains(CNAME_RECORD) && parts[2].contains(FQDN_MARKER)
    }

    /**
     * Determines if the relationship represents a node connection.
     */
    private fun isNodeRelationship(parts: List<String>): Boolean {
        return parts[1].contains(NODE_RELATIONSHIP) && parts[2].contains(FQDN_MARKER)
    }

    /**
     * Determines if the relationship represents an IP address record (A or AAAA).
     */
    private fun isIpAddressRecord(parts: List<String>): Boolean {
        return (parts[1].contains(A_RECORD) || parts[1].contains(AAAA_RECORD)) &&
                parts[2].contains(IP_ADDRESS_MARKER)
    }

    /**
     * Determines if the relationship represents an MX record.
     */
    private fun isMxRecord(parts: List<String>): Boolean {
        return parts[1].contains(MX_RECORD) && parts[2].contains(FQDN_MARKER)
    }

    /**
     * Determines if the relationship represents an NS record.
     */
    private fun isNsRecord(parts: List<String>): Boolean {
        return parts[1].contains(NS_RECORD) && parts[2].contains(FQDN_MARKER)
    }
}