package link.projectjg.apiserver.dto.authotication;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@ApiModel(value = "로그인 req", description = "이메일, 페스워드를 입력 받는다.")
public class SignInReq {

    @ApiModelProperty(value = "이메일", example = "rebuild96@naver.com", required = true)
    @NotBlank
    @Email(regexp = "^[a-z0-9]+@[a-z0-9]+\\.[a-z0-9]+$")
    private String email;

    @ApiModelProperty(value = "패스워드", example = "Password96!", required = true)
    @NotBlank
    @Size(min = 6, max = 20)
    private String password;

}
