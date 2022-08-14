package link.projectjg.apiserver.dto.keyword;

import io.swagger.annotations.ApiModel;
import link.projectjg.apiserver.domain.Keyword;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@ApiModel(value = "키워드 req", description = "회원의 관심 키워드를 지정합니다. 회원의 관심 키워드가 전달 받은 키워드 셋으로 변경됩니다. 관심 키워드는 최대 5개까지 등록이 가능합니다.")
public class KeywordReq {

    @Valid
    @Size(max = 5)
    private Set<KeywordDto> keywordSet = new HashSet<>();

    public static Keyword toEntity(String keyword) {
        return Keyword.builder()
                .keyword(keyword).build();
    }

}
