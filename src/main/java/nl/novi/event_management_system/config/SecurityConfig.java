package nl.novi.event_management_system.config;

import nl.novi.event_management_system.enums.RoleEnum;
import nl.novi.event_management_system.filter.JwtRequestFilter;
import nl.novi.event_management_system.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api-docs/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // comment this out in case of errors that are not clear.
                        //.requestMatchers("/**").permitAll()
                        .requestMatchers("/api/v1/authenticate").permitAll()
                        .requestMatchers("/api/v1/authenticated").authenticated()

                        //User endpoints
                        .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/users").hasAnyAuthority(RoleEnum.getRoleName(RoleEnum.ORGANIZER), RoleEnum.getRoleName(RoleEnum.ADMIN))
                        .requestMatchers(HttpMethod.GET, "/api/v1/users/{username}/photo").hasAnyAuthority(RoleEnum.getRoleName(RoleEnum.PARTICIPANT), RoleEnum.getRoleName(RoleEnum.ORGANIZER), RoleEnum.getRoleName(RoleEnum.ADMIN))
                        .requestMatchers(HttpMethod.POST, "/api/v1/users/{username}/photo").hasAnyAuthority(RoleEnum.getRoleName(RoleEnum.PARTICIPANT), RoleEnum.getRoleName(RoleEnum.ORGANIZER), RoleEnum.getRoleName(RoleEnum.ADMIN))
                        .requestMatchers(HttpMethod.POST, "/api/v1/users/{username}/ticket/{ticketId}").hasAnyAuthority(RoleEnum.getRoleName(RoleEnum.ORGANIZER), RoleEnum.getRoleName(RoleEnum.ADMIN))
                        .requestMatchers(HttpMethod.PUT, "/api/v1/users/{username}").hasAnyAuthority(RoleEnum.getRoleName(RoleEnum.ORGANIZER), RoleEnum.getRoleName(RoleEnum.ADMIN))
                        .requestMatchers(HttpMethod.GET, "/api/v1/users/{username}").hasAnyAuthority(RoleEnum.getRoleName(RoleEnum.ORGANIZER), RoleEnum.getRoleName(RoleEnum.ADMIN))
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/users/{username}").hasAuthority(RoleEnum.getRoleName(RoleEnum.ADMIN))

                        //Event endpoints
                        .requestMatchers(HttpMethod.POST, "/api/v1/events/**").hasAnyAuthority(RoleEnum.getRoleName(RoleEnum.ORGANIZER), RoleEnum.getRoleName(RoleEnum.ADMIN))
                        .requestMatchers(HttpMethod.PUT, "/api/v1/events/**").hasAnyAuthority(RoleEnum.getRoleName(RoleEnum.ORGANIZER), RoleEnum.getRoleName(RoleEnum.ADMIN))
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/events/**").hasAnyAuthority(RoleEnum.getRoleName(RoleEnum.ORGANIZER), RoleEnum.getRoleName(RoleEnum.ADMIN))
                        .requestMatchers(HttpMethod.GET, "/api/v1/events/**").hasAnyAuthority(RoleEnum.getRoleName(RoleEnum.PARTICIPANT), RoleEnum.getRoleName(RoleEnum.ORGANIZER), RoleEnum.getRoleName(RoleEnum.ADMIN))

                        //Ticket endpoints
                        .requestMatchers(HttpMethod.GET, "/api/v1/tickets/**").hasAnyAuthority(RoleEnum.getRoleName(RoleEnum.PARTICIPANT), RoleEnum.getRoleName(RoleEnum.ORGANIZER), RoleEnum.getRoleName(RoleEnum.ADMIN))
                        .requestMatchers(HttpMethod.PUT, "/api/v1/tickets/**").hasAnyAuthority(RoleEnum.getRoleName(RoleEnum.ORGANIZER), RoleEnum.getRoleName(RoleEnum.ADMIN))
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/tickets/**").hasAnyAuthority(RoleEnum.getRoleName(RoleEnum.ORGANIZER), RoleEnum.getRoleName(RoleEnum.ADMIN))
                        .requestMatchers(HttpMethod.POST, "/api/v1/tickets/**").hasAnyAuthority(RoleEnum.getRoleName(RoleEnum.ORGANIZER), RoleEnum.getRoleName(RoleEnum.ADMIN))

                        //Feedback endpoints
                        .requestMatchers(HttpMethod.POST, "/api/v1/feedback/**").hasAnyAuthority(RoleEnum.getRoleName(RoleEnum.PARTICIPANT), RoleEnum.getRoleName(RoleEnum.ORGANIZER), RoleEnum.getRoleName(RoleEnum.ADMIN))
                        .requestMatchers(HttpMethod.PUT, "/api/v1/feedback/**").hasAnyAuthority(RoleEnum.getRoleName(RoleEnum.PARTICIPANT), RoleEnum.getRoleName(RoleEnum.ORGANIZER), RoleEnum.getRoleName(RoleEnum.ADMIN))
                        .requestMatchers(HttpMethod.GET, "/api/v1/feedback/**").hasAnyAuthority(RoleEnum.getRoleName(RoleEnum.ORGANIZER), RoleEnum.getRoleName(RoleEnum.ADMIN), RoleEnum.getRoleName(RoleEnum.PARTICIPANT))
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/feedback/**").hasAnyAuthority(RoleEnum.getRoleName(RoleEnum.ORGANIZER), RoleEnum.getRoleName(RoleEnum.ADMIN))

                        .anyRequest().denyAll()
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
