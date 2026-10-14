# Address search with partial match and autocomplete

## Description
As a support agent, I want to search addresses by district, building number,
or short address code (e.g. "ABCD 1234") with partial matching,
so that I can quickly attach the correct address to a ticket.

## Scope
- Seed ~30 fake addresses on startup (district, buildingNumber, shortCode, city)
- `GET /api/addresses/search?q=...` → up to 10 suggestions
- Search is case-insensitive and matches any of the three fields
- `PUT /api/tickets/{id}/address/{addressId}` → attach address to ticket

## Acceptance Criteria
- [ ] Query shorter than 2 characters returns an empty list
- [ ] Partial input (e.g. "abc" or "12") returns matching results
- [ ] Results are limited to 10
- [ ] Attaching an address fills the address fields on the ticket
- [ ] Each search request is logged (query, user, timestamp)
