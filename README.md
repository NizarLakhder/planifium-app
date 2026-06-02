# Planifium — University Course Planning Web App

Planifium is a full-stack web application that helps students search, compare, and plan university courses.

The platform provides course search, schedule conflict detection, prerequisite checks, course comparison, student reviews, and program-based navigation. The interface is currently available in French and was designed for university course planning.

The project was initially developed for the IFT2255 course and later improved with a cleaner user interface, a Java REST API, a Discord bot for collecting student feedback, and aggregated academic data.

---

## Features

- **Course catalog** — search by course code, keywords, and semester
- **Course details** — section schedules, prerequisites, and equivalents
- **Student reviews** — difficulty and workload ratings collected via Discord bot
- **Academic results** — average grade, score, and participant count per course
- **Course comparison** — side-by-side comparison table
- **Eligibility check** — based on completed courses and study cycle
- **Schedule conflict detection** — between two courses for a given semester
- **Program navigation** — browse all courses in a program by program ID

---

## Tech Stack

| Component | Technology |
|-----------|------------|
| REST API | Java 17 · Javalin 6.7 · Jackson |
| Frontend | HTML · CSS · JavaScript (Vanilla) |
| Discord Bot | Python 3.8+ · discord.py · Mistral AI |
| Tests | JUnit 5 · Mockito |
| Build Tool | Maven 3.8+ |
| Documentation | MkDocs · Material for MkDocs |
| Data | External course API · JSON files |

---

## Getting Started

### 1. REST API + Web Interface

```bash
cd rest-api
mvn clean package -DskipTests
java -jar target/rest-api-1.0-SNAPSHOT.jar
```

The web interface will be available at:

```
http://localhost:7070
```

### 2. Discord Bot

Create a `discord/.env` file:

```
DISCORD_TOKEN=your_discord_token
CLE_API=your_mistral_api_key
```

```bash
cd discord
python bot.py
```

The bot listens for natural language messages, extracts course review information using Mistral AI, asks for confirmation, and submits the review to the API.

**Example usage:**
> *"IFT2255 is approachable, difficulty 2/5, workload 3/5, good introductory course"*

### 3. Run the Documentation

```bash
mkdocs serve
```

The documentation will be available at:

```
http://localhost:8000
```

---

## Tests

```bash
cd rest-api
mvn test
```

---

## Project Structure

```
planifium/
├── discord/            # Discord bot for collecting student reviews (Python + Mistral AI)
│   ├── bot.py
│   └── .env            # Not versioned
├── docs/               # Project documentation (MkDocs)
├── rest-api/           # Java REST API + web frontend
│   ├── data/           # Persistent JSON data (reviews, academic results)
│   ├── src/
│   │   ├── main/java/  # Controllers, services, models
│   │   └── test/java/  # Unit tests
│   ├── src/main/resources/public/  # Static web frontend
│   └── pom.xml
└── mkdocs.yml
```

---

## Demo

The application runs locally at:

```
http://localhost:7070
```
