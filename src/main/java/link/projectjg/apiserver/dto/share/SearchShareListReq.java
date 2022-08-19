package link.projectjg.apiserver.dto.share;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import link.projectjg.apiserver.domain.share.ContentType;
import lombok.Data;

@Data
@ApiModel(value = "검색 검색 요청 DTO")
public class SearchShareListReq {

    @ApiModelProperty(value = "타입")
    private ContentType contentType;

    @ApiModelProperty(value = "검색어")
    private String keyword;
}
