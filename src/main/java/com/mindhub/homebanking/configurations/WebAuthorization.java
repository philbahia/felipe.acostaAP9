package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration

public class WebAuthorization{

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http.authorizeRequests()
                .antMatchers("/h2-console/**","/web/index.html","/web/img/**","/web/js/**","/web/css/**").permitAll()
                .antMatchers(HttpMethod.POST,"/api/logout","/api/login").permitAll()
                .antMatchers(HttpMethod.POST,"/api/clients").permitAll()

                .antMatchers(HttpMethod.POST,"/web/account.html").hasAuthority("CLIENT")
                .antMatchers("/web/loan-application.html").hasAuthority("CLIENT")
                .antMatchers("/web/cards.html","/web/account.html","/web/accounts.html").hasAnyAuthority("CLIENT","ADMIN")
                .antMatchers("/web/create-cards.html","/web/transfers.html").hasAuthority("CLIENT")
                .antMatchers("/api/clients/current").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"/api/transactions").hasAuthority("CLIENT")

                .antMatchers(HttpMethod.POST,"/api/clients/current/accounts","/api/loans").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.GET,"/api/clients/current/accounts","/api/loans").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.GET,"/clients/current/cards").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"/api/clients/current/cards").hasAuthority("CLIENT")
                .antMatchers("/api/accounts/{id}").hasAuthority("CLIENT")
                .antMatchers("/api/clients/current/accounts","/api/clients/current/cards").hasAuthority("CLIENT")

                .antMatchers("/api/accounts","/api/clients/").hasAuthority("ADMIN")
                //.antMatchers("/api/clients/{id}/","/api/clients","/rest/**", "/h2-console/**").hasAnyAuthority("ADMIN")
                .anyRequest().denyAll();


        http.formLogin()

                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");


        http.logout().logoutUrl("/api/logout").deleteCookies("JSESSIONID");

        // turn off checking for CSRF tokens
        http.csrf().disable();
        //disabling frameOptions so h2-console can be accessed
        http.headers().frameOptions().disable();
        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));
        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        return http.build();

    }

        private void clearAuthenticationAttributes(HttpServletRequest request) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            }
        }


}



