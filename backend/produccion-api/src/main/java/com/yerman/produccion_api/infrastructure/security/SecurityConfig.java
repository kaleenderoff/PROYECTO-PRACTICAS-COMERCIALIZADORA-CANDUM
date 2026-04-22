package com.yerman.produccion_api.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final String ROL_JEFE_PRODUCCION = "JEFE_PRODUCCION";
    private static final String ROL_JEFE_LINEA = "JEFE_LINEA";
    private static final String ROL_AUXILIAR_CALIDAD = "AUXILIAR_CALIDAD";
    private static final String ROL_ANALISTA_LACTEOS = "ANALISTA_LACTEOS";
    private static final String ROL_JEFE_PLANTA = "JEFE_PLANTA";
    private static final String ROL_GERENCIA = "GERENCIA";
    private static final String ROL_ADMIN = "ADMIN";

    private static final String RUTA_PRODUCTOS_TERMINADOS = "/productos-terminados/**";
    private static final String RUTA_EMPAQUES = "/empaques/**";

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            UserDetailsService userDetailsService,
            JwtAuthenticationEntryPoint authenticationEntryPoint,
            JwtAccessDeniedHandler accessDeniedHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/error").permitAll()

                        .requestMatchers("/usuarios/mi-password").authenticated()
                        .requestMatchers("/usuarios/**").hasRole(ROL_ADMIN)

                        .requestMatchers(HttpMethod.GET, "/catalogos/**").authenticated()
                        .requestMatchers("/catalogos/**").hasAnyRole(ROL_ADMIN, ROL_JEFE_PRODUCCION)

                        .requestMatchers(HttpMethod.POST, "/validaciones/**")
                        .hasAnyRole(ROL_JEFE_PRODUCCION, ROL_ADMIN)

                        .requestMatchers(HttpMethod.GET, "/validaciones/**")
                        .hasAnyRole(ROL_JEFE_PRODUCCION, ROL_JEFE_PLANTA, ROL_ADMIN)

                        .requestMatchers("/producciones/**")
                        .hasAnyRole(ROL_JEFE_PRODUCCION, ROL_JEFE_LINEA, ROL_JEFE_PLANTA, ROL_ADMIN)

                        .requestMatchers("/detalle-produccion/**")
                        .hasAnyRole(ROL_JEFE_PRODUCCION, ROL_JEFE_LINEA, ROL_JEFE_PLANTA, ROL_ADMIN)

                        .requestMatchers("/consumo-insumo/**")
                        .hasAnyRole(ROL_JEFE_PRODUCCION, ROL_JEFE_LINEA, ROL_JEFE_PLANTA, ROL_ADMIN)

                        // PRODUCTOS TERMINADOS
                        .requestMatchers(HttpMethod.POST, RUTA_PRODUCTOS_TERMINADOS)
                        .hasAnyRole(ROL_ADMIN, ROL_JEFE_PRODUCCION)

                        .requestMatchers(HttpMethod.PUT, RUTA_PRODUCTOS_TERMINADOS)
                        .hasAnyRole(ROL_ADMIN, ROL_JEFE_PRODUCCION)

                        .requestMatchers(HttpMethod.PATCH, RUTA_PRODUCTOS_TERMINADOS)
                        .hasAnyRole(ROL_ADMIN, ROL_JEFE_PRODUCCION)

                        .requestMatchers(HttpMethod.GET, RUTA_PRODUCTOS_TERMINADOS)
                        .hasAnyRole(ROL_ADMIN, ROL_JEFE_PRODUCCION, ROL_JEFE_PLANTA, ROL_JEFE_LINEA,
                                ROL_ANALISTA_LACTEOS)

                        // Empaques
                        .requestMatchers(HttpMethod.POST, RUTA_EMPAQUES)
                        .hasAnyRole(ROL_ADMIN, ROL_JEFE_PRODUCCION, ROL_JEFE_LINEA)

                        .requestMatchers(HttpMethod.PUT, RUTA_EMPAQUES)
                        .hasAnyRole(ROL_ADMIN, ROL_JEFE_PRODUCCION, ROL_JEFE_LINEA)

                        .requestMatchers(HttpMethod.GET, RUTA_EMPAQUES)
                        .hasAnyRole(ROL_ADMIN, ROL_JEFE_PRODUCCION, ROL_JEFE_PLANTA, ROL_JEFE_LINEA,
                                ROL_ANALISTA_LACTEOS)

                        // AUDITORIA
                        .requestMatchers(HttpMethod.GET, "/auditoria/**")
                        .hasAnyRole(ROL_ADMIN, ROL_JEFE_PRODUCCION, ROL_JEFE_PLANTA)

                        // DASHBOARD
                        .requestMatchers(HttpMethod.GET, "/dashboard/**")
                        .hasAnyRole(ROL_ADMIN, ROL_JEFE_PRODUCCION, ROL_JEFE_PLANTA, ROL_GERENCIA,
                                ROL_ANALISTA_LACTEOS)

                        .requestMatchers("/me").authenticated()

                        .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
