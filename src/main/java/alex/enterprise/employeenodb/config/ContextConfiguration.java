package alex.enterprise.employeenodb.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootConfiguration
public class ContextConfiguration {

    @Bean
    public RestTemplate initRestTemplate() {
        return new RestTemplate();
    }
}
