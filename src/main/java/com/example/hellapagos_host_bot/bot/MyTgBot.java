package com.example.hellapagos_host_bot.bot;

import com.example.hellapagos_host_bot.config.BotConfig;
import com.example.hellapagos_host_bot.handler.ButtonHandler;
import com.example.hellapagos_host_bot.handler.MessageHandler;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MyTgBot extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    private final MessageHandler messageHandler;
    private final ButtonHandler buttonHandler;

    public MyTgBot(BotConfig botConfig, @Lazy MessageHandler messageHandler, ButtonHandler buttonHandler) {
        this.botConfig = botConfig;
        this.messageHandler = messageHandler;
        this.buttonHandler = buttonHandler;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            buttonHandler.handleCallback(update.getCallbackQuery());
        } else if (update.hasMessage()) {
            messageHandler.handleMessage(update);
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotUsername();
    }


    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }
}
