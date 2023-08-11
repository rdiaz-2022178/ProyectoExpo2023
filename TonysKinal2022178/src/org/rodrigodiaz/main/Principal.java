/**
 *  Nombre: Rodrigo Emmanuel Díaz García
 *  Carné: 2022178
 *  Código Técnico: IN5AM
 *  Fecha de creación: 28/03/2023
 *  Fechas de Modificación:
 *      11/04/2023: Creamos la vista Principal y la de programador con sus controladores respectivos
 *      12/04/2023: Creamos el método poder cambiar varias escenas y usar varias con multiherencia
 *      17/04/2023: Creamos y mostramos la View e Producto
 *      18/04/2023: Creamos la View de producto con sus respectivos objetos de interacción y lo instanciamos en el menuPrincipal
 *                          también creamos el método para cargar los datos al tableView de Empresa
 *      19/04/2023: Terminamos de crear las funciones de CRUD de la entidad Empresa y Producto
 *      20/04/2023: Colocamos los objetos respectivos en las vistas TipoPlato y TipoEmpleado y su respectivo CRUD
 *      22/04/2023: Programamos las validaciones de Empresa y producto para que
 *                      - No haya campos vacíos
 *                      - para que acepte el formato de número de teléfono
 *                      - para que no sobrepase los caracteres de la BD
 *                      - para eliminar los espacios en blanco del principio
 *      24/04/2023: corregimos algunas validaciones 
 *      25/04/2023: creamos las validaciones para que se haya problema al eliminar algun dato
 *      26/04/2023: creamos las vistas de Presupuesto, Servicio, Empleado y Plato y creamos sus
 *                  objetos para que el usuario pueda interactuar
 *      9/05/2023: creamos las vistas de Servicios_has_plato y productos_has_platos 
 *      10/05/2023: creamos la vista de Servicios_has_Empleados, y tambien creamos el crud completo de la vista de Plato
 *      11/05/2023: le damos funcionalidad a todas las vistas faltantes y hacemos uso del JFoenix para el DatePicker
 *      13/05/2023: validamos algunos aspectos faltantes
 *      23/05/2023: creamos la plantilla del reporte de Empresa 
 *      30/05/2023: logramos hacer la ruta relativa de las imagenes de los reportes y subreportes y creamos el reporte presupuesto 
 *                      y hacer que se levante desde java
 *      31/05/2023: Creamos las entidades de Usuario y Login para el respectivo inicio de sesión
 *                      y luego lo respectivo en java y creamos la loginca del login y para agregar un usuario
 *      01/06/2023: encriptamos la contraseña para que no sea vsible en la base de datos y agregamos un usuario
 */

package org.rodrigodiaz.main;

import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.layout.AnchorPane;
import org.rodrigodiaz.controller.EmpleadoController;
import org.rodrigodiaz.controller.EmpresaController;
import org.rodrigodiaz.controller.LoginController;
import org.rodrigodiaz.controller.MenuPrincipalController;
import org.rodrigodiaz.controller.PlatoController;
import org.rodrigodiaz.controller.PresupuestoController;
import org.rodrigodiaz.controller.ProductoController;
import org.rodrigodiaz.controller.ProductoPlatoController;
import org.rodrigodiaz.controller.ProgramadorController;
import org.rodrigodiaz.controller.ServicioPlatosController;
import org.rodrigodiaz.controller.ServiciosController;
import org.rodrigodiaz.controller.ServiciosEmpleadosController;
import org.rodrigodiaz.controller.TipoEmpleadoController;
import org.rodrigodiaz.controller.TipoPlatoController;
import org.rodrigodiaz.controller.UsuarioController;

public class Principal extends Application {

