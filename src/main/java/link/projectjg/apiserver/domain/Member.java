package link.projectjg.apiserver.domain;

import link.projectjg.apiserver.dto.member.MemberJoinReq;
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
@Getter
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

    private String role;

    // 이메일 인증 관련
    private boolean isAuthentication;

    private String authenticationToken;

    private LocalDateTime tokenIssuanceTime;

    // 프로필 정보
    private int point;

    @Lob
    private String description;

    // 알림 정보
    private boolean isNotificationByWeb;

    private boolean isKeywordByWeb;

    private boolean isNotificationByEmail ;

    private boolean isKeywordByEmail;

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

    public void setKeywordSet(Set<Keyword> keywordSet) {
        this.keywordSet = keywordSet;
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }
}
