package com.healthtracker.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
/**
 * Smoke test mínimo para verificar que el contexto Spring levanta correctamente.
 */
class BackendApplicationTests {

	@Test
	void contextLoads() {
	}

}
