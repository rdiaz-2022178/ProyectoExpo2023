package org.rodrigodiaz.controller;

import com.mysql.jdbc.MysqlDataTruncation;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableListValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.rodrigodiaz.bean.Empresa;
import org.rodrigodiaz.db.Conexion;
import org.rodrigodiaz.main.Principal;
import org.rodrigodiaz.report.GenerarReporte;

public class EmpresaController implements Initializable {

    private enum operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO, REPORTE
    };
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Empresa> listaEmpresa;

    @FXML
    private TextField txtCodigoEmpresa;
    @FXML
    private TextField txtNombreEmpresa;
    @FXML
    private TextField txtDireccionEmpresa;
    @FXML
    private TextField txtTelefonoEmpresa;
    @FXML
    private TableView tblEmpresas;
    @FXML
    private TableColumn colCodigoEmpresa;
    @FXML
    private TableColumn colNombreEmpresa;
    @FXML
    private TableColumn colDireccionEmpresa;
    @FXML
    private TableColumn colTelefonoEmpresa;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnReporte;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgReporte;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }

    public void cargarDatos() {
        tblEmpresas.setItems(getEmpresa());
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, Integer>("codigoEmpresa"));
        colNombreEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("nombreEmpresa"));
        colDireccionEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("direccion"));
        colTelefonoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("telefono"));
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

    public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblEmpresas.getSelectionModel().getSelectedItem() != null) {
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("ACTUALIZAR    ");
                    btnReporte.setText("CANCELAR     ");
                    imgEditar.setImage(new Image("/org/rodrigodiaz/image/iconoActualizar.png"));
                    imgReporte.setImage(new Image("/org/rodrigodiaz/image/iconoCancelar.png"));
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                } else {
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EditarEmpresa(?, ?, ?, ?)");
            Empresa registro = (Empresa) tblEmpresas.getSelectionModel().getSelectedItem();
            registro.setNombreEmpresa(txtNombreEmpresa.getText().trim());
            registro.setDireccion(txtDireccionEmpresa.getText().trim());
            registro.setTelefono(txtTelefonoEmpresa.getText().trim());
            if (registro.getNombreEmpresa().length() == 0 || registro.getDireccion().length() == 0 || registro.getTelefono().length() == 0) {
                JOptionPane.showMessageDialog(null, "Debe de rellenar todos los campos", "AVISO", JOptionPane.WARNING_MESSAGE);
            } else {
                procedimiento.setInt(1, registro.getCodigoEmpresa());
                procedimiento.setString(2, registro.getNombreEmpresa());
                procedimiento.setString(3, registro.getDireccion());
                if (registro.getTelefono().matches("\\d{8}")) {
                    procedimiento.setString(4, registro.getTelefono());
                    procedimiento.execute();
                    postActualizar();
                } else {
                    JOptionPane.showMessageDialog(null, "Teléfono Incorrecto", "ERROR", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (MysqlDataTruncation error) {
            JOptionPane.showMessageDialog(null, "Verifique el número de carácteres", "AVISO", JOptionPane.WARNING_MESSAGE);
        } catch (NumberFormatException error) {
            JOptionPane.showMessageDialog(null, "Valor Incorrecto", "AVISO", JOptionPane.WARNING_MESSAGE);
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public void seleccionarElemento() {
        if (tipoDeOperacion == operaciones.GUARDAR) {
            JOptionPane.showMessageDialog(null, "No puedes seleccionar elementos", "AVISO", JOptionPane.WARNING_MESSAGE);
        } else {
            if (tblEmpresas.getSelectionModel().getSelectedItem() == null) {
                JOptionPane.showMessageDialog(null, "Seleccione una fila correcta", "AVISO", JOptionPane.WARNING_MESSAGE);
            } else {
                txtCodigoEmpresa.setText(String.valueOf(((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa()).trim());
                txtNombreEmpresa.setText(((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getNombreEmpresa());
                txtDireccionEmpresa.setText(((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getDireccion());
                txtTelefonoEmpresa.setText(((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getTelefono());
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
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/rodrigodiaz/image/iconoAgregar.png"));
                imgEliminar.setImage(new Image("/org/rodrigodiaz/image/iconoEliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if (tblEmpresas.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Está seguro de eliminar el registro?", "Eliminar Empresa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EliminarEmpresa(?)");
                            procedimiento.setInt(1, ((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
                            procedimiento.execute();
                            listaEmpresa.remove(tblEmpresas.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            cargarDatos();
                        } catch (SQLException error) {
                            JOptionPane.showMessageDialog(null, "No se puede Eliminar este registro", "AVISO", JOptionPane.ERROR_MESSAGE);
                        } catch (Exception error) {
                            error.printStackTrace();
                        }
                    } else {
                        limpiarControles();
                        cargarDatos();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento", "AVISO", JOptionPane.WARNING_MESSAGE);
                }
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
                imprimirReporte();
                break;
        }
    }

    public void imprimirReporte() {
        Map parametros = new HashMap();
        URL in = this.getClass().getResource("/org/rodrigodiaz/image/logoSalchichaTonysSF.png");
        URL in2 = this.getClass().getResource("/org/rodrigodiaz/image/logoOpaco2.png");
        parametros.put("codigoEmpresa", in);
        parametros.put("logo2", in2);
        GenerarReporte.mostrarReporte("ReporteEmpresa.jasper", "Reporte de Empresas", parametros);
    }

    public void guardar() {
        Empresa registro = new Empresa();
        try {
            if (txtNombreEmpresa.getText().trim().length() == 0 || txtDireccionEmpresa.getText().trim().length() == 0 || txtTelefonoEmpresa.getText().trim().length() == 0) {
                JOptionPane.showMessageDialog(null, "Debe de rellenar todos los campos", "AVISO", JOptionPane.WARNING_MESSAGE);
            } else {
                registro.setNombreEmpresa(txtNombreEmpresa.getText().trim());
                registro.setDireccion(txtDireccionEmpresa.getText().trim());
                if (txtTelefonoEmpresa.getText().trim().matches("\\d{8}")) {
                    registro.setTelefono(txtTelefonoEmpresa.getText().trim());
                    PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_AgregarEmpresa(?,?,?)");
                    procedimiento.setString(1, registro.getNombreEmpresa());
                    procedimiento.setString(2, registro.getDireccion());
                    procedimiento.setString(3, registro.getTelefono().trim());
                    procedimiento.execute();
                    listaEmpresa.add(registro);
                    postGuardar();
                } else {
                    JOptionPane.showMessageDialog(null, "Teléfono Incorrecto", "ERROR", JOptionPane.WARNING_MESSAGE);
                }
            }
        } catch (MysqlDataTruncation error) {
            JOptionPane.showMessageDialog(null, "Verifique el número de carácteres", "AVISO", JOptionPane.WARNING_MESSAGE);
        } catch (NumberFormatException error) {
            JOptionPane.showMessageDialog(null, "Valor Incorrecto", "AVISO", JOptionPane.WARNING_MESSAGE);
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

    public void desactivarControles() {
        txtCodigoEmpresa.setEditable(false);
        txtNombreEmpresa.setEditable(false);
        txtDireccionEmpresa.setEditable(false);
        txtTelefonoEmpresa.setEditable(false);
    }

    public void activarControles() {
        txtCodigoEmpresa.setEditable(false);
        txtNombreEmpresa.setEditable(true);
        txtDireccionEmpresa.setEditable(true);
        txtTelefonoEmpresa.setEditable(true);
    }

    public void limpiarControles() {
        txtCodigoEmpresa.clear();
        txtNombreEmpresa.clear();
        txtDireccionEmpresa.clear();
        txtTelefonoEmpresa.clear();
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

    public void ventanaPresupuesto() {
        escenarioPrincipal.ventanaPresupuesto();
    }

    public void ventanaProgramador() {
        escenarioPrincipal.ventanaProgramador();
    }

    public void ventanaServicios() {
        escenarioPrincipal.ventanaServicios();
    }
}
