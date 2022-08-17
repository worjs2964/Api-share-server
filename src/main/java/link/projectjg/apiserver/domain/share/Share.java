package link.projectjg.apiserver.domain.share;

import link.projectjg.apiserver.domain.Keyword;
import link.projectjg.apiserver.domain.Member;
import link.projectjg.apiserver.domain.MemberShare;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Builder @Getter
@NoArgsConstructor @AllArgsConstructor
public class Share {

    @Id @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ShareState shareState;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ContentType contentType;

    @NotBlank
    private String title;

    @NotBlank
    private String serviceName;

    @Lob
    private String description;

    @NotBlank
    private String shareEmail;

    @NotBlank
    private String sharePassword;

    private long dailyRate;

    private long numberRecruits;

    private LocalDate shareTerminateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member master;

    @OneToMany(mappedBy = "share", cascade = CascadeType.ALL)
    private List<MemberShare> memberShares = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "share_keyword")
    private Set<Keyword> keywordSet = new HashSet<>();


    // State 패턴용 메소드
    public void changeState(ShareState shareState) {
        this.shareState = shareState;
    }

    public boolean canJoinShare(Member member) {
        return shareState.canJoinShare(this, member);
    }

    public Share editShare() {
        return shareState.editShare(this);
    }

    public Share changeVisible() {
        return shareState.changeVisible(this);
    }

    // 공유의 주인인지
    public boolean isMaster(Member member) {
        return this.master.equals(member) ? true : false;
    }

    // 공유에 가입되어 있는지
    public boolean isJoinMember(Member member) {
        return this.memberShares.stream().map(MemberShare::getMember)
                .anyMatch(joinMember -> joinMember.equals(member));
    }

    // 총 비용 (비용 * 남은 기간)
    public long getTotalCost() {
        return getRemainingDate() * dailyRate;
    }

    // 남은 기간
    public long getRemainingDate() {
        long remainingDate = ChronoUnit.DAYS.between(LocalDate.now(), shareTerminateDate) + 1;
        return remainingDate > 0 ? remainingDate : 0;
    }

    public boolean isFull() {
        return memberShares.size() == numberRecruits;
    }
}
