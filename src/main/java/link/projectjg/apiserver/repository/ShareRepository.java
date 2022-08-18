package link.projectjg.apiserver.repository;

import link.projectjg.apiserver.domain.share.Share;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Entity;
import java.util.List;

public interface ShareRepository extends JpaRepository<Share, Long> {

    @EntityGraph(attributePaths = {"keywordSet"})
    Share findWithKeywordById(Long id);

    @EntityGraph(attributePaths = {"memberShares, memberShares.member"})
    Share findWithParticipantsById(Long id);
}
