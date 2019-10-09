package benefitBountyService.controllers;


import benefitBountyService.models.File;
import benefitBountyService.services.FileDatabaseService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/file")
public class FileDatabaseController {

    @Autowired
    private FileDatabaseService fileDatabaseService;

    @PostMapping(value = "/upload")
    public void uploadFile( @RequestParam("taskId") String taskId, @RequestParam("file") MultipartFile file, @RequestParam(value = "uid", required = false) String userId ) throws Exception {
        ObjectId fileId = new ObjectId(fileDatabaseService.uploadFile(taskId, file, userId));
//        return "Document Saved "+fileId.toString();
    }

    //http://localhost:8080/file/display/5d9ca3ca99097f2eb5dfcd0a
    @GetMapping(value = "/display/{id}")
    public ResponseEntity<Resource>  getFilebyFileId(@PathVariable("id") String fileId) throws IllegalStateException, IOException {
        return fileDatabaseService.getFileByFileId(fileId);
    }

    //http://localhost:8080/file/getByTask/harshbhau (taskId = harshbhau)
    @GetMapping(value = "/getByTask/{id}")
    public List<File>  getFilesbyTaskId(@PathVariable("id") String taskId) throws IllegalStateException, IOException {
        return fileDatabaseService.getFiles(taskId);
    }

    @DeleteMapping(value = "/delete/{id}")
    public String   deleteFilebyFileId(@PathVariable("id") String fileId) throws IllegalStateException, IOException {
        return fileDatabaseService.deleteFile(new ObjectId(fileId));
    }

}
