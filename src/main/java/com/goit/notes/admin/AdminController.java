package com.goit.notes.admin;

import com.goit.notes.security.CustomUserDetailsService;
import com.goit.notes.security.SecurityService;
import com.goit.notes.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final CustomUserDetailsService userDetailsService;
    private final SecurityService securityService;
    private final UserService userService;

    @GetMapping()
    public ModelAndView getAdmin() {
        String username = securityService.getUsername();
        ModelAndView result = new ModelAndView("admin/page");
        result.addObject("username", username);
        result.addObject("users", userService.findAllUsers());
        return result;
    }

    @PostMapping("/delete/user")
    public RedirectView delete(@RequestParam(name = "id") Long id) {
        userService.deleteById(id);
        return new RedirectView("/admin");
    }
}
