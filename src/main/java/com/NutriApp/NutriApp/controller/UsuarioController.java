package com.NutriApp.NutriApp.controller;

import com.NutriApp.NutriApp.modelo.dto.PerfilNutricionalDTO;
import com.NutriApp.NutriApp.modelo.dto.PersonaDTO;
import com.NutriApp.NutriApp.modelo.dto.UsuarioDTO;
import com.NutriApp.NutriApp.exceptions.AuthorityInvalidaException;
import com.NutriApp.NutriApp.exceptions.PersonaInvalidaException;
import com.NutriApp.NutriApp.exceptions.UsuarioInvalidoException;
import com.NutriApp.NutriApp.modelo.Usuario;
import com.NutriApp.NutriApp.service.PerfilNutricionalService;
import com.NutriApp.NutriApp.service.PersonaService;
import com.NutriApp.NutriApp.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Validated
@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final PersonaService personaService;
    private final PerfilNutricionalService perfilNutricionalService;

    @PostMapping("/registro")
    public ResponseEntity<String> registrarUsuarioCliente(@RequestBody @Validated Usuario usuario, @RequestParam int idPersonaBuscar) throws PersonaInvalidaException, UsuarioInvalidoException, AuthorityInvalidaException {
        usuarioService.insertarUsuarioCliente(usuario, idPersonaBuscar);
        return ResponseEntity.ok("Usuario registrado correctamente como cliente");
    }

    @PostMapping("/modificarContraseña")
    public ResponseEntity<String> actualizarUsuario(@RequestBody UsuarioDTO nuevo) {
        usuarioService.actualizarContraseñaUsuario(nuevo);
        return ResponseEntity.ok("Se actualizaron correctamente los cambios");
    }

    @DeleteMapping("/eliminarCuenta")
    public ResponseEntity<String> eliminarCuentaPropia() {
        boolean eliminado = usuarioService.eliminarCuentaActual();

        if (eliminado) {
            return ResponseEntity.ok("Cuenta eliminada correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontro el usuario");
        }
    }

    @DeleteMapping("/eliminar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> eliminarCuentaPorAdmin(@RequestParam String username) {
        boolean eliminado = usuarioService.eliminarCuentaPorUsername(username);

        if (eliminado) {
            return ResponseEntity.ok("Usuario eliminado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontro el usuario con username: " + username);
        }
    }

    @GetMapping("/listarClientes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listarClientes() {
        List<Map<String, Object>> clientes = usuarioService.listarClientes();

        if (clientes.isEmpty()) {
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", "La lista de clientes está vacía");
            return ResponseEntity.ok(respuesta);
        }
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/filtrarClientes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> filtrarClientes(@RequestParam String filtro) {
        List<Map<String, Object>> clientes = usuarioService.filtrarClientes(filtro);

        if (clientes.isEmpty()){
            throw new UsuarioInvalidoException("No hay usuarios registrados con el username = " + filtro);
        }

        return ResponseEntity.ok(clientes);
    }
}
