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
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import org.rodrigodiaz.bean.Plato;
import org.rodrigodiaz.bean.TipoPlato;
import org.rodrigodiaz.main.Principal;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javax.swing.JOptionPane;
import org.rodrigodiaz.db.Conexion;

public class PlatoController implements Initializable {

    private Principal escenarioPrincipal;

    private enum operaciones {
        GUARDAR, ACTUALIZAR, ELIMINAR, NINGUNO
    };
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Plato> listaPlatos;
    private ObservableList<TipoPlato> listaTipoPlatos;

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
    private TextField txtCodigoPlato;
    @FXML
    private TextField txtCantidad;
    @FXML
    private TextField txtNombrePlato;
    @FXML
    private TextField txtDescripcionPlato;
    @FXML
    private TextField txtPrecio;
    @FXML
    private ComboBox cbmCodigoTipoPlato;
    @FXML
    private TableView tblPlatos;
    @FXML
    private TableColumn colCodigoPlato;
    @FXML
    private TableColumn colCantidad;
    @FXML
    private TableColumn colNombrePlato;
    @FXML
    private TableColumn colDescripcion;
    @FXML
    private TableColumn colPrecio;
    @FXML
    private TableColumn colCodigoTipoPlato;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbmCodigoTipoPlato.setDisable(true);
        cbmCodigoTipoPlato.setItems(getTipoPlato());
        cargarDatos();
    }

    public void cargarDatos() {
        tblPlatos.setItems(getPlatos());
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("codigoPlato"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("cantidad"));
        colNombrePlato.setCellValueFactory(new PropertyValueFactory<Plato, String>("nombrePlato"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Plato, String>("descripcionPlato"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<Plato, Double>("precioPlato"));
        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<Plato, Integer>("codigoTipoPlato"));

    }

    public void seleccionarElemento() {
        if (tipoDeOperacion == operaciones.GUARDAR) {
            JOptionPane.showMessageDialog(null, "No puedes seleccionar elementos", "AVISO", JOptionPane.WARNING_MESSAGE);
        } else {
            if (tblPlatos.getSelectionModel().getSelectedItem() == null) {
                JOptionPane.showMessageDialog(null, "Seleccione una fila correcta", "AVISO", JOptionPane.WARNING_MESSAGE);
            } else {
                txtCodigoPlato.setText(String.valueOf(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
                txtCantidad.setText(String.valueOf(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getCantidad()));
                txtNombrePlato.setText(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getNombrePlato());
                txtDescripcionPlato.setText(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getDescripcionPlato());
                txtPrecio.setText(String.valueOf(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getPrecioPlato()));
                cbmCodigoTipoPlato.getSelectionModel().select(buscarTipoPlato(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));

            }
        }
    }

    public TipoPlato buscarTipoPlato(int codigoTipoPlato) {
        TipoPlato resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_BuscarTipoPlato(?)");
            procedimiento.setInt(1, codigoTipoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new TipoPlato(registro.getInt("codigoTipoPlato"),
                        registro.getString("descripcionTipo"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
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

    public ObservableList<TipoPlato> getTipoPlato() {
        ArrayList<TipoPlato> lista = new ArrayList<TipoPlato>();
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarTipoPlatos");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new TipoPlato(
                        resultado.getInt("codigoTipoPlato"),
                        resultado.getString("descripcionTipo")));
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
        return listaTipoPlatos = FXCollections.observableArrayList(lista);
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
        Plato registro = new Plato();
        try {
            if (txtCantidad.getText().trim().length() == 0 || cbmCodigoTipoPlato.getSelectionModel().isEmpty() || txtDescripcionPlato.getText().trim().length() == 0 || txtPrecio.getText().trim().length() == 0 || txtNombrePlato.getText().trim().length() == 0) {
                JOptionPane.showMessageDialog(null, "Debe de rellenar todos los campos", "AVISO", JOptionPane.WARNING_MESSAGE);
            } else {
                registro.setCantidad(Integer.parseInt(txtCantidad.getText().trim()));
                registro.setNombrePlato(txtNombrePlato.getText().trim());
                registro.setDescripcionPlato(txtDescripcionPlato.getText().trim());
                registro.setPrecioPlato(Double.parseDouble(txtPrecio.getText().trim()));
                registro.setCodigoTipoPlato(((TipoPlato) cbmCodigoTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_AgregarPlato(?,?,?,?,?)");
                procedimiento.setInt(1, registro.getCantidad());
                procedimiento.setString(2, registro.getNombrePlato());
                procedimiento.setString(3, registro.getDescripcionPlato());
                procedimiento.setDouble(4, registro.getPrecioPlato());
                procedimiento.setInt(5, registro.getCodigoTipoPlato());
                procedimiento.execute();
                listaPlatos.add(registro);
                postGuardar();
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
                if (tblPlatos.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Está seguro de eliminar el registro?", "Eliminar Plato", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EliminarPlato(?)");
                            procedimiento.setInt(1, ((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato());
                            procedimiento.execute();
                            listaPlatos.remove(tblPlatos.getSelectionModel().getSelectedIndex());
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
                if (tblPlatos.getSelectionModel().getSelectedItem() != null) {
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("ACTUALIZAR    ");
                    btnReporte.setText("CANCELAR     ");
                    imgEditar.setImage(new Image("/org/rodrigodiaz/image/iconoActualizar.png"));
                    imgReporte.setImage(new Image("/org/rodrigodiaz/image/iconoCancelar.png"));
                    activarControles();
                    cbmCodigoTipoPlato.setDisable(true);
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EditarPlato(?, ?,?,?,?)");
            Plato registro = (Plato) tblPlatos.getSelectionModel().getSelectedItem();
            registro.setCantidad(Integer.parseInt(txtCantidad.getText().trim()));
            registro.setNombrePlato(txtNombrePlato.getText().trim());
            registro.setDescripcionPlato(txtDescripcionPlato.getText().trim());
            registro.setPrecioPlato(Double.parseDouble(txtPrecio.getText().trim()));
            if (txtCantidad.getText().trim().length() == 0 || txtDescripcionPlato.getText().trim().length() == 0 || txtPrecio.getText().trim().length() == 0 || txtNombrePlato.getText().trim().length() == 0) {
                JOptionPane.showMessageDialog(null, "Debe rellenar todos los campos", "AVISO", JOptionPane.WARNING_MESSAGE);
            } else {
                procedimiento.setInt(1, registro.getCodigoPlato());
                procedimiento.setInt(2, registro.getCantidad());
                procedimiento.setString(3, registro.getNombrePlato());
                procedimiento.setString(4, registro.getDescripcionPlato());
                procedimiento.setDouble(5, registro.getPrecioPlato());
                procedimiento.execute();
                postActualizar();
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

    public void limpiarControles() {
        txtCodigoPlato.clear();
        txtCantidad.clear();
        txtDescripcionPlato.clear();
        txtPrecio.clear();
        txtNombrePlato.clear();
        cbmCodigoTipoPlato.setValue(null);
    }

    public void activarControles() {
        txtCodigoPlato.setEditable(false);
        txtCantidad.setEditable(true);
        txtDescripcionPlato.setEditable(true);
        txtPrecio.setEditable(true);
        txtNombrePlato.setEditable(true);
        cbmCodigoTipoPlato.setDisable(false);
    }

    public void desactivarControles() {
        txtCodigoPlato.setEditable(false);
        txtCantidad.setEditable(false);
        txtDescripcionPlato.setEditable(false);
        txtPrecio.setEditable(false);
        txtNombrePlato.setEditable(false);
        cbmCodigoTipoPlato.setDisable(true);
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

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void ventanaTipoPlato() {
        escenarioPrincipal.ventanaTipoPlato();
    }

    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }

}
