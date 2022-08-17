package link.projectjg.apiserver.service;

import link.projectjg.apiserver.domain.Keyword;
import link.projectjg.apiserver.domain.Member;
import link.projectjg.apiserver.domain.share.Share;
import link.projectjg.apiserver.dto.kakao.ReadyPayRes;
import link.projectjg.apiserver.dto.share.ChangeVisibleShareRes;
import link.projectjg.apiserver.dto.share.CreateShareReq;
import link.projectjg.apiserver.dto.share.ShareRes;
import link.projectjg.apiserver.dto.share.EditShareReq;
import link.projectjg.apiserver.exception.CustomException;
import link.projectjg.apiserver.exception.ErrorCode;
import link.projectjg.apiserver.repository.ShareRepostiory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class ShareService {

    private final PayService payService;
    private final ShareRepostiory shareRepostiory;
    private final ModelMapper modelMapper;


    public ShareRes createShare(Member member, CreateShareReq createShareReq, Set<Keyword> keywords) {
        Share share = createShareReq.toEntity(member, keywords);
        return modelMapper.map(shareRepostiory.save(share), ShareRes.class);
    }

    public ReadyPayRes joinShare(Member member, Long id) {
        Share share = shareRepostiory.findById(id).orElseThrow(() -> new CustomException(ErrorCode.SHARE_NOT_FOUND));

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

    public ShareRes editShare(Share share, EditShareReq editShareReq) {
        modelMapper.map(editShareReq, share);
        share.editShare();
        return modelMapper.map(share, ShareRes.class);
    }


}
