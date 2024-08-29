package com.hotel.lakeside.controller;

import com.hotel.lakeside.exception.UserAlreadyExistsException;
import com.hotel.lakeside.model.User;
import com.hotel.lakeside.request.LoginRequest;
import com.hotel.lakeside.response.JwtResponse;
import com.hotel.lakeside.security.HotelUserDetails;
import com.hotel.lakeside.security.jwt.JwtUtils;
import com.hotel.lakeside.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "Cadastro e login de usuario")
@RequestMapping("/auth")
public class AuthController {

    private final IUserService service;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    @Operation(summary = "Cadastrando Usuario", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Adicionando Usuario"),
            @ApiResponse(responseCode = "409", description = "Conflito quando se usa o mesmo nome, email etc.")
    })
    @PostMapping("/register-user")
    public ResponseEntity<?> registerUser(@RequestBody User user){
        try {
            service.registerUser(user);
            return ResponseEntity.ok("Registration success");
        }catch (UserAlreadyExistsException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @Operation(summary = "Login do Usuario", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recebe o token do login"),
            @ApiResponse(responseCode = "400", description = "Erro ao colocar dados cadastrados")
    })
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                        loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtTokenForUser(authentication);
        HotelUserDetails userDetails = (HotelUserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return ResponseEntity.ok(new JwtResponse(
                userDetails.getId(),
                userDetails.getEmail(),
                jwt,
                roles
        ));
    }
}
