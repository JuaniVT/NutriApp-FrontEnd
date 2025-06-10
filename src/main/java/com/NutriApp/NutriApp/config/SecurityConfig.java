package com.NutriApp.NutriApp.config;

import com.NutriApp.NutriApp.exceptions.Handlers.AccesDeniedExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/usuario/registro").permitAll() // <- Permitir acceso sin login
                        .requestMatchers("/api/persona/listar").permitAll()
                        .requestMatchers("/api/persona/obtener").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )

                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accesDeniedExceptionHandler)       //maneja la exception de acceso denegado aca
                                                                                //porque antes que llegue a globalExceptionHandler
                                                                                //sea catchea antes entonces hay que manejarla aca
                )

                .formLogin(Customizer.withDefaults())   //Body: x-www-form-urlencoded username:admin password:admin123
                .logout(Customizer.withDefaults());

        return http.build();
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
