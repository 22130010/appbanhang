package com.example.appbanhang.user;

import com.example.appbanhang.user.dto.RegisterRequest;
import com.example.appbanhang.user.dto.LoginRequest;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepo;

    public AuthService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public String register(RegisterRequest req) {

        if (userRepo.existsByUsername(req.getUsername())) {
            return "Username đã tồn tại";
        }

        if (!req.getPassword().equals(req.getRepassword())) {
            return "Mật khẩu không khớp";
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(req.getPassword());
        user.setRepassword(req.getRepassword());

        userRepo.save(user);
        return "Đăng ký thành công";
    }

    public String login(LoginRequest req) {

        return userRepo.findById(req.getUsername())
                .filter(u -> u.getPassword().equals(req.getPassword()))
                .map(u -> "Đăng nhập thành công")
                .orElse("Sai tài khoản hoặc mật khẩu");
    }
}