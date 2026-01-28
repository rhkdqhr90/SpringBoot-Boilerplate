package com.community.domain.tag.repository;

import com.community.domain.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 태그 Repository
 */
public interface TagRepository extends JpaRepository<Tag, Long> {

    /**
     * 이름으로 태그 조회
     */
    Optional<Tag> findByName(String name);

    /**
     * 슬러그로 태그 조회
     */
    Optional<Tag> findBySlug(String slug);

    /**
     * 이름 목록으로 태그 목록 조회
     */
    List<Tag> findByNameIn(List<String> names);

    /**
     * 인기 태그 조회 (사용 횟수 상위 N개)
     */
    List<Tag> findTop20ByOrderByUsageCountDesc();
}
