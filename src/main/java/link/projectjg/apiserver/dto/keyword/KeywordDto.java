package link.projectjg.apiserver.dto.keyword;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class KeywordDto {

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9가-힣]{1,20}$")
    @ApiModelProperty(value = "키워드", example = "넷플릭스")
    private String keyword;

}
