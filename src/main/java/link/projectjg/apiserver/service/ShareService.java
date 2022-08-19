package link.projectjg.apiserver.service;

import link.projectjg.apiserver.domain.Keyword;
import link.projectjg.apiserver.domain.Member;
import link.projectjg.apiserver.domain.share.Share;
import link.projectjg.apiserver.dto.Pagination;
import link.projectjg.apiserver.dto.kakao.ReadyPayRes;
import link.projectjg.apiserver.dto.share.*;
import link.projectjg.apiserver.event.share.ShareCreateEvent;
import link.projectjg.apiserver.event.share.ShareEditEvent;
import link.projectjg.apiserver.exception.CustomException;
import link.projectjg.apiserver.exception.ErrorCode;
import link.projectjg.apiserver.repository.ShareRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
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

    // 목록 검색
    public SearchShareListRes showShareList(Pageable pageable, SearchShareListReq searchShareListReq) {
        Page<Share> page = shareRepository.findShareList(searchShareListReq, pageable);

        SearchShareListRes searchShareListRes = new SearchShareListRes();
        page.getContent().forEach(share ->
                searchShareListRes.getShares().add(modelMapper.map(share, ShareDto.class))
        );

        searchShareListRes.setPagination(Pagination.builder()
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .currentPage(pageable.getPageNumber())
                .currentElements(page.getContent().size()).build());

        return searchShareListRes;
    }

    // 생성
    public ShareRes createShare(Member member, CreateShareReq createShareReq, Set<Keyword> keywords) {
        Share share = createShareReq.toEntity(member, keywords);
        Share save = shareRepository.save(share);
        return modelMapper.map(save, ShareRes.class);
    }

    // 검색 (단건)
    public SearchShareRes showShare(Long shareId, Member member) {
        Share share = shareRepository.findShowShareInfoById(shareId)
                .orElseThrow(() -> new CustomException(ErrorCode.SHARE_NOT_FOUND));
        return SearchShareRes.of(share, member);
    }

    // 수정
    public ShareRes editShare(Share share, EditShareReq editShareReq) {
        modelMapper.map(editShareReq, share);
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

    // 가입 진행
    public ReadyPayRes joinShare(Member member, Long id) {
        Share share = shareRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.SHARE_NOT_FOUND));

        if (share.canJoinShare(member)) {
            // 결제 진행
            return payService.readyPay(share, member);
        } else {
            throw new CustomException(ErrorCode.INVALID_VALUE);
        }
    }

    // 키워드 알림
    public String notifyShare(Share share) {
        if (share.canNotify()) {
            eventPublisher.publishEvent(new ShareCreateEvent(share));
            return "성공적으로 알림을 보냈습니다.";
        } else {
            throw new CustomException(ErrorCode.INVALID_ALREADY_NOTIFY);
        }
    }

    // 공개/비공개 상태 변경
    public ChangeVisibleShareRes changeVisibleShare(Share share) {
        Share changed = shareRepository.save(share.changeVisible());
        return modelMapper.map(changed, ChangeVisibleShareRes.class);
    }

    // 키워드 등록/삭제
    public ShareRes addKeywords(Share share, Set<Keyword> keywords) {
        // 키워드는 VISIBLE, INVISIBLE 상태일때만 바꿀 수 있다.
        if (share.canChangeKeyword()) share.setKeywordSet(keywords);
        return modelMapper.map(share, ShareRes.class);
    }
}
