# Append-only notes and reopen with mandatory reason

## Description
As a support agent, I want to add notes to a ticket that cannot be edited or deleted,
and reopen closed tickets with a mandatory reason, so that ticket history stays auditable.

## Scope
- `POST /api/tickets/{id}/notes` → add a note (text, author, createdAt)
- `GET /api/tickets/{id}/notes` → return notes ordered by createdAt (oldest first)
- No update/delete endpoints for notes
- `POST /api/tickets/{id}/reopen` → body: `{ "reason": "..." }`
- Every note and reopen action creates an entry in `activity_log`

## Acceptance Criteria
- [ ] Empty note text returns 400
- [ ] Notes are returned in chronological order
- [ ] Reopen without a reason returns 400
- [ ] Reopen is allowed only when the ticket status is CLOSED (otherwise 409)
- [ ] Reopened ticket returns to its previous status (stored before closing)
- [ ] Activity log stores action type, user name, and timestamp
