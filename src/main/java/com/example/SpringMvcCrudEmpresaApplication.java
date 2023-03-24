package com.example;

import java.time.LocalDate;
import java.time.Month;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.entities.Correo;
import com.example.entities.Departamento;
import com.example.entities.Empleado;
import com.example.entities.Telefono;
import com.example.entities.Empleado.Genero;
import com.example.services.CorreoService;
import com.example.services.DepartamentoService;
import com.example.services.EmpleadoService;
import com.example.services.TelefonoService;

@SpringBootApplication
public class SpringMvcCrudEmpresaApplication implements CommandLineRunner{

	@Autowired
	private CorreoService correoService; 

	@Autowired
	private DepartamentoService departamentoService; 
	
	@Autowired
	private EmpleadoService empleadoService; 

	@Autowired
	private TelefonoService telefonoService; 



	public static void main(String[] args) {
		SpringApplication.run(SpringMvcCrudEmpresaApplication.class, args);
	}



	@Override
	public void run(String... args) throws Exception {
		
		departamentoService.save(Departamento.builder().nombre("RRHH").build());
		departamentoService.save(Departamento.builder().nombre("Contabilidad").build());
		departamentoService.save(Departamento.builder().nombre("Desarrollo").build());

		empleadoService.save(Empleado.builder()
		.id(1)
		.nombre("Celia")
		.apellidos("Cava Ruíz")
		.fechaAlta(LocalDate.of(2000, Month.JANUARY, 11))
		.genero(Genero.MUJER)
		.departamento(departamentoService.findById(1))
		.build()); 

		correoService.save(Correo.builder()
		.id(1)
		.email("celiacava@gmail.com")
		.empleado(empleadoService.findById(1))
		.build());

		telefonoService.save(Telefono.builder()
		.numero("689542134")
		.empleado(empleadoService.findById(1))
		.build());

		telefonoService.save(Telefono.builder()
		.numero("968120846")
		.empleado(empleadoService.findById(1))
		.build());
	}

}
