package link.projectjg.apiserver.domain;

import link.projectjg.apiserver.exception.CustomException;
import link.projectjg.apiserver.exception.ErrorCode;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
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

    // 프로필 정보
    private int point;

    @Lob
    private String description;

    // 알림 정보
    private boolean isNotificationByWeb = true;

    private boolean isKeywordByWeb = true;

    private boolean isNotificationByEmail = false;

    private boolean isKeywordByEmail = false;

    @ManyToMany
    private Set<Keyword> keywordSet = new HashSet<>();


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
