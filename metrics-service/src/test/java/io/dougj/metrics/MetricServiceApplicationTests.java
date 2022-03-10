package io.dougj.metrics;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MetricServiceApplicationTests {

	@Test
	void contextLoads() {}

	@Test
	public void testMain() {
		MetricsServiceApplication.main(new String[]{});
		Assertions.assertNotNull(MetricsServiceApplication.APPLICATION_CONTEXT, "app context is null");
	}
}
