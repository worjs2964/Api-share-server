package link.projectjg.apiserver.dto.keyword;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class KeywordRes {

    private String nickname;

    private Set<KeywordDto> keywordSet = new HashSet<>();
}
