package com.NutriApp.NutriApp;

import com.NutriApp.NutriApp.modelo.Persona;
import com.NutriApp.NutriApp.modelo.enums.Genero;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class NutriAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(NutriAppApplication.class, args);

//		Persona persona = Persona.builder()
//				.id(2)
//				.dni("21312313")
//				.email("ekianuruzuna@gmail.com")
//				.nombre("Ekian")
//				.genero(Genero.FEMENINO)
//				.direccion("Udine 1355")
//				.apellido("Uruzuna")
//				.telefono("2236826147")
//				.fechaNacimiento(LocalDate.now())
//				.build();
//
//		System.out.println(persona.getId());

	}

}
