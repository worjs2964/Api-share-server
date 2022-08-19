package link.projectjg.apiserver.domain.share;

import link.projectjg.apiserver.domain.Member;
import link.projectjg.apiserver.domain.MemberShare;
import link.projectjg.apiserver.exception.CustomException;
import link.projectjg.apiserver.exception.ErrorCode;

import java.util.List;

public class FullShare implements ShareStateOperation {

    @Override
    public boolean canJoinShare(Share share, Member member) {
        throw new CustomException(ErrorCode.INVALID_JOIN_SHARE);
    }

    @Override
    public Share editShare(Share share) {
        List<MemberShare> memberShares = share.getMemberShares();

        // 참여 인원보다 모집인원이 작으면 안되고
        if (memberShares.size() > share.getNumberRecruits()) throw new CustomException(ErrorCode.INVALID_EDIT_RECRUIT);
        // 모집인원이 늘어났으면 모집 상태로 바꿔준다.
        if (!share.isFull()) share.changeState(ShareState.VISIBLE);
        return share;
    }

    @Override
    public boolean canChangeKeyword(Share share) {
        throw new CustomException(ErrorCode.INVALID_SET_KEYWORD);
    }

    @Override
    public Share changeVisible(Share share) {
        throw new CustomException(ErrorCode.INVALID_CHANGE_VISIBLE);
    }

    @Override
    public boolean canNotify(Share share) {
        throw new CustomException(ErrorCode.INVALID_NOTIFY_SHARE);
    }

}
