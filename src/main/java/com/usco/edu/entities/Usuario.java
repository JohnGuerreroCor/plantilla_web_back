package com.usco.edu.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Usuario implements Serializable {

    private int id;
    private String username;
    private String password;
    private String userdb;
    private Persona persona;
    private String role;
    private String horaInicioSesion;
    private boolean state;
    
    private static final long serialVersionUID = 1L;
    
}