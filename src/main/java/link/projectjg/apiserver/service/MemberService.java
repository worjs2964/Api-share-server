package link.projectjg.apiserver.service;

import link.projectjg.apiserver.domain.Member;
import link.projectjg.apiserver.dto.member.MemberJoinReq;
import link.projectjg.apiserver.dto.member.MemberJoinRes;
import link.projectjg.apiserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;

    // 회원가입
    public MemberJoinRes joinMember(MemberJoinReq memberJoinReq) {
        Member member = modelMapper.map(memberJoinReq, Member.class);
        member.createMemberUid();
        Member saveMember = memberRepository.save(member);
        return modelMapper.map(saveMember, MemberJoinRes.class);
    }
}
