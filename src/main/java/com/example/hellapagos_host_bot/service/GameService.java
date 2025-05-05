package com.example.hellapagos_host_bot.service;

import com.example.hellapagos_host_bot.model.GameCharacter;
import com.example.hellapagos_host_bot.model.GameEvent;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;


import java.util.*;
import java.util.stream.Collectors;

@Component
public class GameService {
    private final HashMap<Long, Map<String,GameCharacter>> playingRightNow = new HashMap<>();
    private final HashMap<Long, List<GameEvent>> eventsBathes = new HashMap<>();
    private final Set<Long> awaitingForListOfPlayers= new HashSet<>();
    private final BotMessageService messageService;
    private final CharacterService characterService;
    private final EventService eventService;

    public GameService(BotMessageService messageService, CharacterService characterService, EventService eventService) {
        this.messageService = messageService;
        this.characterService = characterService;
        this.eventService = eventService;
    }

    public HashMap<Long, Map<String, GameCharacter>> getPlayingRightNow() {
        return playingRightNow;
    }

    public HashMap<Long, List<GameEvent>> getEventsBathes() {
        return eventsBathes;
    }

    public Set<Long> getAwaitingForListOfPlayers() {
        return awaitingForListOfPlayers;
    }

    public void newGameQuery(Long chatId) {
            messageService.fastExecute(chatId,"Надішли мені список попаданців");
            addToAwaiting(chatId);

    }

    public void addToAwaiting(Long chatId) {
        awaitingForListOfPlayers.add(chatId);
    }

    public boolean isAwaiting(Long chatId) {
        return awaitingForListOfPlayers.contains(chatId);
    }

    public boolean isPlaying(Long chatId) {
        return playingRightNow.containsKey(chatId);
    }


    public void removeFromAwaiting(Long chatId) {
        if (awaitingForListOfPlayers.contains(chatId))
            awaitingForListOfPlayers.remove(chatId);

    }

    public void removeFromPlaying(Long chatId) {
        if (playingRightNow.containsKey(chatId))
            playingRightNow.remove(chatId);
    }

    public void endGame(Long chatId) {
        if (playingRightNow.containsKey(chatId))
            playingRightNow.remove(chatId);

        if (awaitingForListOfPlayers.contains(chatId))
            awaitingForListOfPlayers.remove(chatId);

        if (eventsBathes.containsKey(chatId))
            eventsBathes.remove(chatId);
    }

    public void restartGame(CallbackQuery callback) {
        eventsBathes.put(callback.getMessage().getChatId(), new ArrayList<>());
        shuffleRoles(callback);
    }

    public void saveNewPlayers(Long chatId, String players) {
        List<String> listOfPlayers = Arrays.stream(players.split(", ")).collect(Collectors.toSet()).stream().toList();
        List<GameCharacter> listOfCharacters = characterService.getRandomCharacters(listOfPlayers.size());
        Map<String, GameCharacter> gameSession = new HashMap<>();

        for (int i = 0; i < listOfPlayers.size(); i++) {
            gameSession.put(listOfPlayers.get(i), listOfCharacters.get(i));
        }


        playingRightNow.put(chatId, gameSession);

        messageService.fastExecute(chatId,
                convertListOfPlayersToString(listOfPlayers).concat("\n Почнемо круїз?"),
                List.of("\uD83D\uDC79 Почати виживання", "\uD83D\uDE45 Залишити острів"),
                List.of("approve-players", "cancel-game-creating"));

        eventsBathes.put(chatId, new ArrayList<>());


        removeFromAwaiting(chatId);
    }

    private static String convertListOfPlayersToString(List<String> players) {
        StringBuilder enumerationPlayers = new StringBuilder("\uD83D\uDCCB Список виживальників:\n");
        for(int i = 0; i < players.size(); i++) {
            enumerationPlayers.append("🧍 ").append(i + 1).append(". ").append(players.get(i)).append("\n");
        }
        return enumerationPlayers.toString();
    }

    public void gameMenu(Long chatId) {
        String message = """
            🏝️ *Ви на острові...*

            ⛵ Пліт ще не готовий.
            🍶 Вода обмежена.
            🍽️ Їжі замало.
            🧍 Та гравців — більше, ніж потрібно.

            Думайте як вижити 👹
            """;

        messageService.fastExecute(
                chatId,
                message,
                List.of(
                        "👀 Переглянути ролі",
                        "📜 Отримати подію",
                        "\uD83D\uDCDDПереглянути всі події",
                        "\uD83D\uDD04Переглянути постійні події",
                        "♻️ Змінити ролі",
                        "🔁 Почати з початку",
                        "🏁 Завершити гру"
                ),
                List.of(
                        "view-roles",
                        "get-event",
                        "view-all-events",
                        "view-permanent-events",
                        "shuffle-roles",
                        "restart-game",
                        "end-game"
                )
        );
    }

