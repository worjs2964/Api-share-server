package link.projectjg.apiserver.event.share;

import link.projectjg.apiserver.domain.share.Share;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ShareEditEvent {

    private final Share share;
}
