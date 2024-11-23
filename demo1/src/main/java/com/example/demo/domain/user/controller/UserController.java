package com.example.demo.domain.user.controller;

import com.example.demo.domain.user.dto.request.UserLoginRequest;
import com.example.demo.domain.user.dto.request.UserSaveRequest;
import com.example.demo.domain.user.service.UserService;
import com.example.demo.global.config.response.CommonResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public CommonResponse<String> register(@RequestBody UserSaveRequest request) {
        userService.save(request);
        return CommonResponse.createSuccess("회원가입에 성공했습니다.");
    }

    @PostMapping("/login")
    public CommonResponse<String> login(@RequestBody UserLoginRequest request, HttpSession session) {
        userService.login(request,session);
        return CommonResponse.createSuccess("로그인에 성공했습니다.(세션에 저장)");
    }

    @PostMapping("/logout")
    public CommonResponse<String> logout(HttpSession session) {
        userService.logout(session);
        return CommonResponse.createSuccess("로그아웃에 성공했습니다.(세션에서 제거)");
    }

    @GetMapping("/auth")
    public CommonResponse<Long> auth(HttpSession session) {
        return CommonResponse.createSuccess("로그인된 사용자 ID 조회에 성공했습니다.", userService.authenticate(session));
    }

    @DeleteMapping
    public CommonResponse<String> delete(HttpSession session) {
        userService.delete(session);
        return CommonResponse.createSuccess("유저 탈퇴에 성공했습니다.");
    }
}
