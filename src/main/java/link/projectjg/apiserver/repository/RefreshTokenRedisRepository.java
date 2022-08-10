package link.projectjg.apiserver.repository;

import link.projectjg.apiserver.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {
}
