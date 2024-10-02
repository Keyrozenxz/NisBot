package org.example.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.logging.Logger;

public class NisBot extends TelegramLongPollingBot {

    private static final Logger logger = Logger.getLogger(NisBot.class.getName());


    private static final long ADMIN_CHAT_ID = 5175675911L;


    private boolean isAwaitingEventSuggestion = false;

    @Override
    public String getBotUsername() {
        return "NisEventsBot";
    }

    @Override
    public String getBotToken() {
        return "7680003856:AAGa64JEf6rpV6QwFTBpoMqiBmEWbyiUXyI";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            logger.info("Получено сообщение от чата: " + chatId + ", текст: " + messageText);


            if (messageText.startsWith("/")) {
                handleCommand(messageText, chatId);
            } else if (isAwaitingEventSuggestion) {
                handleEventSuggestion(messageText, chatId);
            } else {
                sendMessage(chatId, "Извините, я понимаю только команды. Используйте /help для получения списка команд.");
            }
        }
    }

    private void handleCommand(String command, long chatId) {
        String response;
        switch (command.toLowerCase()) {
            case "/start":
                response = "Добро пожаловать! Используйте /help для получения списка команд. Ваш ID: " + chatId;
                break;
            case "/help":
                response = "Доступные команды:\n/start - Начать\n/help - Помощь\n/viewevents - Просмотреть события\n/suggestevent - Предложить событие";
                break;
            case "/viewevents":
                response = "Список событий: \n Нет предстоящих событий.";
                break;
            case "/suggestevent":
                response = "Опишите событие, которое хотите предложить, и я отправлю его администратору.";
                isAwaitingEventSuggestion = true;
                break;

            default:
                response = "Неизвестная команда. Используйте /help для получения списка команд.";
                break;
        }
        sendMessage(chatId, response);
    }

    private void handleEventSuggestion(String suggestion, long userChatId) {
        String adminMessage = "Новое предложение события от пользователя " + userChatId + ":\n" + suggestion;
        sendMessage(ADMIN_CHAT_ID, adminMessage);

        sendMessage(userChatId, "Ваше предложение отправлено администратору. Спасибо!");

        isAwaitingEventSuggestion = false;
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
            logger.info("Сообщение успешно отправлено в чат: " + chatId);
        } catch (TelegramApiException e) {
            logger.severe("Ошибка при отправке сообщения: " + e.getMessage());
        }
    }
}
