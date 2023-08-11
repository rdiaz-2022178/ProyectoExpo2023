package org.rodrigodiaz.bean;

public class ProductosPlatos {

    private int Productos_codigoProducto;
    private int codigoProducto;
    private int codigoPlato;

    public ProductosPlatos(int Productos_codigoProducto, int codigoProducto, int codigoPlato) {
        this.Productos_codigoProducto = Productos_codigoProducto;
        this.codigoProducto = codigoProducto;
        this.codigoPlato = codigoPlato;
    }

    public ProductosPlatos() {
    }

    public int getProductos_codigoProducto() {
        return Productos_codigoProducto;
    }

    public void setProductos_codigoProducto(int Productos_codigoProducto) {
        this.Productos_codigoProducto = Productos_codigoProducto;
    }

    public int getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(int codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public int getCodigoPlato() {
        return codigoPlato;
    }

    public void setCodigoPlato(int codigoPlato) {
        this.codigoPlato = codigoPlato;
    }
    
    





}
