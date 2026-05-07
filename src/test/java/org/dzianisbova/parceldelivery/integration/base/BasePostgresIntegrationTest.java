package org.dzianisbova.parceldelivery.integration.base;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

@Testcontainers
@SpringBootTest
public abstract class BasePostgresIntegrationTest {
    private static String PG_IMAGE_VERSION = "postgres:17-alpine";

    @ServiceConnection
    @Container
    static PostgreSQLContainer postgres = new PostgreSQLContainer(PG_IMAGE_VERSION);
}
