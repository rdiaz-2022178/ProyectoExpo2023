package org.rodrigodiaz.controller;

import com.mysql.jdbc.MysqlDataTruncation;
import java.awt.Toolkit;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.rodrigodiaz.bean.Plato;
import org.rodrigodiaz.bean.Servicio;
import org.rodrigodiaz.bean.ServiciosPlatos;
import org.rodrigodiaz.db.Conexion;
import org.rodrigodiaz.main.Principal;

public class ServicioPlatosController implements Initializable {

    private Principal escenarioPrincipal;

    private enum operaciones {
        GUARDAR, ACTUALIZAR, ELIMINAR, NINGUNO
    };
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<ServiciosPlatos> listaServiciosPlatos;
    private ObservableList<Servicio> listaServicios;
    private ObservableList<Plato> listaPlatos;

    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgReporte;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnReporte;
    @FXML
    private TextField txtCodigoServicioPlato;
    @FXML
    private ComboBox cbxCodigoPlato;
    @FXML
    private ComboBox cbxCodigoServicio;
    @FXML
    private TableView tblServiciosPlatos;
    @FXML
    private TableColumn colCodigoServicioPlato;
    @FXML
    private TableColumn colCodigoPlato;
    @FXML
    private TableColumn colCodigoServicio;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cbxCodigoPlato.setItems(getPlatos());
        cbxCodigoServicio.setItems(getServicios());
        btnEditar.setDisable(true);
        //btnEliminar.setDisable(true);
        cbxCodigoPlato.setDisable(true);
        cbxCodigoServicio.setDisable(true);
    }

    public ObservableList<Plato> getPlatos() {
        ArrayList<Plato> lista = new ArrayList<Plato>();
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarPlatos");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Plato(
                        resultado.getInt("codigoPlato"),
                        resultado.getInt("cantidad"),
                        resultado.getString("nombrePlato"),
                        resultado.getString("descripcionPlato"),
                        resultado.getDouble("precioPlato"),
                        resultado.getInt("codigoTipoPlato")));
            }
        } catch (Exception error) {
            error.printStackTrace();
        }

        return listaPlatos = FXCollections.observableArrayList(lista);
    }

    public Plato buscarPlato(int codigoPlato) {
        Plato resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_BuscarPlato(?)");
            procedimiento.setInt(1, codigoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Plato(registro.getInt("codigoPlato"),
                        registro.getInt("cantidad"),
                        registro.getString("nombrePlato"),
                        registro.getString("descripcionPlato"),
                        registro.getDouble("precioPlato"),
                        registro.getInt("codigoTipoPlato"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public ObservableList<ServiciosPlatos> getServicioPlatos() {
        ArrayList<ServiciosPlatos> lista = new ArrayList<ServiciosPlatos>();
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarServicios_has_Platos");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new ServiciosPlatos(
                        resultado.getInt("Servicios_codigoServicios"),
                        resultado.getInt("codigoPlato"),
                        resultado.getInt("codigoServicio")));
            }
        } catch (Exception error) {
            error.printStackTrace();
        }

        return listaServiciosPlatos = FXCollections.observableArrayList(lista);
    }

    public ObservableList<Servicio> getServicios() {
        ArrayList<Servicio> lista = new ArrayList<Servicio>();
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarServicios");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Servicio(
                        resultado.getInt("codigoServicio"),
                        resultado.getDate("fechaServicio"),
                        resultado.getString("tipoServicio"),
                        resultado.getString("horaServicio"),
                        resultado.getString("lugarServicio"),
                        resultado.getString("telefonoContacto"),
                        resultado.getInt("codigoEmpresa")));
            }
        } catch (Exception error) {
            error.printStackTrace();
        }

        return listaServicios = FXCollections.observableArrayList(lista);
    }

    public Servicio buscarServicio(int codigoServicio) {
        Servicio resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_BuscarServicio(?)");
            procedimiento.setInt(1, codigoServicio);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Servicio(
                        registro.getInt("codigoServicio"),
                        registro.getDate("fechaServicio"),
                        registro.getString("tipoServicio"),
                        registro.getString("horaServicio"),
                        registro.getString("lugarServicio"),
                        registro.getString("telefonoContacto"),
                        registro.getInt("codigoEmpresa"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public void cargarDatos() {
        tblServiciosPlatos.setItems(getServicioPlatos());
        colCodigoServicioPlato.setCellValueFactory(new PropertyValueFactory<ServiciosPlatos, Integer>("Servicios_codigoServicios"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<ServiciosPlatos, Integer>("codigoPlato"));
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServiciosPlatos, Integer>("codigoServicio"));
    }

    public void seleccionarElemento() {
        if (tipoDeOperacion == operaciones.GUARDAR) {
            JOptionPane.showMessageDialog(null, "No puedes seleccionar elementos", "AVISO", JOptionPane.WARNING_MESSAGE);
        } else {
            if (tblServiciosPlatos.getSelectionModel().getSelectedItem() == null) {
                JOptionPane.showMessageDialog(null, "Seleccione una fila correcta", "AVISO", JOptionPane.WARNING_MESSAGE);
            } else {
                txtCodigoServicioPlato.setText(String.valueOf(((ServiciosPlatos) tblServiciosPlatos.getSelectionModel().getSelectedItem()).getServicios_codigoServicios()));
                cbxCodigoPlato.getSelectionModel().select(buscarPlato(((ServiciosPlatos) tblServiciosPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
                cbxCodigoServicio.getSelectionModel().select(buscarServicio(((ServiciosPlatos) tblServiciosPlatos.getSelectionModel().getSelectedItem()).getCodigoServicio()));
            }
        }
    }

    public void eliminar() {
        switch (tipoDeOperacion) {
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("NUEVO         ");
                btnEliminar.setText("ELIMINAR     ");
                btnEditar.setDisable(true);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/rodrigodiaz/image/iconoAgregar.png"));
                imgEliminar.setImage(new Image("/org/rodrigodiaz/image/iconoEliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            case NINGUNO:
                if (tblServiciosPlatos.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Está seguro de eliminar el registro?", "Eliminar Servicio Plato", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EliminarServicio_has_Plato(?)");
                            procedimiento.setInt(1, ((ServiciosPlatos) tblServiciosPlatos.getSelectionModel().getSelectedItem()).getServicios_codigoServicios());
                            procedimiento.execute();
                            listaServiciosPlatos.remove(tblServiciosPlatos.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        } catch (SQLException error) {
                            Toolkit.getDefaultToolkit().beep();
                            JOptionPane.showMessageDialog(null, "No se puede Eliminar este registro", "AVISO", JOptionPane.ERROR_MESSAGE);
                        } catch (Exception error) {
                            error.printStackTrace();
                        }
                    } else {
                        limpiarControles();
                        cargarDatos();
                    }
                } else {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento", "AVISO", JOptionPane.WARNING_MESSAGE);
                }
                break;
        }
    }

    public void nuevo() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                limpiarControles();
                activarControles();
                cargarDatos();
                btnNuevo.setText("GUARDAR     ");
                btnEliminar.setText("CANCELAR     ");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/rodrigodiaz/image/iconoGuardar.png"));
                imgEliminar.setImage(new Image("/org/rodrigodiaz/image/iconoCancelar.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                break;

        }
    }

    public void guardar() {
        ServiciosPlatos registro = new ServiciosPlatos();
        try {
            if (cbxCodigoPlato.getSelectionModel().isEmpty() || cbxCodigoServicio.getSelectionModel().isEmpty() || txtCodigoServicioPlato.getText().trim().length() == 0) {
                JOptionPane.showMessageDialog(null, "Debe de rellenar todos los campos", "AVISO", JOptionPane.WARNING_MESSAGE);
            } else {
                registro.setServicios_codigoServicios(Integer.parseInt(txtCodigoServicioPlato.getText().trim()));
                registro.setCodigoPlato(((Plato) cbxCodigoPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
                registro.setCodigoServicio(((Servicio) cbxCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_AgregarServicio_has_Plato(?,?,?)");
                procedimiento.setInt(1, registro.getServicios_codigoServicios());
                procedimiento.setInt(2, registro.getCodigoPlato());
                procedimiento.setInt(3, registro.getCodigoServicio());
                procedimiento.execute();
                listaServiciosPlatos.add(registro);
                postGuardar();
            }
        } catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException error) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "El código ya existe", "AVISO", JOptionPane.WARNING_MESSAGE);
        } catch (NumberFormatException e) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Código incorrecto", "AVISO", JOptionPane.WARNING_MESSAGE);
        } catch (MysqlDataTruncation e) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Verifique el número de carácteres", "AVISO", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void reporte() {
        switch (tipoDeOperacion) {
            case ACTUALIZAR:
                limpiarControles();
                desactivarControles();
                btnEditar.setText("EDITAR         ");
                btnReporte.setText("CANCELAR     ");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/rodrigodiaz/image/iconoEditar.png"));
                imgReporte.setImage(new Image("/org/rodrigodiaz/image/iconoCancelar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
            case NINGUNO:
                limpiarControles();
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }

    public void postActualizar() {
        limpiarControles();
        desactivarControles();
        btnNuevo.setDisable(false);
        btnEliminar.setDisable(false);
        btnEditar.setText("EDITAR         ");
        btnReporte.setText("CANCELAR     ");
        imgEditar.setImage(new Image("/org/rodrigodiaz/image/iconoEditar.png"));
        imgReporte.setImage(new Image("/org/rodrigodiaz/image/iconoCancelar.png"));
        tipoDeOperacion = operaciones.NINGUNO;
        cargarDatos();
    }

    public void postGuardar() {
        limpiarControles();
        desactivarControles();
        btnNuevo.setText("NUEVO         ");
        btnEliminar.setText("ELIMINAR     ");
        btnEditar.setDisable(true);
        btnReporte.setDisable(false);
        imgNuevo.setImage(new Image("/org/rodrigodiaz/image/iconoAgregar.png"));
        imgEliminar.setImage(new Image("/org/rodrigodiaz/image/iconoEliminar.png"));
        tipoDeOperacion = operaciones.NINGUNO;
        cargarDatos();
    }

    public void limpiarControles() {
        txtCodigoServicioPlato.clear();
        cbxCodigoPlato.setValue(null);
        cbxCodigoServicio.setValue(null);

    }

    public void activarControles() {
        txtCodigoServicioPlato.setEditable(true);
        cbxCodigoPlato.setDisable(false);
        cbxCodigoServicio.setDisable(false);
    }

    public void desactivarControles() {
        txtCodigoServicioPlato.setEditable(false);
        cbxCodigoPlato.setDisable(true);
        cbxCodigoServicio.setDisable(true);
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }

}
