package com.example.shinchonton_backend.domain.recommend.service;

import com.example.shinchonton_backend.domain.partyrequest.entity.PartyRequest;
import com.example.shinchonton_backend.domain.partyrequest.repository.PartyRequestRepository;
import com.example.shinchonton_backend.domain.recommend.client.LlmClient;
import com.example.shinchonton_backend.domain.recommend.dto.req.RecommendReq;
import com.example.shinchonton_backend.domain.recommend.dto.res.ComboItemRes;
import com.example.shinchonton_backend.domain.recommend.dto.res.ComboRes;
import com.example.shinchonton_backend.domain.recommend.entity.Recommendation;
import com.example.shinchonton_backend.domain.recommend.prompt.ComboPromptFactory;
import com.example.shinchonton_backend.domain.recommend.repository.RecommendationRepository;
import com.example.shinchonton_backend.domain.store.entity.MenuItem;
import com.example.shinchonton_backend.domain.store.entity.Store;
import com.example.shinchonton_backend.domain.store.repository.MenuItemRepository;
import com.example.shinchonton_backend.domain.store.repository.StoreRepository;
import com.example.shinchonton_backend.global.apiPayload.code.status.GeneralErrorCode;
import com.example.shinchonton_backend.global.exception.GeneralException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final StoreRepository storeRepository;
    private final PartyRequestRepository partyRequestRepository;
    private final MenuItemRepository menuItemRepository;
    private final RecommendationRepository recommendationRepository;
    private final LlmClient llmClient;
    private final ComboPromptFactory comboPromptFactory;
    private final ComboFallbackGenerator fallbackGenerator;
    private final ObjectMapper objectMapper;

    @Transactional
    public List<ComboRes> recommend(RecommendReq request) {
        Recommendation savedRecommendation = recommendationRepository
                .findByPartyRequest_IdAndStore_IdAndTableBudget(
                        request.partyRequestId(),
                        request.storeId(),
                        request.tableBudget()
                )
                .orElse(null);

        if (savedRecommendation != null) {
            return parseSafely(savedRecommendation.getResultJson());
        }

        Store store = storeRepository.findById(request.storeId())
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.NOT_FOUND));

        PartyRequest partyRequest = partyRequestRepository.findById(request.partyRequestId())
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.NOT_FOUND));

        List<MenuItem> menus = menuItemRepository
                .findAllByStoreIdAndAvailableTrueOrderByIdAsc(store.getId());

        if (menus.isEmpty()) {
            throw new GeneralException(GeneralErrorCode.NOT_FOUND);
        }

        String prompt = comboPromptFactory.create(
                partyRequest,
                request.tableBudget(),
                menus
        );

        String resultJson;

        try {
            resultJson = llmClient.ask(prompt);
            parse(resultJson);
        } catch (Exception e) {
            List<ComboRes> fallback = fallbackGenerator.generate(
                    menus,
                    request.tableBudget()
            );
            resultJson = toJson(fallback);
        }

        Recommendation recommendation = Recommendation.create(
                partyRequest,
                store,
                request.tableBudget(),
                resultJson
        );

        recommendationRepository.save(recommendation);

        return parseSafely(resultJson);
    }

    private List<ComboRes> parseSafely(String resultJson) {
        try {
            return parse(resultJson);
        } catch (Exception e) {
            throw new GeneralException(GeneralErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private List<ComboRes> parse(String resultJson) throws Exception {
        JsonNode root = objectMapper.readTree(resultJson);
        JsonNode combosNode = root.get("combos");

        if (combosNode == null || !combosNode.isArray()) {
            throw new IllegalArgumentException("AI 추천 결과에 combos 배열이 없습니다.");
        }

        List<ComboRes> combos = new ArrayList<>();

        for (JsonNode comboNode : combosNode) {
            List<ComboItemRes> items = new ArrayList<>();

            JsonNode itemsNode = comboNode.get("items");
            if (itemsNode == null || !itemsNode.isArray()) {
                throw new IllegalArgumentException("AI 추천 조합에 items 배열이 없습니다.");
            }

            for (JsonNode itemNode : itemsNode) {
                items.add(new ComboItemRes(
                        itemNode.get("menuId").asLong(),
                        itemNode.get("menuName").asText(),
                        itemNode.get("price").asLong(),
                        itemNode.get("quantity").asInt()
                ));
            }

            combos.add(new ComboRes(
                    comboNode.get("name").asText(),
                    items,
                    comboNode.get("totalPrice").asLong()
            ));
        }

        return combos;
    }

    private String toJson(List<ComboRes> combos) {
        try {
            return objectMapper.writeValueAsString(Map.of("combos", combos));
        } catch (Exception e) {
            throw new GeneralException(GeneralErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
