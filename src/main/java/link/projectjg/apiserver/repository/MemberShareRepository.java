package link.projectjg.apiserver.repository;

import link.projectjg.apiserver.domain.MemberShare;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberShareRepository extends JpaRepository<MemberShare, Long> {
}
