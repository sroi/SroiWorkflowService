package benefitBountyService.services;

import benefitBountyService.dao.UserRepository;
import benefitBountyService.models.User;
import benefitBountyService.models.dtos.PTUserTO;
import benefitBountyService.models.dtos.UserTO;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public List<User> getUsers() {
        List<User> users = userRepository.findAll();
//        List<UserTO> usersTo =  null;
        if (users.isEmpty()) {
            logger.info("Users not found.");
        }
        return users;
    }

    public List<UserTO> getAllUsers() {
        List<User> users = getUsers();
        List<UserTO> usersTo =  null;
        if (users.isEmpty()){
            logger.info("Users not found.");
        } else {
            usersTo = users.parallelStream().map(user -> new UserTO(user.get_id(), user.getUserId(), user.getName(),user.getEmailId(), user.getPhoneNo(),
                    user.getDetails(), user.getAdmin(), user.getStakeholder(), user.getApprover(), user.getVolunteer())).collect(Collectors.toList());
        }
        return usersTo;
    }

    public User getUserById(String _id) {
        User usr = null;
        Optional<User> optUser = userRepository.findById(_id);
        if (optUser.isPresent()){
            usr = optUser.get();
            logger.info("Users found for id '"+ _id +"'.");
        }
        return usr;
    }

    public UserTO getUserDetailsById(String _id) {
        UserTO userTo = null;
        User user = null;
        Optional<User> optUser = userRepository.findById(_id);
        if (optUser.isPresent()){
            user = optUser.get();
            userTo = new UserTO(user.get_id(), user.getUserId(), user.getName(),user.getEmailId(),
                    user.getPhoneNo(), user.getDetails(), user.getAdmin(), user.getStakeholder(), user.getApprover(), user.getVolunteer());
            logger.info("Users found: "+ userTo);
        }
        return userTo;
    }

    public User getUserByEmail(String emailId) {
        User usr = null;
        Optional<User> optUser = userRepository.findByEmail(emailId);
        if (optUser.isPresent()){
            usr = optUser.get();
            logger.info("Users found for Email Id '"+ emailId +"'.");
        }
        return usr;
    }

    private User saveUser(PTUserTO userTo, User user) {
//        User user = new User();
        user.setEmailId(userTo.getEmail());
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

    public User saveOrUpdateUser(User user){
        if (user.getObjectId() == null)
            user.set_id(ObjectId.get());
        logger.info("Saving User: " + user);
        return userRepository.save(user);
    }

    public User saveApprover(PTUserTO appr) {
        User apvr = new User();
        apvr.setApprover("Y");
        return saveUser(appr, apvr);
    }

    public User setVolunteer(PTUserTO volunteer) {
        User vol = new User();
        vol.set_id(ObjectId.get());
        vol.setEmailId(volunteer.getEmail());
        vol.setName(volunteer.getName());
        vol.setPhoneNo(volunteer.getPhoneNo());
        vol.setUserId(volunteer.getEmail());
        vol.setVolunteer("Y");
        return vol;
    }

    public List<User> saveVolunteers(List<PTUserTO> vols) {
        List<User> newVols = vols.stream().map(vol -> setVolunteer(vol)).collect(Collectors.toList());
        logger.info("Saving Volunteers: " + newVols);
        return userRepository.saveAll(newVols);
    }
}
