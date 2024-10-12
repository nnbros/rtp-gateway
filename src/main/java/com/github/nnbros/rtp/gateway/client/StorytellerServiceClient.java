package com.github.nnbros.rtp.gateway.client;

import com.github.nnbros.rtp.gateway.model.ClientType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Set;

import static com.github.nnbros.rtp.gateway.model.ClientType.STORYTELLER;

@FeignClient(name = "storyteller",
		url = "${spring.cloud.openfeign.client.config.storyteller.url}",
		path = "${spring.cloud.openfeign.client.config.storyteller.path}")
public interface StorytellerServiceClient {
	@PostMapping("/actions/{id}")
	void sendAction(@PathVariable String id, @RequestParam(required = false) String data, @RequestBody Update update);

	@DeleteMapping("cache/registration")
	void unregisterUsers(@RequestBody Set<Long> userId);

	default ClientType name() {
		return STORYTELLER;
	}
}
