package com.community.domain.board.dto.response;

import com.community.domain.board.entity.Board;
import com.community.domain.board.entity.BoardType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
public class BoardResponse {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private BoardType boardType;
    private String boardTypeDescription;
    private Map<String, Object> settings;
    private int displayOrder;
    private boolean isActive;
    private boolean writeRequiresLogin;
    private boolean commentRequiresLogin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Entity → DTO 변환
     */
    public static BoardResponse from(Board board) {
        return BoardResponse.builder()
                .id(board.getId())
                .name(board.getName())
                .slug(board.getSlug())
                .description(board.getDescription())
                .boardType(board.getBoardType())
                .boardTypeDescription(board.getBoardType().getDescription())
                .settings(board.getSettings())
                .displayOrder(board.getDisplayOrder())
                .isActive(board.isActive())
                .writeRequiresLogin(board.isWriteRequiresLogin())
                .commentRequiresLogin(board.isCommentRequiresLogin())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build();
    }
}
