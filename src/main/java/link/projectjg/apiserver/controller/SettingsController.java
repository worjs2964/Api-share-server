package link.projectjg.apiserver.controller;

import link.projectjg.apiserver.annotation.CurrentMember;
import link.projectjg.apiserver.domain.Keyword;
import link.projectjg.apiserver.domain.Member;
import link.projectjg.apiserver.dto.Response;
import link.projectjg.apiserver.dto.keyword.KeywordReq;
import link.projectjg.apiserver.service.KeywordService;
import link.projectjg.apiserver.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Set;

@RestController
@RequestMapping("/v1/settings")
@RequiredArgsConstructor
public class SettingsController {

    private final KeywordService keywordService;
    private final MemberService memberService;

    @PutMapping("/keywords")
    public ResponseEntity<Response> addKeywords(@ApiIgnore @CurrentMember Member member, @Validated @RequestBody KeywordReq keywordReq) {
        Set<Keyword> keywords = keywordService.saveKeywords(keywordReq);
        return new ResponseEntity<>(Response.OK(memberService.addKeywords(member, keywords)), HttpStatus.OK);
    }
}
