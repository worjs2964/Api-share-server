package link.projectjg.apiserver.repository;

import link.projectjg.apiserver.domain.share.Share;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.Entity;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ShareRepository extends JpaRepository<Share, Long>, CustomShareRepository {

    @EntityGraph(attributePaths = {"keywordSet"})
    Share findWithKeywordById(Long id);

    @EntityGraph(attributePaths = {"master", "memberShares", "memberShares.member"})
    Optional<Share> findShowShareInfoById(Long shareId);

    @Modifying
    @Query("update Share s set s.shareState = 'TERMINATED' where s.shareTerminateDate < :now")
    void changeTerminated(LocalDate now);
}
