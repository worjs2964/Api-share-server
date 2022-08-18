package link.projectjg.apiserver.dto.keyword;

import link.projectjg.apiserver.domain.Keyword;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
public class KeywordReq {

    @Valid
    @Size(max = 5)
    private Set<KeywordDto> keywordSet = new HashSet<>();

    public static Keyword toEntity(String keyword) {
        return Keyword.builder()
                .keyword(keyword).build();
    }

}
