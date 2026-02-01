package com.community.domain.post.repository;

import com.community.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 게시글 Repository
 */
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    /**
     * 게시글 상세 조회
     */
    @Query("select p from Post p join fetch p.board join fetch p.user where p.id = :postId")
    Optional<Post> findByIdWithBoardAndUser(@Param("postId") Long postId);

    /**
     * 게시판별 게시글 목록 조회 (페이징)
     */
    @Query("SELECT p FROM Post p WHERE p.board.id = :boardId ORDER BY p.isNotice DESC, p.createdAt DESC")
    Page<Post> findByBoardId(@Param("boardId") Long boardId, Pageable pageable);

    /**
     * 게시판별 게시글 목록 조회 - slug (공지글 제외)
     */
    @Query("select p from Post p join fetch p.board b where b.slug = :slug and p.isNotice = false order by p.createdAt desc")
    Page<Post> findByBoardSlug(@Param("slug") String slug, Pageable pageable);

    /**
     * 게시판별 공지글 조회 (최대 5개)
     *
     * <p>fetch join으로 board 함께 조회</p>
     */
    @Query("SELECT p FROM Post p JOIN FETCH p.board b WHERE b.slug = :slug AND p.isNotice = true ORDER BY p.createdAt DESC")
    List<Post> findNoticesByBoardSlug(@Param("slug") String slug);

    /**
     * 사용자별 게시글 목록 조회
     */
    Page<Post> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    /**
     * 게시판에 게시글 존재 여부 확인
     */
    boolean existsByBoardId(Long boardId);

    /**
     * 조회수 증가 (벌크 연산)
     */
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId")
    void incrementViewCount(@Param("postId") Long postId);
}