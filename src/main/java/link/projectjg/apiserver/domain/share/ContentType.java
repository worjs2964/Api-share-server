package link.projectjg.apiserver.domain.share;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContentType {

    VIDEO("영상"),
    MUSIC("음악"),
    GAME("게임"),
    ETC("기타");

    private final String description;

}
