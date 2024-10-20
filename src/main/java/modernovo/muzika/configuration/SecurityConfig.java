package modernovo.muzika.configuration;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modernovo.muzika.repositories.UserRepository;
import modernovo.muzika.security.SHA384PasswordEncoder;
import modernovo.muzika.security.UserDetailsImpl;
import modernovo.muzika.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

@EnableWebSecurity
@Configuration
public class SecurityConfig {


    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        //@formatter:off
        http
                .securityMatcher("/api/**")
                .authorizeHttpRequests((authorize) -> authorize

                        .requestMatchers("/api/auth/register").permitAll()
                        .requestMatchers("/api/auth/logout").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/open/**").permitAll()

                        .anyRequest().authenticated())
                // 401-UNAUTHORIZED when anonymous user tries to access protected URLs
                .exceptionHandling((handle) -> handle.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)) )
                .formLogin((form) ->{
                    form.loginPage("/login");
                    form.loginProcessingUrl("/api/auth/login");
                    form.usernameParameter("username");
                    form.passwordParameter("password");
                    form.successHandler((req, res, auth) -> res.setStatus(HttpStatus.NO_CONTENT.value()));
                    form.failureHandler(new SimpleUrlAuthenticationFailureHandler());
                })
                .csrf(AbstractHttpConfigurer::disable);
        //@formatter:on
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new SHA384PasswordEncoder();
    }
}




