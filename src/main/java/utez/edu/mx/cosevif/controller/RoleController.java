package utez.edu.mx.cosevif.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.cosevif.model.Role;

import java.util.List;



@RestController
@RequestMapping("/roles")
public class RoleController {

    @GetMapping
    public ResponseEntity<?> getRoles() {
        return ResponseEntity.ok(Role.values());
    }
}
