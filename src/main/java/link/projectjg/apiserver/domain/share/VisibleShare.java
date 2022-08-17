package link.projectjg.apiserver.domain.share;

import link.projectjg.apiserver.domain.Member;
import link.projectjg.apiserver.domain.MemberShare;
import link.projectjg.apiserver.exception.CustomException;
import link.projectjg.apiserver.exception.ErrorCode;

import java.util.List;

public class VisibleShare implements ShareStateOperation {

    @Override
    public boolean canJoinShare(Share share, Member member) {
        return (share.isMaster(member) || share.isJoinMember(member)) ? false : true;
    }

    @Override
    public Share editShare(Share share) {

        List<MemberShare> memberShares = share.getMemberShares();

        // 참여 인원이 있다면
        if (!memberShares.isEmpty()) {
            // 참여 인원보다 모집인원이 작으면 안되고
            if (memberShares.size() > share.getNumberRecruits()) throw new CustomException(ErrorCode.INVALID_EDIT_RECRUIT);
            // 가득 차면 상태를 FULL로 바꿔준다.
            if (share.isFull()) share.changeState(ShareState.FULL);
        }
        return share;
    }

    @Override
    public Share changeVisible(Share share) {
        share.changeState(ShareState.INVISIBLE);
        return share;
    }

}
