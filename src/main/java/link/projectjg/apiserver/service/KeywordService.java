package link.projectjg.apiserver.service;

import link.projectjg.apiserver.domain.Keyword;
import link.projectjg.apiserver.dto.keyword.KeywordDto;
import link.projectjg.apiserver.dto.keyword.KeywordReq;
import link.projectjg.apiserver.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;

    public Set<Keyword> saveKeywords(Set<KeywordDto> keywordSet) {
        return keywordSet.stream().map(KeywordDto::getKeyword).map(keyword ->
                // 해당 키워드가 있으면 반환하고 없으면 키워드를 저장하고 반환
                keywordRepository.findByKeyword(keyword).orElseGet(() ->
                        keywordRepository.save(KeywordReq.toEntity(keyword))
                )
        ).collect(Collectors.toSet());
    }
}
