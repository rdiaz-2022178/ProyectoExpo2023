package org.rodrigodiaz.controller;

import com.jfoenix.controls.JFXTimePicker;
import com.mysql.jdbc.MysqlDataTruncation;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.awt.Toolkit;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.rodrigodiaz.bean.Empleado;
import org.rodrigodiaz.bean.Servicio;
import org.rodrigodiaz.bean.ServiciosEmpleados;
import org.rodrigodiaz.db.Conexion;
import org.rodrigodiaz.main.Principal;

public class ServiciosEmpleadosController implements Initializable {

    private Principal escenarioPrincipal;

    private enum operaciones {
        GUARDAR, ACTUALIZAR, ELIMINAR, NINGUNO
    };
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<ServiciosEmpleados> listaServiciosEmpleados;
    private ObservableList<Servicio> listaServicios;
    private ObservableList<Empleado> listaEmpleados;
    private DatePicker fecha;

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
    private TextField txtCodigoServicioEmpleado;
    @FXML
    private TextField txtLugarEvento;
    @FXML
    private ComboBox cbxCodigoServicio;
    @FXML
    private ComboBox cbxCodigoEmpleado;
    @FXML
    private JFXTimePicker dpHora;
    @FXML
    private TableView tblServiciosEmpleados;
    @FXML
    private TableColumn colCodigoServicioEmpleado;
    @FXML
    private TableColumn colCodigoServicio;
    @FXML
    private TableColumn colCodigoEmpleado;
    @FXML
    private TableColumn colFechaEvento;
    @FXML
    private TableColumn colHoraEvento;
    @FXML
    private TableColumn colLugarEvento;
    @FXML
    private GridPane grpContenedor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(true);
        fecha.getStylesheets().add("/org/rodrigodiaz/resource/CalendarStyle.css");
        fecha.setDisable(true);
        grpContenedor.add(fecha, 1, 3);
        fecha.setAlignment(Pos.CENTER);
        cargarDatos();
        cbxCodigoEmpleado.setItems(getEmpleados());
        cbxCodigoServicio.setItems(getServicios());
        cbxCodigoEmpleado.setDisable(true);
        cbxCodigoServicio.setDisable(true);
        //btnEliminar.setDisable(true);
        btnEditar.setDisable(true);
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

