package net.kingchev.core.persistence

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EntityScan(basePackages = ["net.kingchev.core.persistence.entity"])
@EnableJpaRepositories(basePackages = ["net.kingchev.core.persistence.repository"])
@EnableTransactionManagement
class PersistenceConfiguration
//Maybe in the future I'll add anything in this config