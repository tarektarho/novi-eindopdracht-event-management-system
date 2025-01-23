package nl.novi.event_management_system.config;

import nl.novi.event_management_system.enums.RoleEnum;
import nl.novi.event_management_system.filter.JwtRequestFilter;
import nl.novi.event_management_system.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
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

    public final CustomUserDetailsService customUserDetailsService;

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
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        var auth = new DaoAuthenticationProvider();
        auth.setPasswordEncoder(passwordEncoder);
        auth.setUserDetailsService(customUserDetailsService);
        return new ProviderManager(auth);
    }


    @Bean
    protected SecurityFilterChain filter(HttpSecurity http) throws Exception {

        http
                .httpBasic(basic -> basic.disable())
                .authorizeHttpRequests(auth -> auth
                        // Publieke endpoints
                        .requestMatchers("/api-docs/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // Wanneer je deze uncomments, staat je hele security open. Je hebt dan alleen nog een jwt nodig.
                        //.requestMatchers("/**").permitAll()
                        //todo check if I can set Admin at once for everything instead of adding to each endpoint
                        // User endpoints
                        .requestMatchers(HttpMethod.POST, "/api/v1/users").hasRole(RoleEnum.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/api/v1/users/**").hasRole(RoleEnum.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/api/v1/users/**").hasRole(RoleEnum.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/api/v1/users/**").hasRole(RoleEnum.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasRole(RoleEnum.ADMIN.name())
                        // Event endpoints
                        .requestMatchers(HttpMethod.POST, "/api/v1/events").hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.ORGANIZER.name())
                        .requestMatchers(HttpMethod.GET, "/api/v1/events/**").hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.ORGANIZER.name())
                        .requestMatchers(HttpMethod.PUT, "/api/v1/events/**").hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.ORGANIZER.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/events/**").hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.ORGANIZER.name())
                        // Ticket endpoints
                        .requestMatchers(HttpMethod.POST, "/api/v1/tickets").hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.ORGANIZER.name())
                        .requestMatchers(HttpMethod.GET, "/api/v1/tickets/**").hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.ORGANIZER.name())
                        .requestMatchers(HttpMethod.PUT, "/api/v1/tickets/**").hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.ORGANIZER.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/tickets/**").hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.ORGANIZER.name())

                        // Feedback endpoints
                        .requestMatchers(HttpMethod.POST, "/api/v1/feedbacks").hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.ORGANIZER.name(), RoleEnum.PARTICIPANT.name())
                        .requestMatchers(HttpMethod.GET, "/api/v1/feedbacks/**").hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.ORGANIZER.name(), RoleEnum.PARTICIPANT.name())
                        .requestMatchers(HttpMethod.PUT, "/api/v1/feedbacks/**").hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.ORGANIZER.name(), RoleEnum.PARTICIPANT.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/feedbacks/**").hasAnyRole(RoleEnum.ADMIN.name(), RoleEnum.ORGANIZER.name())
                        // Reservation endpoints

                        // Authentication endpoints
                        // Je mag meerdere paths tegelijk definieren
                        .requestMatchers("/api/v1/authenticated").authenticated()
                        .requestMatchers("/api/v1/authenticate").permitAll()
                        .anyRequest().denyAll()
                ).addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

}
