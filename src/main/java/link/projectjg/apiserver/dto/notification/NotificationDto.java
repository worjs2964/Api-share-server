package link.projectjg.apiserver.dto.notification;

import link.projectjg.apiserver.domain.notification.Notification;
import link.projectjg.apiserver.domain.notification.NotificationType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationDto {

    private LocalDateTime createdDateTime;

    private String title;

    private String message;

    private String link;

    private NotificationType notificationType;

    public static NotificationDto of(Notification notification) {
        return NotificationDto.builder()
                .createdDateTime(notification.getCreatedDateTime())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .link(notification.getLink())
                .notificationType(notification.getNotificationType()).build();
    }
}
