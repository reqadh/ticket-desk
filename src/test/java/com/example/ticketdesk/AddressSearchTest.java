package com.example.ticketdesk;

import com.example.ticketdesk.activity.ActivityLogRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.empty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AddressSearchTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ActivityLogRepository activity;

    @Test
    void shortQueryReturnsEmptyList() throws Exception {
        mvc.perform(get("/api/addresses/search").param("q", "a"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void partialShortCodeMatchIsCaseInsensitive() throws Exception {
        mvc.perform(get("/api/addresses/search").param("q", "abc"))
                .andExpect(jsonPath("$", not(empty())))
                .andExpect(jsonPath("$[*].shortCode", everyItem(containsStringIgnoringCase("abc"))));
    }

    @Test
    void matchesDistrictAndBuildingNumber() throws Exception {
        mvc.perform(get("/api/addresses/search").param("q", "palm"))
                .andExpect(jsonPath("$[0].district").value("Palm Gardens"));
        mvc.perform(get("/api/addresses/search").param("q", "10"))
                .andExpect(jsonPath("$", not(empty())));
    }

    @Test
    void resultsAreLimitedToTen() throws Exception {
        // "12" appears in every seeded short code, so there are far more than 10 matches
        mvc.perform(get("/api/addresses/search").param("q", "12"))
                .andExpect(jsonPath("$", hasSize(10)));
        mvc.perform(get("/api/addresses/search").param("q", "1"))
                .andExpect(jsonPath("$.length()", lessThanOrEqualTo(10)));
    }

    @Test
    void attachingAddressFillsTicketFields() throws Exception {
        long id = TestSupport.createTicket(mvc, "NORMAL");
        mvc.perform(put("/api/tickets/" + id + "/address/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.district").value("North Hills"))
                .andExpect(jsonPath("$.buildingNumber").value("1000"))
                .andExpect(jsonPath("$.shortCode").value("ABCD 1200"))
                .andExpect(jsonPath("$.city").value("Sample City"));
    }

    @Test
    void attachingUnknownAddressReturns404() throws Exception {
        long id = TestSupport.createTicket(mvc, "NORMAL");
        mvc.perform(put("/api/tickets/" + id + "/address/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void everySearchIsLogged() throws Exception {
        mvc.perform(get("/api/addresses/search").param("q", "river").header("X-User", "lina"));

        var entries = activity.findByActionOrderByIdAsc("ADDRESS_SEARCH");
        var last = entries.get(entries.size() - 1);
        assertThat(last.getDetails()).isEqualTo("river");
        assertThat(last.getUserName()).isEqualTo("lina");
        assertThat(last.getTimestamp()).isNotNull();
    }
}
