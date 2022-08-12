package link.projectjg.apiserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class Keyword {

    @Id
    @GeneratedValue
    @Column(name = "keyword_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String keyword;

    @JsonIgnore
    @ManyToMany
    private List<Member> memberList = new ArrayList<>();
}
