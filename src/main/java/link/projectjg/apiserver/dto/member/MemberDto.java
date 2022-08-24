package link.projectjg.apiserver.dto.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data @Builder
@ApiModel(value  = "회원 DTO")
public class MemberDto {

    @ApiModelProperty(value = "닉네임", example = "인증된사용자")
    private String nickname;

    @ApiModelProperty(value = "회원 번호", example = "13")
    private Long id;
}
