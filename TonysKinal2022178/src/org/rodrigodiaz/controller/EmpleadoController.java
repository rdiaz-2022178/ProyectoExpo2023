package org.rodrigodiaz.controller;

import com.mysql.jdbc.MysqlDataTruncation;
import java.awt.Toolkit;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import static javafx.application.ConditionalFeature.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.rodrigodiaz.bean.Empleado;
import org.rodrigodiaz.bean.TipoEmpleado;
import org.rodrigodiaz.main.Principal;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.rodrigodiaz.db.Conexion;

public class EmpleadoController implements Initializable {

    private Principal escenarioPrincipal;

    private enum operaciones {
        GUARDAR, ACTUALIZAR, ELIMINAR, NINGUNO
    };
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Empleado> listaEmpleados;
    private ObservableList<TipoEmpleado> listaTipoEmpleados;

    @FXML
    private TextField txtCodigoEmpleado;
    @FXML
    private TextField txtNumeroEmpleado;
    @FXML
    private TextField txtApellidos;
    @FXML
    private TextField txtNombres;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtGrado;
    @FXML
    private TableView tblEmpleados;
    @FXML
    private TableColumn colCodigoEmpleado;
    @FXML
    private TableColumn colNumero;
    @FXML
    private TableColumn colApellidos;
    @FXML
    private TableColumn colNombres;
    @FXML
    private TableColumn colDireccion;
    @FXML
    private TableColumn colTelefono;
    @FXML
    private TableColumn colGrado;
    @FXML
    private TableColumn colCodigoTipoEmpleado;
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
    private ComboBox cbxCodigoTipoEmpleado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbxCodigoTipoEmpleado.setDisable(true);
        cbxCodigoTipoEmpleado.setItems(getTipoEmpleado());
        cargarDatos();
    }

    public void cargarDatos() {
        tblEmpleados.setItems(getEmpleados());
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoEmpleado"));
        colNumero.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("numeroEmpleado"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Empleado, String>("apellidosEmpleado"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombresEmpleado"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Empleado, String>("direccionEmpleado"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Empleado, String>("telefonoContacto"));
        colGrado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("gradoCocinero"));
        colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoTipoEmpleado"));
    }

    public void seleccionarElemento() {
        if (tipoDeOperacion == operaciones.GUARDAR) {
            JOptionPane.showMessageDialog(null, "No puedes seleccionar elementos", "AVISO", JOptionPane.WARNING_MESSAGE);
        } else {
            if (tblEmpleados.getSelectionModel().getSelectedItem() == null) {
                JOptionPane.showMessageDialog(null, "Seleccione una fila correcta", "AVISO", JOptionPane.WARNING_MESSAGE);
            } else {
                if (tblEmpleados.getSelectionModel().getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(null, "Seleccione una fila correcta", "AVISO", JOptionPane.WARNING_MESSAGE);
                } else {
                    txtCodigoEmpleado.setText(String.valueOf(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
                    txtNumeroEmpleado.setText(String.valueOf(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getNumeroEmpleado()));
                    txtApellidos.setText(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getApellidosEmpleado());
                    txtNombres.setText(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getNombresEmpleado());
                    txtDireccion.setText(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getDireccionEmpleado());
                    txtTelefono.setText(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getTelefonoContacto());
                    txtGrado.setText(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getGradoCocinero());
                    cbxCodigoTipoEmpleado.getSelectionModel().select(buscarTipoEmpleado(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));

                }
            }
        }
    }

    public TipoEmpleado buscarTipoEmpleado(int codigoTipoEmpleado) {
        TipoEmpleado resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_BuscarTipoEmpleado(?)");
            procedimiento.setInt(1, codigoTipoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new TipoEmpleado(registro.getInt("codigoTipoEmpleado"),
                        registro.getString("descripcion"));

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

    public ObservableList<TipoEmpleado> getTipoEmpleado() {
        ArrayList<TipoEmpleado> lista = new ArrayList<TipoEmpleado>();
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarTipoEmpleados");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new TipoEmpleado(
                        resultado.getInt("codigoTipoEmpleado"),
                        resultado.getString("descripcion")));
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
        return listaTipoEmpleados = FXCollections.observableArrayList(lista);
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
        Empleado registro = new Empleado();
        if (txtNumeroEmpleado.getText().trim().length() == 0 || cbxCodigoTipoEmpleado.getSelectionModel().isEmpty() || txtApellidos.getText().trim().length() == 0 || txtNombres.getText().trim().length() == 0 || txtDireccion.getText().trim().length() == 0 || txtGrado.getText().trim().length() == 0 || txtTelefono.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(null, "Debe de rellenar todos los campos", "AVISO", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText().trim()));
                registro.setApellidosEmpleado(txtApellidos.getText().trim());
                registro.setNombresEmpleado(txtNombres.getText().trim());
                registro.setDireccionEmpleado(txtDireccion.getText().trim());
                if (txtTelefono.getText().trim().matches("\\d{8}")) {

                    registro.setTelefonoContacto(txtTelefono.getText().trim());
                    registro.setGradoCocinero(txtGrado.getText().trim());
                    registro.setCodigoTipoEmpleado(((TipoEmpleado) cbxCodigoTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
                    PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_AgregarEmpleado(?,?,?,?,?,?,?)");
                    procedimiento.setInt(1, registro.getNumeroEmpleado());
                    procedimiento.setString(2, registro.getApellidosEmpleado());
                    procedimiento.setString(3, registro.getNombresEmpleado());
                    procedimiento.setString(4, registro.getDireccionEmpleado());
                    procedimiento.setString(5, registro.getTelefonoContacto());
                    procedimiento.setString(6, registro.getGradoCocinero());
                    procedimiento.setInt(7, registro.getCodigoTipoEmpleado());
                    procedimiento.execute();
                    listaEmpleados.add(registro);
                    postGuardar();

                } else {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Teléfono Incorrecto", "ERROR", JOptionPane.WARNING_MESSAGE);
                }
            } catch (MysqlDataTruncation error) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "Verifique el número de carácteres", "AVISO", JOptionPane.WARNING_MESSAGE);
            } catch (java.lang.NumberFormatException e) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(null, "Valor Incorrecto", "AVISO", JOptionPane.WARNING_MESSAGE);
            } catch (Exception error) {
                error.printStackTrace();
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
            case NINGUNO:
                if (tblEmpleados.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Está seguro de eliminar el registro?", "Eliminar Empleado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EliminarEmpleado(?)");
                            procedimiento.setInt(1, ((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                            procedimiento.execute();
                            listaEmpleados.remove(tblEmpleados.getSelectionModel().getSelectedIndex());
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
                if (tblEmpleados.getSelectionModel().getSelectedItem() != null) {
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("ACTUALIZAR    ");
                    btnReporte.setText("CANCELAR     ");
                    imgEditar.setImage(new Image("/org/rodrigodiaz/image/iconoActualizar.png"));
                    imgReporte.setImage(new Image("/org/rodrigodiaz/image/iconoCancelar.png"));
                    activarControles();
                    cbxCodigoTipoEmpleado.setDisable(true);
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EditarEmpleado(?,?,?,?,?,?,?)");
            Empleado registro = (Empleado) tblEmpleados.getSelectionModel().getSelectedItem();
            registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText().trim()));
            registro.setApellidosEmpleado(txtApellidos.getText().trim());
            registro.setNombresEmpleado(txtNombres.getText().trim());
            registro.setDireccionEmpleado(txtDireccion.getText().trim());
            registro.setTelefonoContacto(txtTelefono.getText().trim());
            registro.setGradoCocinero(txtGrado.getText().trim());
            registro.setCodigoTipoEmpleado(((TipoEmpleado) cbxCodigoTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
            if (txtNumeroEmpleado.getText().trim().length() == 0 || txtApellidos.getText().trim().length() == 0 || txtNombres.getText().trim().length() == 0 || txtDireccion.getText().trim().length() == 0 || txtGrado.getText().trim().length() == 0 || txtTelefono.getText().trim().length() == 0) {
                JOptionPane.showMessageDialog(null, "Debe de rellenar todos los campos", "AVISO", JOptionPane.WARNING_MESSAGE);
            } else {
                procedimiento.setInt(1, registro.getCodigoEmpleado());
                procedimiento.setInt(2, registro.getNumeroEmpleado());
                procedimiento.setString(3, registro.getApellidosEmpleado());
                procedimiento.setString(4, registro.getNombresEmpleado());
                procedimiento.setString(5, registro.getDireccionEmpleado());
                if (txtTelefono.getText().trim().matches("\\d{8}")) {
                    procedimiento.setString(6, registro.getTelefonoContacto());
                    procedimiento.setString(7, registro.getGradoCocinero());
                    procedimiento.execute();
                    postActualizar();
                } else {
                    Toolkit.getDefaultToolkit().beep();
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
        btnReporte.setText("CANCELAR     ");
        imgEditar.setImage(new Image("/org/rodrigodiaz/image/iconoEditar.png"));
        imgReporte.setImage(new Image("/org/rodrigodiaz/image/iconoCancelar.png"));
        tipoDeOperacion = operaciones.NINGUNO;
        cargarDatos();
    }

    public void limpiarControles() {
        txtCodigoEmpleado.clear();
        txtNumeroEmpleado.clear();
        txtApellidos.clear();
        txtNombres.clear();
        txtDireccion.clear();
        txtTelefono.clear();
        txtGrado.clear();
        cbxCodigoTipoEmpleado.setValue(null);
    }

    public void activarControles() {
        txtCodigoEmpleado.setEditable(false);
        txtNumeroEmpleado.setEditable(true);
        txtApellidos.setEditable(true);
        txtNombres.setEditable(true);
        txtDireccion.setEditable(true);
        txtTelefono.setEditable(true);
        txtGrado.setEditable(true);
        cbxCodigoTipoEmpleado.setDisable(false);
    }

    public void desactivarControles() {
        txtCodigoEmpleado.setEditable(false);
        txtNumeroEmpleado.setEditable(false);
        txtApellidos.setEditable(false);
        txtNombres.setEditable(false);
        txtDireccion.setEditable(false);
        txtTelefono.setEditable(false);
        txtGrado.setEditable(false);
        cbxCodigoTipoEmpleado.setDisable(true);
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void ventanaTipoEmpleado() {
        escenarioPrincipal.ventanaTipoEmpleado();
    }

    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }

}
