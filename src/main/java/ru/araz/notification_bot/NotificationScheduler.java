package ru.araz.notification_bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.araz.notification_bot.NotificationTask;
import ru.araz.notification_bot.NotificationTaskRepository;
import ru.araz.notification_bot.TelegramBotUpdatesListener;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class NotificationScheduler {

    private static final Logger logger = LoggerFactory.getLogger(NotificationScheduler.class);
    private final TelegramBot bot;
    private final NotificationTaskRepository repository;

    public NotificationScheduler(TelegramBot bot, NotificationTaskRepository repository) {
        this.bot = bot;
        this.repository = repository;
    }

    // Запуск каждую минуту (0 секунд)
    @Scheduled(cron = "0 0/1 * * * *")
    public void checkNotifications() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        List<NotificationTask> tasks = repository.findByDateTime(now);

        for (NotificationTask task : tasks) {
            bot.execute(new SendMessage(task.getChatId(), task.getMessage()));
            logger.info("Отправлено напоминание для чата {}", task.getChatId());
        }
    }
}