package link.projectjg.apiserver.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value = "페이징 정보")
public class Pagination {

    @ApiModelProperty(value = "총 페이지 수", example = "3")
    private Integer totalPages;

    @ApiModelProperty(value = "총 엘리먼트 수", example = "41")
    private Long totalElements;

    @ApiModelProperty(value = "현재 페이지(0부터)", example = "2")
    private Integer currentPage;

    @ApiModelProperty(value = "현재 페이지 엘리먼트 수", example = "1")
    private Integer currentElements;
}
