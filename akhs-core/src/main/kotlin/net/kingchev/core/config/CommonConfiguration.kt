package net.kingchev.core.config

import net.kingchev.core.kafka.KafkaConfiguration
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    KafkaConfiguration::class
)
@EnableConfigurationProperties
class CommonConfiguration {
    //Configuring liquibase spring bean
    @Bean
    fun liquibase(): LiquibaseProperties {
        val props = LiquibaseProperties()
        //setting the path to changelog
        props.changeLog = "classpath:/db/changelog/db.changelog-master.yaml"
        return props
    }
}