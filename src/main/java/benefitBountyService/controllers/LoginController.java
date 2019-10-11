package benefitBountyService.controllers;


import benefitBountyService.models.User;
import benefitBountyService.models.dtos.LoginTO;
import benefitBountyService.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "/authenticate" , method= RequestMethod.POST)
    public int login(@RequestBody LoginTO loginTO){
        int validLogin = -1;
        validLogin = loginService.login(loginTO);
        return validLogin;
    }

    @RequestMapping(value = "/auth" , method= RequestMethod.POST)
    public User newLogin(@RequestBody LoginTO loginTO){
        User validLogin = null;
        validLogin = loginService.newLogin(loginTO);
        return validLogin;
    }
}
