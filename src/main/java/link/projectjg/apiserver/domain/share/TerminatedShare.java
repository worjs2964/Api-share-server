package link.projectjg.apiserver.domain.share;

import link.projectjg.apiserver.domain.Member;
import link.projectjg.apiserver.exception.CustomException;
import link.projectjg.apiserver.exception.ErrorCode;

public class TerminatedShare implements ShareStateOperation {

    @Override
    public boolean canJoinShare(Share share, Member member) {
        throw new CustomException(ErrorCode.INVALID_JOIN_SHARE);
    }

    @Override
    public Share editShare(Share share) {
        throw new CustomException(ErrorCode.INVALID_EDIT_SHARE);
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
