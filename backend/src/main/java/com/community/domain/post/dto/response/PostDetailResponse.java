package com.community.domain.post.dto.response;

import com.community.domain.board.entity.BoardType;
import com.community.domain.post.entity.Post;
import com.community.domain.post.entity.PostImage;
import com.community.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 게시글 상세 응답 DTO
 */
@Getter
@Builder
public class PostDetailResponse {

    /**
     * 게시글 ID
     */
    private Long id;

    /**
     * 게시판 정보
     */
    private BoardInfo board;

    /**
     * 제목
     */
    private String title;

    /**
     * 내용 (HTML)
     */
    private String content;

    /**
     * 작성자 정보
     */
    private AuthorInfo author;

    /**
     * 익명 여부
     */
    private boolean isAnonymous;

    /**
     * 조회수
     */
    private int viewCount;

    /**
     * 댓글 수
     */
    private int commentCount;

    /**
     * 좋아요 수
     */
    private int likeCount;

    /**
     * 싫어요 수
     */
    private int dislikeCount;

    /**
     * 이미지 목록
     */
    private List<ImageInfo> images;

    /**
     * 태그 목록
     */
    private List<String> tags;

    /**
     * 내 반응 (LIKE, DISLIKE, null)
     */
    private String myReaction;

    /**
     * 북마크 여부
     */
    private boolean isBookmarked;

    /**
     * 내가 작성자인지
     */
    private boolean isAuthor;

    /**
     * 공지글 여부
     */
    private boolean isNotice;

    /**
     * 추가 필드 (게시판 타입별)
     */
    private Map<String, Object> extraFields;

    /**
     * 작성일시
     */
    private LocalDateTime createdAt;

    /**
     * 수정일시
     */
    private LocalDateTime updatedAt;

    /**
     * Entity → DTO 변환
     */
    public static PostDetailResponse from(
            Post post,
            List<String> tags,
            User currentUser,
            String myReaction,
            boolean isBookmarked) {

        return PostDetailResponse.builder()
                .id(post.getId())
                .board(BoardInfo.from(post.getBoard()))
                .title(post.getTitle())
                .content(post.getContent())
                .author(AuthorInfo.from(post.getUser(), post.isAnonymous()))
                .isAnonymous(post.isAnonymous())
                .viewCount(post.getViewCount())
                .commentCount(post.getCommentCount())
                .likeCount(post.getLikeCount())
                .dislikeCount(post.getDislikeCount())
                .images(ImageInfo.from(post.getImages()))
                .tags(tags)
                .myReaction(myReaction)
                .isBookmarked(isBookmarked)
                .isAuthor(currentUser != null && post.isOwnedBy(currentUser))
                .isNotice(post.isNotice())
                .extraFields(post.getExtraFields())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    /**
     * 게시판 정보 (내부 클래스)
     */
    @Getter
    @Builder
    public static class BoardInfo {
        private Long id;
        private String name;
        private String slug;
        private BoardType boardType;

        public static BoardInfo from(com.community.domain.board.entity.Board board) {
            return BoardInfo.builder()
                    .id(board.getId())
                    .name(board.getName())
                    .slug(board.getSlug())
                    .boardType(board.getBoardType())
                    .build();
        }
    }

    /**
     * 작성자 정보 (내부 클래스)
     */
    @Getter
    @Builder
    public static class AuthorInfo {
        private Long id;
        private String nickname;
        private String profileImage;

        public static AuthorInfo from(User user, boolean isAnonymous) {
            if (isAnonymous) {
                return AuthorInfo.builder()
                        .id(null)
                        .nickname("익명")
                        .profileImage(null)
                        .build();
            }

            return AuthorInfo.builder()
                    .id(user.getId())
                    .nickname(user.getNickname())
                    .profileImage(user.getProfileImage())
                    .build();
        }
    }

    /**
     * 이미지 정보 (내부 클래스)
     */
    @Getter
    @Builder
    public static class ImageInfo {
        private Long id;
        private String url;
        private String originalName;
        private Long fileSize;
        private int displayOrder;

        public static List<ImageInfo> from(List<PostImage> images) {
            return images.stream()
                    .map(image -> ImageInfo.builder()
                            .id(image.getId())
                            .url(image.getUrl())
                            .originalName(image.getOriginalName())
                            .fileSize(image.getFileSize())
                            .displayOrder(image.getDisplayOrder())
                            .build())
                    .collect(Collectors.toList());
        }
    }
}