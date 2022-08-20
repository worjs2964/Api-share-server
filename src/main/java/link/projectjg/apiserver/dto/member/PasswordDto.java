package link.projectjg.apiserver.dto.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@ApiModel(value = "패스워드 DTO")
public class PasswordDto {

    @NotBlank
    @Pattern(regexp = "^.*(?=^.{6,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$")
    @ApiModelProperty(value = "기존 패스워드(확인용)", example = "Password96!", required = true)
    private String password;

    @NotBlank
    @Pattern(regexp = "^.*(?=^.{6,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$")
    @ApiModelProperty(value = "새로운 패스워드", example = "Password1996!", required = true)
    private String newPassword;

}
