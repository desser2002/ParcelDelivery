package org.dzianisbova.parceldelivery.shipment.infrastructure.web;

import org.dzianisbova.parceldelivery.domain.model.Dimensions;
import org.dzianisbova.parceldelivery.domain.model.Parcel;
import org.dzianisbova.parceldelivery.domain.model.Priority;
import org.dzianisbova.parceldelivery.shipment.application.ShipmentNotFoundException;
import org.dzianisbova.parceldelivery.shipment.application.ShipmentService;
import org.dzianisbova.parceldelivery.shipment.domain.model.Address;
import org.dzianisbova.parceldelivery.shipment.domain.model.Shipment;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShipmentController.class)
class ShipmentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ShipmentService shipmentService;
    private static final String VALID_REQUEST = """
            {
              "pickupAddress":  { "street": "Pickup St",  "building": "1", "city": "Warsaw",  "country": "PL" },
              "recipient": "Alice",
              "deliveryAddress": { "street": "Delivery Ave", "building": "99", "city": "Krakow", "country": "DE" },
              "parcel": { "length": 10.0, "width": 10.0, "height": 10.0, "weight": 2.0, "priority": "STANDARD" }
            }
            """;

    private static Shipment anyShipment(String trackingNumber) {
        return new Shipment(
                UUID.randomUUID(), trackingNumber, null,
                new Address("Pickup St", "1", null, "Warsaw", "00-001", "PL"),
                "Alice",
                new Address("Delivery Ave", "99", null, "Krakow", "30-001", "DE"),
                new Parcel("parcel-1", new Dimensions(10.0, 10.0, 10.0), 2.0, false, Priority.STANDARD),
                LocalDateTime.now()
        );
    }

    @Nested
    class CreateShipment {
        @Test
        void validRequest_returns201WithTrackingNumberAndStatus() throws Exception {
            when(shipmentService.createShipment(any(), any(), any(), any()))
                    .thenReturn(anyShipment("TRACK123"));

            mockMvc.perform(post("/shipments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(VALID_REQUEST))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.trackingNumber").value("TRACK123"))
                    .andExpect(jsonPath("$.status").value("PENDING"));
        }

        @Test
        void blankRecipient_returns400() throws Exception {
            mockMvc.perform(post("/shipments")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(VALID_REQUEST.replace("\"Alice\"", "\"\"")))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class TrackShipment {
        @Test
        void existingTrackingNumber_returns200WithShipment() throws Exception {
            when(shipmentService.findByTrackingNumber("TRACK123"))
                    .thenReturn(anyShipment("TRACK123"));

            mockMvc.perform(get("/shipments/TRACK123"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.trackingNumber").value("TRACK123"));
        }

        @Test
        void unknownTrackingNumber_returns404WithErrorBody() throws Exception {
            when(shipmentService.findByTrackingNumber("UNKNOWN"))
                    .thenThrow(new ShipmentNotFoundException("UNKNOWN"));

            mockMvc.perform(get("/shipments/UNKNOWN"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404))
                    .andExpect(jsonPath("$.message").value(containsString("UNKNOWN")));
        }
    }
}
