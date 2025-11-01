# AsyncAPI Documentation

## Overview

The Cross-Docking Operations service publishes domain events to Apache Kafka following the **CloudEvents v1.0** specification. This document provides comprehensive guidance on consuming, testing, and monitoring these events.

## Quick Links

### Development Environment

- **AsyncAPI UI (Interactive)**: http://localhost:8095/springwolf/asyncapi-ui.html
- **AsyncAPI JSON**: http://localhost:8095/springwolf/docs
- **AsyncAPI YAML**: http://localhost:8095/springwolf/docs.yaml
- **OpenAPI REST API**: http://localhost:8095/swagger-ui.html

### Static Documentation

- **AsyncAPI Specification**: `src/main/resources/asyncapi.yaml`

## Event Catalog

### 1. TransferOrderCreatedEvent

**Published When**: A new transfer order is created in the system

**Business Triggers**:
- REST API call to `POST /api/v1/transfer-orders`
- Inbound shipment notification received from WMS
- External system integration creates transfer order

**Example Payload**:
```json
{
  "specversion": "1.0",
  "type": "com.paklog.crossdocking.TransferOrderCreatedEvent",
  "source": "//cross-docking",
  "id": "550e8400-e29b-41d4-a716-446655440001",
  "time": "2025-11-01T14:30:00Z",
  "datacontenttype": "application/json",
  "data": {
    "eventId": "550e8400-e29b-41d4-a716-446655440001",
    "occurredAt": "2025-11-01T14:30:00Z",
    "eventType": "TransferOrderCreatedEvent",
    "transferOrderId": "TO-2025-001234",
    "transferType": "DIRECT",
    "sourceDock": "DOCK-A-01",
    "destinationDock": "DOCK-B-03"
  }
}
```

**Use Cases**:
- Allocate warehouse resources (labor, equipment)
- Send notifications to warehouse staff
- Update operational dashboards
- Initialize cost tracking for billing

### 2. DirectTransferInitiatedEvent

**Published When**: A direct transfer operation begins (no consolidation)

**Business Triggers**:
- Transfer order status changes to IN_PROGRESS
- Dock doors and equipment assigned
- Physical movement starts

**Example Payload**:
```json
{
  "specversion": "1.0",
  "type": "com.paklog.crossdocking.DirectTransferInitiatedEvent",
  "source": "//cross-docking",
  "id": "880e8400-e29b-41d4-a716-446655440004",
  "time": "2025-11-01T14:35:00Z",
  "datacontenttype": "application/json",
  "data": {
    "eventId": "880e8400-e29b-41d4-a716-446655440004",
    "occurredAt": "2025-11-01T14:35:00Z",
    "eventType": "DirectTransferInitiatedEvent",
    "transferOrderId": "TO-2025-001234",
    "sourceDock": "DOCK-A-01",
    "destinationDock": "DOCK-B-03",
    "initiatedAt": "2025-11-01T14:35:00Z"
  }
}
```

**Use Cases**:
- Reserve dock doors and equipment
- Assign workers to specific docks
- Start real-time location tracking
- Begin SLA timer for performance monitoring

### 3. ConsolidationCompletedEvent

**Published When**: Multiple items/orders are consolidated for shipping

**Business Triggers**:
- All items for consolidated shipment are staged
- Consolidation rules satisfied (volume, weight, destination)
- Manual consolidation approval received

**Example Payload**:
```json
{
  "specversion": "1.0",
  "type": "com.paklog.crossdocking.ConsolidationCompletedEvent",
  "source": "//cross-docking",
  "id": "aa0e8400-e29b-41d4-a716-446655440006",
  "time": "2025-11-01T16:00:00Z",
  "datacontenttype": "application/json",
  "data": {
    "eventId": "aa0e8400-e29b-41d4-a716-446655440006",
    "occurredAt": "2025-11-01T16:00:00Z",
    "eventType": "ConsolidationCompletedEvent",
    "consolidationId": "CONSOL-2025-000567",
    "itemsConsolidated": 15,
    "destinationDock": "DOCK-B-05"
  }
}
```

