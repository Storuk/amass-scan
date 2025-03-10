package com.amass.scan.service

import com.amass.scan.exception.AmassScanException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class DockerService(
    @Value("\${scan.timeout-minutes}") private val timeout: String
) {

    fun runAmassScan(domain: String): String {
        try {
            val processBuilder = ProcessBuilder(
                "amass", "enum", "-d", domain, "-timeout", timeout
            )

            val process = processBuilder.start()
            val reader = process.inputStream.bufferedReader()

            val output = StringBuilder()
            reader.useLines { lines ->
                lines.forEach { line ->
                    output.appendLine(line)
                }
            }

            println("Response: $output")

            val exitCode = process.waitFor()

            return if (exitCode == 0)
                output.toString()
            else
                throw AmassScanException("Scan failed for domain: $domain")
        } catch (e: Exception) {
            throw AmassScanException("Error running Amass scan for domain: $domain", e)
        }
    }
}