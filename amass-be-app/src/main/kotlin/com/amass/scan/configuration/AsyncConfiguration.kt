package com.amass.scan.configuration

import com.amass.scan.configuration.properties.AsyncProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@Configuration
class AsyncConfiguration {

    @Bean
    fun taskExecutor(asyncProperties: AsyncProperties): TaskExecutor {
        val executor = ThreadPoolTaskExecutor()
        executor.maxPoolSize = asyncProperties.maxPoolSize
        executor.corePoolSize = asyncProperties.corePoolSize
        executor.setThreadNamePrefix("Async - ")
        executor.initialize()
        return executor
    }
}