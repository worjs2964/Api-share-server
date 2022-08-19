package link.projectjg.apiserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import link.projectjg.apiserver.annotation.CurrentMember;
import link.projectjg.apiserver.domain.Keyword;
import link.projectjg.apiserver.domain.Member;
import link.projectjg.apiserver.domain.share.Share;
import link.projectjg.apiserver.dto.Response;
import link.projectjg.apiserver.dto.kakao.ReadyPayRes;
import link.projectjg.apiserver.dto.keyword.KeywordReq;
import link.projectjg.apiserver.dto.keyword.KeywordRes;
import link.projectjg.apiserver.dto.share.*;
import link.projectjg.apiserver.service.KeywordService;
import link.projectjg.apiserver.service.ShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Set;

@Api(tags = {"share"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/shares")
public class ShareController {

    private final ShareService shareService;
    private final KeywordService keywordService;

    @GetMapping
    @ApiOperation(value = "공유 목록 검색 (간략한 정보)",
            notes = "공유 목록을 검색합니다. 페이지 번호는 0부터 시작하며 조건을 입력하지 않으면 모든 내용이 검색됩니다. \n" +
                    "ex) ...?page=0&size=20 -> 최신 공유 20개 검색, ...?contentType=VIDEO&page=0&size=20 -> 영상 공유 20개 검색")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "페이지 번호", required = false, dataType = "string", paramType = "query", defaultValue = "0"),
            @ApiImplicitParam(name = "size", value = "페이지 당 사이즈", required = false, dataType = "string", paramType = "query", defaultValue = "20")
    })
    public ResponseEntity<Response<SearchShareListRes>> showShareList(@ApiIgnore @PageableDefault(size = 20) Pageable pageable,
                                                                      SearchShareListReq searchShareListReq) {
        return new ResponseEntity<>(Response.OK(shareService.showShareList(pageable, searchShareListReq)), HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation(value = "공유 생성 요청",
            notes = "공유를 생성합니다. 공유는 인증된 회원(이메일 인증)만 생성할 수 있고 키워드는 최대 3개 까지 등록이 가능하며 비공개 상태(INVISIBLE)로 생성됩니다.")
    public ResponseEntity<Response<ShareRes>> createShare(@ApiIgnore @CurrentMember Member member,
                                                          @Validated @RequestBody CreateShareReq createShareReq) {
        Set<Keyword> keywords = keywordService.saveKeywords(createShareReq.getKeywordSet());
        return new ResponseEntity<>(Response.OK(shareService.createShare(member, createShareReq, keywords)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "공유 검색 (단건)",
            notes = "공유를 검색합니다. 검색 결과는 공유의 주인, 참여자, 그 외에 따라 볼 수 없는 항목은 null로 내려옵니다.")
    public ResponseEntity<Response<SearchShareRes>> showShare(@ApiIgnore @CurrentMember Member member, @PathVariable("id") Long shareId) {
        return new ResponseEntity<>(Response.OK(shareService.showShare(shareId, member)), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "공유 수정 요청",
            notes = "공유를 수정합니다. 수정할 내용만 선택하여 보내면 되며 종료된 공유는 수정할 수 없습니다. 참여한 사람이 있는 경우 모집 인원을 참여한 인원보다 적게 수정할 수 없습니다.")
    @PreAuthorize("@authorizationChecker.isMaster(#member, #share)")
    public ResponseEntity<Response<ShareRes>> editShare(@ApiIgnore @CurrentMember Member member,
                                                                     @PathVariable("id") Share share,
                                                                     @Validated @RequestBody EditShareReq editShareReq) {
        return new ResponseEntity<>(Response.OK(shareService.editShare(share, editShareReq)), HttpStatus.OK);
    }

    @PostMapping("/{id}/join")
    @ApiOperation(value = "공유 가입 요청", notes = "공유를 가입을 요청합니다. 응답으로 내려온 결제 url 중 환경에 맞는 url 를 선택하여 결제를 진행하면 됩니다.")
    public ResponseEntity<Response<ReadyPayRes>> joinShare(@ApiIgnore @CurrentMember Member member, @PathVariable Long id) {
        return new ResponseEntity<>(Response.OK(shareService.joinShare(member, id)), HttpStatus.OK);
    }

    @PostMapping("/{id}/notify")
    @ApiOperation(value = "공유 키워드 알림 요청",
            notes = "관심 키워드를 지정한 회원들에게 알림을 보냅니다. 알림은 공유 당 1회 보낼 수 있으며, 공개 상태인 공유(VISIBLE)만 보낼 수 있습니다.")
    @PreAuthorize("@authorizationChecker.isMaster(#member, #share)")
    public ResponseEntity<Response<String>> notifyShare(@ApiIgnore @CurrentMember Member member,
                                                        @PathVariable("id") Share share) {
        return new ResponseEntity<>(Response.OK(shareService.notifyShare(share)), HttpStatus.OK);
    }

    @PostMapping("/{id}/state")
    @ApiOperation(value = "공유 공개/비공개 변경 요청",
            notes = "공유를 공개/비공개 상태로 변경합니다. (종료된 공유또는 모집완료된 공유에는 사용할 수 없습니다.)")
    @PreAuthorize("@authorizationChecker.isMaster(#member, #share)")
    public ResponseEntity<Response<ChangeVisibleShareRes>> changeVisibleShare(@ApiIgnore @CurrentMember Member member,
                                                                              @PathVariable("id") Share share) {
        return new ResponseEntity<>(Response.OK(shareService.changeVisibleShare(share)), HttpStatus.OK);
    }

    @PutMapping("/keywords/{id}")
    @ApiOperation(value = "공유 키워드 등록/삭제 요청", notes = "전송받은 키워드 목록으로 키워드를 변경합니다. 키워드는 3개까지 등록이 가능합니다.")
    @PreAuthorize("@authorizationChecker.isMaster(#member, #share)")
    public ResponseEntity<Response<ShareRes>> addKeywords(@ApiIgnore @CurrentMember Member member,
                                                          @PathVariable("id") Share share, @Validated @RequestBody ShareKeywordReq keywordReq) {
        Set<Keyword> keywords = keywordService.saveKeywords(keywordReq.getKeywordSet());
        return new ResponseEntity<>(Response.OK(shareService.addKeywords(share, keywords)), HttpStatus.OK);
    }

}
