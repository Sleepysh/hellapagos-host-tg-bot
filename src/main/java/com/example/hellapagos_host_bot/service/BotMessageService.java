package com.example.hellapagos_host_bot.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class BotMessageService {

    private final AbsSender bot;

    public BotMessageService(AbsSender bot) {
        this.bot = bot;
    }

    public void fastExecute(Long chatId, String text) {
        try {
            bot.execute(new SendMessage(String.valueOf(chatId), text));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fastExecute(Long chatId, String text, List<String> buttonText, List<String>callbackData) {
        sendRichMessage(chatId, text, buttonText, callbackData);
    }

    public void randomEmojiCallback(Long chatId) {
        String text = "\uD83C\uDF0B\uD83C\uDF2A\uD83D\uDC80";
            int randomNumber = (int) (Math.random() * 4);
            switch (randomNumber) {
                case 1:
                    text = "\uD83C\uDFDD\uD83D\uDE2A\uD83E\uDD65";
                    break;

                case 2:
                    text = "\uD83D\uDC79\uD83D\uDD2A☠\uFE0F";
                    break;

                case 3:
                    text = "\uD83C\uDFA3\uD83E\uDEB5\uD83D\uDE0B";
                    break;
            }
            fastExecute(chatId, text);
    }

    public void sendStartMessage(Long chatId) {
        sendRichMessage(chatId,
                "\uD83C\uDFDD\uFE0F <b>Вітаємо на острові, вцілілі!</b>\n" +
                        "Ви опинилися тут після... скажімо, несподіваної водної пригоди без зворотного квитка \uD83D\uDEF3\uFE0F\uD83D\uDCA5\n" +
                        "Ваш корабель потонув, і тепер навколо лише пісок, пальми, кокоси — і жодного вайфаю.\n" +
                        "\n" +
                        "Але не все так погано!\n" +
                        "\uD83C\uDF1E Тут тепло.\n" +
                        "\uD83C\uDF34 Є трохи ресурсів.\n" +
                        "\uD83E\uDD1D Ви не самі.\n" +
                        "\n" +
                        "Ви маєте шанс вижити, побудувати пліт і втекти з цього острова.\n" +
                        "Співпрацюйте, діліться, плануйте, будуйте...\n" +
                        "\n" +
                        "\uD83D\uDE08 Але не надто розслабляйтесь.\n" +
                        "\n" +
                        "Бо поряд з вами — ті, хто теж хоче вижити.\n" +
                        "\uD83D\uDCA5 І кожен з них може вистрілити вам у спину, коли ви найбільше довірятимете.\n" +
                        "\uD83D\uDC79\uD83C\uDF7D\uFE0F Місцеві аборигени вже вирішують, кого з вас перетворити на вечерю.\n" +
                        "\uD83C\uDF2A\uFE0F І десь за горизонтом зростає тайфун, який не питає — він просто змиває.\n" +
                        "\n" +
                        "⏳ Часу обмаль.\n" +
                        "\uD83D\uDE07 Кому довірити воду?\n" +
                        "\uD83D\uDD2B Кого лишити на березі?\n" +
                        "\uD83D\uDEF6 І чи вистачить плотів для всіх?..\n" +
                        "\n" +
                        "Ласкаво просимо у виживання, де кожен крок — це вибір.\n" +
                        "Удачі. Вам вона точно знадобиться.",
                List.of("Я готовий ☠\uFE0F"),
                List.of("start-game"));
    }

    public void startGameQuery(Long chatId) {
         sendRichMessage(chatId,
                 "\uD83C\uDF34 Починаємо виживання!\n" +
                         "\n" +
                         "Тепер я твій гід на цьому тропічному, не надто гостинному острові.\n" +
                         "Ось як усе працює:\n" +
                         "\n" +
                         "\uD83C\uDD95 Натискаєш «Нова гра» — і я одразу запитаю, хто потрапив на острів цього разу.\n" +
                         "\uD83D\uDCDD Введи імена гравців у форматі:\n" +
                         "Гравець 1, Гравець 2, Гравець 3...\n" +
                         "\n" +
                         "\uD83C\uDFAD Після цього я роздам кожному роль — з унікальними здібностями та правилами.\n" +
                         "\uD83D\uDC40 Якщо щось не подобається — це нікого не хвилює! Але якщо вже так треба, то можеш  натиснути «Змінити ролі», і я превизначу ваші долі.\n" +
                         "\n" +
                         "\uD83D\uDCDC Коли всі ролі на руках, ви зможете розігрувати події кожного раунду — буде весело, або тривожно, або дуже мокро.\n" +
                         "\n" +
                         "\uD83D\uDD01 Почали з початку? Тисни «Скинути події» — і я очищу поле для нової гри.\n" +
                         "\uD83C\uDFC1 Гру завершено? Натисни «Завершити гру», і я відпущу всіх гравців… у небуття \uD83E\uDEA6 (жартую, просто скину дані).",
                 List.of("\uD83C\uDF34 Нова гра"),
                 List.of("new-game"));

    }

    private void sendRichMessage(long chatId, String text,
                                 List<String> buttonTexts, List<String> callbackData) {
        try {
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText(text);
            message.setParseMode("HTML");

            if(!buttonTexts.isEmpty() || !callbackData.isEmpty()) {
                // 3. Add inline buttons
                InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rows = new ArrayList<>();

                for (int i = 0; i < buttonTexts.size(); i++) {
                    rows.add(createButtonRow(buttonTexts.get(i), callbackData.get(i)));
                }

                markup.setKeyboard(rows);
                message.setReplyMarkup(markup); // Attach buttons
            }


            // 4. Send the message
            bot.execute(message);
        } catch (Exception e) {
            e.printStackTrace();
//            SendMessage fallback = new SendMessage();
//            fallback.setChatId(String.valueOf(chatId));
//            fallback.setText(text);
//            fallback.setReplyMarkup(createInlineKeyboard(buttonTexts, callbackData));
//            fallback.setParseMode("HTML");
//            executeMessage(fallback);
        }
    }

    private InlineKeyboardMarkup createInlineKeyboard(List<String> buttonTexts, List<String> callbackData) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (int i = 0; i < buttonTexts.size(); i++) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(buttonTexts.get(i));
            button.setCallbackData(callbackData.get(i));
            row.add(button);
            rows.add(row);
        }

        markup.setKeyboard(rows);
        return markup;
    }

    private List<InlineKeyboardButton> createButtonRow(String text, String dataOrUrl, boolean isCallback) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);

        if (isCallback) {
            button.setCallbackData(dataOrUrl);
        } else {
            button.setUrl(dataOrUrl);
        }

        return List.of(button);
    }

    private List<InlineKeyboardButton> createButtonRow(String text, String dataOrUrl) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);

        if (dataOrUrl.startsWith("url:")) {
            button.setUrl(dataOrUrl.replaceAll("(?m)^url:", "").strip());
        } else {
            button.setCallbackData(dataOrUrl);
        }

        return List.of(button);
    }

    private void executeMessage(SendMessage message) {
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            // Handle exception or log it
        }
    }


    public void fastAnswer(String callbackQueryId, String text) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setText(text);
        answerCallbackQuery.setCallbackQueryId(callbackQueryId);
        try {
            bot.execute(answerCallbackQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
