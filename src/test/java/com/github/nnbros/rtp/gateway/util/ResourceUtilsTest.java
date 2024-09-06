package com.github.nnbros.rtp.gateway.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResourceUtilsTest {
	private static final String TEST_FILE_PATH = "/util/testFile.txt";
	private static final String TEST_FILE_CONTENT = "This is a test file";

	@Test
	public void getResourceAsStream() throws IOException {
		Optional<InputStream> resourceAsStream = ResourceUtils.getResourceAsStream(TEST_FILE_PATH);
		assertTrue(resourceAsStream.isPresent());
		try (InputStream inputStream = resourceAsStream.get()) {
			assertArrayEquals(TEST_FILE_CONTENT.getBytes(StandardCharsets.UTF_8), inputStream.readAllBytes());
		}
	}
}
