package com.community.domain.comment.dto.response;

import com.community.domain.comment.entity.Comment;
import com.community.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class CommentResponse {
    private Long id;
    private String content;
    private Author author;  // 익명이면 null
    private boolean isAnonymous;
    private int likeCount;
    private int dislikeCount;
    private String myReaction;  // "LIKE", "DISLIKE", null (TODO: Reaction 도메인 연동)
    private boolean isAuthor;
    private boolean isSelected;
    private int depth;
    private Long parentId;

    @Builder.Default
    private List<CommentResponse> replies = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    /**
     * 작성자 정보 (내부 클래스)
     * */
    @Getter
    @Builder
    public static class Author {
        private Long id;
        private String nickname;
        private String profileImage;

        public static Author from(User user) {
            return Author.builder()
                    .id(user.getId())
                    .nickname(user.getNickname())
                    .profileImage(user.getProfileImage())
                    .build();
        }
    }

    public static CommentResponse from(Comment comment, User currentUser) {
        if (comment.isDeleted()) {
            return CommentResponse.builder()
                    .id(comment.getId())
                    .content("삭제된 댓글입니다.")
                    .author(null)
                    .isAnonymous(false)
                    .likeCount(0)
                    .dislikeCount(0)
                    .isAuthor(false)
                    .isSelected(false)
                    .depth(comment.getDepth())
                    .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                    .createdAt(comment.getCreatedAt())
                    .updatedAt(comment.getUpdatedAt())
                    .build();
        }
        // 작성자 정보 처리 (익명 or 본인/관리자만 조회 가능)
        Author author = null;
        boolean isAuthor = false;

        if (currentUser != null) {
            isAuthor = comment.isOwnedBy(currentUser);
            boolean isAdmin = currentUser.getRole().name().equals("ADMIN");
            // 익명이 아니거나, 본인이거나, 관리자면 작성자 정보 노출
            if(!comment.isAnonymous() || isAdmin || isAuthor) {
                author = Author.from(comment.getUser());
            }
        }else {
            // 로그인하지 않은 경우 익명이 아닌 댓글만 작성자 정보 노출
            if (!comment.isAnonymous()) {
                author = Author.from(comment.getUser());
            }
        }
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .author(author)
                .isAnonymous(comment.isAnonymous())
                .likeCount(comment.getLikeCount())
                .dislikeCount(comment.getDislikeCount())
                .myReaction(null)  // TODO: Reaction 도메인 연동
                .isAuthor(isAuthor)
                .isSelected(comment.isSelected())
                .depth(comment.getDepth())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    /**
     * 대댓글 추가 (트리 구조 구성용)
     *
     * @param reply 대댓글 CommentResponse
     */
    public void addReply(CommentResponse reply) {
        if (this.replies == null) {
            this.replies = new ArrayList<>();
        }
        this.replies.add(reply);
    }
}
