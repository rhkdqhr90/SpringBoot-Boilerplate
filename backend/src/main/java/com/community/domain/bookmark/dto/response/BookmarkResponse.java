package com.community.domain.bookmark.dto.response;

import com.community.domain.bookmark.entity.Bookmark;
import com.community.domain.post.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BookmarkResponse {

    private Long id;
    private Long postId;
    private String postTitle;
    private String folder;
    private LocalDateTime createdAt;

    public static BookmarkResponse from(Bookmark bookmark) {
        Post post = bookmark.getPost();

        return BookmarkResponse.builder()
                .id(bookmark.getId())
                .postId(post.getId())
                .postTitle(post.getTitle())
                .folder(bookmark.getFolder())
                .createdAt(bookmark.getCreatedAt())
                .build();
    }
}
