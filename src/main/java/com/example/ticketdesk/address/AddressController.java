package com.example.ticketdesk.address;

import com.example.ticketdesk.activity.ActivityLogService;
import com.example.ticketdesk.common.CurrentUser;
import com.example.ticketdesk.ticket.Ticket;
import com.example.ticketdesk.ticket.TicketService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class AddressController {

    static final int MIN_QUERY_LENGTH = 2;
    static final int MAX_RESULTS = 10;

    private final AddressRepository addresses;
    private final TicketService tickets;
    private final ActivityLogService activity;

    public AddressController(AddressRepository addresses, TicketService tickets, ActivityLogService activity) {
        this.addresses = addresses;
        this.tickets = tickets;
        this.activity = activity;
    }

    @GetMapping("/api/addresses/search")
    public List<Address> search(@RequestParam(defaultValue = "") String q,
                                @RequestHeader(value = CurrentUser.HEADER, required = false) String user) {
        String query = q.trim();
        activity.record(null, "ADDRESS_SEARCH", CurrentUser.resolve(user), query);
        if (query.length() < MIN_QUERY_LENGTH) {
            return List.of();
        }
        return addresses.search(query, PageRequest.of(0, MAX_RESULTS));
    }

    @PutMapping("/api/tickets/{ticketId}/address/{addressId}")
    @Transactional
    public Ticket attach(@PathVariable Long ticketId,
                         @PathVariable Long addressId,
                         @RequestHeader(value = CurrentUser.HEADER, required = false) String user) {
        Ticket ticket = tickets.get(ticketId);
        Address address = addresses.findById(addressId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address " + addressId + " not found"));
        ticket.setAddress(address.getDistrict(), address.getBuildingNumber(), address.getShortCode(), address.getCity());
        activity.record(ticketId, "ADDRESS_ATTACHED", CurrentUser.resolve(user), address.getShortCode());
        return ticket;
    }
}
