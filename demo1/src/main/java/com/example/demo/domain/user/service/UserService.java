package com.example.demo.domain.user.service;

import com.example.demo.domain.user.dto.request.UserLoginRequest;
import com.example.demo.domain.user.dto.request.UserSaveRequest;
import com.example.demo.domain.user.entity.User;
import com.example.demo.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RestClient restClient = RestClient.create();

    @Transactional
    public void save(UserSaveRequest dto) {
        User user = User.of(dto.name(), dto.email(), dto.password());
        userRepository.save(user);
    }

    public void login(UserLoginRequest dto, HttpSession session) {
        User user = find(dto.email());

        if(!user.getPassword().equals(dto.password())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        session.setAttribute("userId", user.getId());
        log.info("[Login] session id : {}", session.getId());
    }

    public void logout(HttpSession session) {
        log.info("[Logout] session id : {}", session.getId());
        session.invalidate();
    }

    public Long authenticate(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        User user = find(userId);
        if(!userId.equals(user.getId())) {
            throw new RuntimeException("유효하지 않은 사용자입니다.");
        }
        return userId;
    }

    @Transactional
    public void delete(HttpSession session) {
        Long userId = authenticate(session);
        userRepository.deleteById(userId);
    }

//    private boolean deleteTodo(Long userId) {
//        log.info("유저 삭제 요청. todo 삭제");
//        restClient.delete()
//                .uri("http://13.209.81.42/api/todos/" + userId)
//                .retrieve()
//                .toBodilessEntity();
//
//    }

    private User find(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("사용자가 존재하지 않습니다."));
    }

    private User find(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("사용자가 존재하지 않습니다."));
    }
}
