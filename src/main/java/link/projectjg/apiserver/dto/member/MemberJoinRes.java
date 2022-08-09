package link.projectjg.apiserver.dto.member;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class MemberJoinRes {

    private String email;

    private String nickname;

}
