package ge.OCMS.configuration;

import ge.OCMS.configuration.jwt.JwtAuthFilter;
import ge.OCMS.wrapper.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class RequestMatcherConfig {

    private final JwtAuthFilter jwtAuthFilter;

    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/health").permitAll()

                        .requestMatchers(HttpMethod.GET, "/courses/**").hasAnyAuthority(RoleEnum.STUDENT.getValue(), RoleEnum.INSTRUCTOR.getValue())
                        .requestMatchers("/courses/**").hasAuthority(RoleEnum.INSTRUCTOR.getValue())

                        .requestMatchers(HttpMethod.GET, "/lessons/**").hasAnyAuthority(RoleEnum.STUDENT.getValue(), RoleEnum.INSTRUCTOR.getValue())
                        .requestMatchers("/lessons/**").hasAuthority(RoleEnum.INSTRUCTOR.getValue())

                        .requestMatchers(HttpMethod.GET, "/categories/**").hasAnyAuthority(RoleEnum.STUDENT.getValue(), RoleEnum.INSTRUCTOR.getValue())
                        .requestMatchers("/categories/**").hasAuthority(RoleEnum.INSTRUCTOR.getValue())

                        .requestMatchers(HttpMethod.GET, "/enrollments/**").hasAnyAuthority(RoleEnum.STUDENT.getValue(), RoleEnum.INSTRUCTOR.getValue())
                        .requestMatchers("/enrollments/**").hasAuthority(RoleEnum.INSTRUCTOR.getValue())

                        .requestMatchers(HttpMethod.GET, "/reviews/**").hasAnyAuthority(RoleEnum.STUDENT.getValue(), RoleEnum.INSTRUCTOR.getValue())
                        .requestMatchers("/reviews/**").hasAuthority(RoleEnum.STUDENT.getValue())
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class).build();
    }
}
