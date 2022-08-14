package link.projectjg.apiserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import link.projectjg.apiserver.dto.Response;
import link.projectjg.apiserver.dto.authentication.LogoutReq;
import link.projectjg.apiserver.dto.authentication.SignInReq;
import link.projectjg.apiserver.dto.authentication.TokenReq;
import link.projectjg.apiserver.dto.authentication.TokenRes;
import link.projectjg.apiserver.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"authentication"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/sign-in")
    @ApiOperation(value = "로그인 요청", notes = "로그인 성공 시 액세스 토큰, 리플래쉬 토큰을 발급합니다.")
    public ResponseEntity<Response<TokenRes>> signIn(@Validated @RequestBody SignInReq signInReq) {
        return new ResponseEntity<>(Response.OK(authenticationService.login(signInReq)), HttpStatus.OK);
    }

    @PostMapping("/logout")
    @ApiOperation(value = "로그아웃 요청", notes = "현재 사용중인 액세스 토큰을 전송하여 액세스 토큰, 리프래쉬 토큰을 중지시킵니다.")
    public ResponseEntity<Response<String>> logout(@Validated @RequestBody LogoutReq logoutReq) {
        return new ResponseEntity<>(Response.OK(authenticationService.logout(logoutReq)), HttpStatus.OK);
    }

    @PostMapping("/reissue")
    @ApiOperation(value = "액세스 토큰 재발급 요청", notes = "액세스 토큰과 같이 리프래쉬 토큰을 요청으로 넘겨주면, 액세스 토큰과 리프래쉬 토큰을 재발급 해줍니다.")
    public ResponseEntity<Response<TokenRes>> reissue(@Validated @RequestBody TokenReq tokenReq) {
        return new ResponseEntity<>(Response.OK(authenticationService.reissue(tokenReq)), HttpStatus.OK);
    }
}
