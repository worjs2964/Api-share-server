package link.projectjg.apiserver.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import link.projectjg.apiserver.domain.Member;
import link.projectjg.apiserver.domain.share.Share;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity @Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberShare {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Share share;

    private String tid;

    private String paymentMethodType;

    private LocalDateTime approvedAt;
}
