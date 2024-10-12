package com.github.nnbros.rtp.gateway.bot.controller;

import com.github.nnbros.rtp.gateway.bot.update.LockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserManagementController {
	private final LockService lockService;

	@DeleteMapping("/{userId}/lock")
	public void releaseLock(@PathVariable Long userId) {
		lockService.releaseLock(userId);
	}
}
