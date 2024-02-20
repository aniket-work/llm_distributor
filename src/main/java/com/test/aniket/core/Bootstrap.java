package com.test.aniket.core;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Produced;
import static com.test.aniket.core.Util.*;

import java.util.Properties;

public class Bootstrap {

    public static void main(String[] args) {
        Properties props = getConfig();

        StreamsBuilder streamsBuilder = new StreamsBuilder();
        // Build the Topology
        streamsBuilder.<String, String>stream(GENERATED_RESPONSES_TOPIC)
                .filter((key, value) -> isRelevant(value, HIGH_RELEVANCE_SCORE))
                .to(HIGHLY_RELEVANT_RESPONSES_TOPIC, Produced.with(Serdes.String(), Serdes.String()));

        streamsBuilder.<String, String>stream(GENERATED_RESPONSES_TOPIC)
                .filter((key, value) -> isRelevant(value, MODERATE_RELEVANCE_SCORE))
                .to(MODERATELY_RELEVANT_RESPONSES_TOPIC, Produced.with(Serdes.String(), Serdes.String()));

        streamsBuilder.<String, String>stream(GENERATED_RESPONSES_TOPIC)
                .filter((key, value) -> isRelevant(value, LOW_RELEVANCE_SCORE))
                .to(LOW_RELEVANCE_RESPONSES_TOPIC, Produced.with(Serdes.String(), Serdes.String()));

        // Create the Kafka Streams Application
        KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(), props);
        // Start the application
        kafkaStreams.start();

        // Attach shutdown handler to catch control-c
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
    }

    private static Properties getConfig() {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "rag-distributor");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0");
        return properties;
    }

    private static boolean isRelevant(String response, int relevanceScore) {
        return getRelevanceScore(response) == relevanceScore;
    }

    private static int getRelevanceScore(String response) {

        if (response.contains("\"relevant_score\": " + HIGH_RELEVANCE_SCORE)) {
            return HIGH_RELEVANCE_SCORE;
        } else if (response.contains("\"relevant_score\": " + MODERATE_RELEVANCE_SCORE)) {
            return MODERATE_RELEVANCE_SCORE;
        } else if (response.contains("\"relevant_score\": " + LOW_RELEVANCE_SCORE)) {
            return LOW_RELEVANCE_SCORE;
        } else {
            // Default to low relevance if "relevant_score" field is not found or invalid
            return LOW_RELEVANCE_SCORE;
        }
    }
}
