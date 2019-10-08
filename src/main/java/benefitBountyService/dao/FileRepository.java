package benefitBountyService.dao;


import benefitBountyService.models.File;

import java.util.List;

public interface FileRepository {

    List<File> findAllByTaskId(String taskId);

    File findById(String fileId);

    long deleteById(String fileId);

    void insert(File file);
}