    public ObservableList<Empleado> getEmpleados() {
        ArrayList<Empleado> lista = new ArrayList<Empleado>();
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarEmpleados");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Empleado(
                        resultado.getInt("codigoEmpleado"),
                        resultado.getInt("numeroEmpleado"),
                        resultado.getString("apellidosEmpleado"),
                        resultado.getString("nombresEmpleado"),
                        resultado.getString("direccionEmpleado"),
                        resultado.getString("telefonoContacto"),
                        resultado.getString("gradoCocinero"),
                        resultado.getInt("codigoTipoEmpleado")));
            }
        } catch (Exception error) {
            error.printStackTrace();
        }

        return listaEmpleados = FXCollections.observableArrayList(lista);
    }

    public Empleado buscarEmpleado(int codigoEmpleado) {
        Empleado resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_BuscarEmpleado(?)");
            procedimiento.setInt(1, codigoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Empleado(
                        registro.getInt("codigoEmpleado"),
                        registro.getInt("numeroEmpleado"),
                        registro.getString("apellidosEmpleado"),
                        registro.getString("nombresEmpleado"),
                        registro.getString("direccionEmpleado"),
                        registro.getString("telefonoContacto"),
                        registro.getString("gradoCocinero"),
                        registro.getInt("codigoTipoEmpleado"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public ObservableList<ServiciosEmpleados> getServiciosEmpleados() {
        ArrayList<ServiciosEmpleados> lista = new ArrayList<ServiciosEmpleados>();
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarServicios_has_Empleados");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new ServiciosEmpleados(
                        resultado.getInt("Servicios_codigoServicio"),
                        resultado.getInt("codigoServicio"),
                        resultado.getInt("codigoempleado"),
                        resultado.getDate("fechaEvento"),
                        resultado.getString("horaEvento"),
                        resultado.getString("lugarEvento")));
            }
        } catch (Exception error) {
            error.printStackTrace();
        }

        return listaServiciosEmpleados = FXCollections.observableArrayList(lista);
    }

    public void cargarDatos() {
        tblServiciosEmpleados.setItems(getServiciosEmpleados());
        colCodigoServicioEmpleado.setCellValueFactory(new PropertyValueFactory<ServiciosEmpleados, Integer>("Servicios_codigoServicio"));
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<ServiciosEmpleados, Integer>("codigoServicio"));
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<ServiciosEmpleados, Integer>("codigoempleado"));
        colFechaEvento.setCellValueFactory(new PropertyValueFactory<ServiciosEmpleados, Date>("fechaEvento"));
        colHoraEvento.setCellValueFactory(new PropertyValueFactory<ServiciosEmpleados, String>("horaEvento"));
        colLugarEvento.setCellValueFactory(new PropertyValueFactory<ServiciosEmpleados, String>("lugarEvento"));
    }

    public void seleccionarElemento() {
        if (tipoDeOperacion == operaciones.GUARDAR) {
            JOptionPane.showMessageDialog(null, "No puedes seleccionar elementos", "AVISO", JOptionPane.WARNING_MESSAGE);
        } else {
            if (tblServiciosEmpleados.getSelectionModel().getSelectedItem() == null) {
                JOptionPane.showMessageDialog(null, "Seleccione una fila correcta", "AVISO", JOptionPane.WARNING_MESSAGE);
            } else {
                txtCodigoServicioEmpleado.setText(String.valueOf(((ServiciosEmpleados) tblServiciosEmpleados.getSelectionModel().getSelectedItem()).getServicios_codigoServicio()));
                cbxCodigoEmpleado.getSelectionModel().select(buscarEmpleado(((ServiciosEmpleados) tblServiciosEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
                cbxCodigoServicio.getSelectionModel().select(buscarServicio(((ServiciosEmpleados) tblServiciosEmpleados.getSelectionModel().getSelectedItem()).getCodigoServicio()));
                fecha.selectedDateProperty().set(((ServiciosEmpleados) tblServiciosEmpleados.getSelectionModel().getSelectedItem()).getFechaEvento());
                dpHora.setValue(LocalTime.parse((((ServiciosEmpleados) tblServiciosEmpleados.getSelectionModel().getSelectedItem())).getHoraEvento()));
                txtLugarEvento.setText(((ServiciosEmpleados) tblServiciosEmpleados.getSelectionModel().getSelectedItem()).getLugarEvento());

            }
        }
    }

    public void nuevo() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                limpiarControles();
                cargarDatos();
                cbxCodigoServicio.setOnAction(obtenerDatos());
                activarControles();
                btnNuevo.setText("GUARDAR     ");
                btnEliminar.setText("CANCELAR     ");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/rodrigodiaz/image/iconoGuardar.png"));
                imgEliminar.setImage(new Image("/org/rodrigodiaz/image/iconoCancelar.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                limpiarControles();
                break;
            case GUARDAR:
                guardar();
                break;
        }
    }

    public void guardar() {
        ServiciosEmpleados registro = new ServiciosEmpleados();
        try {
            if (txtCodigoServicioEmpleado.getText().trim().length() == 0 || cbxCodigoEmpleado.getSelectionModel().isEmpty() || cbxCodigoServicio.getSelectionModel().isEmpty() || txtLugarEvento.getText().trim().length() == 0) {
                JOptionPane.showMessageDialog(null, "Debe de rellenar todos los campos", "AVISO", JOptionPane.WARNING_MESSAGE);
            } else {
                registro.setServicios_codigoServicio(Integer.parseInt(txtCodigoServicioEmpleado.getText().trim()));
                registro.setCodigoServicio(((Servicio) cbxCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
                registro.setCodigoEmpleado(((Empleado) cbxCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                registro.setFechaEvento(fecha.getSelectedDate());
                registro.setHoraEvento(String.valueOf(dpHora.getValue()));
                registro.setLugarEvento(txtLugarEvento.getText().trim());
                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_AgregarServicio_has_Empleado(?,?,?,?,?,?)");
                procedimiento.setInt(1, registro.getServicios_codigoServicio());
                procedimiento.setInt(2, registro.getCodigoServicio());
                procedimiento.setInt(3, registro.getCodigoEmpleado());
                procedimiento.setDate(4, new java.sql.Date(registro.getFechaEvento().getTime()));
                procedimiento.setString(5, registro.getHoraEvento());
                procedimiento.setString(6, registro.getLugarEvento());
                procedimiento.execute();
                listaServiciosEmpleados.add(registro);
                postGuardar();
            }
        } catch (MysqlDataTruncation error) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Verifique el número de carácteres", "AVISO", JOptionPane.WARNING_MESSAGE);
        } catch (NumberFormatException error) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Valor Incorrecto", "AVISO", JOptionPane.WARNING_MESSAGE);
        } catch (NullPointerException error) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Debe de rellenar todos los campos", "AVISO", JOptionPane.WARNING_MESSAGE);
        } catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException error) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "El código ya existe", "AVISO", JOptionPane.WARNING_MESSAGE);
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public void eliminar() {
        switch (tipoDeOperacion) {
            case GUARDAR:
                cbxCodigoServicio.setOnAction(null);
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
                if (tblServiciosEmpleados.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Está seguro de eliminar el registro?", "Eliminar Servicios empleados", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EliminarServicio_has_Empleado(?)");
                            procedimiento.setInt(1, ((ServiciosEmpleados) tblServiciosEmpleados.getSelectionModel().getSelectedItem()).getServicios_codigoServicio());
                            procedimiento.execute();
                            listaServiciosEmpleados.remove(tblServiciosEmpleados.getSelectionModel().getSelectedIndex());
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

    public EventHandler<ActionEvent> obtenerDatos() {
        EventHandler<ActionEvent> evento = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Servicio idLugar = buscarServicio(((Servicio) cbxCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
                txtLugarEvento.setText(idLugar.getLugarServicio());
                fecha.selectedDateProperty().set(idLugar.getFechaServicio());
                dpHora.setValue(LocalTime.parse(idLugar.getHoraServicio()));
            }
        };

        return evento;
    }

    public void limpiarControles() {
        txtCodigoServicioEmpleado.clear();
        txtLugarEvento.clear();
        cbxCodigoEmpleado.setValue(null);
        cbxCodigoServicio.setValue(null);
        dpHora.setValue(null);
        fecha.setSelectedDate(null);
    }

    public void activarControles() {
        txtCodigoServicioEmpleado.setEditable(true);
        txtLugarEvento.setEditable(false);
        cbxCodigoEmpleado.setDisable(false);
        cbxCodigoServicio.setDisable(false);
        fecha.setDisable(true);
        dpHora.setDisable(true);

    }

    public void desactivarControles() {
        txtCodigoServicioEmpleado.setEditable(false);
        txtLugarEvento.setEditable(false);
        cbxCodigoEmpleado.setDisable(true);
        cbxCodigoServicio.setDisable(true);
        fecha.setDisable(true);
        dpHora.setDisable(false);
    }

    public void postGuardar() {
        cbxCodigoServicio.setOnAction(null);
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

    public void reporte() {
        switch (tipoDeOperacion) {
            case GUARDAR:
                cbxCodigoServicio.setOnAction(null);
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
                cbxCodigoServicio.setOnAction(null);
                limpiarControles();
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
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
