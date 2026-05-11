package org.dzianisbova.parceldelivery.sortingcenter.infrastructure.web;

import org.dzianisbova.parceldelivery.integration.base.BasePostgresIntegrationTest;
import org.dzianisbova.parceldelivery.sortingcenter.infrastructure.persistence.SortingCenterJpaRepository;
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
class SortingCenterControllerTest extends BasePostgresIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    private static final String VALID_REQUEST = """
        {
        "name": "Hub-Warsaw-01"
        }
        """;
    @Autowired
    private SortingCenterJpaRepository sortingCenterJpaRepository;

    @Test
    void create_onValidRequest_saveSortingCenterToDB() throws Exception {
        mockMvc.perform(post("/sorting-center")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_REQUEST))
            .andExpect(status().isCreated());

        assertThat(sortingCenterJpaRepository.count()).isEqualTo(1);
    }

    @Test
    void create_onInValidRequest_doNotSaveSortingCenterToDB() throws Exception {
        mockMvc.perform(post("/sorting-center")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());

        assertThat(sortingCenterJpaRepository.count()).isZero();
    }

    @AfterEach
    void cleanUp()
    {
        sortingCenterJpaRepository.deleteAll();
    }
}