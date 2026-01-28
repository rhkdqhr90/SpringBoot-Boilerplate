package com.community.domain.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 게시글 수정 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class PostUpdateRequest {

    /**
     * 게시글 제목
     */
    @NotBlank(message = "제목을 입력해주세요")
    @Size(min = 2, max = 200, message = "제목은 2~200자여야 합니다")
    private String title;

    /**
     * 게시글 내용 (HTML)
     */
    @NotBlank(message = "내용을 입력해주세요")
    @Size(max = 50000, message = "내용은 50000자 이하여야 합니다")
    private String content;

    /**
     * 태그 목록 (최대 5개)
     */
    @Size(max = 5, message = "태그는 최대 5개까지 가능합니다")
    private List<String> tags;

    /**
     * 이미지 ID 목록
     *
     * <p>기존 이미지는 삭제되고 새로 전달된 이미지로 대체됩니다.</p>
     */
    private List<Long> imageIds;

    /**
     * 장터 게시판용 추가 필드
     *
     * <p>예: {"price": 50000, "tradeStatus": "RESERVED", "location": "서울"}</p>
     */
    private Map<String, Object> marketFields;
}