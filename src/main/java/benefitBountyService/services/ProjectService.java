package benefitBountyService.services;

import benefitBountyService.dao.ProjectRepository;
import benefitBountyService.models.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public List<Project> getProjects(){
        return projectRepository.findAll();
    }
}
