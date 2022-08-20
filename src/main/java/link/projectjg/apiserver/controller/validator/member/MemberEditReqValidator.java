package link.projectjg.apiserver.controller.validator.member;

import link.projectjg.apiserver.domain.Member;
import link.projectjg.apiserver.domain.MemberAccount;
import link.projectjg.apiserver.dto.member.MemberEditReq;
import link.projectjg.apiserver.exception.CustomException;
import link.projectjg.apiserver.exception.ErrorCode;
import link.projectjg.apiserver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class MemberEditReqValidator implements Validator {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(MemberEditReq.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        MemberEditReq memberEditReq = (MemberEditReq)object;

        if (memberRepository.existsByNickname(memberEditReq.getNickname())) {
            errors.rejectValue("nickname", "duplicate");
        }

        if (memberEditReq.getPasswordDto() != null) {
            String password = ((MemberAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPassword();
            if (!passwordEncoder.matches(memberEditReq.getPasswordDto().getPassword(), password)) {
                throw new CustomException(ErrorCode.INVALID_PASSWORD);
            }
        }


    }
}
