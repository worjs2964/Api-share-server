package link.projectjg.apiserver.domain.share;

import link.projectjg.apiserver.domain.Member;
import link.projectjg.apiserver.domain.MemberShare;
import link.projectjg.apiserver.exception.CustomException;
import link.projectjg.apiserver.exception.ErrorCode;

import java.util.List;

public class InvisibleShare implements ShareStateOperation {

    @Override
    public boolean canJoinShare(Share share, Member member) {
        throw new CustomException(ErrorCode.INVALID_JOIN_SHARE);
    }

    @Override
    public Share editShare(Share share) {
        List<MemberShare> memberShares = share.getMemberShares();

        // 참여 인원이 있다면
        if (!memberShares.isEmpty()) {
            // 참여 인원보다 모집인원이 작으면 안되고
            if (memberShares.size() > share.getNumberRecruits()) throw new CustomException(ErrorCode.INVALID_EDIT_RECRUIT);
            // 가득 차면 공유의 상태를 모집완료로 바꿔준다.
            if (share.isFull()) share.changeState(ShareState.FULL);
        }

        return share;
    }

    @Override
    public boolean canChangeKeyword(Share share) {
        return true;
    }

    @Override
    public Share changeVisible(Share share) {
        share.changeState(ShareState.VISIBLE);
        return share;
    }

    @Override
    public boolean canNotify(Share share) {
        throw new CustomException(ErrorCode.INVALID_NOTIFY_SHARE);
    }
}
