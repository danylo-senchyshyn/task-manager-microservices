package com.example.userservice.config;

import com.example.userservice.service.JwtService;
import com.example.userservice.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    public JwtAuthenticationFilter(@Lazy UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * Этот фильтр выполняется один раз на каждый HTTP-запрос.
     * <p>
     * Его задача — проверить, есть ли в запросе JWT-токен, валиден ли он,
     * и если да — установить аутентификацию пользователя в контекст Spring Security.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        /// 1. Извлекаем заголовок Authorization из запроса.
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        ///  2. Если заголовка нет или он не начинается с "Bearer ",
        ///     пропускаем фильтр дальше (без аутентификации)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        ///  3. Извлекаем JWT из заголовка (убираем "Bearer " префикс)
        jwt = authHeader.substring(7);

        ///  4. Получаем username из JWT через JwtService (парсим токен)
        username = jwtService.extractUsername(jwt);


        ///  5. Проверяем, что username не пустой и пользователь еще не аутентифицирован в контексте
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            ///  6. Загружаем детали пользователя из базы
            ///     (или сервиса UserService, который реализует UserDetailsService)
            var userDetails = userService.loadUserByUsername(username);

            ///  7. Проверяем, валиден ли токен именно для этого пользователя
            ///     (токен не истёк, подпись корректна и т.п.)
            if (jwtService.isTokenValid(jwt, userDetails.getUsername())) {
                ///  8. Создаем объект аутентификации для Spring Security с правами пользователя
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,    /// principal (пользователь)
                                null,           /// credentials (пароль не нужен, т.к. уже авторизован)
                                userDetails.getAuthorities());  /// права пользователя

                ///  9. Добавляем детали запроса в объект аутентификации
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                ///  10. Устанавливаем аутентификацию в SecurityContext -
                ///      дальше Spring Security будет считать пользователя аутентифицированным
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        ///  11. Передаем управление дальше по цепочке фильтров (или до контроллера)
        filterChain.doFilter(request, response);
    }
}