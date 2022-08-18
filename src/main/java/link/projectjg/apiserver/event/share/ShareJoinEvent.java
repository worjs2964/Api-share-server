package link.projectjg.apiserver.event.share;

import link.projectjg.apiserver.domain.Member;
import link.projectjg.apiserver.domain.share.Share;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ShareJoinEvent {

    private final Share share;

    private final Member member;
}
