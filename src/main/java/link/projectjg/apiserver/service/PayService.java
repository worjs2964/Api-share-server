package link.projectjg.apiserver.service;

import link.projectjg.apiserver.domain.Member;
import link.projectjg.apiserver.domain.MemberShare;
import link.projectjg.apiserver.domain.share.Share;
import link.projectjg.apiserver.domain.share.ShareState;
import link.projectjg.apiserver.dto.kakao.ApprovePayRes;
import link.projectjg.apiserver.dto.kakao.ReadyPayRes;
import link.projectjg.apiserver.dto.share.JoinShareRes;
import link.projectjg.apiserver.exception.CustomException;
import link.projectjg.apiserver.exception.ErrorCode;
import link.projectjg.apiserver.repository.ShareRepostiory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class PayService {

    private final ShareRepostiory shareRepostiory;
    private final ModelMapper modelMapper;

    private ConcurrentHashMap<String, String> tidRepository = new ConcurrentHashMap<>();
    private RestTemplate restTemplate = new RestTemplate();
    @Value("${share-service.url}")
    private String url;
    @Value("${kakao.admin-key}")
    private String adminKey;


    public ReadyPayRes readyPay(Share share, Member member) {

        // 헤더, 바디 설정
        HttpHeaders headers = getHeader();
        MultiValueMap<String, String> params = getReadyPayBody(share, member);

        // 적용
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

        // 실행
        try {
            ReadyPayRes readyPayRes = restTemplate.postForObject(new URI("https://kapi.kakao.com/v1/payment/ready"), httpEntity, ReadyPayRes.class);
            tidRepository.put(getOrderId(share, member), readyPayRes.getTid());
            return readyPayRes;
        } catch (URISyntaxException e) {
            throw new CustomException(ErrorCode.INVALID_VALUE);
        }

    }

    public JoinShareRes approvePay(Share share, Member member, String pgToken) {

        // 헤더, 바디 설정
        HttpHeaders headers = getHeader();
        MultiValueMap<String, String> params = getApprovePayBody(share, member, pgToken);

        // 적용
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

        // 실행
        try {
            // 가입이 가능한지 최종 확인
            if (share.canJoinShare(member)) {
                // 결제 승인 진행
                ResponseEntity<ApprovePayRes> res = restTemplate.postForEntity(new URI("https://kapi.kakao.com/v1/payment/approve"), httpEntity, ApprovePayRes.class);
                // 승인이 성공적으로 이루어지면
                if (res.getStatusCode() == HttpStatus.OK) {
                    return modelMapper.map(shareRepostiory.save(joinShare(share, member, res)), JoinShareRes.class);
                } else {
                    throw new CustomException(ErrorCode.INVALID_APPROVED_PAY);
                }
            } else {
                throw new CustomException(ErrorCode.INVALID_APPROVED_PAY);
            }
        } catch (URISyntaxException e) {
            throw new CustomException(ErrorCode.INVALID_APPROVED_PAY);
        }

    }

    private Share joinShare(Share share, Member member, ResponseEntity<ApprovePayRes> res) {
        ApprovePayRes resBody = res.getBody();

        MemberShare memberShare = MemberShare.builder()
                .member(member)
                .share(share)
                .tid(resBody.getTid())
                .approvedAt(resBody.getApprovedAt())
                .paymentMethodType(resBody.getPaymentMethodType()).build();

        share.getMemberShares().add(memberShare);

        if (share.isFull()) {
            share.changeState(ShareState.FULL);
        }

        return share;
    }

    private HttpHeaders getHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + adminKey);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        return headers;
    }

    private MultiValueMap<String, String> getReadyPayBody(Share share, Member member) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", "TC0ONETIME");
        params.add("partner_order_id", getOrderId(share, member));
        params.add("partner_user_id", member.getMemberUid());
        params.add("item_name", share.getTitle());
        params.add("quantity", "1");
        params.add("total_amount", String.valueOf(share.getTotalCost()));
        params.add("tax_free_amount", "0");
        params.add("approval_url", url + "/v1/payment/success?shareId="+ share.getId()+"&memberId="+ member.getId());
        params.add("cancel_url", url + "/v1/payment/cancel");
        params.add("fail_url", url + "/v1/payment/fail");
        return params;
    }

    private MultiValueMap<String, String> getApprovePayBody(Share share, Member member, String pgToken) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", "TC0ONETIME");
        params.add("tid", tidRepository.get(getOrderId(share, member)));
        params.add("partner_order_id", getOrderId(share, member));
        params.add("partner_user_id", member.getMemberUid());
        params.add("pg_token", pgToken);
        return params;
    }

    private String getOrderId(Share share, Member member) {
        return share.getId() + ":" + member.getMemberUid();
    }
}
