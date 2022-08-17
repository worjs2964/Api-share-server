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
@ApiModel(value = "공유 상태 변경 응답 DTO", description = "공유 번호와 바뀐 상태를 내려줍니다.")
public class ChangeVisibleShareRes {

    @ApiModelProperty(value = "공유 번호", example = "13")
    private Long id;

    @ApiModelProperty(value = "공유 상태", example = "INVISIBLE")
    private ShareState shareState;

}
