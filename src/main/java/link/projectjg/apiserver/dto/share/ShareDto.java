package link.projectjg.apiserver.dto.share;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import link.projectjg.apiserver.domain.share.ContentType;
import link.projectjg.apiserver.domain.share.ShareState;
import link.projectjg.apiserver.dto.keyword.KeywordDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@ApiModel(value = "공유 DTO")
public class ShareDto {

    @ApiModelProperty(value = "공유 번호", example = "13", required = true)
    private Long id;

    @ApiModelProperty(value = "콘텐츠 타입", example = "VIDEO", required = true)
    private ContentType contentType;

    @ApiModelProperty(value = "공유 제목", example = "넷플릭스 4인 최저가!!", required = true)
    private String title;

    @ApiModelProperty(value = "서비스 이름", example = "넷플릭스", required = true)
    private String serviceName;

    @ApiModelProperty(value = "하루당 비용", example = "100", required = true)
    private Long dailyRate;

    @ApiModelProperty(value = "모집 인원", example = "4", required = true)
    private long numberRecruits;

    @ApiModelProperty(value = "공유 종료일", example = "2022-12-31", required = true)
    private LocalDate shareTerminateDate;

    @ApiModelProperty(value = "공유 키워드 목록")
    private Set<KeywordDto> keywordSet = new HashSet<>();
}
