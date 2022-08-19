package link.projectjg.apiserver.dto.share;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import link.projectjg.apiserver.dto.keyword.KeywordDto;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@ApiModel(value = "공유 수정 요청 DTO")
public class EditShareReq {

    @ApiModelProperty(value = "공유 제목", example = "넷플릭스 4인 최저가!!")
    @Size(max = 20)
    private String title;

    @ApiModelProperty(value = "서비스 이름", example = "넷플릭스")
    private String serviceName;

    @ApiModelProperty(value = "공유에 대한 설명", example = "4인 공유이고, 최저가 입니다.")
    private String description;

    @ApiModelProperty(value = "서비스에 로그인할 ID", example = "rebuild96@naver.com")
    private String shareEmail;

    @ApiModelProperty(value = "서비스에 로그인할 Password", example = "비밀번호123!")
    private String sharePassword;

    @ApiModelProperty(value = "하루당 비용", example = "100")
    @DecimalMin(value = "0")
    private Long dailyRate;

    @ApiModelProperty(value = "모집 인원", example = "4")
    @DecimalMin(value = "1")
    private Long numberRecruits;

    @ApiModelProperty(value = "공유 종료일", example = "2022-12-31")
    @FutureOrPresent
    private LocalDate shareTerminateDate;
}