    public void viewRoles(Long chatId) {
        messageService.fastExecute(chatId, getListOfAssignedRoles(chatId));
    }

    private String getListOfAssignedRoles(Long chatId) {
        StringBuilder listOfAssignedRoles = new StringBuilder("🎭 *Призначені ролі гравців:*\n\n");

        for (Map.Entry<String, GameCharacter> role : playingRightNow.get(chatId).entrySet()) {
            listOfAssignedRoles
                    .append("🧍‍♂️ *").append(role.getKey()).append("*\n")
                    .append("🔸 Роль: _").append(role.getValue().getName()).append("_\n")
                    .append("💬 _«").append(role.getValue().getQuote()).append("»_ \n")
                    .append("📜 *Особливість:* ").append(role.getValue().getSpecialConditions())
                    .append("\n\n");
        }

        return listOfAssignedRoles.toString();
    }

    public void shuffleRoles(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        int size = playingRightNow.get(chatId).size();
        List<GameCharacter> listOfCharacters = characterService.getRandomCharacters(size);

        for (Map.Entry<String, GameCharacter> role : playingRightNow.get(chatId).entrySet()) {
            playingRightNow.get(chatId).put(role.getKey(), listOfCharacters.removeFirst());
        }

        messageService.fastAnswer(callbackQuery.getId(), "Я перемішав ваші ролі ☯");

    }


    public void generateEvent(Long chatId) {
        int value = (int)(Math.random()*100);
        if (value <= 20) {
            eventsBathes.get(chatId).add(new GameEvent("Звичайний день \uD83C\uDFDD",
                    "Сонце повільно заходить за горизонт, хвилі ніжно омивають берег... \uD83C\uDF05 \n",
                    "Цього разу день минає спокійно. Жодних пригод, жодних неприємностей.",
                    "one-time"));
        } else {
            eventsBathes.get(chatId).add(eventService.getRandomEvents());
        }

        getLastEvent(chatId);
    }

    private void getLastEvent(Long chatId) {
        GameEvent event = eventsBathes.get(chatId).getLast();

        String formattedEvent = """
        🌴 *%s* 🌴

        _"%s"_

        %s
        """.formatted(
                (event.getDuration().equals("permanent") ? "\uD83D\uDD04 " : "1\uFE0F⃣ ") + event.getName(),
                event.getQuote(),
                event.getEventDescription()
        );
        messageService.fastExecute(chatId, formattedEvent);
    }

    public void viewAllEvents(Long chatId, String callbackId) {
        List<GameEvent> events = eventsBathes.get(chatId);

        if (events == null || events.isEmpty()) {
            messageService.fastAnswer(callbackId, "Події ще не розпочалися 🌊");
            return;
        }

        StringBuilder allEvents = new StringBuilder("📝 *Всі події до цього моменту:*\n\n");

        for (GameEvent event : events) {
            allEvents.append("🌴 *").append(event.getName()).append("*\n")
                    .append("_\"").append(event.getQuote()).append("\"_\n")
                    .append(event.getEventDescription()).append("\n\n ➖➖➖➖➖ \n");
        }

        messageService.fastExecute(chatId, allEvents.toString(), List.of("\uD83D\uDD04 Постійні події"), List.of("view-permanent-events"));
    }

    public void putEvent(Long chatId, GameEvent gameEvent) {
        if (eventsBathes.containsKey(chatId)) {
            eventsBathes.get(chatId).add(gameEvent);

        } else {
            eventsBathes.put(chatId, new ArrayList<>(List.of(gameEvent)));
        }

    }

    public void viewPermanentEvents(Long chatId, String callbackId) {

        List<GameEvent> events = eventsBathes.get(chatId)
                                  .stream()
                                  .filter(event -> event.getDuration().equals("permanent")).toList();

        if (events == null || events.isEmpty()) {
            messageService.fastAnswer(callbackId, "Поки ви не маєте постійних подій 🌊");
            return;
        }

        StringBuilder allEvents = new StringBuilder("📝 *Всі події постійні події до цього моменту:*\n\n");

        for (GameEvent event : events) {
            allEvents.append("🌴 *").append(event.getName()).append("*\n")
                    .append("_\"").append(event.getQuote()).append("\"_\n")
                    .append(event.getEventDescription()).append("\n\n ➖➖➖➖➖ \n");
        }

        messageService.fastExecute(chatId, allEvents.toString());
    }


}
