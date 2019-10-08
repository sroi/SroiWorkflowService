package benefitBountyService.configuration;

import benefitBountyService.dao.FileRepository;
import benefitBountyService.dao.ProjectRepository;
import benefitBountyService.dao.TaskRepository;
import benefitBountyService.dao.UserRepository;
import benefitBountyService.dao.impl.FileRepositoryImpl;
import benefitBountyService.dao.impl.ProjectRepositoryImpl;
import benefitBountyService.dao.impl.TaskRepositoryImpl;
import benefitBountyService.dao.impl.UserRepositoryImpl;
import benefitBountyService.models.File;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

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


}
