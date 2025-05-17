package com.example.kindersoft.controlador;

import com.example.kindersoft.modelo.entidades.Usuario;

import java.util.List;
import java.util.function.Consumer;

public class ControladorUsuario {

    private static ControladorUsuario instance;
    private final ControladorUsuario userService = ControladorUsuario.getInstance();

    private ControladorUsuario() {}

    public static ControladorUsuario getInstance(){
        if (instance == null){
            instance = new ControladorUsuario();
        }
        return instance;
    }


}
