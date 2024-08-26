package com.hotel.lakeside.controller;

import com.hotel.lakeside.exception.RoleAlreadyExistException;
import com.hotel.lakeside.model.Role;
import com.hotel.lakeside.model.User;
import com.hotel.lakeside.service.IRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final IRoleService roleService;

    @Operation(summary = "Novo agendamento", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "agendado com sucesso"),
            @ApiResponse(responseCode = "400", description = "erro no agendamento")
    })
    @GetMapping("/all-roles")
    public ResponseEntity<List<Role>> getAllRoles(){
        return new ResponseEntity<>(roleService.getRoles(), HttpStatus.FOUND);
    }

    @Operation(summary = "Novo agendamento", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "agendado com sucesso"),
            @ApiResponse(responseCode = "400", description = "erro no agendamento")
    })
    @PostMapping("/create-new-role")
    public ResponseEntity<String> createRole(@RequestBody Role role){
        try {
            roleService.createRole(role);
            return ResponseEntity.ok("New role success");
        }catch(RoleAlreadyExistException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @Operation(summary = "Novo agendamento", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "agendado com sucesso"),
            @ApiResponse(responseCode = "400", description = "erro no agendamento")
    })
    @DeleteMapping("/delete/{roleId}")
    public void deleteRole(@PathVariable("roleId") Long roleId){
        roleService.deleteRole(roleId);
    }

    @Operation(summary = "Novo agendamento", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "agendado com sucesso"),
            @ApiResponse(responseCode = "400", description = "erro no agendamento")
    })
    @PostMapping("/remove-all-users-from-role/{roleId}")
    public Role removeAllUsersFromRole(@PathVariable("roleId") Long roleId){
        return roleService.removeAllUsersFromRole(roleId);
    }

    @Operation(summary = "Novo agendamento", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "agendado com sucesso"),
            @ApiResponse(responseCode = "400", description = "erro no agendamento")
    })
    @PostMapping("remove-user-to-role")
    public User removeUserFromRole(@RequestParam("userId") Long userId, @RequestParam("roleId") Long roleId){
        return roleService.removeUserFromRole(userId, roleId);
    }

    @Operation(summary = "Novo agendamento", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "agendado com sucesso"),
            @ApiResponse(responseCode = "400", description = "erro no agendamento")
    })
    @PostMapping("/assign-user-to-role")
    public User assignUserToRole(@RequestParam("userId") Long userId, @RequestParam("roleId") Long roleId){
        return roleService.assignRoleToUser(userId, roleId);
    }
}
