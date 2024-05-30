package org.zerock.b01.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    private HttpSession httpSession;

    @GetMapping("/register")
    public void registerGet() {
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
    public String loginGet(HttpServletRequest req) {
        HttpSession session = req.getSession(false); // 세션이 없으면 새로 생성하지 않고, null을 반환하도록 설정

        if (session != null && session.getAttribute("user") != null) {
            return "redirect:/board/list"; // 세션에 "user" 속성이 있다면 게시판 목록 페이지로 리디렉션
        } else {
            return "/users/login"; // 세션이 없거나 "user" 속성이 없는 경우 로그인 페이지로 이동
        }
    }




//    @PostMapping("/login")
//    public String login(@RequestParam String mid, @RequestParam String mpw, RedirectAttributes redirectAttributes) {
//        try {
//            UserDTO userDTO = userService.login(mid, mpw);
//            httpSession.setAttribute("user", userDTO.getMid());
//            return "redirect:/board/list"; // 로그인 성공 후 리디렉션
//        } catch (NoSuchElementException e) {
//            return "redirect:/users/login";
//        }
//    }
    @PostMapping("/login")
    public String login(@RequestParam String mid, @RequestParam String mpw, RedirectAttributes redirectAttributes, HttpServletRequest req) {
        try {
            UserDTO userDTO = userService.login(mid, mpw);
            HttpSession session = req.getSession(); // 세션을 가져옴
            session.setAttribute("user", userDTO.getMid()); // 로그인 사용자의 아이디를 세션에 저장
            return "redirect:/board/list"; // 로그인 성공 후 리디렉션
        } catch (NoSuchElementException e) {
            return "redirect:/users/login"; // 로그인 실패 시 로그인 페이지로 리디렉션
        }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }

}
