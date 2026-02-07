package com.community.domain.bookmark.entity;

import com.community.core.common.entity.BaseEntity;
import com.community.domain.post.entity.Post;
import com.community.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "bookmarks"
,uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_bookmarks_user_post",
                columnNames = {"user_id", "post_id"}
        )
})
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Bookmark extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "folder", length = 100)
    private String folder;

    // =========== 생성자 ============

    private Bookmark(User user, Post post, String folder) {
        this.user = Objects.requireNonNull(user, "사용자는 필수입니다.");
        this.post = Objects.requireNonNull(post, "게시글은 필수입니다.");
        this.folder = folder;
    }

    // ============ 정적 메소드 =========

    /**
     * 북마크 생성
     * @param user 사용자
     * @param post 게시글
     * @param folder 폴더명
     * @return Bookmark
     */
    public static Bookmark create(User user, Post post, String folder) {
        return new Bookmark(user, post, folder);
    }

    /**
     * 특정 폴더에 있는지
     * @param folder 폴더명
     * @return 일치여부
     */
    public boolean isFolder(String folder) {
        if (this.folder == null  && folder == null) {
            return true;
        }
        return this.folder != null && this.folder.equals(folder);
    }

    /**
     *  북마크 폴더 여부
     * @return 폴더 없음 여부
     */
    public boolean hasNoFolder() {
        return this.folder == null || this.folder.isBlank();
    }
}
