package com.amass.scan

import com.amass.scan.configuration.properties.AsyncProperties
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

@EnableAsync
@EnableScheduling
@SpringBootApplication
@EnableSchedulerLock(defaultLockAtMostFor = "PT1H")
@EnableConfigurationProperties(AsyncProperties::class)
class TestTaskApplication

fun main(args: Array<String>) {
    runApplication<TestTaskApplication>(*args)
}