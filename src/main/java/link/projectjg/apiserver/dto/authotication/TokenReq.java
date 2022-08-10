package link.projectjg.apiserver.dto.authotication;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "토큰 재발급 req", description = "accessToken, refreshToken 을 전송")
public class TokenReq {

    @ApiModelProperty(value = "access 토큰", example = "access token 값", required = true)
    @NotBlank
    private String accessToken;

    @ApiModelProperty(value = "refresh 토큰", example = "refresh token 값", required = true)
    @NotBlank
    private String refreshToken;

}
