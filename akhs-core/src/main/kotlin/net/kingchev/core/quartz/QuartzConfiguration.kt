package net.kingchev.core.quartz

import net.kingchev.core.persistence.PersistenceConfiguration
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.scheduling.quartz.SpringBeanJobFactory
import java.util.Properties
import javax.sql.DataSource

@Configuration
@Import(PersistenceConfiguration::class)
class QuartzConfiguration {
    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Bean
    fun springBeanJobFactory(): SpringBeanJobFactory {
        val jobFactory: SpringBeanJobFactory = SpringBeanJobFactory()
        jobFactory.setApplicationContext(applicationContext)
        return jobFactory
    }

    @Bean
    fun scheduler(dataSource: DataSource): SchedulerFactoryBean {
        val schedulerFactory = SchedulerFactoryBean()
        val properties = Properties()
        properties.setProperty("org.quartz.scheduler.instanceName", "AkhsInstance")
        properties.setProperty("org.quartz.scheduler.instanceId", "Instance-1")
        schedulerFactory.setOverwriteExistingJobs(true)
        schedulerFactory.isAutoStartup = true
        schedulerFactory.setQuartzProperties(properties)
        schedulerFactory.setDataSource(dataSource)
        schedulerFactory.setJobFactory(springBeanJobFactory())
        schedulerFactory.setWaitForJobsToCompleteOnShutdown(true)

        return schedulerFactory
    }

    companion object {
        val log: Logger
            get() = LoggerFactory.getLogger(this::class.java)
    }
}