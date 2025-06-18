package com.github.nnbros.rtp.gateway.client;

import com.github.nnbros.rtp.gateway.model.ActionProcessorType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.github.nnbros.rtp.gateway.model.ActionProcessorType.PVE;

@FeignClient(name = "pve",
		url = "${spring.cloud.openfeign.client.config.pve.url}",
		path = "${spring.cloud.openfeign.client.config.pve.path}")
public interface PveServiceClient {

	@PostMapping("/actions/{id}")
	void sendAction(@PathVariable String id, @RequestParam(required = false) String data, @RequestBody Update update);

	default ActionProcessorType name() {
		return PVE;
	}
}
