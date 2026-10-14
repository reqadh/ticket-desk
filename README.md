# ticket-desk-sample

A small sample service-ticket desk built with **Java 17, Spring Boot 3, Spring Data JPA and H2**
(in-memory database, no setup needed). All data in this project is fake.

## Features

| # | Feature | Endpoints |
|---|---------|-----------|
| 1 | Append-only notes and reopen with mandatory reason | `POST/GET /api/tickets/{id}/notes`, `POST /api/tickets/{id}/reopen` |
| 2 | Address search with partial match and autocomplete | `GET /api/addresses/search?q=`, `PUT /api/tickets/{id}/address/{addressId}` |
| 3 | Priority-based notification sounds | `GET /api/notifications/stream` (Server-Sent Events) + demo page at `/` |

Issue descriptions with acceptance criteria are in [`docs/issues`](docs/issues).

Supporting endpoints: `POST /api/tickets`, `GET /api/tickets/{id}`, `PUT /api/tickets/{id}/status`,
`GET /api/tickets/{id}/activity`.

There is no login in this sample; pass the acting user's name in the `X-User` header
(defaults to `agent`).

## Run

```bash
mvn spring-boot:run
```

Then open http://localhost:8080, click **Enable sound**, and create test tickets.

## Test

```bash
mvn test
```

## Example requests

```bash
# create a ticket
curl -X POST localhost:8080/api/tickets -H 'Content-Type: application/json' \
  -d '{"title":"Printer not working","priority":"URGENT"}'

# add a note
curl -X POST localhost:8080/api/tickets/1/notes -H 'X-User: sara' -H 'Content-Type: application/json' \
  -d '{"text":"Checked the cable"}'

# close, then reopen with a reason
curl -X PUT localhost:8080/api/tickets/1/status -H 'Content-Type: application/json' -d '{"status":"CLOSED"}'
curl -X POST localhost:8080/api/tickets/1/reopen -H 'Content-Type: application/json' -d '{"reason":"Issue came back"}'

# search addresses
curl 'localhost:8080/api/addresses/search?q=abcd'
```
