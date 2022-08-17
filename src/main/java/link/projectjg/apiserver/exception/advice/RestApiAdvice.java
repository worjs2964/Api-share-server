package link.projectjg.apiserver.exception.advice;

import link.projectjg.apiserver.exception.CustomException;
import link.projectjg.apiserver.exception.ErrorCode;
import link.projectjg.apiserver.exception.ErrorContent;
import link.projectjg.apiserver.exception.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class RestApiAdvice {

    private final MessageSource messageSource;

    // Bean validation 관련 에러 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> BindException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error("URI: ({}){}", request.getMethod(), request.getRequestURI(), e);
        List<ErrorContent> errorList = new ArrayList<>();

        BindingResult bindingResult = e.getBindingResult();
        bindingResult.getAllErrors().forEach(error -> {

            ErrorContent errorContent = new ErrorContent();

            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                errorContent.setField(fieldError.getField());
                errorContent.setMessage(getMessage(fieldError));
                if (fieldError.getRejectedValue() != null) {
                    errorContent.setInvalidValue(fieldError.getRejectedValue().toString());
                }
            } else {
                errorContent.setMessage(getMessage(error));
            }
            errorList.add(errorContent);
        });

        return ErrorResponse.toResponseEntity(ErrorCode.INVALID_VALUE, errorList);
    }

    // errors.properties 에 저장된 메시지 사용
    private String getMessage(ObjectError error) {
        return Arrays.stream(Objects.requireNonNull(error.getCodes()))
                .map(c-> {
                    Object[] arguments = error.getArguments();
                    Locale locale = LocaleContextHolder.getLocale();
                    try {
                        return messageSource.getMessage(c, arguments, locale);
                    } catch (NoSuchMessageException noSuchMessageException) {
                        return null;
                    }
                }).filter(Objects::nonNull)
                .findFirst()
                .orElse(error.getDefaultMessage());
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> CustomException(CustomException e, HttpServletRequest request) {
        log.error("URI: ({}){}",request.getMethod(), request.getRequestURI(), e);
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> CustomException(AccessDeniedException e, HttpServletRequest request) {
        log.error("URI: ({}){}",request.getMethod(), request.getRequestURI(), e);
        return ErrorResponse.toResponseEntity(ErrorCode.DENIED_AUTH_TOKEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception e, HttpServletRequest request) {
        log.error("URI: ({}){}",request.getMethod(), request.getRequestURI(), e);
        return ErrorResponse.toResponseEntity(ErrorCode.SERVER_ERROR);
    }
}
