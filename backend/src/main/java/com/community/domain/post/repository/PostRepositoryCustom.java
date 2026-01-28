package com.community.domain.post.repository;

import com.community.domain.post.dto.condition.PostSearchCondition;
import com.community.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 게시글 Custom Repository (QueryDSL)
 */
public interface PostRepositoryCustom {

    /**
     * 게시글 검색 (동적 쿼리)
     *
     * @param condition 검색 조건
     * @param pageable 페이징 정보
     * @return 게시글 목록
     */
    Page<Post> searchPosts(PostSearchCondition condition, Pageable pageable);
}