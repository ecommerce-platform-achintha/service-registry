# service-registry

Spring Cloud Netflix Eureka server that microservices register with and discover each other through.
Runs in **standalone mode** (a single registry, not a peer-replicated cluster), so it does not register
with or fetch from any other Eureka server.

- Java 25, Spring Boot 4.1.1, Spring Cloud 2025.1.3
- Port: `8761`
- Pulls optional config from the Config Server at `http://localhost:8888` (override with `CONFIG_SERVER_URL`).
  The import is `optional:`, so the registry still starts if the Config Server is down.

## Run locally

```bash
mvn spring-boot:run
```

Or build and run the jar:

```bash
mvn clean package
java -jar target/service-registry-0.0.1-SNAPSHOT.jar
```

If the Config Server is running (see the `config-server` repo), start it first so its config is applied.

## Run with Docker

```bash
docker build -t service-registry .
docker run --rm -p 8761:8761 service-registry
```

Inside a container, `localhost` is the container itself, so point it at the Config Server explicitly, e.g.:

```bash
# Config Server running on the host machine
docker run --rm -p 8761:8761 \
  -e CONFIG_SERVER_URL=http://host.docker.internal:8888 \
  --add-host=host.docker.internal:host-gateway \
  service-registry

# Config Server running as a container on the same Docker network
docker run --rm -p 8761:8761 --network <network> \
  -e CONFIG_SERVER_URL=http://config-server:8888 \
  service-registry
```

## Confirm it's up

- **Eureka dashboard:** open http://localhost:8761. Registered services show up under
  "Instances currently registered with Eureka".
- **Health check:**

  ```bash
  curl http://localhost:8761/actuator/health
  # {"status":"UP", ...}
  ```

- **Registry (JSON):**

  ```bash
  curl -H 'Accept: application/json' http://localhost:8761/eureka/apps
  ```

## Registering a client

In a client service, add `spring-cloud-starter-netflix-eureka-client` and set:

```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

## Tests

```bash
mvn test
```

The context test turns the Config Server off (`spring.cloud.config.enabled=false`), so it runs without
any other services.
