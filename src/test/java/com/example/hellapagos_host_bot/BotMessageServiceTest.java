package com.example.hellapagos_host_bot;

import com.example.hellapagos_host_bot.service.BotMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BotMessageServiceTest {

    @Mock
    private AbsSender bot;
    @InjectMocks
    private BotMessageService service;

    @BeforeEach
    void setUp() {
        bot = mock(AbsSender.class);
        service = new BotMessageService(bot);
    }

    @Test
    void testFastExecuteSimpleMessage() throws Exception {
        Long chatId = 123L;
        String text = "Hello, World!";

        service.fastExecute(chatId, text);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot).execute(captor.capture());

        SendMessage message = captor.getValue();
        assertEquals(String.valueOf(chatId), message.getChatId());
        assertEquals(text, message.getText());
    }

    @Test
    void testFastExecuteWithButtons() throws Exception {
        Long chatId = 123L;
        String text = "Choose:";
        List<String> buttons = List.of("Yes", "No");
        List<String> data = List.of("yes-callback", "no-callback");

        service.fastExecute(chatId, text, buttons, data);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot).execute(captor.capture());

        SendMessage message = captor.getValue();
        assertEquals(String.valueOf(chatId), message.getChatId());
        assertEquals(text, message.getText());
        assertNotNull(message.getReplyMarkup());
    }

    @Test
    void testRandomEmojiCallbackDoesNotThrow() throws TelegramApiException {
        Long chatId = 123L;

        assertDoesNotThrow(() -> service.randomEmojiCallback(chatId));
        verify(bot, atLeastOnce()).execute(any(SendMessage.class));
    }

    @Test
    void testSendStartMessage() throws Exception {
        Long chatId = 123L;

        service.sendStartMessage(chatId);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot).execute(captor.capture());

        SendMessage message = captor.getValue();
        assertTrue(message.getText().contains("Вітаємо на острові"));
        assertNotNull(message.getReplyMarkup());
    }

    @Test
    void testStartGameQuery() throws Exception {
        Long chatId = 123L;

        service.startGameQuery(chatId);

        ArgumentCaptor<SendMessage> captor = ArgumentCaptor.forClass(SendMessage.class);
        verify(bot).execute(captor.capture());

        SendMessage message = captor.getValue();
        assertTrue(message.getText().contains("виживання"));
        assertNotNull(message.getReplyMarkup());
    }

    @Test
    void testFastAnswer() throws Exception {
        String queryId = "callback123";
        String text = "Answered!";

        service.fastAnswer(queryId, text);

        ArgumentCaptor<AnswerCallbackQuery> captor = ArgumentCaptor.forClass(AnswerCallbackQuery.class);
        verify(bot).execute(captor.capture());

        AnswerCallbackQuery query = captor.getValue();
        assertEquals(queryId, query.getCallbackQueryId());
        assertEquals(text, query.getText());
    }
}
