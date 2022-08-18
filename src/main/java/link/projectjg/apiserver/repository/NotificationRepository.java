package link.projectjg.apiserver.repository;

import link.projectjg.apiserver.domain.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
