package com.example.hellapagos_host_bot;

import com.example.hellapagos_host_bot.bot.MyTgBot;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class HellapagosHostBotApplication {

	public static void main(String[] args) {

			Dotenv dotenv = Dotenv.configure()
					.ignoreIfMissing()
					.load();

			dotenv.entries().forEach(entry ->
					System.setProperty(entry.getKey(), entry.getValue()));

		var context = SpringApplication.run(HellapagosHostBotApplication.class, args);

		try {
			TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
			// Get the bot bean from the context
			MyTgBot bot = context.getBean(MyTgBot.class);
			botsApi.registerBot(bot);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

}
