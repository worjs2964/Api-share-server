package link.projectjg.apiserver.domain.notification;

import link.projectjg.apiserver.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Notification {

    @Id @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private String title;

    private String link;

    private String message;

    private boolean checked;

    @CreatedDate
    private LocalDateTime createdDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
}
