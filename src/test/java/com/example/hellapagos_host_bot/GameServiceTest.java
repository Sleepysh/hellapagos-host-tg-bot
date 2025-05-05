package com.example.hellapagos_host_bot;

import com.example.hellapagos_host_bot.model.GameCharacter;
import com.example.hellapagos_host_bot.model.GameEvent;
import com.example.hellapagos_host_bot.service.BotMessageService;
import com.example.hellapagos_host_bot.service.CharacterService;
import com.example.hellapagos_host_bot.service.EventService;
import com.example.hellapagos_host_bot.service.GameService;
import org.junit.jupiter.api.BeforeAll;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {
    @InjectMocks
    private GameService gameService;
    @Mock
    private BotMessageService messageService;
    @Mock
    private CharacterService characterService;
    @Mock
    private EventService eventService;



    @Test
    void newGameQuery_shouldCallFastMessageAndAddToAwaiting() {
        Long chatId = 123L;
        gameService.newGameQuery(chatId);

        assertTrue(gameService.isAwaiting(chatId));
        verify(messageService).fastExecute(chatId,"Надішли мені список попаданців");
    }


    @Test
    void removeFromAwaiting_shouldRemoveFromAwaitingForListOfPlayerIfContains() {
        Long chatId = 123L;
        gameService.addToAwaiting(chatId);

        assertTrue(gameService.isAwaiting(chatId));

        gameService.removeFromAwaiting(chatId);

        assertFalse(gameService.isAwaiting(chatId));
    }

    @Test
    void endGame_shouldRemoveFromAwaitingForListOfPlayerAndPlayingRightNowIfContains() {
        Long chatId = 123L;
        gameService.addToAwaiting(chatId);

        assertTrue(gameService.isAwaiting(chatId));

        gameService.removeFromAwaiting(chatId);

        assertFalse(gameService.isAwaiting(chatId));
    }

    @Test
    void testSaveNewPlayers_shouldCreateGameSessionAndSendMenu() {
        Long chatId = 1L;
        String players = "Alice, Bob";

        List<GameCharacter> mockCharacters = List.of(
                new GameCharacter("Лідер", "Я веду всіх!", "Має перевагу в голосуванні"),
                new GameCharacter("Рибалка", "Я ловлю рибу!", "Дає +1 їжі")
        );

        when(characterService.getRandomCharacters(2)).thenReturn(mockCharacters);

        gameService.saveNewPlayers(chatId, players);

        assertTrue(gameService.getPlayingRightNow().containsKey(chatId));
        assertFalse(gameService.getAwaitingForListOfPlayers().contains(chatId));
        assertEquals(2, gameService.getPlayingRightNow().get(chatId).size());

        verify(messageService).fastExecute(
                eq(chatId),
                contains("Список виживальників"),
                anyList(),
                anyList()
        );
    }

    @Test
    void testEndGame_shouldRemoveEverything() {
        Long chatId = 99L;

        gameService.getPlayingRightNow().put(chatId, new HashMap<>());
        gameService.getAwaitingForListOfPlayers().add(chatId);
        gameService.getEventsBathes().put(chatId, new ArrayList<>());

        gameService.endGame(chatId);

        assertFalse(gameService.isPlaying(chatId));
        assertFalse(gameService.isAwaiting(chatId));
        assertNull(gameService.getEventsBathes().get(chatId));
    }

    @Test
    void testViewRoles_shouldSendAssignedRoles() {
        Long chatId = 1L;
        Map<String, GameCharacter> session = new HashMap<>();
        session.put("Alice", new GameCharacter("Роль1", "Цитата1", "Особливість1"));
        session.put("Bob", new GameCharacter("Роль2", "Цитата2", "Особливість2"));
        gameService.getPlayingRightNow().put(chatId, session);

        gameService.viewRoles(chatId);

        verify(messageService).fastExecute(eq(chatId), contains("Призначені ролі гравців"));
    }

    @Test
    void testShuffleRoles_shouldReassignCharacters() {
        Long chatId = 1L;
        CallbackQuery callback = mock(CallbackQuery.class);
        Message message = mock(Message.class);
        when(callback.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(chatId);

        Map<String, GameCharacter> session = new LinkedHashMap<>();
        session.put("Alice", new GameCharacter("old1", "q1", "s1"));
        session.put("Bob", new GameCharacter("old2", "q2", "s2"));

        gameService.getPlayingRightNow().put(chatId, session);

        List<GameCharacter> newCharacters = new LinkedList<>();
        newCharacters.add(new GameCharacter("new1", "q3", "s3"));
        newCharacters.add(new GameCharacter("new2", "q4", "s4"));

        when(characterService.getRandomCharacters(2)).thenReturn(newCharacters);

        gameService.shuffleRoles(callback);

        Map<String, GameCharacter> updated = gameService.getPlayingRightNow().get(chatId);
        assertNotEquals("old1", updated.get("Alice").getName());
        verify(messageService).fastAnswer(callback.getId(), "Я перемішав ваші ролі ☯");
    }

    @Test
    void testGenerateEvent_shouldAddNormalOrRandomEvent() {
        Long chatId = 1L;
        gameService.getEventsBathes().put(chatId, new ArrayList<>());

        GameEvent mockEvent = new GameEvent("Буря", "Дме вітер", "Усі мокрі", "one-time");
        lenient().when(eventService.getRandomEvents()).thenReturn(mockEvent);

        gameService.generateEvent(chatId);

        assertFalse(gameService.getEventsBathes().get(chatId).isEmpty());
    }

//    @Test
//    void testViewAllEvents_shouldSendList() {
//        Long chatId = 1L;
//        String callbackId = "cb1";
//        List<GameEvent> events = List.of(
//                new GameEvent("Подія1", "Цитата1", "Опис1", "one-time"),
//                new GameEvent("Подія2", "Цитата2", "Опис2", "permanent")
//        );
//        gameService.putEvent(chatId, events.getFirst());
//        gameService.putEvent(chatId, events.getLast());
//
//
//        gameService.viewAllEvents(chatId, callbackId);
//
//        verify(messageService).fastExecute(eq(chatId), contains("Всі події"));
//    }

    @Test
    void testViewAllEvents_shouldHandleEmpty() {
        Long chatId = 1L;
        String callbackId = "cb1";

        gameService.getEventsBathes().put(chatId, new ArrayList<>());

        gameService.viewAllEvents(chatId, callbackId);

        verify(messageService).fastAnswer(callbackId, "Події ще не розпочалися 🌊");
    }

    @Test
    void testViewPermanentEvents_shouldFilterAndSend() {
        Long chatId = 1L;
        String callbackId = "cb1";

        List<GameEvent> events = List.of(
                new GameEvent("Шторм", "Буря!", "Ховаємося", "permanent"),
                new GameEvent("Сонце", "Смагає", "Спека", "one-time")
        );
        gameService.getEventsBathes().put(chatId, events);

        gameService.viewPermanentEvents(chatId, callbackId);

        verify(messageService).fastExecute(eq(chatId), contains("постійні події"));
    }

    @Test
    void testViewPermanentEvents_shouldHandleEmpty() {
        Long chatId = 1L;
        String callbackId = "cb1";

        gameService.getEventsBathes().put(chatId, List.of());

        gameService.viewPermanentEvents(chatId, callbackId);

        verify(messageService).fastAnswer(callbackId, "Поки ви не маєте постійних подій 🌊");
    }
}
