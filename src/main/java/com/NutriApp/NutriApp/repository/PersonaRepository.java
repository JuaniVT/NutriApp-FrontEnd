package com.NutriApp.NutriApp.repository;

import com.NutriApp.NutriApp.modelo.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends JpaRepository <Persona, Integer> {
    boolean existsByDni(String dni); //aca no hace falta especificar la query que se va a lanzar con este metodo ya que hay
    //algunos nombres de metodos como este caso que JPA los interpreta automaticamente.
    //Pero sino, se deberia de poner la firma del metodo y arriba la sentencia sql con @Query.
    //Y si la query es muy compleja se deberia de crear un DAO y delegarle el metodo


}
