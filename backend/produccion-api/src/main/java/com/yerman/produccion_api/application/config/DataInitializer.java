package com.yerman.produccion_api.application.config;

import com.yerman.produccion_api.infrastructure.entity.UsuarioEntity;
import com.yerman.produccion_api.infrastructure.repository.UsuarioJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Profile("dev")
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    private static final String ADMIN_CC = "1111111111";

    private final UsuarioJpaRepository usuarioJpaRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            UsuarioJpaRepository usuarioJpaRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioJpaRepository = usuarioJpaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        log.info("Verificando si existe usuario admin inicial...");

        if (usuarioJpaRepository.findByCc(ADMIN_CC).isEmpty()) {
            UsuarioEntity admin = new UsuarioEntity();
            admin.setCc(ADMIN_CC);
            admin.setPrimerNombre("Admin");
            admin.setSegundoNombre(null);
            admin.setPrimerApellido("Sistema");
            admin.setSegundoApellido(null);
            admin.setEmail(null);
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setRol(UsuarioEntity.Rol.ADMIN);
            admin.setActivo(true);
            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());

            usuarioJpaRepository.save(admin);
            log.info("Usuario admin inicial creado correctamente.");
        } else {
            log.info("El usuario admin ya existe, no se crea nuevamente.");
        }
    }
}
