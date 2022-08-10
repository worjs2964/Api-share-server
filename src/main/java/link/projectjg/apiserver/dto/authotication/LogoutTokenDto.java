package link.projectjg.apiserver.dto.authotication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogoutTokenDto {

    private String accessToken;

    private Long expiration;
}
