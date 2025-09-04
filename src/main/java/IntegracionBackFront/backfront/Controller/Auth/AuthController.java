package IntegracionBackFront.backfront.Controller.Auth;

import IntegracionBackFront.backfront.Services.Auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/apiAuth")
public class AuthController {

    @Autowired
    private AuthService service;


}
