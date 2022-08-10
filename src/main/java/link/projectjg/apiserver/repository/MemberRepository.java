package link.projectjg.apiserver.repository;

import link.projectjg.apiserver.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<Member> findByMemberUid(String memberUid);

    Optional<Member> findByEmail(String email);
}
