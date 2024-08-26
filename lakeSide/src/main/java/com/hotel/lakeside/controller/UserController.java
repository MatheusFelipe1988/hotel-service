package com.hotel.lakeside.controller;

import com.hotel.lakeside.model.User;
import com.hotel.lakeside.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService iUserService;

    @Operation(summary = "Novo agendamento", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "agendado com sucesso"),
            @ApiResponse(responseCode = "400", description = "erro no agendamento")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<User>> getUsers(){
        return new ResponseEntity<>(iUserService.getUsers(), HttpStatus.FOUND);
    }

    @Operation(summary = "Novo agendamento", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "agendado com sucesso"),
            @ApiResponse(responseCode = "400", description = "erro no agendamento")
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable("email") String email){
        try {
            User theUser = iUserService.getUser(email);
            return ResponseEntity.ok(theUser);
        }catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching user");
        }
    }

    @Operation(summary = "Novo agendamento", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "agendado com sucesso"),
            @ApiResponse(responseCode = "400", description = "erro no agendamento")
    })
    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ADMIN') and #email == principal.username")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") String email){
        try {
            iUserService.deleteUser(email);
            return ResponseEntity.ok("User deleted");
        }catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user");
        }
    }
}
