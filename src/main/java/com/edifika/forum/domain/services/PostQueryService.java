package com.edifika.forum.domain.services;

import com.edifika.forum.domain.model.aggregates.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface PostQueryService {
    Page<Post> list(Pageable pageable);
    Optional<Post> findById(Long id);
}