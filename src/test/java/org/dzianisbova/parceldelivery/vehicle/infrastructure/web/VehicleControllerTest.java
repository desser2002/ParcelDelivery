package org.dzianisbova.parceldelivery.vehicle.infrastructure.web;

import org.dzianisbova.parceldelivery.integration.base.BasePostgresIntegrationTest;
import org.dzianisbova.parceldelivery.vehicle.infrastructure.persistence.VehicleJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class VehicleControllerTest extends BasePostgresIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VehicleJpaRepository vehicleJpaRepository;

    private static final String VALID_REQUEST = """
        {
        "plateNumber" : "WA12345",
            "dimensions" :{ "length": 400.0, "width": 200.0, "height": 200.0 },
            "maxWeight": 1000.0
        }
        """;

    @Test
    void create_onValidRequest_saveVehicleToDB() throws Exception {
        mockMvc.perform(post("/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_REQUEST))
            .andExpect(status().isCreated()
            );

        assertThat(vehicleJpaRepository.count()).isEqualTo(1);
    }

    @Test
    void create_inValidRequestEmpty_DoNotSaveVehicleToDB() throws Exception {
        mockMvc.perform(post("/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest()
            );

        assertThat(vehicleJpaRepository.count()).isZero();
    }

    @AfterEach
    void cleanUp()
    {
        vehicleJpaRepository.deleteAll();
    }
}