package link.projectjg.apiserver.dto.authentication;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "로그아웃 요청 DTO", description = "액세스 토큰을 입력 받습니다.")
public class LogoutReq {

    @ApiModelProperty(value = "액세스 토큰", example = "액세스 토큰 값", required = true)
    @NotBlank
    private String accessToken;
}
