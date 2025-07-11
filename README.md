Hellapagos Host Bot is a Java + Spring Boot based Telegram bot for running a party game inspired by "Hellapagos". It manages players, randomly assigns roles, generates game events, and interacts with users through a Telegram chat interface.
<br/>
_____________________________

<b> Tech Stack</b>: <br/>

* Java 17+ <br/>
* Spring Boot<br/>
* Spring Data JPA<br/>
* PostgreSQL<br/>
* TelegramBots Java Library (`org.telegram.telegrambots`)<br/>
* Maven<br/>
_________________________

<b>  Features</b>:<br/>

* `/start`: Initializes bot and resets previous game state if needed.<br/>

* `/menu`: Displays game control menu.<br/>

* Inline buttons for:<br/>

* Starting a new game<br/>

* Approving players<br/>

* Viewing roles<br/>

* Generating random events<br/>

* Shuffling roles<br/>

* Viewing all/permanent events<br/>

* Ending or restarting the game<br/>

* Random selection of characters and events from the database<br/>

* Multi-player session management per chat<br/>

* Message personalization using emojis and styled responses<br/>

____________________________

<b> Class explanations</b> </br>
<i><b>GameCharacter.java</b></i><br/>
Represents a character in the game (name, quote, special rules).<br/>
Used to randomly assign characters to players. It's mapped to the character table in the database.<br/>

<i><b>GameEvent.java</b></i><br/>
Represents a random game event (event name, description, quote, duration).<br/>
Used to spice up the game by generating events that affect gameplay. Mapped to the event table in the database.<br/>

<i><b>CharacterRepository.java</b></i><br/>
Database access layer for characters.<br/>
Provides methods (like `findRandomCharacters`) to fetch random characters from the DB.<br/>

<i><b>EventsRepository.java</b></i><br/>
Database access layer for events.<br/>
Fetches random or all events from the database using custom queries.<br/>

<i><b>GameService.java</b></i><br/>
Handles core game logic like player registration, assigning roles, generating events.<br/>
Keeps game state organized and reusable. Acts like a controller behind the scenes.<br/>

<i><b>BotMessageService.java</b></i><br/>
Builds all the Telegram messages and inline buttons.<br/>
Separates message logic from handlers. Helps keep responses clean and dynamic.<br/>

<i><b>MyTgBot.java</b></i><br/>
The main bot class. Registers with Telegram and delegates updates.<br/>
It's the entry point for receiving updates from Telegram — like a controller that splits message and button logic.<br/>

<i><b>MessageHandler.java</b></i><br/>
Handles user messages like `/start`, `/menu`, or any name inputs.<br/>
Used to parse and act on text commands. Also captures player names during game setup.<br/>

<i><b>ButtonHandler.java</b></i><br/>
Handles all button clicks — like starting the game, shuffling roles, showing events.<br/>
Executes actions when users press inline buttons and updates the game state accordingly.<br/>

____________________________

<b>📬 Message Flow</b> <br/>

<b><i>MyTgBot.java</b></i> <br/>
Delegates incoming Update objects:<br/>

<i>Messages → MessageHandler</i><br/>

Callback buttons → ButtonHandler<br/>

<b><i>MessageHandler.java</b></i> <br/>
Handles user commands:<br/>

`/start` — Sends greeting and resets state<br/>

`/menu` — Shows game menu (if game is active)<br/>

Any other text during awaiting → Saved as player name<br/>

Otherwise → Sends fun random emoji message<br/>

<b><i>ButtonHandler.java</b></i> <br/>
Processes inline button callbacks:<br/>

`start-game` — Displays welcome menu<br/>

`new-game` — Starts a new player registration<br/>

`approve-players` — Moves to game phase<br/>

`shuffle-roles` — Assigns roles randomly<br/>

`get-event` — Picks a random game event<br/>

`view-all-events` — Lists all events<br/>

`view-permanent-events` — Lists only persistent ones<br/>

`restart-game` / `end-game` — Game session reset<br/>
#   h e l l a p a g o s - h o s t - t g - b o t 
 
 
