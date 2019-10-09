
package benefitBountyService.services;

import benefitBountyService.dao.FileRepository;
import benefitBountyService.models.File;
import benefitBountyService.models.User;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class FileDatabaseService {

    private static final Logger logger = LoggerFactory.getLogger(FileDatabaseService.class);

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFsOperations operations;

    /**
     *
     * @param taskId
     * @param file
     * @return  Object Id which is stored in files collection for task
     * @throws Exception
     */
    public String uploadFile(@RequestParam("taskId") String taskId, @RequestParam("file") MultipartFile file, @RequestParam("uid") String userId ) throws Exception {
        //Todo : Get User from session and set below.
        User user = new User();
        userId = "5d9984b61c9d440000d024be";
        user.set_id(new ObjectId(userId));

        DBObject metaData = new BasicDBObject();
        metaData.put("type", "image");
        metaData.put("originalFileName", file.getOriginalFilename());
        metaData.put("uid", user.get_id());
        ObjectId id = gridFsTemplate.store(
                file.getInputStream(), taskId, file.getContentType(), metaData);

        insertFile(id,taskId,file.getContentType(), file.getOriginalFilename(), user.get_id());

        return id.toString();
    }

    /**
     *
     * @param fileId - Assuming fileId will always be unique
     * @return
     * @throws IllegalStateException
     * @throws IOException
     */
    public ResponseEntity<Resource> getFileByFileId(@PathVariable("id") String fileId) throws IllegalStateException, IOException {
        GridFSFile file =
                operations.findOne(Query.query(Criteria.where("_id").is(fileId)));

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(file.getMetadata().getString("_contentType")))
                .contentLength(file.getLength())
                .body(operations.getResource(file));
    }

    public void insertFile(ObjectId fileId, String taskId, String contentType,String fileName, String uid) {
        fileRepository.insert(new File(fileId,taskId,contentType,fileName,new Date(),uid));
    }

    public List<File> getFiles(String taskId){
        return fileRepository.findAllByTaskId(taskId);
    }

    public String deleteFile(ObjectId fileId) {
        gridFsTemplate.delete(new Query(Criteria.where("_id").is(fileId)));
        fileRepository.deleteById(fileId.toString());
        return "Successfully deleted file"+fileId.toString();
    }

    public String contentTypeByFileId(ObjectId fileId) {
        GridFSFile file =
                operations.findOne(Query.query(Criteria.where("_id").is(fileId)));
        return file.getMetadata().getString("_contentType");
    }

}
