package benefitBountyService.services;

import benefitBountyService.dao.UserRepository;
import benefitBountyService.models.User;
import benefitBountyService.models.dtos.PTUserTO;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public List<User> getUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            logger.info("Users not found.");
        }
        return users;
    }

    public User getUserById(String _id) {
        return userRepository.findById(_id);
    }

    public User getUserDetailsById(String _id) {
        User user = userRepository.findById(_id);
        logger.info("Users found: "+ user);
        return user;
    }

    public User getUserByEmail(String emailId) {
        User user = userRepository.findByEmail(emailId);
        if (user != null) {
            logger.info("Users found for Email Id '" + emailId + "'.");
        }
        return user;
    }

    public User getUserByUserID(String userId) {
        User user = userRepository.findByUserId(userId);
        if (user != null) {
            logger.info("Users found for User Id '"+ userId +"'.");
        }
        return user;
    }

    private User saveUser(PTUserTO userTo, User user) {
        user.setEmail(userTo.getEmail());
        user.setName(userTo.getName());
        user.setPhoneNo(userTo.getPhoneNo());
        user.setUserId(userTo.getEmail());
        User savedSH = saveOrUpdateUser(user);
        return savedSH;
    }

    public User saveStakeHolder(PTUserTO userTo) {
        User sh = new User();
        sh.setStakeholder("Y");
        return saveUser(userTo, sh);
    }

    public User savePOC(PTUserTO userTo) {
        User sh = new User();
        return saveUser(userTo, sh);
    }

    public User saveApprover(PTUserTO appr) {
        User apvr = new User();
        apvr.setApprover("Y");
        return saveUser(appr, apvr);
    }

    public User saveOrUpdateUser(User user){
        if (user.get_id() == null)
            user.set_id(ObjectId.get());
        logger.info("Saving User: " + user);
        return userRepository.save(user);
    }

    public User saveUser(User user){
        logger.info("Saving User: " + user);
        User savedUser = userRepository.save(user);
        if (savedUser != null) {
            logger.info("Users saved "+ savedUser);
        }
        return savedUser;
    }

    public User setVolunteer(User volunteer) {
        //User vol = new User();
        volunteer.set_id(ObjectId.get());
        /*vol.setEmail(volunteer.getEmail());
        vol.setName(volunteer.getName());
        vol.setPhoneNo(volunteer.getPhoneNo());*/
        volunteer.setUserId(volunteer.getEmail());
        volunteer.setVolunteer("Y");
        return volunteer;
    }

    public List<User> saveVolunteers(List<User> vols) {
        List<User> newVols = vols.stream().map(vol -> setVolunteer(vol)).collect(Collectors.toList());
        logger.info("Saving Volunteers: " + newVols);
        return userRepository.saveAll(newVols);
    }

    public long saveAllUsers(List<User> users) {
        int saveStatus = -1;
        int saveCount = userRepository.saveAll(users).size();
        return saveCount > 0 ? 0 : saveStatus;
    }
}
