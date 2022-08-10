package link.projectjg.apiserver.domain;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class MemberAccount extends User {

    public Member member;

    public MemberAccount(Member member) {
        super(member.getEmail(), member.getPassword(), List.of(new SimpleGrantedAuthority(member.getRole())));
        this.member = member;
    }
}
