package link.projectjg.apiserver.dto.keyword;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@ApiModel(value = "키워드 응답 DTO", description = "회원의 닉네임과 관심 키워드 목록을 내려줍니다.")
@Data
public class KeywordRes {

    @ApiModelProperty(value = "닉네임", example = "쉐어서비스")
    private String nickname;

    @ApiModelProperty(value = "관심 키워드 목록")
    private Set<KeywordDto> keywordSet = new HashSet<>();
}
