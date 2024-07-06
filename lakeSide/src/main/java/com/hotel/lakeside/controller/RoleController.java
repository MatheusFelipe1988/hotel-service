package com.hotel.lakeside.controller;

import com.hotel.lakeside.exception.RoleAlreadyExistException;
import com.hotel.lakeside.model.Role;
import com.hotel.lakeside.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {
    private final IRoleService roleService;

    @GetMapping("/all")
    public ResponseEntity<List<Role>> getAllRoles(){
        return new ResponseEntity<>(roleService.getRoles(), HttpStatus.FOUND);
    }

    @PostMapping("")
    public ResponseEntity<String> createRole(@RequestBody Role role){
        try {
            roleService.createRole(role);
            return ResponseEntity.ok("New role success");
        }catch(RoleAlreadyExistException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
