package link.projectjg.apiserver.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import link.projectjg.apiserver.domain.share.ContentType;
import link.projectjg.apiserver.domain.share.Share;
import link.projectjg.apiserver.domain.share.ShareState;
import link.projectjg.apiserver.dto.share.SearchShareListReq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static link.projectjg.apiserver.domain.share.QShare.share;

@RequiredArgsConstructor
public class ShareRepositoryImpl implements CustomShareRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Share> findShareList(SearchShareListReq searchShareListReq, Pageable pageable) {
        ContentType contentType = searchShareListReq.getContentType();
        String keyword = searchShareListReq.getKeyword();

        QueryResults<Share> shareQueryResults = queryFactory.select(share).distinct()
                .from(share)
                .where(share.shareState.eq(ShareState.VISIBLE),
                        keyword(keyword),
                        contentType(contentType))
                .orderBy(share.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(shareQueryResults.getResults(), pageable, shareQueryResults.getTotal());
    }

    private BooleanExpression keyword(String keywordCond) {
        if (keywordCond != null) {
            return share.title.containsIgnoreCase(keywordCond)
                    .or(share.serviceName.containsIgnoreCase(keywordCond))
                    .or(share.keywordSet.any().keyword.containsIgnoreCase(keywordCond));
        } else {
            return null;
        }
    }

    private BooleanExpression contentType(ContentType contentType) {
        return contentType != null ? share.contentType.eq(contentType) : null;
    }
}
