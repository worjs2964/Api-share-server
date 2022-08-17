package link.projectjg.apiserver.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "결제 준비 응답 DTO", description = "결제 고유 번호와 결제 준비 요청 시간, 결제를 진행하는 주소들")
public class ReadyPayRes {

    @ApiModelProperty(value = "결제 고유 번호")
    private String tid;

    @ApiModelProperty(value = "결제 준비 요청 시간")
    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("next_redirect_app_url")
    private String appUrl;

    @JsonProperty("next_redirect_mobile_url")
    private String mobileUrl;

    @JsonProperty("next_redirect_pc_url")
    private String pcUrl;

    @JsonProperty("android_app_scheme")
    private String appScheme;

    @JsonProperty("ios_app_scheme")
    private String iosScheme;

}