    private final String PAQUETE_VISTA = "/org/rodrigodiaz/view/";
    private Stage escenarioPrincipal;
    private Scene escena;

    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Tony's Kinal 2023");
        escenarioPrincipal.getIcons().add(new Image("/org/rodrigodiaz/image/logoSalchichaTonysSF.png"));
        // Parent root = FXMLLoader.load(getClass().getResource("/org/rodrigodiaz/view/MenuPrincipalView.fxml"));
        // Scene escena = new Scene(root);
        // escenarioPrincipal.setScene(escena);
        login();
        escenarioPrincipal.show();
    }

    @FXML
    public void menuPrincipal() {
        try {
            MenuPrincipalController menu = (MenuPrincipalController) cambiarEscena("MenuPrincipalView.fxml", 800, 540);
            menu.setEscenarioPrincipal(this);
            escenarioPrincipal.centerOnScreen();
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public void ventanaProgramador() {
        try {
            ProgramadorController programador = (ProgramadorController) cambiarEscena("ProgramadorView.fxml", 800, 400);
            programador.setEscenarioPrincipal(this);
            escenarioPrincipal.centerOnScreen();
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public void ventanaEmpresa() {
        try {
            EmpresaController empresa = (EmpresaController) cambiarEscena("EmpresaView.fxml", 1500, 800);
            empresa.setEscenarioPrincipal(this);
            escenarioPrincipal.centerOnScreen();
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public void ventanaProducto() {
        try {
            ProductoController producto = (ProductoController) cambiarEscena("ProductoView.fxml", 1500, 800);
            producto.setEscenarioPrincipal(this);
            escenarioPrincipal.centerOnScreen();
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public void ventanaTipoEmpleado() {
        try {
            TipoEmpleadoController tipoEmpleado = (TipoEmpleadoController) cambiarEscena("TipoEmpleadoView.fxml", 1500, 800);
            tipoEmpleado.setEscenarioPrincipal(this);
            escenarioPrincipal.centerOnScreen();
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public void ventanaTipoPlato() {
        try {
            TipoPlatoController tipoPlato = (TipoPlatoController) cambiarEscena("TipoPlatoView.fxml", 1500, 800);
            tipoPlato.setEscenarioPrincipal(this);
            escenarioPrincipal.centerOnScreen();
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public void ventanaPresupuesto() {
        try {
            PresupuestoController presupuesto = (PresupuestoController) cambiarEscena("PresupuestoView.fxml", 1500, 800);
            presupuesto.setEscenarioPrincipal(this);
            escenarioPrincipal.centerOnScreen();
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public void ventanaPlato() {
        try {
            PlatoController plato = (PlatoController) cambiarEscena("PlatoView.fxml", 1500, 800);
            plato.setEscenarioPrincipal(this);
            escenarioPrincipal.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaEmpleado() {
        try {
            EmpleadoController empleado = (EmpleadoController) cambiarEscena("EmpleadoView.fxml", 1500, 800);
            empleado.setEscenarioPrincipal(this);
            escenarioPrincipal.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaServicios() {
        try {
            ServiciosController servicios = (ServiciosController) cambiarEscena("ServiciosView.fxml", 1500, 800);
            servicios.setEscenarioPrincipal(this);
            escenarioPrincipal.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaProductoPlato(){
        try {
            ProductoPlatoController pt = (ProductoPlatoController)cambiarEscena("ProductosPlatoView.fxml", 1500, 800);
            pt.setEscenarioPrincipal(this);
            escenarioPrincipal.centerOnScreen();
        } catch (Exception error) {
            error.printStackTrace();
        }
    }
    
    public void ventanaServicioPlato(){
        try {
            ServicioPlatosController  sp = (ServicioPlatosController)cambiarEscena("ServiciosPlatoView.fxml", 1500, 800);
            sp.setEscenarioPrincipal(this);
            escenarioPrincipal.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    public void ventanaServiciosEmpleados(){
        try {
            ServiciosEmpleadosController se = (ServiciosEmpleadosController)cambiarEscena("ServiciosEmpleado.fxml", 1500, 800);
            se.setEscenarioPrincipal(this);
            escenarioPrincipal.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void login(){
        try {
            LoginController login = (LoginController)cambiarEscena("LoginVista.fxml", 600, 400);
            login.setEscenarioPrincipal(this);
            escenarioPrincipal.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaUsuario(){
        try {
            UsuarioController usuario = (UsuarioController)cambiarEscena("UsuarioView.fxml", 1500, 800);
            usuario.setEscenarioPrincipal(this);
            escenarioPrincipal.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception {
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA + fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA + fxml));
        escena = new Scene((AnchorPane) cargadorFXML.load(archivo), ancho, alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable) cargadorFXML.getController();
        return resultado;
    }

}
