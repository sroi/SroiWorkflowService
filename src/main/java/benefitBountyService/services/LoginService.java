
package benefitBountyService.services;

import benefitBountyService.dao.UserRepository;
import benefitBountyService.models.User;
import benefitBountyService.models.dtos.LoginTO;
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
            Optional<User> optUser = userRepository.findByUserId(login.getUsername());
            User user = optUser.get();
            if (!(user.getUserId().equals(login.getUsername()) && user.getPassword().equals(login.getPassword()))){
                loginStatus = 2;
                logger.info("User '"+login.getUsername()+"' is not authorized because of wrong password.");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "2");
            } else if (!user.isValidRole(login.getRole())) {
                loginStatus = 3;
                logger.info("User '"+login.getUsername()+"' is not authorized because of wrong role.");
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"3");
            } else {
                loginStatus = 0;
                logger.info("User '"+login.getUsername()+"' is authorized");
            }
        } catch (NoSuchElementException ex) {
            loginStatus = 1;
            logger.info("User '"+login.getUsername()+"' is not authorized because of wrong Username.");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "1", ex);
        }
        return loginStatus;
    }
}
