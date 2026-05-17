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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final String ROL_ADMIN = "ADMIN";
    private static final String ROL_DUENO_EMPRESA = "DUENO_EMPRESA";
    private static final String ROL_JEFE_PLANTA = "JEFE_PLANTA";
    private static final String ROL_JEFE_PRODUCCION = "JEFE_PRODUCCION";
    private static final String ROL_JEFE_LINEA = "JEFE_LINEA";
    private static final String ROL_AUXILIAR_CALIDAD = "AUXILIAR_CALIDAD";

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
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))

                .authorizeHttpRequests(auth -> auth

                        // PERMITIR OPTIONS (PREFLIGHT)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // AUTH
                        .requestMatchers("/auth/**", "/error").permitAll()

                        // PERFIL
                        .requestMatchers("/me").authenticated()
                        .requestMatchers("/usuarios/mi-password").authenticated()

                        // USUARIOS
                        .requestMatchers(HttpMethod.GET, "/usuarios/**")
                        .hasAnyRole(
                                ROL_ADMIN,
                                ROL_DUENO_EMPRESA,
                                ROL_JEFE_PLANTA,
                                ROL_JEFE_PRODUCCION)

                        .requestMatchers("/usuarios/**")
                        .hasAnyRole(
                                ROL_ADMIN,
                                ROL_DUENO_EMPRESA)

                        // CATALOGOS
                        .requestMatchers(HttpMethod.GET, "/catalogos/**")
                        .authenticated()

                        .requestMatchers("/catalogos/**")
                        .hasAnyRole(
                                ROL_ADMIN,
                                ROL_DUENO_EMPRESA,
                                ROL_JEFE_PLANTA,
                                ROL_JEFE_PRODUCCION)

                        // PROGRAMACIONES Y FORMULAS
                        .requestMatchers("/programaciones/**", "/formulas/**", "/programacion-skus/**")
                        .hasAnyRole(
                                ROL_ADMIN,
                                ROL_DUENO_EMPRESA,
                                ROL_JEFE_PLANTA,
                                ROL_JEFE_PRODUCCION)

                        // RECEPCION LECHE, DESCREMADO, MOVIMIENTOS
                        .requestMatchers("/recepciones-leche/**", "/descremados-recepcion/**", "/movimientos-leche/**")
                        .hasAnyRole(
                                ROL_ADMIN,
                                ROL_DUENO_EMPRESA,
                                ROL_JEFE_PLANTA,
                                ROL_JEFE_PRODUCCION,
                                ROL_JEFE_LINEA)

                        // TANQUES
                        .requestMatchers("/tanques-leche/**")
                        .hasAnyRole(
                                ROL_ADMIN,
                                ROL_DUENO_EMPRESA,
                                ROL_JEFE_PLANTA,
                                ROL_JEFE_PRODUCCION,
                                ROL_JEFE_LINEA,
                                ROL_AUXILIAR_CALIDAD)

                        // CONSULTA DE ORDENES Y EJECUCION
                        .requestMatchers(HttpMethod.GET, "/ordenes-produccion/**", "/ejecucion-batch/**")
                        .hasAnyRole(
                                ROL_ADMIN,
                                ROL_DUENO_EMPRESA,
                                ROL_JEFE_PLANTA,
                                ROL_JEFE_PRODUCCION,
                                ROL_JEFE_LINEA,
                                ROL_AUXILIAR_CALIDAD)

                        // ORDENES Y EJECUCION
                        .requestMatchers("/ordenes-produccion/**", "/ejecucion-batch/**", "/producciones-lactea/**")
                        .hasAnyRole(
                                ROL_ADMIN,
                                ROL_DUENO_EMPRESA,
                                ROL_JEFE_PLANTA,
                                ROL_JEFE_PRODUCCION,
                                ROL_JEFE_LINEA)

                        // CALIDAD
                        .requestMatchers("/mediciones-calidad-lactea/**", "/controles-calidad-lactea/**")
                        .hasAnyRole(
                                ROL_ADMIN,
                                ROL_DUENO_EMPRESA,
                                ROL_JEFE_PLANTA,
                                ROL_JEFE_PRODUCCION,
                                ROL_AUXILIAR_CALIDAD)

                        // EMPAQUE Y OTROS PROCESOS MES
                        .requestMatchers(
                                "/empaques-lacteos/**",
                                "/productos-terminados-lacteos/**",
                                "/registros-insumo-lacteo/**",
                                "/dashboard-operativo/**",
                                "/reportes/lacteos/**")
                        .hasAnyRole(
                                ROL_ADMIN,
                                ROL_DUENO_EMPRESA,
                                ROL_JEFE_PLANTA,
                                ROL_JEFE_PRODUCCION)

                        // DASHBOARD GENERAL
                        .requestMatchers("/dashboard/**")
                        .hasAnyRole(
                                ROL_ADMIN,
                                ROL_DUENO_EMPRESA,
                                ROL_JEFE_PLANTA,
                                ROL_JEFE_PRODUCCION)

                        // TODO LO DEMAS
                        .anyRequest().authenticated())

                .authenticationProvider(authenticationProvider())

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
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
