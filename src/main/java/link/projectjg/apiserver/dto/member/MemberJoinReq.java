package link.projectjg.apiserver.dto.member;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class MemberJoinReq {

    @NotBlank
    @Email(regexp = "^[a-z0-9]+@[a-z0-9]+\\.[a-z0-9]+$")
    private String email;

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9]{2,20}$")
    private String nickname;

    @NotBlank
    @Pattern(regexp = "^.*(?=^.{6,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$")
    private String password;
}
