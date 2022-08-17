package link.projectjg.apiserver.service;

import link.projectjg.apiserver.domain.MemberShare;
import link.projectjg.apiserver.repository.MemberShareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberShareService {

    private final MemberShareRepository memberShareRepository;

    public MemberShare save(MemberShare memberShare) {
        return memberShareRepository.saveAndFlush(memberShare);
    }
}
