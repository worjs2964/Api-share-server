package link.projectjg.apiserver.domain.share;

import link.projectjg.apiserver.domain.Member;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ShareState implements ShareStateOperation {

    INVISIBLE(new InvisibleShare()),
    VISIBLE(new VisibleShare()),
    FULL(new FullShare()),
    TERMINATED(new TerminatedShare());

    private final ShareStateOperation operations;


    @Override
    public boolean canJoinShare(Share share, Member member) {
        return operations.canJoinShare(share, member);
    }

    @Override
    public Share editShare(Share share) {
        return operations.editShare(share);
    }

    @Override
    public Share changeVisible(Share share) {
        return operations.changeVisible(share);
    }

}
