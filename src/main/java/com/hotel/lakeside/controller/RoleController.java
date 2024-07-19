package com.hotel.lakeside.controller;

import com.hotel.lakeside.exception.RoleAlreadyExistException;
import com.hotel.lakeside.model.Role;
import com.hotel.lakeside.model.User;
import com.hotel.lakeside.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final IRoleService roleService;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/all-roles")
    public ResponseEntity<List<Role>> getAllRoles(){
        return new ResponseEntity<>(roleService.getRoles(), HttpStatus.FOUND);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/create-new-role")
    public ResponseEntity<String> createRole(@RequestBody Role role){
        try {
            roleService.createRole(role);
            return ResponseEntity.ok("New role success");
        }catch(RoleAlreadyExistException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @DeleteMapping("/delete/{roleId}")
    public void deleteRole(@PathVariable("roleId") Long roleId){
        roleService.deleteRole(roleId);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/remove-all-users-from-role/{roleId}")
    public Role removeAllUsersFromRole(@PathVariable("roleId") Long roleId){
        return roleService.removeAllUsersFromRole(roleId);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("remove-user-to-role")
    public User removeUserFromRole(@RequestParam("userId") Long userId, @RequestParam("roleId") Long roleId){
        return roleService.removeUserFromRole(userId, roleId);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/assign-user-to-role")
    public User assignUserToRole(@RequestParam("userId") Long userId, @RequestParam("roleId") Long roleId){
        return roleService.assignRoleToUser(userId, roleId);
    }
}
