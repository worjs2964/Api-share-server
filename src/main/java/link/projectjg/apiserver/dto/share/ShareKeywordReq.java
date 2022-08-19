package link.projectjg.apiserver.dto.share;

import link.projectjg.apiserver.domain.Keyword;
import link.projectjg.apiserver.dto.keyword.KeywordDto;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
public class ShareKeywordReq {

    @Valid
    @Size(max = 3)
    private Set<KeywordDto> keywordSet = new HashSet<>();

    public static Keyword toEntity(String keyword) {
        return Keyword.builder()
                .keyword(keyword).build();
    }

}
