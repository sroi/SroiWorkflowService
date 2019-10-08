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

    @RequestMapping(value = "/upload" , method= RequestMethod.POST)
    public String uploadFile( @RequestParam("taskId") String taskId, @RequestParam("file") MultipartFile file ) throws Exception {
        ObjectId fileId = new ObjectId(fileDatabaseService.uploadFile(taskId, file));
        return "Image Saved "+fileId.toString();
    }

    @RequestMapping(value = "/display/{id}" , method= RequestMethod.GET)
    public ResponseEntity<Resource>  getFilebyFileId(@PathVariable("id") String fileId) throws IllegalStateException, IOException {
        return fileDatabaseService.getFileByFileId(fileId);
    }

    @RequestMapping(value = "/getByTask/{id}" , method= RequestMethod.GET)
    public List<File>  getFilesbyTaskId(@PathVariable("id") String taskId) throws IllegalStateException, IOException {
        return fileDatabaseService.getFiles(taskId);
    }

    @RequestMapping(value = "/delete/{id}" , method= RequestMethod.GET)
    public String   deleteFilebyFileId(@PathVariable("id") String fileId) throws IllegalStateException, IOException {
        return fileDatabaseService.deleteFile(new ObjectId(fileId));
    }

}
