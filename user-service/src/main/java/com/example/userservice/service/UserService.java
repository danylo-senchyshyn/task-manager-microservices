package com.example.userservice.service;

import com.example.userservice.dto.RegisterRequest;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Регистрирует нового пользователя.
     * Проверяет, что email ещё не используется,
     * сохраняет пользователя с зашифрованным паролем,
     * возвращает DTO созданного пользователя.
     *
     * @param request данные регистрации пользователя
     * @return DTO зарегистрированного пользователя
     * @throws RuntimeException если email уже занят
     */
    public UserDTO register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    /**
     * Загружает пользователя по имени для Spring Security.
     *
     * @param username имя пользователя
     * @return объект UserDetails с данными пользователя и ролями
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }

    /**
     * Конвертирует сущность User в DTO для передачи клиенту.
     *
     * @param user сущность пользователя
     * @return DTO пользователя
     */
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }

    /**
     * Получает пользователя по его ID.
     *
     * @param id идентификатор пользователя
     * @return DTO пользователя
     * @throws RuntimeException если пользователь не найден
     */
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDTO(user);
    }

    /**
     * Обновляет данные пользователя по ID.
     * Пароль при обновлении шифруется.
     *
     * @param id      идентификатор пользователя
     * @param request данные для обновления
     * @return DTO обновлённого пользователя
     * @throws RuntimeException если пользователь не найден
     */
    public UserDTO updateUser(Long id, RegisterRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }

    /**
     * Удаляет пользователя по ID.
     *
     * @param id идентификатор пользователя
     * @throws RuntimeException если пользователь не найден
     */
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    /**
     * Удаляет всех пользователей из базы.
     */
    public void reset() {
        userRepository.deleteAll();
    }
}