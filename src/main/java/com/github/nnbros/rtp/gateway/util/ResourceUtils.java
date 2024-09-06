package com.github.nnbros.rtp.gateway.util;

import java.io.InputStream;
import java.util.Optional;

public class ResourceUtils {

	public static Optional<InputStream> getResourceAsStream(String resourcePath) {
		return Optional.ofNullable(ResourceUtils.class.getResourceAsStream(resourcePath));
	}
}
