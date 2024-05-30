package org.zerock.b01.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.b01.domain.User;
import org.zerock.b01.dto.UserDTO;
import org.zerock.b01.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/users")
@Log4j2
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/register")
    public void registerGet(){

    }

    @PostMapping("/register")
    public String registerPost(@Valid UserDTO userDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()){
          redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
          return "redirect:/users/register";
        }

        Long id = userService.register(userDTO);
        redirectAttributes.addFlashAttribute("message", "User registered successfully");
        return "redirect:/board/list";
    }

    @GetMapping("/login")
    public void loginGet(){}

    @PostMapping("/login")
    public String login(@RequestParam String mid, @RequestParam String mpw, RedirectAttributes redirectAttributes) {
        try {
            UserDTO userDTO = userService.login(mid, mpw);
            redirectAttributes.addFlashAttribute("message", "User logged in successfully");
            return "redirect:/board/list"; // 로그인 성공 후 리디렉션
        } catch (NoSuchElementException e) {
            return "redirect:/users/login";
        }
    }

}
