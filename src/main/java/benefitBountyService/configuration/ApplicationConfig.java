package benefitBountyService.configuration;

import benefitBountyService.dao.*;
import benefitBountyService.dao.impl.*;
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

    @Bean(name="fileRepository")
    public FileRepository getFileRepository(){
        return new FileRepositoryImpl();
    }

    @Bean(name="mailRepository")
    public MailRepository getMailRepository(){ return new MailRepositoryImpl(); }

    @Bean(name="sroiCalcRepository")
    public SroiCalcRepository getSroiCalcRepository(){
        return new SroiCalcRepositoryImpl();
    }
}
