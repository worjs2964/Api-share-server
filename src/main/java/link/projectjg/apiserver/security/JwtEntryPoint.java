package link.projectjg.apiserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import link.projectjg.apiserver.exception.ErrorCode;
import link.projectjg.apiserver.exception.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        ErrorCode exception = (ErrorCode) request.getAttribute("exception");
        if (exception != null) {
            setResponse(response, request, exception);
        } else {
            setResponse(response, request, ErrorCode.INVALID_AUTH_TOKEN);
        }
    }

    private void setResponse(HttpServletResponse response, HttpServletRequest request, ErrorCode errorCode) throws IOException {

        ErrorResponse errorResponse = ErrorResponse.of(errorCode);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

}
