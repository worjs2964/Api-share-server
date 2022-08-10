package link.projectjg.apiserver.domain;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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

    // uuid 생성
    public void initMemberUid() {
        if (this.memberUid == null) {
            this.memberUid = UUID.randomUUID().toString();
        }
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }
}
