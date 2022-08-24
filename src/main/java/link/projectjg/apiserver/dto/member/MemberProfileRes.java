package link.projectjg.apiserver.dto.member;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import link.projectjg.apiserver.domain.Keyword;
import link.projectjg.apiserver.domain.Member;
import link.projectjg.apiserver.domain.share.Share;
import link.projectjg.apiserver.dto.keyword.KeywordDto;
import link.projectjg.apiserver.dto.share.ShareDto;
import lombok.Builder;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApiModel(value = "프로파일 응답 DTO")
@Data @Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberProfileRes {

    @ApiModelProperty(value = "검색자", example = "me")
    private String whoSearched;

    @ApiModelProperty(value = "회원 id 번호", example = "1")
    private Long id;

    @ApiModelProperty(value = "회원 이메일", example = "checked@share-service.com")
    private String email;

    @ApiModelProperty(value = "회원 닉네임", example = "인증된사용자")
    private String nickname;

    @ApiModelProperty(value = "회원 인증 여부", example = "true")
    private Boolean isAuthentication;

    @ApiModelProperty(value = "포인트", example = "10000")
    private Integer point;

    @ApiModelProperty(value = "자기소개", example = "안녕하세요.")
    private String description;

    @ApiModelProperty(value = "웹 일반 알림 여부", example = "true")
    private Boolean isNotificationByWeb;

    @ApiModelProperty(value = "웹 키워드 알림 여부", example = "true")
    private Boolean isKeywordByWeb;

    @ApiModelProperty(value = "이메일 알림 여부", example = "false")
    private Boolean isNotificationByEmail ;

    @ApiModelProperty(value = "이메일 키워드 알림 여부", example = "false")
    private Boolean isKeywordByEmail;

    @ApiModelProperty(value = "관심 키워드")
    private Set<KeywordDto> keywordSet;

    @ApiModelProperty(value = "생성한 공유")
    private List<ShareDto> shareList;

    @ApiModelProperty(value = "참여한 공유")
    private List<ShareDto> participatedShare;

    // 자기 자신일 때
    public static MemberProfileRes of(Member member, List<Share> shareList, List<Share> participatedShareList) {
        ModelMapper modelMapper = new ModelMapper();

        Set<KeywordDto> keywordDtoSet = member.getKeywordSet().stream().map(Keyword::getKeyword).map(KeywordDto::new).collect(Collectors.toSet());

        List<ShareDto> shareDtoList = shareList.stream().map(share -> modelMapper.map(share, ShareDto.class))
                .collect(Collectors.toList());

        List<ShareDto> participatedShareDtoList = participatedShareList.stream().map(share -> modelMapper.map(share, ShareDto.class))
                .collect(Collectors.toList());

        return MemberProfileRes.builder()
                .whoSearched("me")
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .isAuthentication(member.isAuthentication())
                .point(member.getPoint())
                .description(member.getDescription() != null ? member.getDescription() : "자기소개가 없습니다.")
                .isNotificationByWeb(member.isNotificationByWeb())
                .isKeywordByWeb(member.isKeywordByWeb())
                .isNotificationByEmail(member.isNotificationByEmail())
                .isKeywordByEmail(member.isKeywordByEmail())
                .keywordSet(keywordDtoSet)
                .shareList(shareDtoList)
                .participatedShare(participatedShareDtoList).build();
    }

    // 다른 사람이 조회했을때
    public static MemberProfileRes of(Member member, List<Share> shareList) {
        ModelMapper modelMapper = new ModelMapper();

        List<ShareDto> shareDtoList = shareList.stream().map(share -> modelMapper.map(share, ShareDto.class))
                .collect(Collectors.toList());

        return MemberProfileRes.builder()
                .whoSearched("other")
                .id(member.getId())
                .nickname(member.getNickname())
                .description(member.getDescription() != null ? member.getDescription() : "자기소개가 없습니다.")
                .shareList(shareDtoList).build();
    }

}
