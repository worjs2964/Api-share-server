package link.projectjg.apiserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import link.projectjg.apiserver.annotation.CurrentMember;
import link.projectjg.apiserver.domain.Keyword;
import link.projectjg.apiserver.domain.Member;
import link.projectjg.apiserver.dto.Response;
import link.projectjg.apiserver.dto.keyword.KeywordReq;
import link.projectjg.apiserver.dto.keyword.KeywordRes;
import link.projectjg.apiserver.service.KeywordService;
import link.projectjg.apiserver.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Set;

@Api(tags = {"profile"})
@RestController
@RequestMapping("/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final KeywordService keywordService;
    private final MemberService memberService;

    @PutMapping("/keywords")
    @ApiOperation(value = "회원 관심사 등록/삭제 요청", notes = "전송받은 키워드 목록으로 회원 관심사 키워드를 변경합니다.")
    public ResponseEntity<Response<KeywordRes>> addKeywords(@ApiIgnore @CurrentMember Member member, @Validated @RequestBody KeywordReq keywordReq) {
        Set<Keyword> keywords = keywordService.saveKeywords(keywordReq.getKeywordSet());
        return new ResponseEntity<>(Response.OK(memberService.addKeywords(member, keywords)), HttpStatus.OK);
    }
}
