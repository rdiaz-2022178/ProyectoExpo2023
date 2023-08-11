package org.rodrigodiaz.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.rodrigodiaz.main.Principal;

public class MenuPrincipalController implements Initializable {

    private Principal escenarioPrincipal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void ventanaProgramador() {
        escenarioPrincipal.ventanaProgramador();
    }

    public void ventanaEmpresa() {
        escenarioPrincipal.ventanaEmpresa();
    }

    public void ventanaProducto() {
        escenarioPrincipal.ventanaProducto();
    }

    public void ventanaTipoEmpleado() {
        escenarioPrincipal.ventanaTipoEmpleado();
    }

    public void ventanaTipoPlato() {
        escenarioPrincipal.ventanaTipoPlato();
    }

    public void ventanaProductoPlato() {
        escenarioPrincipal.ventanaProductoPlato();
    }

    public void ventanaServicioPlato() {
        escenarioPrincipal.ventanaServicioPlato();
    }
    
    public void ventanaServiciosEmpleados(){
        escenarioPrincipal.ventanaServiciosEmpleados();
    }
    
    public void login(){
        escenarioPrincipal.login();
    }
    
    public void ventanaUsuario(){
        escenarioPrincipal.ventanaUsuario();
    }
}
