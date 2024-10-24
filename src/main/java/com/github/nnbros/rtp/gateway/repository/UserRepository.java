package com.github.nnbros.rtp.gateway.repository;

import com.github.nnbros.rtp.gateway.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

	@Modifying
	@Query("UPDATE User u SET u.lastAction=:lastAction WHERE u.id = :userId")
	void updateLastAction(Long userId, String lastAction);
}
