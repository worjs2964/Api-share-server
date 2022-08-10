package link.projectjg.apiserver.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 400 BAD_REQUEST : 잘못된 요청 */
    MISMATCH_MEMBER_INFO(BAD_REQUEST, "아이디 또는 비밀번호가 일치하지 않습니다."),
    INVALID_VALUE(BAD_REQUEST, "유효하지 않은 값을 입력하였습니다."),
    INVALID_REFRESH_TOKEN(BAD_REQUEST, "리프레시 토큰이 유효하지 않습니다"),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    INVALID_AUTH_TOKEN(UNAUTHORIZED, "토큰이 유효하지 않습니다"),

    /* 403 FORBIDDEN : 권한이 없는 사용자 */
    DENIED_AUTH_TOKEN(FORBIDDEN, "권한이 없는 사용자입니다."),

    /* 404 NOT_FOUND : Resource 를 찾을 수 없음 */
    REQUEST_NOT_FOUND(NOT_FOUND, "해당 요청이 존재하지 않습니다."),
    ARGUMENT_NOT_FOUND(NOT_FOUND, "해당 정보를 찾을 수 없습니다."),

    /* 500 Server Error : 서버 에러 */
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "알 수 없는 서버 에러가 발생하였습니다.");

    private final HttpStatus httpStatus;
    private final String detail;

}
