package org.rodrigodiaz.bean;

import javax.swing.JOptionPane;

public class ServiciosPlatos {
    private int Servicios_codigoServicios;
    private int codigoPlato;
    private int codigoServicio;

    public ServiciosPlatos() {
    }

    public ServiciosPlatos(int Servicios_codigoServicios, int codigoPlato, int codigoServicio) {
        this.Servicios_codigoServicios = Servicios_codigoServicios;
        this.codigoPlato = codigoPlato;
        this.codigoServicio = codigoServicio;
    }

    public int getServicios_codigoServicios() {
        return Servicios_codigoServicios;
    }

    public void setServicios_codigoServicios(int Servicios_codigoServicios) {
        this.Servicios_codigoServicios = Servicios_codigoServicios;
    }

    public int getCodigoPlato() {
        return codigoPlato;
    }

    public void setCodigoPlato(int codigoPlato) {
        this.codigoPlato = codigoPlato;
    }

    public int getCodigoServicio() {
        return codigoServicio;
    }

    public void setCodigoServicio(int codigoServicio) {
        this.codigoServicio = codigoServicio;
    }
    

    
}
