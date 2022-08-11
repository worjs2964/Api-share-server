package link.projectjg.apiserver.dto.authentication;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "로그아웃 req", description = "accessToken을 전송")
public class LogoutReq {

    @ApiModelProperty(value = "access 토큰", example = "access token 값", required = true)
    @NotBlank
    private String accessToken;
}
