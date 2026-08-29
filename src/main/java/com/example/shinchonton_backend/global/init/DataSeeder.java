package com.example.shinchonton_backend.global.init;

import com.example.shinchonton_backend.domain.member.entity.Member;
import com.example.shinchonton_backend.domain.member.entity.MemberRole;
import com.example.shinchonton_backend.domain.member.repository.MemberRepository;
import com.example.shinchonton_backend.domain.store.entity.Region;
import com.example.shinchonton_backend.domain.store.entity.Store;
import com.example.shinchonton_backend.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@Profile("local")
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;

    @Override
    public void run(String... args) {
        Member student = memberRepository.findByLoginId("student1")
                .orElseGet(() -> memberRepository.save(Member.register(
                        "student1",
                        "{noop}password",
                        MemberRole.STUDENT,
                        "테스트 학생",
                        "010-0000-0000"
                )));

        Member merchant = memberRepository.findByLoginId("merchant1")
                .orElseGet(() -> memberRepository.save(Member.register(
                        "merchant1",
                        "{noop}password",
                        MemberRole.MERCHANT,
                        "테스트 사장님",
                        "010-1111-1111"
                )));

        if (storeRepository.findAllByMerchantIdOrderByCreatedAtDesc(merchant.getId()).isEmpty())  {
            storeRepository.save(Store.register(
                    merchant,
                    "신촌 테스트포차",
                    Region.SINCHON,
                    "서울 서대문구 신촌로 1",
                    80,
                    LocalTime.of(17, 0),
                    LocalTime.of(2, 0),
                    "로컬 테스트용 가게입니다."
            ));
        }
    }
}
