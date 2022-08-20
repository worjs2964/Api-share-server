package link.projectjg.apiserver.service;

import link.projectjg.apiserver.domain.Keyword;
import link.projectjg.apiserver.domain.Member;
import link.projectjg.apiserver.domain.MemberShare;
import link.projectjg.apiserver.domain.share.Share;
import link.projectjg.apiserver.domain.share.ShareState;
import link.projectjg.apiserver.dto.keyword.KeywordRes;
import link.projectjg.apiserver.dto.member.*;
import link.projectjg.apiserver.exception.CustomException;
import link.projectjg.apiserver.exception.ErrorCode;
import link.projectjg.apiserver.mail.EmailMessage;
import link.projectjg.apiserver.mail.MailSender;
import link.projectjg.apiserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final MailSender mailSender;
    private final TemplateEngine templateEngine;

    // 회원가입
    public MemberJoinRes joinMember(MemberJoinReq memberJoinReq) {
        Member member = memberJoinReq.toEntity();
        member.init();
        member.encodePassword(passwordEncoder);
        Member saveMember = memberRepository.save(member);
        sendEmailCheckToken(member);
        return modelMapper.map(saveMember, MemberJoinRes.class);
    }

    public MemberProfileRes showProfile(Member member, Long id) {
        Member findMember = memberRepository.findWithShareListById(id).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        List<Share> myShareList = findMember.getMyShareList();

        if (findMember.equals(member)) {
            // 참여 중인 공유 확인
            List<Share> participatedShareList = findMember.getMemberShares().stream()
                    .map(MemberShare::getShare).collect(Collectors.toList());
            return MemberProfileRes.of(findMember, myShareList, participatedShareList);
        } else {
            // 해당 회원의 공유 중 모집 중인 공유만 볼 수 있게 설정
            List<Share> shareList = myShareList.stream().filter(share ->
                    share.getShareState().equals(ShareState.VISIBLE)
            ).collect(Collectors.toList());
            return MemberProfileRes.of(findMember, shareList);
        }
    }

    // 인증 메일 재전송
    public String resendAuthenticationEmail(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.INVALID_VALUE));
        member.generateAuthenticationToken();
        sendEmailCheckToken(member);
        return member.getEmail() + " 계정으로 인증 메일을 재발송하였습니다.";
    }

    // 메일 전송
    private void sendEmailCheckToken(Member member) {
        Context context = new Context();
        context.setVariable("nickname", member.getNickname());
        context.setVariable("link", "v1/members/authentication?token=" + member.getAuthenticationToken() + "&email=" + member.getEmail());
        String message = templateEngine.process("authentication-email", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(member.getEmail())
                .title("ott-share-service 이메일 인증")
                .message(message).build();

        mailSender.send(emailMessage);
    }

    // 인증 진행
    public String authenticate(String token, String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.INVALID_URL));
        member.authenticate(token);
        return email + " 계정의 인증이 완료되었습니다.";
    }

    public KeywordRes addKeywords(Member member, Set<Keyword> keywords) {
        member.setKeywordSet(keywords);
        return modelMapper.map(memberRepository.save(member), KeywordRes.class);
    }

    public MemberEditRes editMember(Member member, MemberEditReq memberEditReq) {
        memberRepository.save(update(member, memberEditReq));
        return modelMapper.map(member, MemberEditRes.class);
    }

    private Member update(Member member, MemberEditReq memberEditReq) {
        member.updateNickname(memberEditReq.getNickname());
        member.updateDescription(memberEditReq.getDescription());
        member.updateKeywordByEmail(memberEditReq.getIsKeywordByEmail());
        member.updateNotificationByEmail(memberEditReq.getIsNotificationByEmail());
        member.updateKeywordByWeb(memberEditReq.getIsKeywordByWeb());
        member.updateNotificationByWeb(memberEditReq.getIsNotificationByWeb());
        if (memberEditReq.getPasswordDto() != null) {
            member.updatePassword(memberEditReq.getPasswordDto().getNewPassword());
            member.encodePassword(passwordEncoder);
        }
        return member;
    }
}
