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
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public List<User> getUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()){
            logger.info("Users not found.");
        }
        return users;
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

    private User saveOrUpdateUser(User user){
        user.set_id(ObjectId.get());
        logger.info("Saving User: " + user);
        return userRepository.save(user);
    }
}
