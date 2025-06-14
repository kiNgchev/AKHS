package net.kingchev.core.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import liquibase.integration.spring.SpringLiquibase
import net.kingchev.core.cache.ExtRedisConfiguration
import net.kingchev.core.kafka.KafkaConfiguration
import net.kingchev.core.persistence.PersistenceConfiguration
import net.kingchev.core.quartz.QuartzConfiguration
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.scheduling.annotation.EnableScheduling
import javax.sql.DataSource

@Configuration
@EnableScheduling
@ConfigurationPropertiesScan(basePackages = ["net.kingchev"])
@ComponentScan(basePackages = ["net.kingchev"])
@Import(PersistenceConfiguration::class, KafkaConfiguration::class, QuartzConfiguration::class, ExtRedisConfiguration::class)
@EnableConfigurationProperties
class CommonConfiguration {
    @Bean
    fun gson(): Gson = GsonBuilder().create()

    @Bean
    fun liquibase(datasource: DataSource): SpringLiquibase {
        val props = SpringLiquibase()
        props.dataSource = datasource
        props.changeLog = "classpath:/db/changelog/db.changelog-master.yaml"
        return props
    }
}