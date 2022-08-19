package link.projectjg.apiserver.dto.share;

import io.swagger.annotations.ApiModel;
import link.projectjg.apiserver.dto.Pagination;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(value = "공유 검색 응답 DTO")
public class SearchShareListRes {

    private Pagination pagination;

    private List<ShareDto> shares = new ArrayList<>();
}
