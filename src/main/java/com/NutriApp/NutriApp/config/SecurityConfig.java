package com.NutriApp.NutriApp.config;

import com.NutriApp.NutriApp.exceptions.Handlers.AccesDeniedExceptionHandler;
import com.NutriApp.NutriApp.exceptions.Handlers.TokenInvalidoExceptionHandler;
import com.NutriApp.NutriApp.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {


    /* dejo comentado por si no tenemos acceso a la base de datos en algun momento
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }*/

    @Autowired
    private AccesDeniedExceptionHandler accesDeniedExceptionHandler;  //atributo para manejar la exception de acceso denegado

    @Autowired
    private TokenInvalidoExceptionHandler tokenInvalidoExceptionHandler;

    // Configuración del filtro de seguridad para proteger rutas y validar JWT
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthenticationFilter jwtAuthFilter,
                                                   UsuarioService userDetailsService) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/usuario/registro").permitAll() // <- Permitir acceso sin login
                        .requestMatchers("/api/persona/listar").permitAll()
                        .requestMatchers("api/alimentos/buscar").permitAll()
                        .requestMatchers("api/alimentos/detalle/{fdcId}").permitAll()
                        .requestMatchers("/api/persona/obtener").hasRole("ADMIN")
                        .requestMatchers("/auth/login", "/auth/registro").permitAll()

                        .requestMatchers("/api/solicitud/listarTodas").hasRole("ADMIN")
                        .requestMatchers("/api/solicitud/filtrarPorFecha").hasRole("ADMIN")
                        .requestMatchers("/api/solicitud/filtrar/username").hasRole("ADMIN")
                        .requestMatchers("/api/solicitud/filtrar/nombreComida").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accesDeniedExceptionHandler)
                        //maneja la exception de acceso denegado aca
                        //porque antes que llegue a globalExceptionHandler
                        //sea catchea antes entonces hay que manejarla aca

                        .authenticationEntryPoint(tokenInvalidoExceptionHandler) //manejador para token faltante o invalido
                )

                .authenticationProvider(authenticationProvider(userDetailsService))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

    // Proveedor de autenticación que conecta al servicio de usuarios y al codificador
    @Bean
    public AuthenticationProvider authenticationProvider(UsuarioService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // AuthenticationManager usando directamente el AuthenticationProvider
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider) {
        return new ProviderManager(authenticationProvider);
    }

    //Eleccion del tipo de passwordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Por ejemplo, BCryptPasswordEncoder es una buena práctica
        return new BCryptPasswordEncoder();
    }


    // nos permite manejar usuarios desde el codigo Java
    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    //herencia de roles
    @Bean
    public RoleHierarchy roleHierarchy() {
        var hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy(
                "ROLE_BOSS > ROLE_CLIENT"
        );
        return hierarchy;
    }
}

