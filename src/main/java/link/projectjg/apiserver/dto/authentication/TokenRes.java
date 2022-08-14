package link.projectjg.apiserver.dto.authentication;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@ApiModel(value = "토큰 응답", description = "인증된 액세스, 리플래쉬 토큰을 내려줍니다.")
@Data
@AllArgsConstructor
@Builder
public class TokenRes {

    @ApiModelProperty(value = "액세스 토큰", example = "액세스 토큰")
    private String accessToken;

    @ApiModelProperty(value = "리프래쉬 토큰", example = "리프래쉬 토큰")
    private String refreshToken;

    public static TokenRes of(String accessToken, String refreshToken) {
        return TokenRes.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
