package link.projectjg.apiserver.dto.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenDto {

    private String id;

    private String refreshToken;

    private Long expiration;
}
