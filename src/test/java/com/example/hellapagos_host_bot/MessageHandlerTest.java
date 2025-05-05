package com.example.hellapagos_host_bot;

import com.example.hellapagos_host_bot.handler.MessageHandler;
import com.example.hellapagos_host_bot.service.BotMessageService;
import com.example.hellapagos_host_bot.service.GameService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageHandlerTest {
    @InjectMocks
    MessageHandler messageHandler;
    @Mock
    Update update;
    @Mock
    Message message;
    @Mock
    BotMessageService botMessageService;
    @Mock
    GameService gameService;

    @Test
    void handleMessage_start_shouldCallStartMethod() {
        when(update.getMessage()).thenReturn(message);
        when(message.getText()).thenReturn("/start");
        when(message.getChatId()).thenReturn(123L);

        messageHandler.handleMessage(update);

        verify(botMessageService).sendStartMessage(message.getChatId());
        verifyNoMoreInteractions(botMessageService);
    }

    @Test
    void handleMessage_menu_isPlaying_shouldCallGameMenu() {
        when(update.getMessage()).thenReturn(message);
        when(message.getText()).thenReturn("/menu");
        when(message.getChatId()).thenReturn(123L);
        when(gameService.isPlaying(message.getChatId())).thenReturn(true);
        when(gameService.isAwaiting(message.getChatId())).thenReturn(false);


        messageHandler.handleMessage(update);

        verify(gameService).gameMenu(message.getChatId());
        verifyNoMoreInteractions(botMessageService, gameService);
    }


    @Test
    void handleMessage_menu_isNotPlaying_shouldCallFastAnswer() {
        when(update.getMessage()).thenReturn(message);
        when(message.getText()).thenReturn("/menu");
        when(message.getChatId()).thenReturn(123L);
        when(gameService.isPlaying(message.getChatId())).thenReturn(false);
        when(gameService.isAwaiting(message.getChatId())).thenReturn(false);

        messageHandler.handleMessage(update);

        verify(botMessageService).fastExecute(message.getChatId(),"Спершу необхідно вирушити в круїз!\uD83D\uDE24");
        verifyNoMoreInteractions(botMessageService, gameService);
    }

    @Test
    void handleMessage_isAwaitingForListOfPlayers_shouldCallFastAnswer() {
        when(update.getMessage()).thenReturn(message);
        when(message.getText()).thenReturn("Гравець 1, Гравець 2");
        when(message.getChatId()).thenReturn(123L);
        when(gameService.isAwaiting(message.getChatId())).thenReturn(true);

        messageHandler.handleMessage(update);

        verify(gameService).saveNewPlayers(message.getChatId(),message.getText());
        verifyNoMoreInteractions(botMessageService, gameService);
    }

    @Test
    void handleMessage_unexpectedMessage_shouldCallFastAnswer() {
        when(update.getMessage()).thenReturn(message);
        when(message.getText()).thenReturn("asdasd");
        when(message.getChatId()).thenReturn(123L);

        messageHandler.handleMessage(update);

        verify(botMessageService).randomEmojiCallback(message.getChatId());
        verifyNoMoreInteractions(botMessageService);
    }
}
