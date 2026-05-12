package org.dzianisbova.parceldelivery.integration.base;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.postgresql.PostgreSQLContainer;

@SpringBootTest
public abstract class BasePostgresIntegrationTest {
    private static final String PG_IMAGE_VERSION = "postgres:17-alpine";

    @ServiceConnection
    static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer(PG_IMAGE_VERSION);

    static {
        POSTGRES.start();
    }
}
