# Discord Bot — Student Course Reviews

A Discord bot built in Python using the `discord.py` library (`bot.py`) that allows students to submit course reviews through natural language messages.

## How it works

1. A student sends a message describing their experience with a course.
2. The bot uses **Mistral AI** to detect whether the message is a course review.
3. If it is, the bot extracts the relevant information (course code, difficulty, workload, comment).
4. The student is asked to confirm before submission.
5. Upon confirmation, the review is sent to the REST API via `POST /api/avis`.

## Setup

Create a `.env` file in the `discord/` folder:

```
DISCORD_TOKEN=your_discord_token
CLE_API=your_mistral_api_key
```

Then run:

```bash
python bot.py
```

## Integration

- The bot stores no data locally.
- All reviews are persisted by the backend at `rest-api/data/avis.json`.
- The REST API must be running on `http://localhost:7070` for the bot to submit reviews.
