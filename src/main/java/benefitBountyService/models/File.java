package benefitBountyService.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "files")
public class File {

	@Id
	@Field("_id")
	private ObjectId fileId;
	@Field("task_id")
	private String taskId;
	@Field("content_type")
	private String contentType;
	@Field("file_name")
	private String fileName;
	@Field("created_on")
	private Date createdOn;
	@Field("created_by")
	private String createdBy;

	public File(ObjectId fileId, String taskId, String contentType, String fileName, Date createdOn, String createdBy) {
		this.fileId = fileId;
		this.taskId = taskId;
		this.contentType = contentType;
		this.fileName = fileName;
		this.createdOn = createdOn;
		this.createdBy = createdBy;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getFileId() {
		return fileId != null ? fileId.toString() : null;
	}

	public void setFileId(ObjectId fileId) {
		this.fileId = fileId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	@Override
	public String toString() {
		return "File{" +
				"fileId=" + fileId.toString() +
				", taskId='" + taskId + '\'' +
				", createdOn=" + createdOn +
				", createdBy='" + createdBy + '\'' +
				'}';
	}
}
