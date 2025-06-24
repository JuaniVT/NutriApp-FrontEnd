package com.NutriApp.NutriApp.service;

import com.NutriApp.NutriApp.modelo.dto.MacronutrienteDTO;
import com.NutriApp.NutriApp.modelo.dto.ModificarCantidadComidaFavoritaDTO;
import com.NutriApp.NutriApp.exceptions.ComidaFavoritaException;
import com.NutriApp.NutriApp.exceptions.ComidaIngeridaException;
import com.NutriApp.NutriApp.modelo.ComidaFavorita;
import com.NutriApp.NutriApp.modelo.ComidaIngerida;
import com.NutriApp.NutriApp.modelo.Usuario;
import com.NutriApp.NutriApp.modelo.enums.TipoComida;
import com.NutriApp.NutriApp.repository.ComidaFavoritaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ComidaFavoritaService {
    private final ComidaFavoritaRepository comidaFavoritaRepository;
    private final NutricionService nutricionService;
    private final AlimentoIngresadoPorUsuarioService alimentoIngresadoPorUsuarioService;
    private final ComidaIngeridaService comidaIngeridaService;

    public void agregarComidaFavorita(String nombrePaquete, String nombreComida, long comidaId, double cantidad) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();

        MacronutrienteDTO dto = new MacronutrienteDTO();

        Optional<MacronutrienteDTO> optionalMacronutrienteDTO = alimentoIngresadoPorUsuarioService.obtenerMacronutrientes(nombreComida, comidaId);

        if (optionalMacronutrienteDTO.isEmpty()) {
            optionalMacronutrienteDTO = nutricionService.obtenerMacronutrientesPorId(comidaId);
        }

        if (!optionalMacronutrienteDTO.isEmpty() && optionalMacronutrienteDTO.get().getNombreComida().equals(nombreComida)) {

            boolean yaExiste = comidaFavoritaRepository.existsByNombrePaqueteAndComidaIdAndNombreComidaAndUsuario(
                    nombrePaquete, comidaId, optionalMacronutrienteDTO.get().getNombreComida(), usuario
            );

            if (yaExiste) {

                modificarCantidadComidaFavorita(new ModificarCantidadComidaFavoritaDTO(nombrePaquete, comidaId, cantidad));

            } else {
                ComidaFavorita favorita = new ComidaFavorita();
                favorita.setNombrePaquete(nombrePaquete);
                favorita.setNombreComida(nombreComida);
                favorita.setComidaId(comidaId);
                favorita.setCantidad(cantidad);
                favorita.setUsuario(usuario);

                comidaFavoritaRepository.save(favorita);
            }

        } else {
            throw new ComidaIngeridaException("No se encontro la comida con nombre: " + nombreComida);
        }

    }

    // Modificar solo la cantidad
    public void modificarCantidadComidaFavorita(ModificarCantidadComidaFavoritaDTO dto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();

        ComidaFavorita favorita = comidaFavoritaRepository.findByNombrePaqueteAndComidaIdAndUsuario(dto.getNombrePaquete(), dto.getIdComida(), usuario)
                .orElseThrow(() -> new ComidaIngeridaException("No se encontró en el paquete: " + dto.getNombrePaquete() + " el aliemento con id: " + dto.getIdComida()));

        favorita.setCantidad(favorita.getCantidad() + dto.getNuevaCantidad());
        comidaFavoritaRepository.save(favorita);
    }

    // Eliminar comida favorita
    public void eliminarComidaFavorita(String paquete, long idFavorita) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();

        ComidaFavorita favorita = comidaFavoritaRepository.findByNombrePaqueteAndComidaIdAndUsuario(paquete, idFavorita, usuario)
                .orElseThrow(() -> new ComidaIngeridaException("No se encontró en el paquete: + " + paquete + " el aliemento con id: " + idFavorita));


        comidaFavoritaRepository.delete(favorita);
    }

    // Listar comidas favoritas de un paquete para el usuario actual
    public List<ComidaFavorita> listarComidasFavoritasPorPaquete(String nombrePaquete) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();

        return comidaFavoritaRepository.findAllByNombrePaqueteAndUsuario(nombrePaquete, usuario);
    }

    public void agregarComidaFavoritaaIngerida(String nombrePaquete, TipoComida tipo, LocalDate fecha) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) auth.getPrincipal();

        // Buscar todas las comidas favoritas de este paquete para el usuario
        List<ComidaFavorita> favoritas = comidaFavoritaRepository.findAllByNombrePaqueteAndUsuario(nombrePaquete, usuario);

        if (favoritas.isEmpty()) {
            throw new ComidaFavoritaException("El paquete '" + nombrePaquete + "' no tiene comidas favoritas");
        }

        // Por cada comida favorita -> cargarla como comida ingerida
        for (ComidaFavorita favorita : favoritas) {

            comidaIngeridaService.agregarComidaIngerida(
                    favorita.getComidaId(),
                    favorita.getNombreComida(),
                    favorita.getCantidad(),
                    tipo,
                    fecha
            );
        }
    }
}
