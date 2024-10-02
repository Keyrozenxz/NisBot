package org.example;

import org.example.bot.NisBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new NisBot());

            logger.info("Бот успешно зарегистрирован!");
        } catch (TelegramApiException e) {
            logger.severe("Ошибка при регистрации бота: " + e.getMessage());
        }
    }
}
