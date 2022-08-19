package link.projectjg.apiserver.dto.share;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import link.projectjg.apiserver.domain.Keyword;
import link.projectjg.apiserver.domain.Member;
import link.projectjg.apiserver.domain.share.ContentType;
import link.projectjg.apiserver.domain.share.Share;
import link.projectjg.apiserver.domain.share.ShareState;
import link.projectjg.apiserver.dto.keyword.KeywordDto;
import link.projectjg.apiserver.dto.member.MemberDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Data @Builder
@ApiModel(value = "공유 응답 DTO", description = "검색자는 master, participant, anonymous")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchShareRes {

    @ApiModelProperty(value = "검색자", example = "master")
    private String whoSearched;

    @ApiModelProperty(value = "공유자 정보")
    private MemberDto masterInfo;

    @ApiModelProperty(value = "공유 번호", example = "13", required = true)
    private Long id;

    @ApiModelProperty(value = "공유 상태", example = "INVISIBLE", required = true)
    private ShareState shareState;

    @ApiModelProperty(value = "콘텐츠 타입", example = "VIDEO", required = true)
    private ContentType contentType;

    @ApiModelProperty(value = "공유 제목", example = "넷플릭스 4인 최저가!!", required = true)
    private String title;

    @ApiModelProperty(value = "서비스 이름", example = "넷플릭스", required = true)
    private String serviceName;

    @ApiModelProperty(value = "공유에 대한 설명", example = "4인 공유이고, 최저가 입니다.", required = true)
    private String description;

    @ApiModelProperty(value = "서비스에 로그인할 ID", example = "rebuild96@naver.com", required = true)
    private String shareEmail;

    @ApiModelProperty(value = "서비스에 로그인할 Password", example = "비밀번호123!", required = true)
    private String sharePassword;

    @ApiModelProperty(value = "하루당 비용", example = "100", required = true)
    private Long dailyRate;

    @ApiModelProperty(value = "모집 인원", example = "4", required = true)
    private long numberRecruits;

    @ApiModelProperty(value = "참여 인원", example = "2", required = true)
    private long numberParticipant;

    @ApiModelProperty(value = "공유 종료일", example = "2022-12-31", required = true)
    private LocalDate shareTerminateDate;

    @ApiModelProperty(value = "공유 키워드 목록")
    private Set<KeywordDto> keywordSet;

    public static SearchShareRes of(Share share, Member member) {
        // 공유자 정보
        MemberDto masterInfo = MemberDto.builder()
                .nickname(share.getMaster().getNickname())
                .id(share.getMaster().getId()).build();

        // 키워드
        Set<KeywordDto> collect = share.getKeywordSet().stream()
                .map(Keyword::getKeyword).map(KeywordDto::new).collect(Collectors.toSet());

        // 참여자 수
        int numberParticipant = share.getMemberShares().size();

        SearchShareRes res = SearchShareRes.builder()
                .whoSearched("master")
                .masterInfo(masterInfo)
                .id(share.getId())
                .shareState(share.getShareState())
                .contentType(share.getContentType())
                .title(share.getTitle())
                .serviceName(share.getServiceName())
                .description(share.getDescription())
                .shareEmail(share.getShareEmail())
                .sharePassword(share.getSharePassword())
                .dailyRate(share.getDailyRate())
                .numberRecruits(share.getNumberRecruits())
                .numberParticipant(numberParticipant)
                .shareTerminateDate(share.getShareTerminateDate())
                .keywordSet(collect).build();

        // 공유자가 아니라면 필요 없는 데이터를 지움
        if (!share.isMaster(member)) {
            if (share.isJoinMember(member)) {
                res.setWhoSearched("participant");
            } else {
                res.setWhoSearched("anonymous");
                res.setShareEmail(null);
                res.setSharePassword(null);
            }
            res.setShareState(null);
        }
        return res;
    }

}
