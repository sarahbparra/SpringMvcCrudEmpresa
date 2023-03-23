package com.example.entities;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "empleados")

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Empleado implements Serializable{
    
    private static final long serialVersionUID = 1L; 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id; 

    @NotNull(message = "Campo no puede ser nulo.")

    private String nombre; 
    private String apellidos; 

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaAlta; 

    private Genero genero; 

    public enum Genero {
        HOMBRE, MUJER, OTRO
    }
    
}
