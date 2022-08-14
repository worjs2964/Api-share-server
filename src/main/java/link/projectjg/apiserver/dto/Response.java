package link.projectjg.apiserver.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "응답 형식", description = "요청 성공 시 응답 양식")
@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Response<T> {

    @ApiModelProperty(value = "성공 메시지", example = "SUCCESS_REQUEST")
    private String code;

    @ApiModelProperty(value = "응답 데이터")
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
