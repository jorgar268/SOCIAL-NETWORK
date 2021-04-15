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

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "/")
    public String mainView(Model model, Principal principal) {
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

        Publication pub1 = new Publication();
        pub1.setUser(daniuser);
        pub1.setText("Programando la web...");
        pub1.setRestricted(false);
        pub1.setTimestamp(new Date());

        Publication pub2 = new Publication();
        pub2.setUser(userJohn);
        pub2.setText("Watching TV for 8 hours in a row. It's a new record!!!");
        pub2.setRestricted(true);
        pub2.setTimestamp(new Date());
        publications.add(pub1);
        publications.add(pub2);
        model.addAttribute("profileUser", profileUser);
        model.addAttribute("publications", publications);

        User user = userRepository.findByEmail(principal.getName());
        model.addAttribute("user", user);
        
        return "main_view";
    }

    @GetMapping(path = "/profile_view")
    public String profileView(Model model) {
        User jorgeUser = new User();
        jorgeUser.setName("Jorge García");
        jorgeUser.setDescription("Estudiante UC3M apasionado por el desarrollo Frontend");
        jorgeUser.setEmail("jorge@example.com");

        List<Publication> publicationsJorge = new ArrayList<Publication>();

        Publication pub1 = new Publication();
        pub1.setUser(jorgeUser);
        pub1.setText("Programando en css...");
        pub1.setRestricted(false);
        pub1.setTimestamp(new Date());

        Publication pub2 = new Publication();
        pub1.setUser(jorgeUser);
        pub1.setText(jorgeUser.getDescription());
        pub1.setRestricted(false);
        pub1.setTimestamp(new Date());

        //publicationsJorge.add(pub1);
        //publicationsJorge.add(pub2);

        model.addAttribute("jorgeUser", jorgeUser);
        model.addAttribute("publicationsJorge", publicationsJorge);


        return "profile_view";
    }

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

    @GetMapping(path = "/login")
    public String loginForm() {
        return "login";
    }

    

    @GetMapping(path = "/register")
    public String register(User user) {
        return "register";
    }
    
}
