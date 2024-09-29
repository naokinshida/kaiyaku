package com.example.nagoyameshi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.repository.FavoriteRepository;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    public Favorite addFavorite(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }

    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }

    public List<Favorite> getFavoritesByUserId(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }
    
    public List<Favorite> getFavoritesByUserIdAndStoreId(Long userId, Long storeId) {
        return favoriteRepository.findByUserIdAndStoreId(userId, storeId);
    }

	public List<Favorite> getFavoritesByUserIdAndStoreId(Integer id, Integer storeId) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	public List<Favorite> getFavoritesByUserId(Integer id) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
}