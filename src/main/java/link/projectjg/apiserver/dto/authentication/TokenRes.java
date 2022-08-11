package link.projectjg.apiserver.dto.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class TokenRes {

    private String accessToken;
    private String refreshToken;

    public static TokenRes of(String accessToken, String refreshToken) {
        return TokenRes.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
