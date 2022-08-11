package link.projectjg.apiserver.service;

import link.projectjg.apiserver.domain.LogoutAccessToken;
import link.projectjg.apiserver.domain.Member;
import link.projectjg.apiserver.domain.RefreshToken;
import link.projectjg.apiserver.dto.authentication.*;
import link.projectjg.apiserver.exception.CustomException;
import link.projectjg.apiserver.exception.ErrorCode;
import link.projectjg.apiserver.repository.LogoutAccessTokenRedisRepository;
import link.projectjg.apiserver.repository.MemberRepository;
import link.projectjg.apiserver.repository.RefreshTokenRedisRepository;
import link.projectjg.apiserver.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRedisRepository refreshTokenRepository;
    private final LogoutAccessTokenRedisRepository logoutRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    @Value("${spring.jwt.refreshTokenExpiration}")
    private Long REFRESH_TOKEN_EXPIRATION_TIME;

    public TokenRes login(SignInReq signInReq) {
        String memberUid = verifySignInReq(signInReq);

        String accessToken = jwtTokenUtil.generateAccessToken(memberUid);
        String refreshToken = generateRefreshToken(memberUid);

        return TokenRes.of(accessToken, refreshToken);
    }

    private String verifySignInReq(SignInReq signInReq) {
        Member member = memberRepository.findByEmail(signInReq.getEmail()).orElseThrow(() -> new CustomException(ErrorCode.MISMATCH_MEMBER_INFO));
        if (!passwordEncoder.matches(signInReq.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.MISMATCH_MEMBER_INFO);
        }
        return member.getMemberUid();
    }

    private String generateRefreshToken(String memberUid) {
        return refreshTokenRepository.save(RefreshToken.builder()
                .memberUid(memberUid)
                .refreshToken(jwtTokenUtil.generateRefreshToken())
                .expiration(REFRESH_TOKEN_EXPIRATION_TIME / 1000).build()).getRefreshToken();
    }

    public void logout(LogoutReq logoutReq) {
        String accessToken = logoutReq.getAccessToken();
        logoutToken(accessToken);
    }

    public TokenRes reissue(TokenReq tokenReq) {

        String accessToken = tokenReq.getAccessToken();
        String refreshToken = tokenReq.getRefreshToken();

        // 토큰 검증
        if (logoutRepository.existsById(accessToken)) throw new CustomException(ErrorCode.INVALID_AUTH_TOKEN);
        if (jwtTokenUtil.isTokenExpired(refreshToken)) throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);

        // 사용자가 보내온 refresh 토큰과 서버에 저장된 refresh 토큰을 비교
        String memberUid = jwtTokenUtil.getPayloadInfo(accessToken, "memberUid");
        String findRefreshToken = refreshTokenRepository.findById(memberUid).orElseThrow(() -> new CustomException(ErrorCode.INVALID_AUTH_TOKEN)).getRefreshToken();
        if (!refreshToken.equals(findRefreshToken)) throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);

        // 토큰이 아직 만료되지 않았으면 토큰을 정지시킴
        logoutToken(accessToken);

        String newAccessToken = jwtTokenUtil.generateAccessToken(memberUid);
        String newRefreshToken = generateRefreshToken(memberUid);
        return new TokenRes(newAccessToken, newRefreshToken);
    }

    private void logoutToken(String accessToken) {
        if (!jwtTokenUtil.isTokenExpired(accessToken)) {
            logoutRepository.save(LogoutAccessToken.builder()
                    .id(accessToken)
                    .expiration(jwtTokenUtil.getRemainMilliSeconds(accessToken)/1000).build());
        }
    }
}
