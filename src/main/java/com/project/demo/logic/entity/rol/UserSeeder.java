package com.project.demo.logic.entity.rol;

import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Order(2)
@Component
public class UserSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSeeder(
            RoleRepository roleRepository,
            UserRepository  userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.createUser("Jeffry", "Valverde", "super.admin@gmail.com", "superadmin123", RoleEnum.SUPER_ADMIN);
        this.createUser("Miguel", "Perez", "test@gmail.com", "test123", RoleEnum.USER);
    }

    private void createUser(String name, String lastName, String email, String password, RoleEnum role) {
        User superAdminRole = new User();
        superAdminRole.setName(name);
        superAdminRole.setLastname(lastName);
        superAdminRole.setEmail(email);
        superAdminRole.setPassword(password);

        Optional<Role> optionalRole = roleRepository.findByName(role);
        Optional<User> optionalUser = userRepository.findByEmail(superAdminRole.getEmail());

        if (optionalRole.isEmpty() || optionalUser.isPresent()) {
            return;
        }

        var user = new User();
        user.setName(superAdminRole.getName());
        user.setLastname(superAdminRole.getLastname());
        user.setEmail(superAdminRole.getEmail());
        user.setPassword(passwordEncoder.encode(superAdminRole.getPassword()));
        user.setRole(optionalRole.get());

        userRepository.save(user);
    }
}
