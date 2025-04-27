package net.kingchev.core.kafka

import net.kingchev.core.model.CrosspostingMessage
import net.kingchev.core.model.NotificationMessage
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.config.TopicConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.*
import org.springframework.kafka.listener.ContainerProperties.AckMode
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer

const val CROSSPOSTING = "akhs.cross-posting"
const val TWITCH_NOTIFICATION = "akhs.twitch.notification"
const val YOUTUBE_NOTIFICATION = "akhs.youtube.notification"

@EnableKafka
@Configuration
@Import(
    KafkaConfiguration.Topics::class,
    KafkaConfiguration.Crossposting::class,
    KafkaConfiguration.TwitchNotification::class
)
@EnableAutoConfiguration(exclude = [KafkaAutoConfiguration::class])
class KafkaConfiguration {
    class Topics {
        @Bean
        fun topicCrossposting() = TopicBuilder
            .name(CROSSPOSTING)
            .configs(topicProps())
            .build()

        @Bean
        fun topicTwitchNotification() = TopicBuilder
            .name(TWITCH_NOTIFICATION)
            .configs(topicProps())
            .build()

        @Bean
        fun topicYoutubeNotification() = TopicBuilder
            .name(YOUTUBE_NOTIFICATION)
            .configs(topicProps())
            .build()
    }

    class Crossposting {
        @Bean
        fun kafkaTemplate() = KafkaTemplate(producerFactory())

        @Bean
        fun consumerFactory(): ConsumerFactory<String, CrosspostingMessage> =
            DefaultKafkaConsumerFactory(
                consumerProps(),
                ErrorHandlingDeserializer(StringDeserializer()),
                ErrorHandlingDeserializer(JsonDeserializer())
            )

        @Bean
        fun producerFactory(): ProducerFactory<String, CrosspostingMessage> {
            return DefaultKafkaProducerFactory(producerProps())
        }

        @Bean
        fun kafkaListenerContainerFactory() =
            ConcurrentKafkaListenerContainerFactory<String, CrosspostingMessage>()
                .apply(kafkaListenerContainerFactory(consumerFactory()))
    }

    class TwitchNotification {
        @Bean
        fun kafkaTemplateTwitch() =
            KafkaTemplate(producerFactoryTwitch())

        @Bean
        fun consumerFactoryTwitch(): ConsumerFactory<String, NotificationMessage> =
            DefaultKafkaConsumerFactory(
                consumerProps(),
                ErrorHandlingDeserializer(StringDeserializer()),
                ErrorHandlingDeserializer(JsonDeserializer())
            )

        @Bean
        fun producerFactoryTwitch(): ProducerFactory<String, NotificationMessage> =
            DefaultKafkaProducerFactory(producerProps())

        @Bean
        fun kafkaListenerContainerFactoryTwitch() =
            ConcurrentKafkaListenerContainerFactory<String, NotificationMessage>()
                .apply(kafkaListenerContainerFactory(consumerFactoryTwitch()))
    }

    companion object {
        private val bootstrapServers = listOf("localhost:9092")

        private fun topicProps(): Map<String, String> {
            val map: MutableMap<String, String> = HashMap()
            map[TopicConfig.MAX_MESSAGE_BYTES_CONFIG] = "104857600"
            return map
        }

        private fun consumerProps(): MutableMap<String, Any> {
            val map: MutableMap<String, Any> = HashMap()
            map[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
            map[ConsumerConfig.GROUP_ID_CONFIG] = "akhs-group"
            map[ConsumerConfig.CLIENT_ID_CONFIG] = "akhs"
            map[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
            map[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = false
            map[ConsumerConfig.MAX_POLL_RECORDS_CONFIG] = 1
            map[ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG] = 36000
            map[ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG] = "104857600"
            map[ConsumerConfig.FETCH_MAX_BYTES_CONFIG] = "104857600"
            map[JsonSerializer.ADD_TYPE_INFO_HEADERS] = false
            map[JsonDeserializer.TRUSTED_PACKAGES] = "net.kingchev.core.model"
            return map
        }

        private fun producerProps(): MutableMap<String, Any> {
            val map: MutableMap<String, Any> = HashMap()
            map[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
            map[ProducerConfig.RETRIES_CONFIG] = 2
            map[ProducerConfig.CLIENT_ID_CONFIG] = "akhs"
            map[ProducerConfig.MAX_REQUEST_SIZE_CONFIG] = "104857600"
            map[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
            map[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java
            return map
        }

        private fun <E>kafkaListenerContainerFactory(consumer: ConsumerFactory<String, E>): ConcurrentKafkaListenerContainerFactory<String, E>.() -> Unit = {
            consumerFactory = consumer
            containerProperties.ackMode = AckMode.MANUAL
            containerProperties.pollTimeout = 1800000
            containerProperties.clientId = "akhs"
            setConcurrency(2)
        }
    }
}