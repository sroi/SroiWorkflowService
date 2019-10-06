package benefitBountyService.controllers;

import benefitBountyService.models.User;
import benefitBountyService.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @GetMapping("/get")
    public User getUserById(@RequestParam("uid") String id){
        return userService.getUserDetailsById(id);
    }

    @GetMapping("/get/email")
    public User getUserByEmail(@RequestParam("email") String email){
        return userService.getUserByEmailId(email);
    }

    @GetMapping("/get/id")
    public User getUserByUserId(@RequestParam("userId") String userId){
        return userService.getUserByUserID(userId);
    }

    @PostMapping("/save")
    public User saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PostMapping("/saveall")
    public long saveAllUsers(@RequestBody List<User> users) {
        return userService.saveAllUsers(users);
    }

}
