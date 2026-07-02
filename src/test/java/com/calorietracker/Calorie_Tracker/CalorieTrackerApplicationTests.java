package com.calorietracker.Calorie_Tracker;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"MAIL_HOST=localhost",
		"MAIL_PORT=2525",
		"MAIL_USERNAME=test@example.com",
		"MAIL_PASSWORD=test",
		"JWT_SECRET=test-secret-key-for-context-loads",
		"JWT_EXPIRATION=86400000"
})
class CalorieTrackerApplicationTests {

	@Test
	void contextLoads() {
	}

}
