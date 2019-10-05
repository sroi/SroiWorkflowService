
package benefitBountyService.services;

import benefitBountyService.dao.UserRepository;
import benefitBountyService.models.User;
import benefitBountyService.models.dtos.LoginTO;
import benefitBountyService.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    private UserRepository userRepository;

/**
     *
     * @param login
     * @return 0 - success
     *         1 - Username wrong
     *         2 - Password wrong
     *         3 - role wrong
     */

    public Integer login(LoginTO login) {
        Integer loginStatus = -1;
        try {
            User user = userRepository.findByUserId(login.getUsername());
//            User user = optUser.get();
            if (!(user.getUserId().equals(login.getUsername()) && user.getPassword().equals(login.getPassword()))){
                logger.info("User '"+login.getUsername()+"' is not authorized because of wrong password.");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "2");
            } else if (!isValidRole(login.getRole(), user)) {
                logger.info("User '"+login.getUsername()+"' is not authorized because of wrong role.");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"3");
            } else {
                logger.info("User '"+login.getUsername()+"' is authorized");
            }
        } catch (NoSuchElementException ex) {
            logger.info("User '"+login.getUsername()+"' is not authorized because of wrong Username.");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "1", ex);
        }
        return loginStatus;
    }

    public boolean isValidRole(String role, User user) {
        boolean isValid = false;
        switch(Constants.ROLES.valueOf(role)){
            case ADMIN:
                isValid = user.getAdmin().equals(Constants.YES); break;
            case APPROVER:
                isValid = user.getApprover().equals(Constants.YES); break;
            case STAKEHOLDER:
                isValid = user.getStakeholder().equals(Constants.YES); break;
            case VOLUNTEER:
                isValid = user.getVolunteer().equals(Constants.YES); break;
            default:
                isValid = false;
        }
        return isValid;
    }
}
