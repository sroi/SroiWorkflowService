package benefitBountyService.configuration;

import benefitBountyService.dao.ProjectRepository;
import benefitBountyService.dao.TaskRepository;
import benefitBountyService.dao.UserRepository;
import benefitBountyService.dao.impl.ProjectRepositoryImpl;
import benefitBountyService.dao.impl.TaskRepositoryImpl;
import benefitBountyService.dao.impl.UserRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean(name="userRepository")
    public UserRepository getUserRepository(){
        return new UserRepositoryImpl();
    }

    @Bean(name="projectRepository")
    public ProjectRepository getProjectRepository(){
        return new ProjectRepositoryImpl();
    }

    @Bean(name="taskRepository")
    public TaskRepository getTaskRepository(){
        return new TaskRepositoryImpl();
    }

}
