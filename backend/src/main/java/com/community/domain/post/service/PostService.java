package com.community.domain.post.service;

import com.community.core.exception.ErrorCode;
import com.community.core.exception.custom.ForbiddenException;
import com.community.core.exception.custom.NotFoundException;
import com.community.domain.board.entity.Board;
import com.community.domain.board.repository.BoardRepository;
import com.community.domain.post.dto.request.PostCreateRequest;
import com.community.domain.post.dto.request.PostUpdateRequest;
import com.community.domain.post.entity.Post;
import com.community.domain.post.entity.PostTag;
import com.community.domain.post.repository.PostRepository;
import com.community.domain.post.service.strategy.PostStrategy;
import com.community.domain.post.service.strategy.PostStrategyFactory;
import com.community.domain.tag.entity.Tag;
import com.community.domain.tag.repository.TagRepository;
import com.community.domain.user.entity.User;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final BoardRepository boardRepository;
    private final TagRepository tagRepository;
    private final PostStrategyFactory strategyFactory;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 게시글 생성
     */
    public Long createPost(String slug, PostCreateRequest request, User user){
        //게시판 조회
        Board board = boardRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND));

        //글쓰기 권한 확인
        if (board.isWriteRequiresLogin() && user == null) {
            throw new ForbiddenException("로그인 후 이용 가능합니다");
        }

        // 타입별 전략 선택 및 검증
        PostStrategy strategy = strategyFactory.getStrategy(board.getBoardType());
        strategy.validateCreate(request);

        // Post 엔티티 생성
        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .board(board)
                .isAnonymous(request.isAnonymous())
                .build();

        // 전략별 전처리
        strategy.beforeCreate(post, request);

        //저장
        postRepository.save(post);

        //전략별 후처리
        strategy.afterCreate(post);

        //태크처리
        if (request.getTags() != null && !request.getTags().isEmpty()){
            processTags(post, request.getTags());
        }

        log.info("게시글 생성 완료: postId={}, boardId={}, userId={}",
                post.getId(), board.getId(), user.getId());

        return post.getId();
    }

    /**
     * 게시글 수정
     */
    public void updatePost(Long postId, PostUpdateRequest request, User user) {
        //게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND, postId));

        // 권한 확인 (작성자 또는 관리자)
        if (!post.isOwnedBy(user) && !user.isAdmin()) {
            throw new ForbiddenException("게시글 수정 권한이 없습니다.");
        }

        // 타입별 전략 선택 및 검증
        PostStrategy strategy = strategyFactory.getStrategy(post.getBoard().getBoardType());
        strategy.validateUpdate(request);

        // 전략별 전처리
        strategy.beforeUpdate(post, request);

        // 게시글 업데이트
        post.update(request.getTitle(), request.getContent());

        // 전략별 후처리
        strategy.afterUpdate(post);

        // 태그 재설정 (기존 태그 삭제 후 새로 추가)
        if (request.getTags() != null) {
            post.getTags().clear();
            if (!request.getTags().isEmpty()) {
                processTags(post, request.getTags());
            }
        }

        log.info("게시글 수정 완료: postId={}, userId={}", postId, user.getId());
    }

    /**
     * 게시글 삭제
     */
    public void deletePost(Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND, postId));

        if (!post.isOwnedBy(user) && !user.isAdmin()) {
            throw new ForbiddenException("게시글 삭제 권한이 없습니다");
        }

        post.delete();

        log.info("게시글 삭제 완료: postId={}, userId={}", postId, user.getId());
    }

    /**
     * 공지글 토글 (관리자 전용)
     */
    public void toggleNotice(Long postId, User user) {
        // 관리자 권한 확인
        if (!user.isAdmin()) {
            throw new ForbiddenException("관리자만 공지글을 설정할 수 있습니다.");
        }

        //게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND, postId));

        //공지 토글
        post.toggleNotice();

        log.info("공지글 토글 완료: postId={}, isNotice={}, userId={}",
                postId, post.isNotice(), user.getId());
    }


    private void processTags(Post post,List<String> tagName) {
        List<Tag> tags = tagName.stream()
                .distinct()
                .map(name -> tagRepository.findByName(name).orElseGet( () -> {
                    Tag newTag = Tag.create(name);
                    tagRepository.save(newTag);
                    return newTag;
                })).toList();

        tags.forEach(tag -> {
            PostTag posttag = PostTag.of(post, tag);
            post.addTag(posttag);
            tag.incrementUsageCount();
        });

    }
}
