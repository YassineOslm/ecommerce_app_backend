package eafc.uccle.be.controller;

import eafc.uccle.be.dto.UserDto;
import eafc.uccle.be.entity.User;
import eafc.uccle.be.entity.UserAddress;
import eafc.uccle.be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/addresses")
    public ResponseEntity<Set<UserAddress>> getUserAddresses(@PathVariable Long id) {
        Set<UserAddress> addresses = userService.getUserAddresses(id);
        if (addresses != null && !addresses.isEmpty()) {
            return ResponseEntity.ok(addresses);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody UserDto user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }
}

