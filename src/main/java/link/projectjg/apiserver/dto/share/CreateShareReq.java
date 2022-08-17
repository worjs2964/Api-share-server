package link.projectjg.apiserver.dto.share;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import link.projectjg.apiserver.domain.Keyword;
import link.projectjg.apiserver.domain.Member;
import link.projectjg.apiserver.domain.share.ContentType;
import link.projectjg.apiserver.domain.share.Share;
import link.projectjg.apiserver.domain.share.ShareState;
import link.projectjg.apiserver.dto.keyword.KeywordDto;
import lombok.Data;

import javax.persistence.Lob;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@ApiModel(value = "공유 생성 요청 DTO", description = "contentType은 VIDEO, MUSIC, GAME, ETC 중 하나만 가능합니다.")
public class CreateShareReq {

    @ApiModelProperty(value = "콘텐츠 타입", example = "VIDEO", required = true)
    @NotNull
    private ContentType contentType;

    @ApiModelProperty(value = "공유 제목", example = "넷플릭스 4인 최저가!!", required = true)
    @NotBlank
    @Size(max = 20)
    private String title;

    @ApiModelProperty(value = "서비스 이름", example = "넷플릭스", required = true)
    @NotBlank
    private String serviceName;

    @ApiModelProperty(value = "공유에 대한 설명", example = "4인 공유이고, 최저가 입니다.", required = true)
    @NotBlank
    private String description;

    @ApiModelProperty(value = "서비스에 로그인할 ID", example = "rebuild96@naver.com", required = true)
    @NotBlank
    private String shareEmail;

    @ApiModelProperty(value = "서비스에 로그인할 Password", example = "비밀번호123!", required = true)
    @NotBlank
    private String sharePassword;

    @ApiModelProperty(value = "하루당 비용", example = "100", required = true)
    @DecimalMin(value = "0")
    private long dailyRate;

    @ApiModelProperty(value = "모집 인원", example = "4", required = true)
    @DecimalMin(value = "1")
    private long numberRecruits;

    @ApiModelProperty(value = "공유 종료일", example = "2022-12-31", required = true)
    @FutureOrPresent @NotNull
    private LocalDate shareTerminateDate;

    @ApiModelProperty(value = "공유 키워드")
    @Size(max = 3)
    @Valid
    private Set<KeywordDto> keywordSet = new HashSet<>();

    public Share toEntity(Member member, Set<Keyword> keywords) {
        return Share.builder()
                .master(member)
                .shareState(ShareState.INVISIBLE)
                .contentType(contentType)
                .title(title)
                .serviceName(serviceName)
                .description(description)
                .shareEmail(shareEmail)
                .sharePassword(sharePassword)
                .dailyRate(dailyRate)
                .numberRecruits(numberRecruits)
                .shareTerminateDate(shareTerminateDate)
                .keywordSet(keywords).build();
    }

}
