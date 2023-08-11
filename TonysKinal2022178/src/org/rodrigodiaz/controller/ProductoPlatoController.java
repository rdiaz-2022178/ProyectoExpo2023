package org.rodrigodiaz.controller;

import com.mysql.jdbc.MysqlDataTruncation;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
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
import org.rodrigodiaz.bean.Producto;
import org.rodrigodiaz.bean.ProductosPlatos;
import org.rodrigodiaz.db.Conexion;
import org.rodrigodiaz.main.Principal;

public class ProductoPlatoController implements Initializable {

    private Principal escenarioPrincipal;

    private enum operaciones {
        GUARDAR, ACTUALIZAR, ELIMINAR, NINGUNO
    };
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<ProductosPlatos> listaProductosPlatos;
    private ObservableList<Producto> listaProductos;
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
    private TextField txtCodigoProductoPlato;
    @FXML
    private ComboBox cbxCodigoProducto;
    @FXML
    private ComboBox cbxCodigoPlato;
    @FXML
    private TableView tblProductoPlatos;
    @FXML
    private TableColumn colCodigoProductoPlato;
    @FXML
    private TableColumn colCodigoProducto;
    @FXML
    private TableColumn colCodigoPlato;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cbxCodigoPlato.setDisable(true);
        cbxCodigoProducto.setDisable(true);
        cbxCodigoPlato.setItems(getPlatos());
        cbxCodigoProducto.setItems(getProducto());
        btnEditar.setDisable(true);
        // btnEliminar.setDisable(true);
    }

    public ObservableList<Producto> getProducto() {
        ArrayList<Producto> lista = new ArrayList<Producto>();
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarProductos");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Producto(resultado.getInt("codigoProducto"),
                        resultado.getString("nombreProducto"),
                        resultado.getInt("cantidadProducto")));
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
        return listaProductos = FXCollections.observableArrayList(lista);
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

    public ObservableList<ProductosPlatos> getProductoPlatos() {
        ArrayList<ProductosPlatos> lista = new ArrayList<ProductosPlatos>();
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_ListarProductos_has_Platos");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new ProductosPlatos(
                        resultado.getInt("Productos_codigoProducto"),
                        resultado.getInt("codigoProducto"),
                        resultado.getInt("codigoPlato")));
            }
        } catch (Exception error) {
            error.printStackTrace();
        }

        return listaProductosPlatos = FXCollections.observableArrayList(lista);
    }

    public void cargarDatos() {
        tblProductoPlatos.setItems(getProductoPlatos());
        colCodigoProductoPlato.setCellValueFactory(new PropertyValueFactory<ProductosPlatos, Integer>("Productos_codigoProducto"));
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<ProductosPlatos, Integer>("codigoProducto"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<ProductosPlatos, Integer>("codigoPlato"));
    }

    public void seleccionarElemento() {
        if (tipoDeOperacion == operaciones.GUARDAR) {
            JOptionPane.showMessageDialog(null, "No puedes seleccionar elementos", "AVISO", JOptionPane.WARNING_MESSAGE);
        } else {
            if (tblProductoPlatos.getSelectionModel().getSelectedItem() == null) {
                JOptionPane.showMessageDialog(null, "Seleccione una fila correcta", "AVISO", JOptionPane.WARNING_MESSAGE);
            } else {
                txtCodigoProductoPlato.setText(String.valueOf(((ProductosPlatos) tblProductoPlatos.getSelectionModel().getSelectedItem()).getProductos_codigoProducto()));
                cbxCodigoPlato.getSelectionModel().select(buscarPlato(((ProductosPlatos) tblProductoPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
                cbxCodigoProducto.getSelectionModel().select(buscarProducto(((ProductosPlatos) tblProductoPlatos.getSelectionModel().getSelectedItem()).getCodigoProducto()));

            }
        }
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

    public Producto buscarProducto(int codigoProducto) {
        Producto resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_BuscarProducto(?)");
            procedimiento.setInt(1, codigoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Producto(registro.getInt("codigoProducto"),
                        registro.getString("nombreProducto"),
                        registro.getInt("cantidadProducto"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
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
        ProductosPlatos registro = new ProductosPlatos();
        try {
            if (cbxCodigoPlato.getSelectionModel().isEmpty() || cbxCodigoProducto.getSelectionModel().isEmpty() || txtCodigoProductoPlato.getText().trim().length() == 0) {
                JOptionPane.showMessageDialog(null, "Debe de rellenar todos los campos", "AVISO", JOptionPane.WARNING_MESSAGE);
            } else {
                registro.setProductos_codigoProducto(Integer.parseInt(txtCodigoProductoPlato.getText().trim()));
                registro.setCodigoProducto(((Producto) cbxCodigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
                registro.setCodigoPlato(((Plato) cbxCodigoPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
                PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_AgregarProducto_has_Plato(?,?,?)");
                procedimiento.setInt(1, registro.getProductos_codigoProducto());
                procedimiento.setInt(2, registro.getCodigoProducto());
                procedimiento.setInt(3, registro.getCodigoPlato());
                procedimiento.execute();
                listaProductosPlatos.add(registro);
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

   /* public void editar() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                if (tblProductoPlatos.getSelectionModel().getSelectedItem() != null) {
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
                //actualizar();
                break;
        }
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EditarProducto_has_Plato(?,?,?)");
            ProductosPlatos registro = (ProductosPlatos) tblProductoPlatos.getSelectionModel().getSelectedItem();
            registro.setProductos_codigoProducto(Integer.parseInt(txtCodigoProductoPlato.getText().trim()));
            registro.setCodigoProducto(Integer.parseInt(txtCodigoProductoPlato.getText().trim()));
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
*/
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
                if (tblProductoPlatos.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Está seguro de eliminar el registro?", "Eliminar Producto Plato", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_NO_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("call sp_EliminarProducto_has_Plato(?)");
                            procedimiento.setInt(1, ((ProductosPlatos) tblProductoPlatos.getSelectionModel().getSelectedItem()).getProductos_codigoProducto());
                            procedimiento.execute();
                            listaProductosPlatos.remove(tblProductoPlatos.getSelectionModel().getSelectedIndex());
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
        txtCodigoProductoPlato.clear();
        cbxCodigoPlato.setValue(null);
        cbxCodigoProducto.setValue(null);

    }

    public void activarControles() {
        txtCodigoProductoPlato.setEditable(true);
        cbxCodigoPlato.setDisable(false);
        cbxCodigoProducto.setDisable(false);
    }

    public void desactivarControles() {
        txtCodigoProductoPlato.setEditable(false);
        cbxCodigoPlato.setDisable(true);
        cbxCodigoProducto.setDisable(true);
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
