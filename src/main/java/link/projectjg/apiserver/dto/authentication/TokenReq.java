package link.projectjg.apiserver.dto.authentication;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "토큰 재발급 요청 DTO", description = "액세스 토큰과 리플래쉬 토큰을 입력받습니다.")
public class TokenReq {

    @ApiModelProperty(value = "액세스 토큰", example = "액세스 토큰 값", required = true)
    @NotBlank
    private String accessToken;

    @ApiModelProperty(value = "리플래쉬 토큰", example = "리플래쉬 토큰 값", required = true)
    @NotBlank
    private String refreshToken;

}
