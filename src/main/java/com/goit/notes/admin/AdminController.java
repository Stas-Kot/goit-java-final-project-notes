package com.goit.notes.admin;

import com.goit.notes.exceptions.NotValidUserNameException;
import com.goit.notes.exceptions.UserAlreadyExistException;
import com.goit.notes.security.CustomUserDetailsService;
import com.goit.notes.security.SecurityService;
import com.goit.notes.user.User;
import com.goit.notes.user.UserDetailsImpl;
import com.goit.notes.user.UserDto;
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

    @GetMapping("/edit")
    public ModelAndView editUser(@RequestParam(name = "id") Long id) {
        String username = securityService.getUsername();
        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(username);
        User user = userService.getById(id);

        ModelAndView result = new ModelAndView("admin/edit");
        result.addObject("user", UserDto.fromUser(user));
        result.addObject("username", username);

        return result;
    }

    @PostMapping("/edit")
    public ModelAndView saveUpdatedNote(UserDto userDto) {
        ModelAndView result = new ModelAndView("admin/edit");
        String username = securityService.getUsername();
        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(username);
        result.addObject("user", userDto);
        result.addObject("username", username);
        System.out.println("userDto = " + userDto);

        try {
            userService.update(userDto.toUser(userDto));
        } catch (NotValidUserNameException ex) {
            result.addObject("message",
                    "Username must be alphanumeric and must be between 5 and 50 in length!");
            return result;
        } catch (UserAlreadyExistException ex) {
            result.addObject("message",
                    "An account for that username(\"" + userDto.getName() + "\") already exists.");
            return result;
        }
        return new ModelAndView("redirect:/admin");
    }
}
