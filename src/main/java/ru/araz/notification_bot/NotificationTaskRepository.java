package ru.araz.notification_bot;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Long> {
    // Метод для поиска задач на конкретную минуту
    List<NotificationTask> findByDateTime(LocalDateTime dateTime);
}