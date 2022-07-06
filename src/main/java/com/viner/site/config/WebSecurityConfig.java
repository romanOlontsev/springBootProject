package com.viner.site.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailsServiceConfig userDetailsService;

//    @Autowired
//    public DataSource dataSource() {
//        return new EmbeddedDatabaseBuilder()
//                .setType(EmbeddedDatabaseType.H2)
//                .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
//                .build();
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager =
                http.getSharedObject(AuthenticationManagerBuilder.class)
                    .userDetailsService(userDetailsService)

//                    .jdbcAuthentication()
//                    .dataSource(dataSource())
//                    .passwordEncoder(NoOpPasswordEncoder.getInstance())
//                    .usersByUsernameQuery("SELECT username, password FROM users WHERE username=?")
//                    .authoritiesByUsernameQuery(
//                            "SELECT u.username, ur.roles FROM users u INNER JOIN user_role ur ON u.id = ur.user_id " +
//                                    "WHERE u.username=?")
                    .and()
                    .build();


        http.authorizeRequests()
            .antMatchers("/", "/users/**", "/registration/**")
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .formLogin(form -> form
                    .loginPage("/login")
                    .permitAll())
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            .permitAll()
            .and()
            .authenticationManager(authenticationManager);
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                         .antMatchers("/images/**",
                                 "/js/**",
                                 "/webjars/**",
                                 "/h2-console/**");
    }

    @Bean
    public AuthenticationManager jdbcAuthenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();

    }

//    @Autowired
//    public void configure(AuthenticationManagerBuilder builder) throws Exception {
//        builder.jdbcAuthentication()
//               .dataSource(dataSource())
//               .passwordEncoder(NoOpPasswordEncoder.getInstance())
//               .usersByUsernameQuery("SELECT username, password FROM users WHERE username=?")
//               .authoritiesByUsernameQuery(
//                       "SELECT u.username, ur.roles FROM users u INNER JOIN user_role ur ON u.id = ur.user_id " +
//                               "WHERE u.username=?");
//    }



//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user =
//                User.withDefaultPasswordEncoder()
//                    .username("user@mail.ru")
//                    .password("p")
//                    .roles("USER")
//                    .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }
}