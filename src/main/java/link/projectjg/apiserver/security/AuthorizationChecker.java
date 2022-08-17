package link.projectjg.apiserver.security;


import link.projectjg.apiserver.domain.Member;
import link.projectjg.apiserver.domain.share.Share;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("authorizationChecker")
@RequiredArgsConstructor
public class AuthorizationChecker {

    // 공유의 주인인지 확인
    public boolean isMaster(Member member, Share share) {
        return share.isMaster(member);
    }
}
