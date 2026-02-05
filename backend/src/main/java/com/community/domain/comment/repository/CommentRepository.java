package com.community.domain.comment.repository;

import com.community.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 댓글 상세 조회
     */
    @Query("select c from Comment c join fetch c.post join fetch c.user where c.id = :commentId")
    Optional<Comment> findByIdWithPostAndUser(@Param("commentId") Long commentId);

    /**
     * 게시판 댓글 목록 조회 (Depth 0, 최신순)
     */
    @Query("select c from Comment c join fetch c.user where c.post.id = :postId and c.depth = 0 ORDER BY c.createdAt asc ")
    List<Comment> findCommentsByPostId(@Param("postId") Long postId);

    /**
     * 특정 댓글의 대댓글 목록 조회
     */
    @Query("select c  from Comment c join fetch c.user where c.parent.id = :parentId and c.depth = 1 order by c.createdAt asc")
    List<Comment> findRepliesByParentId(@Param("parentId") Long parentId);

    /**
     * 게시글의 모든 대댓글 조회 (N+1 방지용)
     */
    @Query("select c from Comment c join fetch c.user where c.post.id = :postId and c.depth = 1 order by c.createdAt asc")
    List<Comment> findRepliesByPostId(@Param("postId") Long postId);

    /**
     * 게시물 전체 댓글 수
     */
    @Query("select COUNT(c) from Comment c where c.post.id = :postId")
    Long countByPostId(@Param("postId") Long postId);

    /**
     * 대댓글 확인
     * <p>삭제 시 "삭제된 댓글입니다" 표시 여부 판단에 사용</p>
     */
    @Query("select case when count(c) > 0 THEN true ELSE false END From Comment c where c.parent.id = :parentId")
    boolean existsRepliesByParentId(@Param("parentId") Long parentId);

    /**
     * 사용자 댓글 목록 조회
     */
    @Query("select c from Comment c join fetch c.post p join fetch p.board where c.user.id = :userId order by c.createdAt DESC")
    Page<Comment> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId, Pageable pageable);

    /**
     * 게시글 댓글 존재 여부
     * <p>게시글 삭제 전 확인 </p>
     */
    boolean existsByPostId(Long postId);
}
