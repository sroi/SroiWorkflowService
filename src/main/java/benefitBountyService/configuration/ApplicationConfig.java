package benefitBountyService.configuration;

import benefitBountyService.dao.UserRepository;
import benefitBountyService.dao.impl.UserRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean(name="userRepository")
    public UserRepository getUserRepository(){
        return new UserRepositoryImpl();
    }

}
