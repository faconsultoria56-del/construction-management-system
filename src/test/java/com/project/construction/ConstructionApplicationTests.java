package com.project.construction;

import com.management.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ConstructionApplicationTests {

	@MockBean
	private AuthService authService;

	@Test
	void contextLoads() {
	}

}
