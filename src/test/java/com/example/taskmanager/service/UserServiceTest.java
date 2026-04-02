package com.example.taskmanager.service;

import com.example.taskmanager.dto.UserRegisterDTO;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private BCryptPasswordEncoder encoder;

    @Test
    @DisplayName("회원가입 테스트")
    void register() {
        // given
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setUsername("id");
        dto.setPassword("1234");
        dto.setPasswordConfirm("1234");
        when(userRepository.findByUsername("id")).thenReturn(Optional.empty());

        // when
        userService.register(dto);

        // then
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("아이디 중복")
    void 아이디중복() {
        // given
        UserRegisterDTO dto = new UserRegisterDTO();
        User user = new User();
        dto.setUsername("id");
        when(userRepository.findByUsername("id")).thenReturn(Optional.of(user));

        // when&then
        assertThatThrownBy(() -> userService.register(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 아이디입니다.");
    }

    @Test
    @DisplayName("비밀번호 불일치")
    void 비밀번호불일치() {
        // given
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setUsername("id");
        when(userRepository.findByUsername("id")).thenReturn(Optional.empty());
        dto.setPassword("1234");
        dto.setPasswordConfirm("123");

        // when&then
        assertThatThrownBy(() -> userService.register(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
    }
}
