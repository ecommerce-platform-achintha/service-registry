package com.achintha.serviceregistry;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.ApplicationContext;

import com.netflix.eureka.registry.PeerAwareInstanceRegistry;

// Config Server is disabled so the test doesn't depend on (or wait for) a running instance
@SpringBootTest(properties = "spring.cloud.config.enabled=false")
class ServiceRegistryApplicationTests {

    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoadsWithEurekaServerEnabled() {
        assertThat(context.getBeansWithAnnotation(EnableEurekaServer.class)).hasSize(1);
        // The instance registry only exists when @EnableEurekaServer switches on the server auto-configuration
        assertThat(context.getBeansOfType(PeerAwareInstanceRegistry.class)).hasSize(1);
    }
}
