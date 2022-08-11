package link.projectjg.apiserver.domain;

import link.projectjg.apiserver.exception.CustomException;
import link.projectjg.apiserver.exception.ErrorCode;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "memberUid")
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String memberUid;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    private String password;

    private String role = "ROLE_MEMBER";

    // 이메일 인증 관련
    private Boolean isAuthentication = false;

    private String authenticationToken;

    private LocalDateTime tokenIssuanceTime;

    // 회원 기본 데이터 초기화
    public void init() {
        if (this.memberUid == null) {
            this.memberUid = UUID.randomUUID().toString();
            this.generateAuthenticationToken();
        }
    }

    public void generateAuthenticationToken() {
        if (!isAuthentication && canSendEmail()) {
            this.authenticationToken = UUID.randomUUID().toString();
            this.tokenIssuanceTime = LocalDateTime.now();
        } else {
            throw new CustomException(ErrorCode.INVALID_AUTHENTICATION_RESEND_EMAIL);
        }
    }

    private boolean canSendEmail() {
        return tokenIssuanceTime == null || tokenIssuanceTime.isBefore(LocalDateTime.now().minusMinutes(1));
    }

    public void authenticate(String token) {
        if (!isAuthentication && this.authenticationToken.equals(token) && tokenIssuanceTime.isAfter(LocalDateTime.now().minusMinutes(3))) {
            this.isAuthentication = true;
            this.role = "ROLE_CHECKED_MEMBER";
        } else {
            throw new CustomException(ErrorCode.INVALID_URL);
        }
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }
}
