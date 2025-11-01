package com.paklog.crossdocking.infrastructure.config;

import io.github.springwolf.core.asyncapi.components.ComponentsService;
import io.github.springwolf.core.asyncapi.scanners.classes.SpringwolfClassScanner;
import io.github.springwolf.core.asyncapi.scanners.channels.ChannelPriority;
import io.github.springwolf.core.asyncapi.scanners.channels.ChannelsScanner;
import io.github.springwolf.core.asyncapi.scanners.channels.annotations.SpringAnnotationChannelsScanner;
import io.github.springwolf.core.asyncapi.scanners.operations.OperationsScanner;
import io.github.springwolf.core.asyncapi.scanners.operations.annotations.SpringAnnotationOperationsScanner;
import io.github.springwolf.kafka.configuration.properties.SpringwolfKafkaConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AsyncAPI documentation configuration using Springwolf.
 *
 * This configuration enables automatic generation of AsyncAPI documentation
 * for Kafka events published by this service. The documentation can be accessed at:
 *
 * - AsyncAPI UI: http://localhost:8095/springwolf/asyncapi-ui.html
 * - AsyncAPI JSON: http://localhost:8095/springwolf/docs
 * - AsyncAPI YAML: http://localhost:8095/springwolf/docs.yaml
 *
 * Features:
 * - Automatic schema generation from domain event classes
 * - CloudEvents integration
 * - Interactive documentation UI
 * - Support for multiple environments (dev, staging, production)
 */
@Configuration
public class AsyncApiConfig {

    /**
     * Configure Springwolf to scan domain event classes for AsyncAPI generation.
     *
     * This scanner looks for domain events in the specified package and generates
     * schemas based on the class structure.
     */
    @Bean
    public SpringwolfClassScanner springwolfClassScanner() {
        return new SpringwolfClassScanner("com.paklog.crossdocking.domain.event");
    }
}
