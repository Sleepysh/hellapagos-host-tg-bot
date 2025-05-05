package com.example.hellapagos_host_bot;

import com.example.hellapagos_host_bot.handler.ButtonHandler;
import com.example.hellapagos_host_bot.service.BotMessageService;
import com.example.hellapagos_host_bot.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ButtonHandlerTest {

    @Mock
    private BotMessageService botMessageService;

    @Mock
    private GameService gameService;

    @Mock
    private CallbackQuery callback;

    @Mock
    private Message message;

    @InjectMocks
    private ButtonHandler buttonHandler;

    @Test
    void handleCallback_startGame_shouldCallStartGameQuery() {
        when(callback.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
        when(callback.getData()).thenReturn("start-game");

        buttonHandler.handleCallback(callback);

        verify(botMessageService).startGameQuery(123L);
        verifyNoMoreInteractions(botMessageService, gameService);
    }

    @Test
    void handleCallback_newGame_shouldCallNewGameQuery() {
        when(callback.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
        when(callback.getData()).thenReturn("new-game");
        when(gameService.isPlaying(message.getChatId())).thenReturn(false);

        buttonHandler.handleCallback(callback);

        verify(gameService).newGameQuery(message.getChatId());
        verifyNoMoreInteractions(botMessageService, gameService);
    }

    @Test
    void handleCallback_newGame_shouldSendFastAnswer() {
        when(callback.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
        when(callback.getId()).thenReturn("callback_id");
        when(callback.getData()).thenReturn("new-game");
        when(gameService.isPlaying(123L)).thenReturn(true);

        buttonHandler.handleCallback(callback);

        verify(botMessageService).fastAnswer("callback_id", "Ви вже на острові!\uD83D\uDC7A");
        verifyNoMoreInteractions(botMessageService, gameService);
    }

    @Test
    void handleCallback_approvePlayers_shouldCallGameMenu() {
        when(callback.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
        when(callback.getData()).thenReturn("approve-players");
        when(gameService.isPlaying(123L)).thenReturn(true);

        buttonHandler.handleCallback(callback);

        verify(gameService).gameMenu(123L);
        verifyNoMoreInteractions(botMessageService, gameService);
    }
    @Test
    void handleCallback_approvePlayers_isPLaying_shouldFastAnswer() {
        when(callback.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
        when(callback.getId()).thenReturn("callback_id");
        when(callback.getData()).thenReturn("approve-players");
        when(gameService.isPlaying(123L)).thenReturn(false);

        buttonHandler.handleCallback(callback);

        verify(botMessageService).fastAnswer(callback.getId(),"Почни нову гру!\uD83D\uDC7A");
        verifyNoMoreInteractions(botMessageService);
    }

    @Test
    void handleCallback_viewRoles_isPlaying_shouldCallViewRoles() {
        when(callback.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
        when(callback.getData()).thenReturn("view-roles");
        when(gameService.isPlaying(123L)).thenReturn(true);

        buttonHandler.handleCallback(callback);

        verify(gameService).viewRoles(message.getChatId());
        verifyNoMoreInteractions(gameService, botMessageService);
    }


    @Test
    void handleCallback_viewRoles_notPlaying_shouldSendFastAnswer() {
        when(callback.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
        when(callback.getId()).thenReturn("callback_id");
        when(callback.getData()).thenReturn("view-roles");
        when(gameService.isPlaying(123L)).thenReturn(false);

        buttonHandler.handleCallback(callback);

        verify(botMessageService).fastAnswer(callback.getId(),"Спершу необхідно вирушити в круїз!\uD83D\uDE24");
        verifyNoMoreInteractions(botMessageService);
    }

    @Test
    void handleCallback_getEvent_isPlaying_shouldCallGenerateEvent() {
        when(callback.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
        when(callback.getData()).thenReturn("get-event");
        when(gameService.isPlaying(123L)).thenReturn(true);

        buttonHandler.handleCallback(callback);

        verify(gameService).generateEvent(message.getChatId());
        verifyNoMoreInteractions(botMessageService, gameService);
    }

    @Test
    void handleCallback_getEvent_notPlaying_shouldCallFastAnswer() {
        when(callback.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
        when(callback.getId()).thenReturn("callback_id");
        when(callback.getData()).thenReturn("get-event");
        when(gameService.isPlaying(123L)).thenReturn(false);

        buttonHandler.handleCallback(callback);

        verify(botMessageService).fastAnswer(callback.getId(),"Спершу необхідно вирушити в круїз!\uD83D\uDE24");
        verifyNoMoreInteractions(botMessageService);
    }

    @Test
    void handleCallback_viewAllEvents_isPlaying_shouldCallViewAllEvents() {
        when(callback.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
        when(callback.getId()).thenReturn("callback_id");
        when(callback.getData()).thenReturn("view-all-events");
        when(gameService.isPlaying(123L)).thenReturn(true);

        buttonHandler.handleCallback(callback);

        verify(gameService).viewAllEvents(message.getChatId(), callback.getId());
        verifyNoMoreInteractions(botMessageService, gameService);
    }


    @Test
    void handleCallback_viewAllEvents_notPlaying_shouldCallFastAnswer() {
        when(callback.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
        when(callback.getId()).thenReturn("callback_id");
        when(callback.getData()).thenReturn("view-all-events");
        when(gameService.isPlaying(123L)).thenReturn(false);

        buttonHandler.handleCallback(callback);

        verify(botMessageService).fastAnswer(callback.getId(),"Спершу необхідно вирушити в круїз!\uD83D\uDE24");
        verifyNoMoreInteractions(botMessageService);
    }

    @Test
    void handleCallback_viewPermanentEvents_isPlaying_shouldCallViewPermanentEvents() {
        when(callback.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
        when(callback.getId()).thenReturn("callback_id");
        when(callback.getData()).thenReturn("view-permanent-events");
        when(gameService.isPlaying(123L)).thenReturn(true);

        buttonHandler.handleCallback(callback);

        verify(gameService).viewPermanentEvents(message.getChatId(), callback.getId());
        verifyNoMoreInteractions(botMessageService, gameService);
    }


    @Test
    void handleCallback_viewPermanentEvents_notPlaying_shouldCallFastAnswer() {
        when(callback.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
        when(callback.getId()).thenReturn("callback_id");
        when(callback.getData()).thenReturn("view-permanent-events");
        when(gameService.isPlaying(123L)).thenReturn(false);

        buttonHandler.handleCallback(callback);

        verify(botMessageService).fastAnswer(callback.getId(),"Спершу необхідно вирушити в круїз!\uD83D\uDE24");
        verifyNoMoreInteractions(botMessageService);
    }

    @Test
    void handleCallback_shuffleRoles_isPlaying_shouldCallShuffleRoles() {
        when(callback.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
        when(callback.getData()).thenReturn("shuffle-roles");
        when(gameService.isPlaying(123L)).thenReturn(true);

        buttonHandler.handleCallback(callback);

        verify(gameService).shuffleRoles(callback);
        verifyNoMoreInteractions(botMessageService, gameService);
    }


    @Test
    void handleCallback_shuffleRoles_notPlaying_shouldCallFastAnswer() {
        when(callback.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
        when(callback.getId()).thenReturn("callback_id");
        when(callback.getData()).thenReturn("shuffle-roles");
        when(gameService.isPlaying(123L)).thenReturn(false);

        buttonHandler.handleCallback(callback);

        verify(botMessageService).fastAnswer(callback.getId(),"Спершу необхідно вирушити в круїз!\uD83D\uDE24");
        verifyNoMoreInteractions(botMessageService);
    }

    @Test
    void handleCallback_cancelGameCreating_isPlaying_shouldCallEndGameAndFastAnswerAndSendMessage() {
        when(callback.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
        when(callback.getId()).thenReturn("callback_id");
        when(callback.getData()).thenReturn("cancel-game-creating");
        when(gameService.isPlaying(123L)).thenReturn(true);

        buttonHandler.handleCallback(callback);

        verify(gameService).endGame(message.getChatId());
        verify(botMessageService).fastAnswer(callback.getId(), "Я спалив список \uD83D\uDD25");
        verify(botMessageService).startGameQuery(message.getChatId());
        verifyNoMoreInteractions(botMessageService, gameService);
    }


    @Test
    void handleCallback_cancelGameCreating_notPlaying_shouldCallFastAnswer() {
        when(callback.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
        when(callback.getId()).thenReturn("callback_id");
        when(callback.getData()).thenReturn("cancel-game-creating");
        when(gameService.isPlaying(123L)).thenReturn(false);

        buttonHandler.handleCallback(callback);

        verify(botMessageService).fastAnswer(callback.getId(),"Ви ще не створили список!\uD83D\uDE24");
        verifyNoMoreInteractions(botMessageService);
    }

    @Test
    void handleCallback_restartGame_isPlaying_shouldCallRestartGame() {
        when(callback.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
        when(callback.getId()).thenReturn("callback_id");
        when(callback.getData()).thenReturn("restart-game");
        when(gameService.isPlaying(123L)).thenReturn(true);

        buttonHandler.handleCallback(callback);

        verify(gameService).restartGame(callback);
        verify(botMessageService).fastAnswer(callback.getId(), "Нова гра, свіжі голови, нові події\uD83E\uDD65");
        verifyNoMoreInteractions(botMessageService, gameService);
    }


    @Test
    void handleCallback_restartGame_notPlaying_shouldCallFastAnswerAndFastAnswer() {
        when(callback.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
        when(callback.getId()).thenReturn("callback_id");
        when(callback.getData()).thenReturn("restart-game");
        when(gameService.isPlaying(123L)).thenReturn(false);

        buttonHandler.handleCallback(callback);

        verify(botMessageService).fastAnswer(callback.getId(),"Спершу необхідно вирушити в круїз!\uD83D\uDE24");
        verifyNoMoreInteractions(botMessageService);
    }

    @Test
    void handleCallback_endGame_isPlaying_shouldCallEndGameAndSendMessages() {
        when(callback.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
        when(callback.getData()).thenReturn("end-game");
        when(gameService.isPlaying(123L)).thenReturn(true);


        buttonHandler.handleCallback(callback);

        verify(gameService).endGame(123L);
        verify(botMessageService).fastExecute(123L, "Гру завершено. Острів прощається з вами... 🏝️😔");
        verify(botMessageService).fastExecute(
                eq(123L),
                eq("Бажаєте знову ступити на берег Геллапагоса? 🌊🦜"),
                eq(List.of("Так, вирушаймо!")),
                eq(List.of("start-game"))
        );
    }

    @Test
    void handleCallback_endGame_notPlaying_shouldCallFastAnswer() {
        when(callback.getMessage()).thenReturn(message);
        when(message.getChatId()).thenReturn(123L);
        when(callback.getId()).thenReturn("callback_id");
        when(callback.getData()).thenReturn("end-game");

        buttonHandler.handleCallback(callback);

        verify(botMessageService).fastAnswer(callback.getId(),"Спершу необхідно вирушити в круїз!\uD83D\uDE24");
        verifyNoMoreInteractions(botMessageService);
    }

}
