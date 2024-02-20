echo "Waiting for Kafka to come online..."

cub kafka-ready -b kafka:9092 1 20

# create the users topic
kafka-topics \
  --bootstrap-server kafka:9092 \
  --topic generated-responses-topic \
  --replication-factor 1 \
  --partitions 1 \
  --create

kafka-topics \
  --bootstrap-server kafka:9092 \
  --topic highly-relevant-responses \
  --replication-factor 1 \
  --partitions 1 \
  --create

  kafka-topics \
    --bootstrap-server kafka:9092 \
    --topic moderately-relevant-responses \
    --replication-factor 1 \
    --partitions 1 \
    --create

  kafka-topics \
    --bootstrap-server kafka:9092 \
    --topic low-relevance-responses \
    --replication-factor 1 \
    --partitions 1 \
    --create

sleep infinity