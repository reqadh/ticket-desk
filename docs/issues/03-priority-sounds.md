# Priority-based notification sounds

## Description
As a support agent, I want to hear a different sound for each ticket priority
when a new ticket arrives, so that I can tell urgent tickets apart without looking at the screen.

## Scope
- Priorities: URGENT, NORMAL, LOW
- Sound mapping in `application.yml` (priority → sound file path)
- Sound files stored under `src/main/resources/static/sounds/`
- Server-Sent Events endpoint: `GET /api/notifications/stream`
- Simple HTML page that listens to the stream and plays the matching sound

## Acceptance Criteria
- [ ] Creating a ticket pushes an event with its priority and sound path
- [ ] Each priority plays a different sound
- [ ] Multiple tickets arriving together play sequentially (queue, no overlap)
- [ ] Unknown priority falls back to the NORMAL sound
