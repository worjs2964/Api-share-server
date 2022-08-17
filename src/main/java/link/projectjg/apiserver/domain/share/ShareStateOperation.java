package link.projectjg.apiserver.domain.share;

import link.projectjg.apiserver.domain.Member;

public interface ShareStateOperation {

    boolean canJoinShare(Share share, Member member);

    Share editShare(Share share);

    Share changeVisible(Share share);

}
