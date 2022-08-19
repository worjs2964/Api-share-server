package link.projectjg.apiserver.event.share;

import link.projectjg.apiserver.domain.Keyword;
import link.projectjg.apiserver.domain.Member;
import link.projectjg.apiserver.domain.MemberShare;
import link.projectjg.apiserver.domain.notification.Notification;
import link.projectjg.apiserver.domain.notification.NotificationType;
import link.projectjg.apiserver.domain.share.Share;
import link.projectjg.apiserver.dto.notification.NotificationDto;
import link.projectjg.apiserver.mail.EmailMessage;
import link.projectjg.apiserver.mail.MailSender;
import link.projectjg.apiserver.repository.MemberRepository;
import link.projectjg.apiserver.repository.NotificationRepository;
import link.projectjg.apiserver.repository.ShareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Set;


@Async
@Component
@RequiredArgsConstructor
@Transactional
public class ShareEventListener {

    private final ShareRepository shareRepository;
    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    private final MailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${share-service.url}")
    private String url;

    // 관심 키워드에 해당하는 공유가 생겼을 알림
    @EventListener
    public void notifyCreate(ShareCreateEvent shareCreateEvent) {

        Share share = shareRepository.findWithKeywordById(shareCreateEvent.getShare().getId());
        Set<Keyword> keywordSet = share.getKeywordSet();

        memberRepository.findDistinctMemberByKeywordSetIn(keywordSet).stream().forEach(member -> {
            NotificationDto info = NotificationDto.builder()
                    .title("관심 키워드로 등록한 공유가 생성되었습니다.")
                    .message("제목 : '" + share.getTitle() + "' 이 생성되었습니다. 지금 바로 확인해보세요.")
                    .link(url + "/share/" + share.getId())
                    .notificationType(NotificationType.CREATE).build();

            if (member.isKeywordByEmail()) sendByMail(member, info);
            if (member.isKeywordByWeb()) sendByWeb(member, info);
        });
    }

    // 공유에 참여했을때 참여자, 공유 마스터에게 알림
    @EventListener
    public void notifyJoin(ShareJoinEvent shareJoinEvent) {
        Member participant = shareJoinEvent.getMember();
        Share share = shareJoinEvent.getShare();

        // 참가자에게 알림
        notify(participant, share, NotificationType.JOIN,
                "공유의 참여하였습니다.",
                "제목 : '" + share.getTitle() + "' 에 참여하였습니다. 지금 바로 확인해보세요.");
        // 마스터에게 알림
        notify(share.getMaster(), share, NotificationType.JOIN,
                "공유의 알림이 있습니다.",
                "제목 : '" + share.getTitle() + "' 에 새로운 회원님이 참여하였습니다.");
    }

    // 참여한 공유가 변경됐을때 알림
    @EventListener
    public void notifyEdit(ShareEditEvent shareEditEvent) {
        Share share = shareEditEvent.getShare();
        // 공유 참여자들에게 알림
        share.getMemberShares().stream().map(MemberShare::getMember).forEach(participant ->
            notify(participant, share, NotificationType.EDIT,
                    "참여한 공유의 변경사항이 있습니다.",
                    "제목 : '" + share.getTitle() + "' 공유를 확인해보세요.")
        );
    }

    // 알림 보내기
    private void notify(Member member, Share share, NotificationType type, String title, String message) {
        NotificationDto info = NotificationDto.builder()
                .title(title)
                .message(message)
                .link(url + "/share/" + share.getId())
                .notificationType(type).build();

        if (member.isNotificationByEmail()) sendByMail(member, info);
        if (member.isNotificationByWeb()) sendByWeb(member, info);
    }

    // 메일 알림
    private void sendByMail(Member member, NotificationDto notificationDto) {
        Context context = new Context();
        context.setVariable("nickname", member.getNickname());
        context.setVariable("title", notificationDto.getTitle());
        context.setVariable("message", notificationDto.getMessage());
        context.setVariable("link", notificationDto.getLink());

        String message = templateEngine.process("/email-form", context);

        mailSender.send(EmailMessage.builder()
                .to(member.getEmail())
                .title(notificationDto.getTitle())
                .message(message).build());
    }

    // 웹 알림
    private void sendByWeb(Member member, NotificationDto notificationDto) {
        notificationRepository.save(Notification.builder()
                .member(member)
                .notificationType(notificationDto.getNotificationType())
                .title(notificationDto.getTitle())
                .message(notificationDto.getMessage())
                .link(notificationDto.getLink()).build());
    }
}
