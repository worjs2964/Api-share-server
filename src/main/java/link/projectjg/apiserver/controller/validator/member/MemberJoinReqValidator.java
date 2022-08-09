package link.projectjg.apiserver.controller.validator.member;

import link.projectjg.apiserver.dto.member.MemberJoinReq;
import link.projectjg.apiserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class MemberJoinReqValidator implements Validator {

    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(MemberJoinReq.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        MemberJoinReq memberJoinReq = (MemberJoinReq)object;

        if (memberRepository.existsByEmail(memberJoinReq.getEmail())) {
            errors.rejectValue("email", "duplicate");
        }

        if (memberRepository.existsByNickname(memberJoinReq.getNickname())) {
            errors.rejectValue("nickname", "duplicate");
        }
    }
}
