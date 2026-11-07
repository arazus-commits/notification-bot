package ru.araz.notification_bot;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification_task")
@Getter
@Setter
@NoArgsConstructor
public class NotificationTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    public NotificationTask(Long chatId, String message, LocalDateTime dateTime) {
        this.chatId = chatId;
        this.message = message;
        this.dateTime = dateTime;
    }
}