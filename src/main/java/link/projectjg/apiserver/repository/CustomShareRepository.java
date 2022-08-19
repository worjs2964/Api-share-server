package link.projectjg.apiserver.repository;

import link.projectjg.apiserver.domain.share.Share;
import link.projectjg.apiserver.dto.share.SearchShareListReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomShareRepository {

    Page<Share> findShareList(SearchShareListReq searchShareListReq, Pageable pageable);

}
