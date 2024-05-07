package net.kingchev.core.kafka

import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.listener.ContainerProperties.AckMode
import java.time.Duration

@EnableKafka
@Configuration
class KafkaConfiguration(kafkaProperties: KafkaProperties) {
    init {
        kafkaProperties.bootstrapServers = listOf("localhost:29092")
        kafkaProperties.consumer.apply { consumer() }
        kafkaProperties.producer.apply { producer() }
        kafkaProperties.listener.apply { listener() }
    }

    companion object {
        private val consumer: KafkaProperties.Consumer.() -> Unit = {
            autoOffsetReset = "earliest"
            enableAutoCommit = false
            groupId = "akhs"
            clientId = "akhs"
            maxPollRecords = 1
            fetchMaxWait = Duration.ofMillis(36000)
            keyDeserializer = org.apache.kafka.common.serialization.StringSerializer::class.java
            valueDeserializer = org.apache.kafka.common.serialization.StringSerializer::class.java
        }

        private val producer: KafkaProperties.Producer.() -> Unit = {
            retries = 2
            clientId = "akhs"
            keySerializer = org.apache.kafka.common.serialization.StringSerializer::class.java
            valueSerializer = org.apache.kafka.common.serialization.StringSerializer::class.java
        }

        private val listener: KafkaProperties.Listener.() -> Unit = {
            ackMode = AckMode.MANUAL_IMMEDIATE
            concurrency = 1
            pollTimeout = Duration.ofMillis(1800000)
        }
    }
}