**Use Cases**:
- Prepare consolidated shipment documentation
- Update item locations in inventory system
- Generate shipping labels
- Create carrier pickup requests

### 4. CrossDockCompletedEvent

**Published When**: A complete cross-docking operation finishes successfully

**Business Triggers**:
- All items transferred to destination
- Quality checks passed
- Documentation finalized
- Operation status set to COMPLETED

**Example Payload**:
```json
{
  "specversion": "1.0",
  "type": "com.paklog.crossdocking.CrossDockCompletedEvent",
  "source": "//cross-docking",
  "id": "cc0e8400-e29b-41d4-a716-446655440008",
  "time": "2025-11-01T15:00:00Z",
  "datacontenttype": "application/json",
  "data": {
    "eventId": "cc0e8400-e29b-41d4-a716-446655440008",
    "occurredAt": "2025-11-01T15:00:00Z",
    "eventType": "CrossDockCompletedEvent",
    "operationId": "XDOCK-2025-004321",
    "totalItems": 45,
    "durationMinutes": 25,
    "completedAt": "2025-11-01T15:00:00Z"
  }
}
```

**Use Cases**:
- Record performance metrics and KPIs
- Finalize billing charges based on actual duration
- Update operational reports
- Release dock resources
- Validate SLA compliance

## Consumer Implementation Guide

### Java Spring Kafka Consumer

```java
@Component
public class CrossDockEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(CrossDockEventConsumer.class);

    @KafkaListener(
        topics = "transportation-logistics.cross-docking.events",
        groupId = "my-service-consumer-group",
        containerFactory = "cloudEventKafkaListenerContainerFactory"
    )
    public void handleCrossDockEvent(CloudEvent event) {
        log.info("Received event: type={}, id={}, time={}",
            event.getType(), event.getId(), event.getTime());

        // Check event type
        switch (event.getType()) {
            case "com.paklog.crossdocking.TransferOrderCreatedEvent":
                handleTransferOrderCreated(event);
                break;
            case "com.paklog.crossdocking.DirectTransferInitiatedEvent":
                handleDirectTransferInitiated(event);
                break;
            case "com.paklog.crossdocking.ConsolidationCompletedEvent":
                handleConsolidationCompleted(event);
                break;
            case "com.paklog.crossdocking.CrossDockCompletedEvent":
                handleCrossDockCompleted(event);
                break;
            default:
                log.warn("Unknown event type: {}", event.getType());
        }
    }

    private void handleTransferOrderCreated(CloudEvent event) {
        // Deserialize event data
        ObjectMapper mapper = new ObjectMapper();
        JsonNode data = mapper.readTree(event.getData().toBytes());

        String transferOrderId = data.get("transferOrderId").asText();
        String transferType = data.get("transferType").asText();

        log.info("Processing transfer order creation: {}, type: {}",
            transferOrderId, transferType);

        // Implement your business logic here
        // - Allocate resources
        // - Send notifications
        // - Update dashboards
    }

    // Implement other handlers...
}
```

### Consumer Configuration

```java
@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, CloudEvent> cloudEventConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "my-service-consumer-group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CloudEventDeserializer.class);

        // Enable idempotent consumption
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CloudEvent>
            cloudEventKafkaListenerContainerFactory(
                ConsumerFactory<String, CloudEvent> consumerFactory) {

        ConcurrentKafkaListenerContainerFactory<String, CloudEvent> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(3); // 3 consumer threads
        factory.getContainerProperties().setAckMode(AckMode.MANUAL);

        return factory;
    }
}
```

### Python Consumer (using confluent-kafka)

