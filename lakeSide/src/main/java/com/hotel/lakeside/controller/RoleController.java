package com.hotel.lakeside.controller;

import com.hotel.lakeside.exception.RoleAlreadyExistException;
import com.hotel.lakeside.model.Role;
import com.hotel.lakeside.model.User;
import com.hotel.lakeside.service.IRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Tag(name = "Role de usuarios")
public class RoleController {

    private final IRoleService roleService;

    @Operation(summary = "Buscando todas as roles", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ao buscar todas as roles"),
            @ApiResponse(responseCode = "400", description = "erro na busca completa")
    })
    @GetMapping("/all-roles")
    public ResponseEntity<List<Role>> getAllRoles(){
        return new ResponseEntity<>(roleService.getRoles(), HttpStatus.FOUND);
    }

    @Operation(summary = "Nova Role", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role criada com sucesso"),
            @ApiResponse(responseCode = "409", description = "Conflito com role existente")
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

    @Operation(summary = "Removendo Role", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "remoção com sucesso"),
            @ApiResponse(responseCode = "400", description = "error")
    })
    @DeleteMapping("/delete/{roleId}")
    public void deleteRole(@PathVariable("roleId") Long roleId){
        roleService.deleteRole(roleId);
    }

    @Operation(summary = "Removendo usuarios pela role", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "remoção com sucesso"),
            @ApiResponse(responseCode = "400", description = "error")
    })
    @PostMapping("/remove-all-users-from-role/{roleId}")
    public Role removeAllUsersFromRole(@PathVariable("roleId") Long roleId){
        return roleService.removeAllUsersFromRole(roleId);
    }

    @Operation(summary = "Remove o user da role", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "sucesso"),
            @ApiResponse(responseCode = "400", description = "error")
    })
    @PostMapping("remove-user-to-role")
    public User removeUserFromRole(@RequestParam("userId") Long userId, @RequestParam("roleId") Long roleId){
        return roleService.removeUserFromRole(userId, roleId);
    }

    @Operation(summary = "Alterando role", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "mudança for OK"),
            @ApiResponse(responseCode = "400", description = "erro na mudança")
    })
    @PostMapping("/assign-user-to-role")
    public User assignUserToRole(@RequestParam("userId") Long userId, @RequestParam("roleId") Long roleId){
        return roleService.assignRoleToUser(userId, roleId);
    }
}
