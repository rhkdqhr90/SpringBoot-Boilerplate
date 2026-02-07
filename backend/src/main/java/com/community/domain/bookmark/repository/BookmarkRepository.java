package com.community.domain.bookmark.repository;

import com.community.domain.bookmark.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    /**
     * 사용자 게시글 기준 북마크 (단건)
     * @param userId 사용자
     * @param postId 게시물
     * @return 북마크
     */
    Optional<Bookmark> findByUserIdAndPostId(Long userId, Long postId);

    /**
     *  사용자 게시글 기준 북마크 존재 여부
     * @param userId 사용자
     * @param postId 게시물
     * @return 불린
     */
    boolean existsByUserIdAndPostId(Long userId, Long postId);

    /**
     *  사용자 북마크 목록 조회
     * @param userId 사용자
     * @param pageable 페이징
     * @return 북마크 목록
     */
    @Query("select b from Bookmark b join fetch b.post p where b.user.id = :userId and p.deletedAt IS null order by b.createdAt DESC")
    Page<Bookmark> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    /**
     *  사용자 폴더별 북마크 목록 조회
     * @param userId 사용자
     * @param folder 폴더
     * @param pageable 페이징
     * @return 북마크 목록
     */
    @Query("select b from Bookmark b join fetch b.post p where b.user.id = :userId and b.folder = :folder and" +
            " p.deletedAt is null order by b.createdAt desc")
    Page<Bookmark> findAllByUserIdAndFolder(@Param("userId") Long userId, @Param("folder") String folder, Pageable pageable);

    /**
     *  사용자 폴더 목록 조회(중복 제거)
     * @param userId 사용자
     * @return 폴더명 목록
     */
    @Query("select distinct b.folder from Bookmark b where b.user.id = :userId and b.folder is null order by b.folder")
    List<String> findDistinctFoldersByUserId(@Param("userId") Long userId);

    /**
     *  사용자 게시글 기준 북마크 제거
     * @param userId 사용자
     * @param postId 게시글
     */
    void deleteByUserIdAndPostId(Long userId, Long postId);

    /**
     *  사용자 북마크 수
     * @param userId 사용자
     * @return 북마크 수
     */
    long countByUserId(Long userId);

    /**
     *  사용자 폴더별 북마크 수
     * @param userId 사용자
     * @param folder 폴더
     * @return 북마크 수
     */
    long countByUserIdAndFolder(Long userId, String folder);
}
