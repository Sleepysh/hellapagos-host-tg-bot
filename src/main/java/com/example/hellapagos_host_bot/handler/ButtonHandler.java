package com.example.hellapagos_host_bot.handler;

import com.example.hellapagos_host_bot.service.BotMessageService;
import com.example.hellapagos_host_bot.service.GameService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;


@Component
public class ButtonHandler {

    private final BotMessageService botMessageService;
    private final GameService gameService;

    public ButtonHandler(@Lazy BotMessageService botMessageService, @Lazy GameService gameService) {
        this.botMessageService = botMessageService;
        this.gameService = gameService;
    }

    public void handleCallback(CallbackQuery callback) {
        String callbackData  = callback.getData();
        Long chatId = callback.getMessage().getChatId();

        switch (callbackData) {
            case "start-game":
                botMessageService.startGameQuery(chatId);
                break;

            case "new-game":
                if(!gameService.isPlaying(chatId)) {
                    gameService.newGameQuery(chatId);
                } else {
                    botMessageService.fastAnswer(callback.getId(),"Ви вже на острові!\uD83D\uDC7A");
                }
                break;

            case "approve-players":
                if (gameService.isPlaying(chatId)) {
                    gameService.gameMenu(chatId);
                } else {
                    botMessageService.fastAnswer(callback.getId(),"Почни нову гру!\uD83D\uDC7A");
                }
                break;

            case "view-roles":
                if (gameService.isPlaying(chatId)) {
                    gameService.viewRoles(chatId);
                } else {
                    botMessageService.fastAnswer(callback.getId(),"Спершу необхідно вирушити в круїз!\uD83D\uDE24");
                }
                break;

            case "get-event":
                if (gameService.isPlaying(chatId)) {
                    gameService.generateEvent(chatId);
                } else {
                    botMessageService.fastAnswer(callback.getId(),"Спершу необхідно вирушити в круїз!\uD83D\uDE24");
                }
                break;

            case "view-all-events":
                if (gameService.isPlaying(chatId)) {
                    gameService.viewAllEvents(chatId, callback.getId());
                } else {
                    botMessageService.fastAnswer(callback.getId(),"Спершу необхідно вирушити в круїз!\uD83D\uDE24");
                }
                break;

            case "view-permanent-events":
                if (gameService.isPlaying(chatId)) {
                    gameService.viewPermanentEvents(chatId, callback.getId());
                } else {
                    botMessageService.fastAnswer(callback.getId(),"Спершу необхідно вирушити в круїз!\uD83D\uDE24");
                }
                break;

            case "shuffle-roles":
                if (gameService.isPlaying(chatId)) {
                    gameService.shuffleRoles(callback);
                } else {
                    botMessageService.fastAnswer(callback.getId(),"Спершу необхідно вирушити в круїз!\uD83D\uDE24");
                }
                break;

            case "cancel-game-creating":
                if(gameService.isPlaying(chatId)) {
                    gameService.endGame(chatId);
                    botMessageService.fastAnswer(callback.getId(), "Я спалив список \uD83D\uDD25");
                    botMessageService.startGameQuery(chatId);
                } else {
                    botMessageService.fastAnswer(callback.getId(),"Ви ще не створили список!\uD83D\uDE24");
                }
                break;

            case "restart-game":
                if(gameService.isPlaying(chatId)) {
                gameService.restartGame(callback);
                botMessageService.fastAnswer(callback.getId(), "Нова гра, свіжі голови, нові події\uD83E\uDD65");
                } else {
                    botMessageService.fastAnswer(callback.getId(),"Спершу необхідно вирушити в круїз!\uD83D\uDE24");
                }
                break;

            case "end-game":
                if(gameService.isPlaying(chatId)) {
                gameService.endGame(chatId);
                botMessageService.fastExecute(chatId, "Гру завершено. Острів прощається з вами... 🏝️😔");
                botMessageService.fastExecute(
                        chatId,
                        "Бажаєте знову ступити на берег Геллапагоса? 🌊🦜",
                        List.of("Так, вирушаймо!"),
                        List.of("start-game")
                );
                } else {
                    botMessageService.fastAnswer(callback.getId(),"Спершу необхідно вирушити в круїз!\uD83D\uDE24");
                }
                break;
        }
    }
}
