package link.projectjg.apiserver.controller;

import io.swagger.annotations.ApiOperation;
import link.projectjg.apiserver.dto.Response;
import link.projectjg.apiserver.dto.authotication.LogoutReq;
import link.projectjg.apiserver.dto.authotication.SignInReq;
import link.projectjg.apiserver.dto.authotication.TokenReq;
import link.projectjg.apiserver.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/sign-in")
    @ApiOperation(value = "로그인", notes = "로그인 성공 시 access token, refresh token을 발급합니다.")
    public ResponseEntity<Response> signIn(@Validated @RequestBody SignInReq signInReq) {
        return new ResponseEntity<>(Response.OK(authenticationService.login(signInReq)), HttpStatus.OK);
    }

    @PostMapping("/logout")
    @ApiOperation(value = "로그아웃", notes = "access token을 헤더로 전송하면 refresh token을 사용하지 못하도록 만듭니다.")
    public ResponseEntity<Response> logout(@Validated @RequestBody LogoutReq logoutReq) {
        authenticationService.logout(logoutReq);
        return new ResponseEntity<>(Response.OK(), HttpStatus.OK);
    }

    @PostMapping("/reissue")
    @ApiOperation(value = "access token 재발급", notes = "access token와 같이 발급된 refresh token을 주면 새로은 access, refresh token을 발급합니다.")
    public ResponseEntity<Response> reissue(@Validated @RequestBody TokenReq tokenReq) {
        return new ResponseEntity<>(Response.OK(authenticationService.reissue(tokenReq)), HttpStatus.OK);
    }
}
