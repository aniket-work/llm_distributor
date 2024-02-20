# llm_distributor

1. go to kafka container and open in total 4 terminals ( 1 for producer and 3 for consumers)

# Highly Relevant Response
echo '{"response_id": "1", "text": "This is a highly relevant response.", "relevant_score": 3}' | kafka-console-producer --broker-list localhost:9092 --topic generated-responses-topic

# Moderately Relevant Response
echo '{"response_id": "2", "text": "This is a moderately relevant response.", "relevant_score": 2}' | kafka-console-producer --broker-list localhost:9092 --topic generated-responses-topic

# Low Relevance Response
echo '{"response_id": "3", "text": "This is a low relevance response.", "relevant_score": 1}' | kafka-console-producer --broker-list localhost:9092 --topic generated-responses-topic



# Start console consumer for highly-relevant-responses topic
kafka-console-consumer --bootstrap-server localhost:9092 --topic highly-relevant-responses --from-beginning

# Start console consumer for moderately-relevant-responses topic
kafka-console-consumer --bootstrap-server localhost:9092 --topic moderately-relevant-responses --from-beginning

# Start console consumer for low-relevance-responses topic
kafka-console-consumer --bootstrap-server localhost:9092 --topic low-relevance-responses --from-beginning
