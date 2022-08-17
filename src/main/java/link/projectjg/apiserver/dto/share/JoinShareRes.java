package link.projectjg.apiserver.dto.share;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "공유 참여 응답 DTO", description = "참여한 공유 ")
public class JoinShareRes {

    @ApiModelProperty(value = "공유 번호", example = "13")
    private Long shareId;

    @ApiModelProperty(value = "공유 제목", example = "넷플릭스 4인 최저가!!", required = true)
    private String shareTitle;

    @ApiModelProperty(value = "서비스 이름", example = "넷플릭스", required = true)
    private String shareServiceName;

    @ApiModelProperty(value = "공유에 대한 설명", example = "4인 공유이고, 최저가 입니다.", required = true)
    private String shareDescription;

    @ApiModelProperty(value = "서비스에 로그인할 ID", example = "rebuild96@naver.com", required = true)
    private String shareEmail;

    @ApiModelProperty(value = "서비스에 로그인할 Password", example = "비밀번호123!", required = true)
    private String sharePassword;

    @ApiModelProperty(value = "하루당 비용", example = "100", required = true)
    private Long shareDailyRate;

    @ApiModelProperty(value = "공유 종료일", example = "2022-12-31", required = true)
    private LocalDate shareTerminateDate;

}
