package link.projectjg.apiserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import link.projectjg.apiserver.annotation.CurrentMember;
import link.projectjg.apiserver.controller.validator.member.MemberJoinReqValidator;
import link.projectjg.apiserver.domain.Member;
import link.projectjg.apiserver.dto.Response;
import link.projectjg.apiserver.dto.member.MemberJoinReq;
import link.projectjg.apiserver.dto.member.MemberJoinRes;
import link.projectjg.apiserver.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Api(tags = {"member"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/members")
public class MemberController {

    private final MemberService memberService;
    private final MemberJoinReqValidator memberJoinReqValidator;

    @InitBinder
    public void init(WebDataBinder dataBinder) {
        dataBinder.addValidators(memberJoinReqValidator);
    }

    @PostMapping
    @ApiOperation(value = "회원 가입 요청", notes = "회원 가입을 진행합니다. 정상적으로 회원가입이 완료되면 응답으로 생성된 회원에 이메일, 닉네임을 반환합니다.")
    public ResponseEntity<Response<MemberJoinRes>> joinMember(@Validated @RequestBody MemberJoinReq memberJoinReq) {
        return new ResponseEntity<>(Response.OK(memberService.joinMember(memberJoinReq)), HttpStatus.OK);
    }

    @GetMapping("/authentication")
    @ApiOperation(value = "인증 메일 확인", notes = "인증 메일을 확인용 요청입니다. 인증은 메일이 발송된 후 3분안에 완료해야 합니다.")
    public ResponseEntity<Response<String>> checkEmailToken(@RequestParam("token") String token, @RequestParam("email") String email) {
        return new ResponseEntity<>(Response.OK(memberService.authenticate(token, email)), HttpStatus.OK);
    }
}
