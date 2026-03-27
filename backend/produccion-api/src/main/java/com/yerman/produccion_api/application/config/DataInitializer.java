package com.yerman.produccion_api.application.config;

import com.yerman.produccion_api.infrastructure.repository.UsuarioJpaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioJpaRepository usuarioJpaRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioJpaRepository usuarioJpaRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioJpaRepository = usuarioJpaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        usuarioJpaRepository.findByEmail("admin@yermansas.com").ifPresent(usuario -> {
            if (!passwordEncoder.matches("admin123", usuario.getPasswordHash())) {
                usuario.setPasswordHash(passwordEncoder.encode("admin123"));
                usuarioJpaRepository.save(usuario);
                System.out.println("Hash del admin corregido.");
            }
        });
    }
}
