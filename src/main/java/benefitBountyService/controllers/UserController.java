package benefitBountyService.controllers;

import benefitBountyService.models.dtos.UserTO;
import benefitBountyService.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
//@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public List<UserTO> getUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/get")
    public UserTO getUserById(@RequestParam("uid") String id){
        return userService.getUserDetailsById(id);
    }
}
