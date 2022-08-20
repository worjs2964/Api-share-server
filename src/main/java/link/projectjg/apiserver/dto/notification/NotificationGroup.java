package link.projectjg.apiserver.dto.notification;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(value = "알림 그룹", description = "알림 타입별로 구분")
public class NotificationGroup {

    @ApiModelProperty(value = "키워드 알림")
    private List<NotificationDto> create = new ArrayList<>();

    @ApiModelProperty(value = "참여 알림")
    private List<NotificationDto> join = new ArrayList<>();

    @ApiModelProperty(value = "공유 변동 알림")
    private List<NotificationDto> edit = new ArrayList<>();
}
