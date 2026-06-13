import discord
from discord.ext import commands
import aiohttp
import os
import json
import re
from dotenv import load_dotenv

# =========================
# ENV
# =========================
load_dotenv()
DISCORD_TOKEN = os.getenv("DISCORD_TOKEN")
MISTRAL_API_KEY = os.getenv("CLE_API")

if not DISCORD_TOKEN or not MISTRAL_API_KEY:
    raise RuntimeError("Variables d’environnement manquantes")

# =========================
# DISCORD
# =========================
intents = discord.Intents.default()
intents.message_content = True
bot = commands.Bot(command_prefix="!", intents=intents)

# =========================
# ÉTAT
# =========================
pending_reviews = {}

# =========================
# LLM CALL
# =========================
async def mistral_call(prompt: str) -> str:
    async with aiohttp.ClientSession() as session:
        async with session.post(
            "https://api.mistral.ai/v1/chat/completions",
            headers={
                "Authorization": f"Bearer {MISTRAL_API_KEY}",
                "Content-Type": "application/json"
            },
            json={
                "model": "mistral-small",
                "messages": [{"role": "user", "content": prompt}],
                "temperature": 0
            }
        ) as resp:
            data = await resp.json()
            return data["choices"][0]["message"]["content"]

# =========================
# AVIS OU PAS
# =========================
async def is_course_review(message: str) -> bool:
    prompt = f"""
Réponds STRICTEMENT par OUI ou NON.

Un avis de cours :
- mentionne un cours (ex: MAT1400, IFT2255 , Génie logiciel , programmation 1 )
- parle de difficulté, charge ou expérience

Message :
\"\"\"{message}\"\"\"
"""
    return (await mistral_call(prompt)).strip().upper() == "OUI"

# =========================
# EXTRACTION AVIS
# =========================
def extract_json(text: str) -> dict:
    match = re.search(r"\{.*\}", text, re.DOTALL)
    if not match:
        raise ValueError("JSON introuvable")
    return json.loads(match.group())

async def extract_review(message: str) -> dict:
    prompt = f"""
Retourne UNIQUEMENT un JSON avec :
- cours
- difficulte (1-5)
- charge (1-5)
- commentaire

Message :
\"\"\"{message}\"\"\"
"""
    raw = await mistral_call(prompt)
    avis = extract_json(raw)

    avis["cours"] = avis["cours"].upper()
    avis["difficulte"] = int(avis["difficulte"])
    avis["charge"] = int(avis["charge"])
    avis["commentaire"] = avis["commentaire"].strip()

    return avis

# =========================
# EVENTS
# =========================
@bot.event
async def on_ready():
    print("Bot prêt")

@bot.event
async def on_message(message: discord.Message):
    if message.author == bot.user:
        return

    user_id = message.author.id
    content = message.content.strip()

    # ---- CONFIRMATION ----
    if user_id in pending_reviews:
        if content.upper() == "OUI":
            avis = pending_reviews.pop(user_id)

            async with aiohttp.ClientSession() as session:
                async with session.post(
                    "http://localhost:7070/api/avis",
                    json=avis
                ):
                    await message.channel.send(" Avis enregistré")
            return

        if content.upper() == "NON":
            pending_reviews.pop(user_id)
            await message.channel.send(" Avis annulé")
            return

        await message.channel.send("Merci de répondre par OUI ou NON.")
        return

    # ---- DÉTECTION ----
    if not await is_course_review(content):
        await message.channel.send(" Ce message ne concerne pas un avis de cours.")
        return

    # ---- EXTRACTION ----
    try:
        avis = await extract_review(content)
    except Exception:
        await message.channel.send(" Je n’ai pas compris l’avis.")
        return

    avis["auteur"] = str(message.author)
    pending_reviews[user_id] = avis

    await message.channel.send(
        f" J’ai compris :\n"
        f"Cours : {avis['cours']}\n"
        f"Difficulté : {avis['difficulte']} / 5\n"
        f"Charge : {avis['charge']} / 5\n"
        f"Commentaire : {avis['commentaire']}\n\n"
        "Confirme-tu ? (OUI / NON)"
    )

# =========================
# RUN
# =========================
bot.run(DISCORD_TOKEN)