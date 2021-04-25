package socialnetwork.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import socialnetwork.model.Publication;
import socialnetwork.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import socialnetwork.services.UserService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import org.springframework.validation.BindingResult;

import socialnetwork.model.UserRepository;
import org.springframework.web.bind.annotation.RequestParam;

import socialnetwork.model.PublicationRepository;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PublicationRepository publicationRepository;

    // CONTROL PAGINA PRINCIPAL
    @GetMapping(path = "/")
    public String mainView(Model model, Principal principal, Publication publication) {
        User profileUser = new User();
        profileUser.setName("Mary Jones");
        profileUser.setDescription("Addicted to social networks");
        
        List<Publication> publications = new ArrayList<Publication>();
        User daniuser = new User();
        daniuser.setEmail("dani@example.com");
        daniuser.setName("Daniel Dominguez");
        daniuser.setDescription("Me encantan los deportes");

        User userJohn = new User();
        userJohn.setEmail("john@excample.com");
        userJohn.setName("John Doe");
        userJohn.setDescription("Professional couch potato");

        User jorgeUser = new User();
        jorgeUser.setName("Jorge García");
        jorgeUser.setDescription("Estudiante UC3M apasionado por el desarrollo Frontend");
        jorgeUser.setEmail("jorge@example.com");


        publications = publicationRepository.findFirst10ByRestrictedIsFalseOrderByTimestampDesc();
        
        model.addAttribute("profileUser", daniuser);
        model.addAttribute("publications", publications);

        User user = userRepository.findByEmail(principal.getName());
        model.addAttribute("user", user);
        
        return "main_view";
    }

    


    // CONTROL PAGINA USER_VIEW
    
    @GetMapping(path = "/user/{userId}")
    public String userView(@PathVariable int userId, Model model) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");

        }
        User user = userOpt.get();
        model.addAttribute("user", user);
        model.addAttribute("publications", publicationRepository.findByUserOrderByTimestampDesc(user));
        return "user_view";
    }


    // CONTROL SUBMIT USUARIO AL REGISTRAR
    @PostMapping(path = "/register")
    public String register(@Valid @ModelAttribute("user") User user,
                        BindingResult bindingResult,
                        @RequestParam String passwordRepeat) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return "redirect:register?duplicate_email";
        }
        if (user.getPassword().equals(passwordRepeat)) {
            userService.register(user);
        } else {
            return "redirect:register?passwords";
        }
        return "redirect:login?registered";
    }

    // CONTROL DE PANTALLA LOGIN
    @GetMapping(path = "/login")
    public String loginForm() {
        return "login";
    }

    //CONTROL PANTALLA REGISTER
    @GetMapping(path = "/register")
    public String register(User user) {
        return "register";
    }

    // CONTROL POST DE PUBLICACION DESDE MAIN_VIEW
    @PostMapping(path = "/post")
    public String postPublication(@Valid @ModelAttribute("publication") Publication publication,
                                BindingResult bindingResult,
                                Principal principal) {
        if (bindingResult.hasErrors()) {
            return "redirect:/";
        }
        User user = userRepository.findByEmail(principal.getName());
        publication.setUser(user);
        publication.setTimestamp(new Date());
        publicationRepository.save(publication);
        return "redirect:/";
    }


    @GetMapping(path = "/settings")
    public String settingsView(User user){

        return "settings";
    }

    @PostMapping(path = "/postSettings")
    public String settings(
                        @RequestParam String description,
                        Principal principal) {
        
        
        User user = userRepository.findByEmail(principal.getName());
        user.setDescription(description);
        userRepository.save(user);

        return "redirect:/user/"+user.getId();
    }
    
}
