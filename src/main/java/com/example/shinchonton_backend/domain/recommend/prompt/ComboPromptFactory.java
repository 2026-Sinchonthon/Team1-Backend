package com.example.shinchonton_backend.domain.recommend.prompt;

import com.example.shinchonton_backend.domain.partyrequest.entity.PartyRequest;
import com.example.shinchonton_backend.domain.store.entity.MenuItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ComboPromptFactory {

    public String create(
            PartyRequest partyRequest,
            long tableBudget,
            List<MenuItem> menus
    ) {
        StringBuilder menuText = new StringBuilder();

        for (MenuItem menu : menus) {
            menuText.append("- menuId: ")
                    .append(menu.getId())
                    .append(", name: ")
                    .append(menu.getName())
                    .append(", price: ")
                    .append(menu.getPrice())
                    .append(", category: ")
                    .append(menu.getCategory())
                    .append(", servingSize: ")
                    .append(menu.getServingSize())
                    .append("\n");
        }

        return """
                아래 가게 메뉴 중에서만 대학 단체 술자리용 안주 조합을 추천해줘.

                조건:
                - 테이블당 예산: %d원
                - 테이블 수: %d개
                - 전체 인원: %d명
                - 단체명: %s
                - 모임 목적: %s

                추천 규칙:
                - 각 조합의 totalPrice는 테이블당 예산을 넘지 않아야 함
                - 메뉴는 반드시 제공된 메뉴 목록 안에서만 선택
                - menuId는 반드시 제공된 메뉴 목록의 menuId만 사용
                - 조합은 2개 추천
                - 첫 번째는 든든한 구성
                - 두 번째는 가성비 구성
                - quantity는 1 이상의 정수
                - JSON만 응답
                - 마크다운, 설명 문장, 코드블럭 금지

                메뉴 목록:
                %s

                응답 형식:
                {
                  "combos": [
                    {
                      "name": "든든한 기본 세트",
                      "items": [
                        {
                          "menuId": 1,
                          "menuName": "메뉴명",
                          "price": 19000,
                          "quantity": 1
                        }
                      ],
                      "totalPrice": 19000
                    }
                  ]
                }
                """.formatted(
                tableBudget,
                partyRequest.getTableCount(),
                partyRequest.getHeadcount(),
                partyRequest.getGroupName(),
                partyRequest.getPurpose(),
                menuText
        );
    }
}
