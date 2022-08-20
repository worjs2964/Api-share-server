package link.projectjg.apiserver.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import link.projectjg.apiserver.annotation.CurrentMember;
import link.projectjg.apiserver.domain.Member;
import link.projectjg.apiserver.dto.Response;
import link.projectjg.apiserver.dto.notification.NotificationRes;
import link.projectjg.apiserver.dto.share.ChangeVisibleShareRes;
import link.projectjg.apiserver.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = {"notification"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @ApiOperation(value = "알림 확인 요청", notes = "알림을 확인합니다. 알림을 확인하면 알림이 읽은 상태로 변경됩니다.")
    public ResponseEntity<Response<NotificationRes>> readNotifications(@ApiIgnore @CurrentMember Member member) {
        return new ResponseEntity<>(Response.OK(notificationService.readNotifications(member)), HttpStatus.OK);
    }

    @DeleteMapping
    @ApiOperation(value = "읽은 알림 삭제", notes = "읽은 알림을 삭제합니다.")
    public ResponseEntity<Response<String>> deleteNotifications(@ApiIgnore @CurrentMember Member member) {
        return new ResponseEntity<>(Response.OK(notificationService.deleteNotifications(member)), HttpStatus.OK);
    }
}
