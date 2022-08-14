package link.projectjg.apiserver.dto.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value = "회원가입 응답 DTO", description = "회원 가입 성공 시 이메일, 닉네임을 응답합니다.")
public class MemberJoinRes {

    @ApiModelProperty(value = "이메일", example = "rebuild96@naver.com")
    private String email;

    @ApiModelProperty(value = "닉네임", example = "쉐어서비스")
    private String nickname;

}
