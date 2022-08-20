package link.projectjg.apiserver.repository;

import link.projectjg.apiserver.domain.Member;
import link.projectjg.apiserver.domain.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByMember(Member member);

    void deleteByMemberAndChecked(Member member, boolean checked);
}
