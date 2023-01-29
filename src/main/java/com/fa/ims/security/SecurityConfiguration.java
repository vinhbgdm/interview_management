package com.fa.ims.security;

import com.fa.ims.enums.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration {
    private final AuthenticationSuccessHandler loginSuccessHandler;
    private final AuthenticationFailureHandler loginFailureHandler;

    @Bean
    public StrictHttpFirewall httpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowSemicolon(true);
        firewall.setAllowUrlEncodedSlash(true);
        firewall.setAllowUrlEncodedDoubleSlash(true);
        return firewall;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        .and()
                .formLogin()
                .loginPage("/login") // Define login url
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)
                .loginProcessingUrl("/authentication") // POST Mapping handle authentication
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .deleteCookies("JSESSIONID")
                //            .deleteCookies("remember-me")
                .permitAll()
        .and()
                .rememberMe().key("uniqueAndSecret")
                .and()
                .exceptionHandling().accessDeniedPage("/403")
        .and()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/forgot-password").permitAll()
                .antMatchers("/reset-password/**").permitAll()
                .antMatchers("/assets/**").permitAll()
                .antMatchers("/admin/**", "/api/admin/**")
                .hasAuthority(UserRole.ROLE_ADMIN.name())
                .antMatchers("/candidates/create/**", "/candidates/edit/**", "/offers/create/**", "/offers/**" )
                .hasAnyAuthority(UserRole.ROLE_ADMIN.name(), UserRole.ROLE_MANAGER.name(), UserRole.ROLE_RECRUITER.name())
                .antMatchers("/offers/edit/**" )
                .hasAnyAuthority(UserRole.ROLE_ADMIN.name(), UserRole.ROLE_RECRUITER.name(), UserRole.ROLE_MANAGER.name())
                .anyRequest()
                .authenticated(); // Public page;

        return http.build();
        // @formatter:on
    }
}