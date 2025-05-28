package net.kingchev.core.persistence

import net.kingchev.core.getenv
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.util.*
import javax.sql.DataSource

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = ["net.kingchev.core.persistence.repository"], repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean::class)
@EnableTransactionManagement
@EnableAutoConfiguration(exclude = [DataSourceAutoConfiguration::class])
class PersistenceConfiguration {
    private object DatabaseConnection {
        var username = getenv("POSTGRES_USER") ?: "postgres"
        var password = getenv("POSTGRES_PASSWORD") ?: "secret"
        var database = getenv("POSTGRES_DATABASE") ?: "akhs"
        var host = getenv("POSTGRES_HOST") ?: "localhost"
        var port = getenv("POSTGRES_PORT") ?: "5432"
    }
    @Bean
    fun dataSource(): DataSource {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName("org.postgresql.Driver")
        dataSource.username = DatabaseConnection.username
        dataSource.password = DatabaseConnection.password
        dataSource.url = "jdbc:postgresql://${DatabaseConnection.host}:${DatabaseConnection.port}/${DatabaseConnection.database}?createDatabaseIfNotExist=true"
        return dataSource
    }

    @Bean
    fun entityManagerFactory(): LocalContainerEntityManagerFactoryBean {
        val vendor = HibernateJpaVendorAdapter()
        val em = LocalContainerEntityManagerFactoryBean()

        em.dataSource = dataSource()
        em.jpaVendorAdapter = vendor
        em.setJpaProperties(jpaProperties())
        em.setPackagesToScan("net.kingchev.core.persistence.entity")

        return em
    }

    fun jpaProperties(): Properties {
        val properties = Properties()
        return properties
    }
}