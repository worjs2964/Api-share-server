package link.projectjg.apiserver.service;

import link.projectjg.apiserver.domain.Keyword;
import link.projectjg.apiserver.domain.Member;
import link.projectjg.apiserver.domain.share.ContentType;
import link.projectjg.apiserver.domain.share.Share;
import link.projectjg.apiserver.dto.kakao.ReadyPayRes;
import link.projectjg.apiserver.dto.share.ChangeVisibleShareRes;
import link.projectjg.apiserver.dto.share.CreateShareReq;
import link.projectjg.apiserver.dto.share.EditShareReq;
import link.projectjg.apiserver.dto.share.ShareRes;
import link.projectjg.apiserver.event.share.ShareCreateEvent;
import link.projectjg.apiserver.event.share.ShareEditEvent;
import link.projectjg.apiserver.exception.CustomException;
import link.projectjg.apiserver.exception.ErrorCode;
import link.projectjg.apiserver.repository.ShareRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class ShareService {

    private final PayService payService;
    private final ShareRepository shareRepository;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher eventPublisher;

    public ShareRes createShare(Member member, CreateShareReq createShareReq, Set<Keyword> keywords) {
        Share share = createShareReq.toEntity(member, keywords);
        Share save = shareRepository.save(share);
        return modelMapper.map(save, ShareRes.class);
    }

    public ShareRes editShare(Share share, EditShareReq editShareReq, Set<Keyword> keywords) {
        modelMapper.map(editShareReq, share);
        if (keywords != null) share.setKeywordSet(keywords);
        share.editShare();

        // 사용자에게 중요한 정보가 변경됐다면 참여자에게 알림
        if (isNotify(editShareReq)) eventPublisher.publishEvent(new ShareEditEvent(share));
        return modelMapper.map(share, ShareRes.class);
    }

    // 공유의 설명, 아이디, 비밀번호가 변경됐는지 확인
    private boolean isNotify(EditShareReq editShareReq) {
        return editShareReq.getDescription() != null ||
                editShareReq.getShareEmail() != null ||
                editShareReq.getSharePassword() != null;
    }

    public ChangeVisibleShareRes changeVisibleShare(Share share) {
        Share changed = shareRepository.save(share.changeVisible());
        return modelMapper.map(changed, ChangeVisibleShareRes.class);
    }

    public String notifyShare(Share share) {
        if (share.isCanNotify()) {
            eventPublisher.publishEvent(new ShareCreateEvent(share));
            return "성공적으로 알림을 보냈습니다.";
        } else {
            throw new CustomException(ErrorCode.INVALID_ALREADY_NOTIFY);
        }
    }

    public ReadyPayRes joinShare(Member member, Long id) {
        Share share = shareRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.SHARE_NOT_FOUND));

        if (share.canJoinShare(member)) {
            return payService.readyPay(share, member);
        } else {
            throw new CustomException(ErrorCode.INVALID_VALUE);
        }
    }

    public ChangeVisibleShareRes changeVisibleShare(Share share) {
        Share changed = shareRepostiory.save(share.changeVisible());
        return modelMapper.map(changed, ChangeVisibleShareRes.class);
    }
}