```python
from confluent_kafka import Consumer, KafkaError
from cloudevents.kafka import from_binary
import json
import logging

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

def create_consumer():
    """Create and configure Kafka consumer"""
    config = {
        'bootstrap.servers': 'localhost:9092',
        'group.id': 'python-consumer-group',
        'auto.offset.reset': 'earliest',
        'enable.auto.commit': False
    }
    return Consumer(config)

def handle_transfer_order_created(event_data):
    """Handle TransferOrderCreatedEvent"""
    transfer_order_id = event_data['transferOrderId']
    transfer_type = event_data['transferType']

    logger.info(f"Processing transfer order: {transfer_order_id}, type: {transfer_type}")
    # Implement your business logic here

def handle_cross_dock_completed(event_data):
    """Handle CrossDockCompletedEvent"""
    operation_id = event_data['operationId']
    duration = event_data['durationMinutes']

    logger.info(f"Cross-dock operation {operation_id} completed in {duration} minutes")
    # Implement your business logic here

def consume_events():
    """Main consumer loop"""
    consumer = create_consumer()
    consumer.subscribe(['transportation-logistics.cross-docking.events'])

    try:
        while True:
            msg = consumer.poll(timeout=1.0)

            if msg is None:
                continue

            if msg.error():
                if msg.error().code() == KafkaError._PARTITION_EOF:
                    continue
                else:
                    logger.error(f"Consumer error: {msg.error()}")
                    break

            # Deserialize CloudEvent
            event = from_binary(msg.headers(), msg.value())

            logger.info(f"Received event: type={event['type']}, id={event['id']}")

            # Parse event data
            event_data = json.loads(event.data)

            # Route to appropriate handler
            event_type = event['type']
            if event_type == 'com.paklog.crossdocking.TransferOrderCreatedEvent':
                handle_transfer_order_created(event_data)
            elif event_type == 'com.paklog.crossdocking.CrossDockCompletedEvent':
                handle_cross_dock_completed(event_data)
            # Add other handlers...

            # Commit offset after successful processing
            consumer.commit(asynchronous=False)

    except KeyboardInterrupt:
        pass
    finally:
        consumer.close()

if __name__ == '__main__':
    consume_events()
```

## Testing Events

### 1. Using Kafka Console Consumer

```bash
# Consume all events from the beginning
kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic transportation-logistics.cross-docking.events \
  --from-beginning \
  --property print.key=true \
  --property print.headers=true

# Consume events with CloudEvents deserialization
kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic transportation-logistics.cross-docking.events \
  --from-beginning \
  --formatter kafka.tools.CloudEventsFormatter
```

### 2. Using kcat (formerly kafkacat)

```bash
# Monitor events in real-time
kcat -b localhost:9092 \
  -t transportation-logistics.cross-docking.events \
  -C -f 'Key: %k\nHeaders: %h\nPayload: %s\n---\n'

# Count events by type
kcat -b localhost:9092 \
  -t transportation-logistics.cross-docking.events \
  -C -o beginning -e \
  -f '%s\n' | jq -r '.type' | sort | uniq -c
```

### 3. Trigger Test Events via REST API

```bash
# Create a transfer order (triggers TransferOrderCreatedEvent)
curl -X POST http://localhost:8095/api/v1/transfer-orders \
  -H "Content-Type: application/json" \
  -d '{
    "transferType": "DIRECT",
    "sourceDockId": "DOCK-A-01",
    "destinationDockId": "DOCK-B-03",
    "items": [
      {
        "sku": "ITEM-001",
        "quantity": 10,
        "weight": 5.5,
        "dimensions": {"length": 30, "width": 20, "height": 15}
      }
    ],
    "priority": "HIGH",
    "timingWindow": {
      "expectedArrival": "2025-11-01T15:00:00Z",
      "expectedDeparture": "2025-11-01T17:00:00Z"
    }
  }'

# Start a cross-dock operation (triggers DirectTransferInitiatedEvent)
curl -X POST http://localhost:8095/api/v1/cross-dock-operations/start \
  -H "Content-Type: application/json" \
  -d '{
    "transferOrderIds": ["TO-2025-001234"],
    "transferType": "DIRECT"
  }'

# Complete an operation (triggers CrossDockCompletedEvent)
curl -X POST http://localhost:8095/api/v1/cross-dock-operations/XDOCK-2025-004321/complete
```

## Event Schemas

### CloudEvents Envelope

All events use this standard CloudEvents v1.0 envelope:

```json
{
  "specversion": "1.0",
  "type": "com.paklog.crossdocking.{EventType}",
  "source": "//cross-docking",
  "id": "uuid",
  "time": "ISO-8601 timestamp",
  "datacontenttype": "application/json",
  "data": {
    // Event-specific payload
  }
}
```

