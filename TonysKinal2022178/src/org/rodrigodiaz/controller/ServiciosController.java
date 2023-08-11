package org.rodrigodiaz.controller;

import com.jfoenix.controls.JFXTimePicker;
import com.mysql.jdbc.MysqlDataTruncation;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.awt.Toolkit;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import org.rodrigodiaz.bean.Empresa;
import org.rodrigodiaz.bean.Servicio;
import org.rodrigodiaz.db.Conexion;
import org.rodrigodiaz.main.Principal;
import org.rodrigodiaz.report.GenerarReporte;

public class ServiciosController implements Initializable {

    private Principal escenarioPrincipal;

    private enum operaciones {
        GUARDAR, ACTUALIZAR, ELIMINAR, NINGUNO
    };
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Servicio> listaServicios;
    private ObservableList<Empresa> listaEmpresa;
    private DatePicker fecha;

    @FXML
    private TextField txtCodigoServicio;
    @FXML
    private TextField txtTipoServicio;
    @FXML
    private TextField txtLugar;
    @FXML
    private TextField txtTelefono;
    @FXML
    private JFXTimePicker dtHora;
    @FXML
    private ComboBox cbxCodigoEmpresa;
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
    private GridPane grpContenedor;
    @FXML
    private TableView tblServicios;
    @FXML
    private TableColumn colCodigoServicio;
    @FXML
    private TableColumn colFecha;
    @FXML
    private TableColumn colTipo;
    @FXML
    private TableColumn colHora;
    @FXML
    private TableColumn colLugar;
    @FXML
    private TableColumn colTelefono;
    @FXML
    private TableColumn colCodigoEmpresa;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(true);
        fecha.getStylesheets().add("/org/rodrigodiaz/resource/CalendarStyle.css");
        fecha.setDisable(true);
        cbxCodigoEmpresa.setDisable(true);
        grpContenedor.add(fecha, 1, 1);
        fecha.setAlignment(Pos.CENTER);
        cbxCodigoEmpresa.setItems(getEmpresa());
        dtHora.setDisable(true);
        dtHora.setEditable(false);

    }

    public void cargarDatos() {
        tblServicios.setItems(getServicios());
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicio, Integer>("codigoServicio"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Servicio, Date>("fechaServicio"));
        colTipo.setCellValueFactory(new PropertyValueFactory<Servicio, String>("tipoServicio"));
        colHora.setCellValueFactory(new PropertyValueFactory<Servicio, Time>("horaServicio"));
        colLugar.setCellValueFactory(new PropertyValueFactory<Servicio, String>("lugarServicio"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Servicio, String>("telefonoContacto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Servicio, Integer>("codigoEmpresa"));
    }

    public void seleccionarElemento() {
        if (tipoDeOperacion == operaciones.GUARDAR) {
            JOptionPane.showMessageDialog(null, "No puedes seleccionar elementos", "AVISO", JOptionPane.WARNING_MESSAGE);
        } else {
            if (tblServicios.getSelectionModel().getSelectedItem() == null) {
                JOptionPane.showMessageDialog(null, "Seleccione una fila correcta", "AVISO", JOptionPane.WARNING_MESSAGE);
            } else {
                txtCodigoServicio.setText(String.valueOf(((Servicio) tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio()));
                fecha.selectedDateProperty().set(((Servicio) tblServicios.getSelectionModel().getSelectedItem()).getFechaServicio());
                txtTipoServicio.setText(String.valueOf(((Servicio) tblServicios.getSelectionModel().getSelectedItem()).getTipoServicio()));
                dtHora.setValue(LocalTime.parse((((Servicio) tblServicios.getSelectionModel().getSelectedItem())).getHoraServicio()));
                txtLugar.setText(String.valueOf(((Servicio) tblServicios.getSelectionModel().getSelectedItem()).getLugarServicio()));
                txtTelefono.setText(String.valueOf(((Servicio) tblServicios.getSelectionModel().getSelectedItem()).getTelefonoContacto()));
                cbxCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Servicio) tblServicios.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));

            }
        }
    }

    public Empresa buscarEmpresa(int codigoEmpresa) {
        Empresa resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_BuscarEmpresa(?)");
            procedimiento.setInt(1, codigoEmpresa);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Empresa(registro.getInt("codigoEmpresa"),
                        registro.getString("nombreEmpresa"),
                        registro.getString("direccion"),
                        registro.getString("telefono"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
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
        Servicio registro = new Servicio();
        try {
            if (txtTipoServicio.getText().trim().length() == 0 || txtLugar.getText().trim().length() == 0 || txtTelefono.getText().trim().length() == 0 || cbxCodigoEmpresa.getSelectionModel().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe de rellenar todos los campos", "AVISO", JOptionPane.WARNING_MESSAGE);
            } else {
                registro.setFechaServicio(fecha.getSelectedDate());
                registro.setTipoServicio(txtTipoServicio.getText().trim());
                registro.setHoraServicio(String.valueOf(dtHora.getValue()));
                registro.setLugarServicio(txtLugar.getText().trim());
                if (txtTelefono.getText().trim().matches("\\d{8}")) {
                    registro.setTelefonoContacto(txtTelefono.getText().trim());
                    registro.setCodigoEmpresa(((Empresa) cbxCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
                    PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_AgregarServicio(?,?,?,?,?,?)");
                    procedimiento.setDate(1, new java.sql.Date(registro.getFechaServicio().getTime()));
                    procedimiento.setString(2, registro.getTipoServicio());
                    procedimiento.setString(3, registro.getHoraServicio());
                    procedimiento.setString(4, registro.getLugarServicio());
                    procedimiento.setString(5, registro.getTelefonoContacto());
                    procedimiento.setInt(6, registro.getCodigoEmpresa());
                    procedimiento.execute();
                    listaServicios.add(registro);
                    postGuardar();
                } else {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Teléfono Incorrecto", "ERROR", JOptionPane.WARNING_MESSAGE);
                }
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
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public void eliminar() {
        switch (tipoDeOperacion) {
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("NUEVO         ");
                btnEliminar.setText("ELIMINAR     ");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/rodrigodiaz/image/iconoAgregar.png"));
                imgEliminar.setImage(new Image("/org/rodrigodiaz/image/iconoEliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            case NINGUNO:
                if (tblServicios.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Está seguro de eliminar el registro?", "Eliminar Servicio", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EliminarPresupuesto(?)");
                            procedimiento.setInt(1, ((Servicio) tblServicios.getSelectionModel().getSelectedItem()).getCodigoServicio());
                            procedimiento.execute();
                            listaServicios.remove(tblServicios.getSelectionModel().getSelectedIndex());
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

    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblServicios.getSelectionModel().getSelectedItem() != null) {
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("ACTUALIZAR    ");
                    btnReporte.setText("CANCELAR     ");
                    imgEditar.setImage(new Image("/org/rodrigodiaz/image/iconoActualizar.png"));
                    imgReporte.setImage(new Image("/org/rodrigodiaz/image/iconoCancelar.png"));
                    activarControles();
                    cbxCodigoEmpresa.setDisable(true);
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                } else {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento", "AVISO", JOptionPane.WARNING_MESSAGE);
                }
                break;

            case ACTUALIZAR:
                actualizar();
                break;
        }
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EditarServicio(?,?,?,?,?,?)");
            Servicio registro = (Servicio) tblServicios.getSelectionModel().getSelectedItem();
            registro.setFechaServicio(fecha.getSelectedDate());
            registro.setTipoServicio(txtTipoServicio.getText().trim());
            registro.setHoraServicio(String.valueOf(dtHora.getValue()));
            registro.setLugarServicio(txtLugar.getText().trim());
            registro.setTelefonoContacto(txtTelefono.getText().trim());
            if (registro.getFechaServicio().toString().isEmpty() || registro.getTipoServicio().length() == 0 || registro.getHoraServicio().length() == 0 || registro.getLugarServicio().length() == 0 || registro.getTelefonoContacto().length() == 0 || registro.getCodigoEmpresa() == 0) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "Debe de rellenar todos los campos", "AVISO", JOptionPane.WARNING_MESSAGE);
            } else {
                procedimiento.setInt(1, registro.getCodigoServicio());
                procedimiento.setDate(2, new java.sql.Date(registro.getFechaServicio().getTime()));
                procedimiento.setString(3, registro.getTipoServicio());
                procedimiento.setString(4, registro.getHoraServicio());
                procedimiento.setString(5, registro.getLugarServicio());
                if (txtTelefono.getText().trim().matches("\\d{8}")) {
                    procedimiento.setString(6, registro.getTelefonoContacto());
                    procedimiento.execute();
                    postActualizar();
                } else {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Teléfono Incorrecto", "ERROR", JOptionPane.WARNING_MESSAGE);
                }
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
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public void reporte() {
        switch (tipoDeOperacion) {
            case ACTUALIZAR:
                limpiarControles();
                desactivarControles();
                btnEditar.setText("EDITAR         ");
                btnReporte.setText("REPORTE     ");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/rodrigodiaz/image/iconoEditar.png"));
                imgReporte.setImage(new Image("/org/rodrigodiaz/image/iconoReporte.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
            case NINGUNO:
                if (tblServicios.getSelectionModel().getSelectedItem() != null) {
                    imprimirReporte();
                } else {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento", "AVISO", JOptionPane.WARNING_MESSAGE);
                }
                break;
        }
    }

    public void imprimirReporte() {
        Map parametros = new HashMap();
        int codEmpresa = Integer.valueOf(((Empresa) cbxCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        URL in = this.getClass().getResource("/org/rodrigodiaz/image/logoSalchichaTonysSF.png");
        URL in2 = this.getClass().getResource("/org/rodrigodiaz/image/logoOpaco2.png");
        parametros.put("codEmpresa", codEmpresa);
        parametros.put("logo1", in);
        parametros.put("logo2", in2);
        GenerarReporte.mostrarReporte("ReporteServiciosEmpresa.jasper", "Reporte de Servicios", parametros);

    }

    public ObservableList<Empresa> getEmpresa() {
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarEmpresas");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Empresa(resultado.getInt("codigoEmpresa"),
                        resultado.getString("nombreEmpresa"),
                        resultado.getString("direccion"),
                        resultado.getString("telefono")));
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
        return listaEmpresa = FXCollections.observableArrayList(lista);
    }

    public void postGuardar() {
        limpiarControles();
        desactivarControles();
        btnNuevo.setText("NUEVO         ");
        btnEliminar.setText("ELIMINAR     ");
        btnEditar.setDisable(false);
        btnReporte.setDisable(false);
        imgNuevo.setImage(new Image("/org/rodrigodiaz/image/iconoAgregar.png"));
        imgEliminar.setImage(new Image("/org/rodrigodiaz/image/iconoEliminar.png"));
        tipoDeOperacion = operaciones.NINGUNO;
        cargarDatos();
    }

    public void postActualizar() {
        limpiarControles();
        desactivarControles();
        btnNuevo.setDisable(false);
        btnEliminar.setDisable(false);
        btnEditar.setText("EDITAR         ");
        btnReporte.setText("REPORTE     ");
        imgEditar.setImage(new Image("/org/rodrigodiaz/image/iconoEditar.png"));
        imgReporte.setImage(new Image("/org/rodrigodiaz/image/iconoReporte2.png"));
        tipoDeOperacion = operaciones.NINGUNO;
        cargarDatos();
    }

    public void limpiarControles() {
        txtCodigoServicio.clear();
        txtTelefono.clear();
        txtTipoServicio.clear();
        txtLugar.clear();
        dtHora.setValue(null);
        cbxCodigoEmpresa.setValue(null);
        fecha.setSelectedDate(null);
    }

    public void activarControles() {
        txtCodigoServicio.setEditable(false);
        txtTipoServicio.setEditable(true);
        txtLugar.setEditable(true);
        txtTelefono.setEditable(true);
        cbxCodigoEmpresa.setDisable(false);
        fecha.setDisable(false);
        dtHora.setDisable(false);
    }

    public void desactivarControles() {
        txtCodigoServicio.setEditable(false);
        txtTipoServicio.setEditable(false);
        txtLugar.setEditable(false);
        txtTelefono.setEditable(false);
        cbxCodigoEmpresa.setDisable(true);
        fecha.setDisable(true);
        dtHora.setDisable(true);
        dtHora.setEditable(false);
        //dtHora.setDisable(false);
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void ventanaEmpresa() {
        escenarioPrincipal.ventanaEmpresa();
    }

    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }

}
