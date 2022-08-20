package link.projectjg.apiserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import link.projectjg.apiserver.annotation.CurrentMember;
import link.projectjg.apiserver.controller.validator.member.MemberEditReqValidator;
import link.projectjg.apiserver.controller.validator.member.MemberJoinReqValidator;
import link.projectjg.apiserver.domain.Keyword;
import link.projectjg.apiserver.domain.Member;
import link.projectjg.apiserver.dto.Response;
import link.projectjg.apiserver.dto.keyword.KeywordReq;
import link.projectjg.apiserver.dto.keyword.KeywordRes;
import link.projectjg.apiserver.dto.member.*;
import link.projectjg.apiserver.service.KeywordService;
import link.projectjg.apiserver.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.Set;

@Api(tags = {"member"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/members")
public class MemberController {

    private final MemberService memberService;
    private final KeywordService keywordService;
    private final MemberJoinReqValidator memberJoinReqValidator;
    private final MemberEditReqValidator memberEditReqValidator;

    @InitBinder("memberJoinReq")
    public void initJoinValidator(WebDataBinder dataBinder) {
        dataBinder.addValidators(memberJoinReqValidator);
    }

    @InitBinder("memberEditReq")
    public void initEditValidator(WebDataBinder dataBinder) {
        dataBinder.addValidators(memberEditReqValidator);
    }

    @PostMapping
    @ApiOperation(value = "회원 가입 요청", notes = "회원 가입을 진행합니다. 정상적으로 회원가입이 완료되면 응답으로 생성된 회원에 이메일, 닉네임을 반환합니다.")
    public ResponseEntity<Response<MemberJoinRes>> joinMember(@Validated @RequestBody MemberJoinReq memberJoinReq) {
        return new ResponseEntity<>(Response.OK(memberService.joinMember(memberJoinReq)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "회원 프로필 확인",
            notes = "회원의 프로필을 확인합니다. 타인의 프로필인 경우 id, nickname, description, shareList(VISIBLE인 공유)에 대한 정보만 확인할 수 있습니다.")
    public ResponseEntity<Response<MemberProfileRes>> showProfile(@ApiIgnore @CurrentMember Member member, @PathVariable Long id) {
        return new ResponseEntity<>(Response.OK(memberService.showProfile(member, id)), HttpStatus.OK);
    }

    @PostMapping("/authentication")
    @ApiOperation(value = "인증 메일 재전송 요청", notes = "인증 메일을 재전송할 수 있습니다. 단 인증메일은 아직 인증이 되지 않은 회원이 인증 메일을 보낸지 1분이 지나야 보낼 수 있습니다.")
    public ResponseEntity<Response<String>> resendAuthenticationEmail(@ApiIgnore Principal principal) {
        return new ResponseEntity<>(Response.OK(memberService.resendAuthenticationEmail(principal.getName())), HttpStatus.OK);
    }

    @GetMapping("/authentication")
    @ApiOperation(value = "인증 메일 확인", notes = "인증 메일을 확인용 요청입니다. 인증은 메일이 발송된 후 3분안에 완료해야 합니다.")
    public ResponseEntity<Response<String>> checkEmailToken(@RequestParam("token") String token, @RequestParam("email") String email) {
        return new ResponseEntity<>(Response.OK(memberService.authenticate(token, email)), HttpStatus.OK);
    }

    @PutMapping("/keywords")
    @ApiOperation(value = "회원 관심사 등록/삭제 요청", notes = "전송받은 키워드 목록으로 회원 관심사 키워드를 변경합니다.")
    public ResponseEntity<Response<KeywordRes>> addKeywords(@ApiIgnore @CurrentMember Member member, @Validated @RequestBody KeywordReq keywordReq) {
        Set<Keyword> keywords = keywordService.saveKeywords(keywordReq.getKeywordSet());
        return new ResponseEntity<>(Response.OK(memberService.addKeywords(member, keywords)), HttpStatus.OK);
    }

    @PatchMapping
    @ApiOperation(value = "회원 정보 수정", notes = "회원 정보를 수정합니다. 수정할 내용만 요청으로 넘겨주면 해당 내용이 수정됩니다.")
    public ResponseEntity<Response<MemberEditRes>> editMember(@ApiIgnore @CurrentMember Member member, @Validated @RequestBody MemberEditReq memberEditReq) {
        return new ResponseEntity<>(Response.OK(memberService.editMember(member, memberEditReq)), HttpStatus.OK);
    }

}
