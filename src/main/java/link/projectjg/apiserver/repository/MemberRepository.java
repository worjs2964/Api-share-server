package link.projectjg.apiserver.repository;

import link.projectjg.apiserver.domain.Keyword;
import link.projectjg.apiserver.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MemberRepository extends JpaRepository<Member, Long>{

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<Member> findByMemberUid(String memberUid);

    Optional<Member> findByEmail(String email);

    List<Member> findDistinctMemberByKeywordSetIn(Set<Keyword> keywordSet);
}
