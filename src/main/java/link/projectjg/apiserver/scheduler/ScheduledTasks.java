package link.projectjg.apiserver.scheduler;

import link.projectjg.apiserver.repository.ShareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Transactional
public class ScheduledTasks {

    private final ShareRepository shareRepository;

    // 매일 밤마다 종료일자가 지난 공유들 TERMINATED 상태로 변경
    @Scheduled(cron = "0 0 0 * * *")
    public void removeExpirationToken() {
        LocalDate now = LocalDate.now();
        shareRepository.changeTerminated(now);
    }
}
