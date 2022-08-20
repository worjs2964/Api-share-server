package link.projectjg.apiserver.service;

import link.projectjg.apiserver.domain.Member;
import link.projectjg.apiserver.domain.notification.Notification;
import link.projectjg.apiserver.domain.notification.NotificationType;
import link.projectjg.apiserver.dto.notification.NotificationDto;
import link.projectjg.apiserver.dto.notification.NotificationGroup;
import link.projectjg.apiserver.dto.notification.NotificationRes;
import link.projectjg.apiserver.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationRes readNotifications(Member member) {
        List<Notification> notifications = notificationRepository.findByMember(member);

        NotificationRes res = NotificationRes.builder()
                .newNotifications(setNotificationGroup(notifications, false))
                .oldNotifications(setNotificationGroup(notifications, true)).build();

        // 알림을 모두 읽음으로 변경
        notifications.stream().filter(n -> !n.isChecked()).forEach(Notification::check);
        return res;
    }

    public String deleteNotifications(Member member) {
        notificationRepository.deleteByMemberAndChecked(member, true);
        return "알림이 정상적으로 삭제되었습니다.";
    }

    // 알림 분류
    private NotificationGroup setNotificationGroup(List<Notification> notifications, boolean checked) {
        NotificationGroup notificationGroup = new NotificationGroup();

        // 분류
        notifications.stream().filter(n -> n.isChecked() == checked)
                // 최신 알림순으로 정렬
                .sorted(Comparator.comparing(Notification::getCreatedDateTime, Comparator.reverseOrder()))
                .map(NotificationDto::of).forEach(notificationDto -> {
                    if (notificationDto.getNotificationType().equals(NotificationType.CREATE)) {
                        notificationGroup.getCreate().add(notificationDto);
                    } else if (notificationDto.getNotificationType().equals(NotificationType.JOIN)) {
                        notificationGroup.getJoin().add(notificationDto);
                    } else {
                        notificationGroup.getEdit().add(notificationDto);
                    }
                });

        return notificationGroup;
    }
}
