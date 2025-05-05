package com.example.hellapagos_host_bot.handler;

import com.example.hellapagos_host_bot.service.BotMessageService;
import com.example.hellapagos_host_bot.service.GameService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.*;

@Component
public class MessageHandler {

    private final BotMessageService botMessageService;
    private final GameService gameService;

    public MessageHandler(BotMessageService botMessageService, GameService gameService) {
        this.botMessageService = botMessageService;
        this.gameService = gameService;
    }

    public void handleMessage(Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();

        if (message.getText().equals("/start")) {
            botMessageService.sendStartMessage(chatId);

            if (gameService.isAwaiting(chatId))
                gameService.removeFromAwaiting(chatId);

            return;

        } else if (message.getText().equals("/menu")) {
            if (gameService.isPlaying(chatId)) {
                gameService.gameMenu(chatId);
            } else {
                botMessageService.fastExecute(chatId,"Спершу необхідно вирушити в круїз!\uD83D\uDE24");
            }

            if (gameService.isAwaiting(chatId))
                gameService.removeFromAwaiting(chatId);

            return;

        } else if (gameService.isAwaiting(chatId)) {
            gameService.saveNewPlayers(chatId, message.getText());
            return;

        } else {
            botMessageService.randomEmojiCallback(chatId);
            return;
        }
        
    }

}
