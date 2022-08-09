package link.projectjg.apiserver.exception;

import lombok.Data;

@Data
public class ErrorContent {

    private String field;

    private String message;

    private String invalidValue;
}
