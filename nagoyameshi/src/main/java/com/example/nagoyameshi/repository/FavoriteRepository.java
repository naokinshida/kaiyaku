package com.example.nagoyameshi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    @EntityGraph(attributePaths = { "storeinfo" })
    List<Favorite> findByUserId(Long userId);

    List<Favorite> findByUserIdAndStoreId(Long userId, Long storeId);

    void deleteById(Long id);
}
