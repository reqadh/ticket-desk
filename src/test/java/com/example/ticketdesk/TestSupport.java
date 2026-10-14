package com.example.ticketdesk;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

final class TestSupport {

    private static final ObjectMapper JSON = new ObjectMapper();

    private TestSupport() {
    }

    static long createTicket(MockMvc mvc, String priority) throws Exception {
        String body = mvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Printer not working\",\"priority\":\"" + priority + "\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return read(body).get("id").asLong();
    }

    static void changeStatus(MockMvc mvc, long id, String newStatus) throws Exception {
        mvc.perform(put("/api/tickets/" + id + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"" + newStatus + "\"}"))
                .andExpect(status().isOk());
    }

    static JsonNode read(String json) throws Exception {
        return JSON.readTree(json);
    }
}
