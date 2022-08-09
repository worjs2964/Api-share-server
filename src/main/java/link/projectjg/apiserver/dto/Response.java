package link.projectjg.apiserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Response<T> {

    private String code;

    private T success;

    @SuppressWarnings("unchecked")
    public static <T> Response<T> OK() {
        return (Response<T>) Response.builder()
                .code("SUCCESS_REQUEST")
                .build();
    }

    @SuppressWarnings("unchecked")
    public static <T> Response<T> OK(T data) {
        return (Response<T>) Response.builder()
                .code("SUCCESS_REQUEST")
                .success(data)
                .build();
    }
}
