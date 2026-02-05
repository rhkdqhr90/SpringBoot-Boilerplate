package com.community.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentCreateRequest {
    @NotBlank(message = "내용을 입력해주세요")
    @Size(min = 1, max = 10000, message = "댓글은 1~10000자여야 합니다")
    private String content;

    /**
     * 부모 댓글 ID (대댓글 작성 시)
     * - null: 댓글 (depth 0)
     * - not null: 대댓글 (depth 1)
     */
    private Long parentId;

    /**
     * 익명 여부
     */
    private boolean isAnonymous = false;
}