### Common Fields in Event Data

All event data payloads contain these base fields:

```json
{
  "eventId": "550e8400-e29b-41d4-a716-446655440001",
  "occurredAt": "2025-11-01T14:30:00Z",
  "eventType": "TransferOrderCreatedEvent",
  // Event-specific fields...
}
```

## Monitoring and Observability

### Kafka Topic Metrics

Monitor these key metrics for the event topic:

```bash
# Check topic configuration
kafka-topics.sh --bootstrap-server localhost:9092 \
  --describe --topic transportation-logistics.cross-docking.events

# Monitor consumer lag
kafka-consumer-groups.sh --bootstrap-server localhost:9092 \
  --describe --group your-consumer-group

# View topic message rates
kafka-run-class.sh kafka.tools.GetOffsetShell \
  --broker-list localhost:9092 \
  --topic transportation-logistics.cross-docking.events
```

### Prometheus Metrics

The service exposes Prometheus metrics at `http://localhost:8095/actuator/prometheus`:

- `kafka_producer_record_send_total` - Total events published
- `kafka_producer_record_error_total` - Failed event publishes
- `kafka_producer_record_send_rate` - Events per second
- `kafka_producer_request_latency_avg` - Average publish latency

### Alert Recommendations

Configure alerts for:

1. **Consumer Lag > 1000 messages**
   - Indicates consumers can't keep up with event production
   - May require scaling consumer instances

2. **Event Publish Failures > 1%**
   - Check Kafka broker health
   - Verify network connectivity

3. **SLA Breaches** (durationMinutes > threshold)
   - Monitor CrossDockCompletedEvent durations
   - Urgent: 60min, High: 90min, Normal: 120min

## Schema Evolution Guidelines

### Adding New Event Types

1. Create new domain event class extending `DomainEvent`
2. Add event to AsyncAPI specification
3. Publish in `EventPublisher` with CloudEvents wrapper
4. Document in this README
5. Notify consumer teams before deployment

### Adding Fields to Existing Events

**Safe Changes** (no consumer update required):
- Adding optional fields
- Adding new enum values (with fallback handling)

**Breaking Changes** (requires coordination):
- Removing fields
- Changing field types
- Renaming fields
- Changing field semantics

### Versioning Strategy

- Event type includes implicit version: `com.paklog.crossdocking.EventType`
- Breaking changes create new event type: `com.paklog.crossdocking.EventTypeV2`
- Both versions published during migration period
- Old version deprecated after all consumers migrate

## Troubleshooting

### Problem: Events Not Being Received

**Checklist**:
1. Verify Kafka broker is running: `docker-compose ps kafka`
2. Check topic exists: `kafka-topics.sh --list --bootstrap-server localhost:9092`
3. Verify consumer group ID is unique
4. Check consumer logs for errors
5. Confirm consumer is subscribed to correct topic name

### Problem: Duplicate Event Processing

**Solution**: Implement idempotent consumers
```java
@Service
public class EventDeduplicationService {
    private final Set<String> processedEventIds =
        Collections.newSetFromMap(new ConcurrentHashMap<>());

    public boolean isProcessed(String eventId) {
        return !processedEventIds.add(eventId);
    }
}
```

### Problem: CloudEvents Deserialization Error

**Solution**: Ensure correct deserializer configuration
```java
config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
    CloudEventDeserializer.class);
// Or for JSON payloads:
config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
    JsonDeserializer.class);
```

## Additional Resources

- [CloudEvents Specification](https://cloudevents.io/)
- [AsyncAPI Specification](https://www.asyncapi.com/)
- [Apache Kafka Documentation](https://kafka.apache.org/documentation/)
- [Springwolf Documentation](https://www.springwolf.dev/)
- [Confluent Kafka Tutorials](https://docs.confluent.io/platform/current/tutorials/index.html)

## Support

For questions or issues related to cross-docking events:

- **Platform Team**: platform@paklog.com
- **Slack Channel**: #paklog-platform
- **Issue Tracker**: https://jira.paklog.internal/projects/CROSSDOCK
