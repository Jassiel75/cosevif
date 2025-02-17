package utez.edu.mx.cosevif.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.cosevif.model.Role;
import utez.edu.mx.cosevif.repository.RoleRepository;
import utez.edu.mx.cosevif.service.RoleService;

import java.util.List;


@RestController
@RequestMapping("/roles")
public class Rolecontroller {

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createRole(@RequestBody Role role) {
        roleRepository.save(role);
        return ResponseEntity.ok("role creado Exitosamente");
    }

    @GetMapping
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}