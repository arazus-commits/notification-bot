package ru.araz.notification_bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TelegramBotUpdatesListener {

    private static final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot bot;
    private final NotificationTaskRepository repository;

    private final Pattern pattern = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4}\\s\\d{2}:\\d{2})(\\s+)(.+)");
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public TelegramBotUpdatesListener(TelegramBot bot, NotificationTaskRepository repository) {
        this.bot = bot;
        this.repository = repository;
    }

    @PostConstruct
    public void init() {
        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                if (update.message() != null && update.message().text() != null) {
                    String text = update.message().text();
                    Long chatId = update.message().chat().id();

                    if ("/start".equals(text)) {
                        bot.execute(new SendMessage(chatId, "Привет! Формат напоминания: 01.01.2022 20:00 Текст"));
                    } else {
                        Matcher matcher = pattern.matcher(text);
                        if (matcher.matches()) {
                            try {
                                LocalDateTime dateTime = LocalDateTime.parse(matcher.group(1), formatter);
                                String message = matcher.group(3);
                                repository.save(new NotificationTask(chatId, message, dateTime));
                                bot.execute(new SendMessage(chatId, "Напоминание создано на " + dateTime.format(formatter)));
                            } catch (Exception e) {
                                bot.execute(new SendMessage(chatId, "Ошибка парсинга даты."));
                            }
                        } else {
                            bot.execute(new SendMessage(chatId, "Неверный формат. Пример: 01.01.2022 20:00 Сделать домашнюю работу"));
                        }
                    }
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
        logger.info("Бот успешно запущен!");
    }
}