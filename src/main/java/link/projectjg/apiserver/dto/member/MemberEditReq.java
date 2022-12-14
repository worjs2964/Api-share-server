package link.projectjg.apiserver.dto.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value = "회원 정보 수정 요청 DTO")
public class MemberEditReq {

    @Pattern(regexp = "^[A-Za-z0-9가-힣]{2,20}$")
    @ApiModelProperty(value = "닉네임", example = "인증된사용자")
    private String nickname;

    @ApiModelProperty(value = "자기소개", example = "안녕하세요.")
    private String description;

    @Valid
    private PasswordDto passwordDto;

    @ApiModelProperty(value = "웹 일반 알림 여부", example = "true")
    private Boolean isNotificationByWeb;

    @ApiModelProperty(value = "웹 키워드 알림 여부", example = "true")
    private Boolean isKeywordByWeb;

    @ApiModelProperty(value = "이메일 알림 여부", example = "false")
    private Boolean isNotificationByEmail ;

    @ApiModelProperty(value = "이메일 키워드 알림 여부", example = "false")
    private Boolean isKeywordByEmail;
}
