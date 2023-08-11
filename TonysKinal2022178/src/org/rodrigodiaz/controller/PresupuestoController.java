package org.rodrigodiaz.controller;

import com.mysql.jdbc.MysqlDataTruncation;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.awt.Toolkit;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
import org.rodrigodiaz.bean.Presupuesto;
import org.rodrigodiaz.db.Conexion;
import org.rodrigodiaz.main.Principal;
import org.rodrigodiaz.report.GenerarReporte;

public class PresupuestoController implements Initializable {

    private Principal escenarioPrincipal;

    private enum operaciones {
        GUARDAR, ACTUALIZAR, ELIMINAR, NINGUNO
    };
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Presupuesto> listaPresupuesto;
    private ObservableList<Empresa> listaEmpresa;
    private DatePicker fecha;

    @FXML
    private TextField txtCodigoPresupuesto;
    @FXML
    private TextField txtCantidad;
    @FXML
    private GridPane grpFecha;
    @FXML
    private ComboBox cmbCodigoEmpresa;
    @FXML
    private TableView tblPresupuestos;
    @FXML
    private TableColumn colCodigoPresupuesto;
    @FXML
    private TableColumn colFechaSolicitud;
    @FXML
    private TableColumn colCantidad;
    @FXML
    private TableColumn colCodigoEmpresa;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(true);
        fecha.getStylesheets().add("/org/rodrigodiaz/resource/CalendarStyle.css");
        fecha.setDisable(true);
        cmbCodigoEmpresa.setDisable(true);
        grpFecha.add(fecha, 1, 1);
        fecha.setAlignment(Pos.CENTER);
        cmbCodigoEmpresa.setItems(getEmpresa());
    }

    public void cargarDatos() {
        tblPresupuestos.setItems(getPresupuestos());
        colCodigoPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer>("codigoPresupuesto"));
        colFechaSolicitud.setCellValueFactory(new PropertyValueFactory<Presupuesto, Date>("fechaSolicitud"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Presupuesto, Double>("cantidadPresupuesto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer>("codigoEmpresa"));

    }

    public void seleccionarElemento() {
        if (tipoDeOperacion == operaciones.GUARDAR) {
            JOptionPane.showMessageDialog(null, "No puedes seleccionar elementos", "AVISO", JOptionPane.WARNING_MESSAGE);
        } else {
            if (tblPresupuestos.getSelectionModel().getSelectedItem() == null) {
                JOptionPane.showMessageDialog(null, "Seleccione una fila correcta", "AVISO", JOptionPane.WARNING_MESSAGE);
            } else {
                txtCodigoPresupuesto.setText(String.valueOf(((Presupuesto) tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoPresupuesto()));
                fecha.selectedDateProperty().set(((Presupuesto) tblPresupuestos.getSelectionModel().getSelectedItem()).getFechaSolicitud());
                txtCantidad.setText(String.valueOf(((Presupuesto) tblPresupuestos.getSelectionModel().getSelectedItem()).getCantidadPresupuesto()));
                cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Presupuesto) tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));

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

    public ObservableList<Presupuesto> getPresupuestos() {
        ArrayList<Presupuesto> lista = new ArrayList<Presupuesto>();
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarPresupuestos");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Presupuesto(
                        resultado.getInt("codigoPresupuesto"),
                        resultado.getDate("fechaSolicitud"),
                        resultado.getDouble("cantidadPresupuesto"),
                        resultado.getInt("codigoEmpresa")));
            }
        } catch (Exception error) {
            error.printStackTrace();
        }

        return listaPresupuesto = FXCollections.observableArrayList(lista);
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
            limpiarControles();
        } catch (Exception error) {
            error.printStackTrace();
        }
        return listaEmpresa = FXCollections.observableArrayList(lista);
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
        try {
            Presupuesto registro = new Presupuesto();
            if (txtCantidad.getText().trim().length() == 0 || cmbCodigoEmpresa.getSelectionModel().isEmpty() || cmbCodigoEmpresa.getSelectionModel().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe de rellenar todos los campos", "AVISO", JOptionPane.WARNING_MESSAGE);
            } else {
                registro.setFechaSolicitud(fecha.getSelectedDate());
                registro.setCantidadPresupuesto(Double.parseDouble(txtCantidad.getText().trim()));
                registro.setCodigoEmpresa(((Empresa) cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_AgregarPresupuesto(?,?,?)");
                procedimiento.setDate(1, new java.sql.Date(registro.getFechaSolicitud().getTime()));
                procedimiento.setDouble(2, registro.getCantidadPresupuesto());
                procedimiento.setInt(3, registro.getCodigoEmpresa());
                procedimiento.execute();
                listaPresupuesto.add(registro);
                postGuardar();
            }
        } catch (MysqlDataTruncation error) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Verifique el número de carácteres", "AVISO", JOptionPane.WARNING_MESSAGE);
        } catch (java.lang.NumberFormatException e) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Valor Incorrecto", "AVISO", JOptionPane.WARNING_MESSAGE);
        } catch (NullPointerException e) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Debe rellenar la fecha", "AVISO", JOptionPane.WARNING_MESSAGE);
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
                if (tblPresupuestos.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Está seguro de eliminar el registro?", "Eliminar Presupuesto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EliminarPresupuesto(?)");
                            procedimiento.setInt(1, ((Presupuesto) tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoPresupuesto());
                            procedimiento.execute();
                            listaPresupuesto.remove(tblPresupuestos.getSelectionModel().getSelectedIndex());
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
                if (tblPresupuestos.getSelectionModel().getSelectedItem() != null) {
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("ACTUALIZAR    ");
                    btnReporte.setText("CANCELAR     ");
                    imgEditar.setImage(new Image("/org/rodrigodiaz/image/iconoActualizar.png"));
                    imgReporte.setImage(new Image("/org/rodrigodiaz/image/iconoCancelar.png"));
                    activarControles();
                    cmbCodigoEmpresa.setDisable(true);
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
                if (tblPresupuestos.getSelectionModel().getSelectedItem() != null) {
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
        int codEmpresa = Integer.valueOf(((Empresa) cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        URL in = this.getClass().getResource("/org/rodrigodiaz/image/logoSalchichaTonysSF.png");
        URL in2 = this.getClass().getResource("/org/rodrigodiaz/image/logoOpaco2.png");
        URL in3 = this.getClass().getResource("/org/rodrigodiaz/report/");
        parametros.put("codEmpresa", codEmpresa);
        parametros.put("logo1", in);
        parametros.put("logo2", in2);
        parametros.put("subReporte", in3);
        GenerarReporte.mostrarReporte("ReportePresupuesto.jasper", "Reporte de Presupuestos", parametros);
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EditarPresupuesto(?,?,?)");
            Presupuesto registro = (Presupuesto) tblPresupuestos.getSelectionModel().getSelectedItem();
            registro.setFechaSolicitud(fecha.getSelectedDate());
            registro.setCantidadPresupuesto(Double.parseDouble(txtCantidad.getText().trim()));
            if (registro.getFechaSolicitud().toString().isEmpty() || registro.getCantidadPresupuesto() == 0 || registro.getCodigoEmpresa() == 0) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "Debe de rellenar todos los campos", "AVISO", JOptionPane.WARNING_MESSAGE);
            } else {
                procedimiento.setInt(1, registro.getCodigoPresupuesto());
                procedimiento.setDate(2, new java.sql.Date(registro.getFechaSolicitud().getTime()));
                procedimiento.setDouble(3, registro.getCantidadPresupuesto());
                procedimiento.execute();
                postActualizar();
            }
        } catch (MysqlDataTruncation error) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Verifique el número de carácteres", "AVISO", JOptionPane.WARNING_MESSAGE);
        } catch (java.lang.NumberFormatException e) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Valor Incorrecto", "AVISO", JOptionPane.WARNING_MESSAGE);
        } catch (NullPointerException e) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Debe rellenar la fecha", "AVISO", JOptionPane.WARNING_MESSAGE);
        } catch (Exception error) {
            error.printStackTrace();
        }
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
        activarControles();
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
        txtCantidad.clear();
        txtCodigoPresupuesto.clear();
        cmbCodigoEmpresa.setValue(null);
        fecha.setSelectedDate(null);
    }

    public void activarControles() {
        txtCantidad.setEditable(true);
        txtCodigoPresupuesto.setEditable(false);
        fecha.setDisable(false);
        cmbCodigoEmpresa.setDisable(false);
    }

    public void desactivarControles() {
        txtCantidad.setEditable(false);
        txtCodigoPresupuesto.setEditable(false);
        cmbCodigoEmpresa.setDisable(true);
        fecha.setDisable(true);
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
