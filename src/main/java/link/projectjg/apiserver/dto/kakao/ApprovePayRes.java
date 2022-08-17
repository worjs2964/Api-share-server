package link.projectjg.apiserver.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApprovePayRes {

    private String tid;

    @JsonProperty("payment_method_type")
    private String paymentMethodType;

    @JsonProperty("approved_at")
    private LocalDateTime approvedAt;

}
