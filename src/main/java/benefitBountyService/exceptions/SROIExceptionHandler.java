package benefitBountyService.exceptions;

import com.mongodb.MongoException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class SROIExceptionHandler extends ResponseEntityExceptionHandler {

    //Commented out above code as this code doesn't send HTTP Status and sends Error Message only
    /*@ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<Object> handleNotFound(Exception ex, WebRequest request){
        System.out.println("**************Inside handleNotFound");
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NO_CONTENT, request);
    }*/

    @ExceptionHandler({MongoException.class})
    public ResponseEntity<Object> handleMongoException(Exception ex, WebRequest request){
        logger.info("Handling MongoException. "+ex.getMessage());
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.PRECONDITION_FAILED, request);
    }
}
