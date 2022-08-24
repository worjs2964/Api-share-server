package link.projectjg.apiserver.dto.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import link.projectjg.apiserver.domain.Member;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value = "회원가입 요청 DTO", description = "이메일, 닉네임, 페스워드를 입력 받습니다.")
public class MemberJoinReq {

    @NotBlank
    @Email(regexp = "^[a-z0-9]+@[a-z0-9]+\\.[a-z0-9]+$")
    @ApiModelProperty(value = "이메일", example = "checked@share-service.com", required = true)
    private String email;

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9가-힣]{2,20}$")
    @ApiModelProperty(value = "닉네임", example = "인증된사용자", required = true)
    private String nickname;

    @NotBlank
    @Pattern(regexp = "^.*(?=^.{6,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$")
    @ApiModelProperty(value = "패스워드", example = "Password96!", required = true)
    private String password;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .role("ROLE_MEMBER")
                .isNotificationByWeb(true)
                .isKeywordByWeb(true)
                .build();
    }
}
