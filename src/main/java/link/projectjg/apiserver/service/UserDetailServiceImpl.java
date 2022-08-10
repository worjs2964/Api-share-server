package link.projectjg.apiserver.service;

import link.projectjg.apiserver.domain.Member;
import link.projectjg.apiserver.domain.MemberAccount;
import link.projectjg.apiserver.exception.CustomException;
import link.projectjg.apiserver.exception.ErrorCode;
import link.projectjg.apiserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;
    @Override
    public MemberAccount loadUserByUsername(String memberUid) throws UsernameNotFoundException {
        Member member = memberRepository.findByMemberUid(memberUid).orElseThrow(() -> new CustomException(ErrorCode.INVALID_AUTH_TOKEN));
        return new MemberAccount(member);
    }
}
