package link.projectjg.apiserver.dto.notification;

import link.projectjg.apiserver.domain.notification.NotificationType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationDto {

    private String title;

    private String message;

    private String link;

    private NotificationType notificationType;
}
