package com.community.domain.post.repository;

import com.community.core.common.entity.QBaseEntity;
import com.community.domain.board.entity.QBoard;
import com.community.domain.post.dto.condition.PostSearchCondition;
import com.community.domain.post.entity.Post;
import com.community.domain.post.entity.QPost;
import com.community.domain.user.entity.QUser;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 게시글 Custom Repository 구현체 (QueryDSL)
 */
@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Post> searchPosts(PostSearchCondition condition, Pageable pageable) {
        QPost post = QPost.post;
        QBoard board = QBoard.board;
        QUser user = QUser.user;

        // 게시글 목록 조회
        List<Post> content = queryFactory
                .selectFrom(post)
                .join(post.board, board).fetchJoin()
                .join(post.user, user).fetchJoin()
                .where(
                        boardIdEq(condition.getBoardId()),
                        keywordContains(condition.getKeyword()),
                        tagEq(condition.getTag())
                )
                .orderBy(
                        post.isNotice.desc(),
                        post.createdAt.desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 개수 조회 (카운트 쿼리 최적화)
        JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post)
                .where(
                        boardIdEq(condition.getBoardId()),
                        keywordContains(condition.getKeyword()),
                        tagEq(condition.getTag())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    // ========== 동적 쿼리 조건 ==========

    /**
     * 게시판 ID 조건
     */
    private BooleanExpression boardIdEq(Long boardId) {
        return boardId != null ? QPost.post.board.id.eq(boardId) : null;
    }

    /**
     * 키워드 검색 (제목 + 내용)
     */
    private BooleanExpression keywordContains(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        return QPost.post.title.contains(keyword)
                .or(QPost.post.content.contains(keyword));
    }

    /**
     * 태그 검색 (TODO: PostTag 조인 필요)
     */
    private BooleanExpression tagEq(String tag) {
        // TODO: PostTag 조인 후 구현
        return null;
    }
}