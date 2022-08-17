package link.projectjg.apiserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import link.projectjg.apiserver.domain.share.Share;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class Keyword {

    @Id
    @GeneratedValue
    @Column(name = "keyword_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String keyword;

    @ManyToMany(mappedBy = "keywordSet")
    private List<Member> memberList = new ArrayList<>();

    @ManyToMany(mappedBy = "keywordSet")
    private List<Share> shareList = new ArrayList<>();

}
