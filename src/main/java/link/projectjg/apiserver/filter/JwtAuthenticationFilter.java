package link.projectjg.apiserver.filter;

import io.jsonwebtoken.JwtException;
import link.projectjg.apiserver.domain.MemberAccount;
import link.projectjg.apiserver.exception.ErrorCode;
import link.projectjg.apiserver.repository.LogoutAccessTokenRedisRepository;
import link.projectjg.apiserver.security.JwtTokenUtil;
import link.projectjg.apiserver.service.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailServiceImpl userDetailsServiceImpl;
    private final LogoutAccessTokenRedisRepository logoutTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = getToken(authorization);

        try {
            String uid = jwtTokenUtil.getMemberUid(accessToken);
            if (uid != null && !isLogout(accessToken)) {
                MemberAccount memberAccount = userDetailsServiceImpl.loadUserByUsername(uid);
                processSecurity(request, memberAccount);
            } else {
                request.setAttribute("exception", ErrorCode.INVALID_AUTH_TOKEN);
            }
        } catch (JwtException e) {
            request.setAttribute("exception", ErrorCode.INVALID_AUTH_TOKEN);
        }

        filterChain.doFilter(request, response);
    }

    // Authorization ???????????? ???????????? ????????????.
    private String getToken(String authorization) {
            return authorization.replace("Bearer ", "");
    }

    // ??????????????? ???????????? ??????
   private boolean isLogout(String accessToken) {
       return logoutTokenRepository.existsById(accessToken);
    }

    // ?????? ????????? ????????? SecurityContextHolder??? ?????? ????????? ?????????.
    private void processSecurity(HttpServletRequest request, MemberAccount memberAccount) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(memberAccount, null, memberAccount.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
}
