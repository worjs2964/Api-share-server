package link.projectjg.apiserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import link.projectjg.apiserver.domain.Member;
import link.projectjg.apiserver.domain.share.Share;
import link.projectjg.apiserver.dto.Response;
import link.projectjg.apiserver.dto.share.JoinShareRes;
import link.projectjg.apiserver.service.PayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"payment"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payment")
public class PayController {

    private final PayService payService;

    @GetMapping("/success")
    @ApiOperation(value = "결제 인증 요청", notes = "결제 진행 시 자동적으로 호출되는 api")
    public ResponseEntity<Response<JoinShareRes>> paySuccess(@RequestParam("pg_token") String pgToken,
                                                             @RequestParam("shareId") Share share,
                                                             @RequestParam("memberId") Member member) {
        return new ResponseEntity<>(Response.OK(payService.approvePay(share, member, pgToken)), HttpStatus.OK);
    }
}
