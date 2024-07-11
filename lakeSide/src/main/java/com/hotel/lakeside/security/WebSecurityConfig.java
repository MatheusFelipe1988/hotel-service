package com.hotel.lakeside.security;

import com.hotel.lakeside.security.jwt.AuthTokenFIlter;
import com.hotel.lakeside.security.jwt.JwtEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final HotelUserService hotelUserService;
    private final JwtEntryPoint jwtEntryPoint;

    @Bean
    public AuthTokenFIlter authenticationTokenFilter(){
        return new AuthTokenFIlter();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(hotelUserService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
