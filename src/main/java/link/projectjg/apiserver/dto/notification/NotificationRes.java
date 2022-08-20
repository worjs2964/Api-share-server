package link.projectjg.apiserver.dto.notification;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel(value = "알림 응답 DTO")
@Builder @NoArgsConstructor @AllArgsConstructor
public class NotificationRes {

    @ApiModelProperty(value = "새로운 알림")
    private NotificationGroup newNotifications;

    @ApiModelProperty(value = "확인한 알림")
    private NotificationGroup oldNotifications;
}
