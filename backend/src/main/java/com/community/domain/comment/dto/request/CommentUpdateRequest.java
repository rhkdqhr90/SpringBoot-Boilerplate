package com.community.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentUpdateRequest {
    @NotBlank(message = "내용을 입력해주세요")
    @Size(min = 1, max = 10000, message = "댓글은 1~10000자여야 합니다")
    private String content;
}
