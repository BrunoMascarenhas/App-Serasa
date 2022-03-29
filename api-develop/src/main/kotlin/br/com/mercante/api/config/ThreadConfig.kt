package br.com.mercante.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.task.TaskExecutor
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@EnableAsync
@Configuration
class ThreadConfig {

    @Bean
    @Primary
    fun threadPoolTaskExecutor(): TaskExecutor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 8
        executor.maxPoolSize = 8
        executor.setThreadNamePrefix("default_task_executor_thread")
        executor.initialize()

        return executor
    }
}