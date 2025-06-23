package com.NutriApp.NutriApp.service;

import com.NutriApp.NutriApp.exceptions.AlimetoIngreadoPorElUsuarioException;
import com.NutriApp.NutriApp.modelo.AlimentoIngresadoPorUsuario;
import com.NutriApp.NutriApp.modelo.SolicitudAltaAlimento;
import com.NutriApp.NutriApp.repository.AlimentoIngresadoPorUsuarioRepository;
import com.NutriApp.NutriApp.repository.SolicitudRespository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlimentoIngresadoPorUsuarioService {

    @Autowired
    private AlimentoIngresadoPorUsuarioRepository alimentoRepository;
    @Autowired
    private SolicitudRespository solicitudRespository;

    public boolean existsByNombre (String nombre){
        return alimentoRepository.existsByNombreComidaIgnoreCase(nombre);
    }

    public boolean existsById (long id){
        return alimentoRepository.existsById(id);
    }

    @Transactional
    public void insertarBasandoseEnSolicitud (SolicitudAltaAlimento solicitudAltaAlimento) throws AlimetoIngreadoPorElUsuarioException {
        //Creamos el alimneto en base a una solicitud
        AlimentoIngresadoPorUsuario alimento = AlimentoIngresadoPorUsuario.builder()
                .nombreComida(solicitudAltaAlimento.getNombreComida())
                .porcion(solicitudAltaAlimento.getPorcion())
                .calorias(solicitudAltaAlimento.getCalorias())
                .proteinas(solicitudAltaAlimento.getProteinas())
                .grasas(solicitudAltaAlimento.getGrasas())
                .carbohidratos(solicitudAltaAlimento.getCarbohidratos())
                .build();

        //validacion extra por las dudas (se supone que no se puede cargar una solicitud con algun dato nulo)
        if (!alimento.esValido()){
            throw new AlimetoIngreadoPorElUsuarioException("Datos nulos");
        }

        //se guarda en la bdd el alimento
        alimentoRepository.save(alimento);
    }


}
