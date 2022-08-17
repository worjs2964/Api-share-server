package link.projectjg.apiserver.repository;

import link.projectjg.apiserver.domain.share.Share;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShareRepostiory extends JpaRepository<Share, Long> {
}
