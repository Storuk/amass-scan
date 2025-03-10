package com.amass.scan.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "async")
data class AsyncProperties(
    val maxPoolSize: Int,
    val corePoolSize: Int
)