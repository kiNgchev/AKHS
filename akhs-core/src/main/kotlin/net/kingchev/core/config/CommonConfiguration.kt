package net.kingchev.core.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import liquibase.integration.spring.SpringLiquibase
import net.kingchev.core.kafka.KafkaConfiguration
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.scheduling.annotation.EnableScheduling
import javax.sql.DataSource

@Configuration
@Import(
    KafkaConfiguration::class
)
@EnableScheduling
@ConfigurationPropertiesScan(basePackages = ["net.kingchev"])
@ComponentScan(basePackages = ["net.kingchev"])
@EnableConfigurationProperties
class CommonConfiguration {
    @Bean
    fun gson(): Gson = GsonBuilder().create()

    //Configuring liquibase spring bean
    @Bean
    fun liquibase(datasource: DataSource): SpringLiquibase {
        val props = SpringLiquibase()
        //setting the datasource
        props.dataSource = datasource
        //setting the path to changelog
        props.changeLog = "classpath:/db/changelog/db.changelog-master.yaml"
        return props
    }
}