package org.rodrigodiaz.controller;

import com.mysql.jdbc.MysqlDataTruncation;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.awt.Toolkit;
import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.apache.commons.codec.cli.Digest;
import org.apache.commons.codec.digest.DigestUtils;
import org.rodrigodiaz.bean.Usuario;
import org.rodrigodiaz.db.Conexion;
import org.rodrigodiaz.main.Principal;

public class UsuarioController implements Initializable {

    private Principal escenarioPrincipal;

    private enum operaciones {
        GUARDAR, NINGUNO
    }
    private operaciones tipoDeOperacion = operaciones.NINGUNO;

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtContraseña;
    @FXML
    private PasswordField txtRepetirContraseña;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEliminar;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgEliminar;

    public UsuarioController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtContraseña.setEditable(false);
        txtRepetirContraseña.setEditable(false);
    }

    public void nuevo() {
        switch (tipoDeOperacion) {
            case NINGUNO:
                limpiarControles();
                activarControles();
                
                btnNuevo.setText("GUARDAR     ");
                btnEliminar.setText("CANCELAR     ");
                imgNuevo.setImage(new Image("/org/rodrigodiaz/image/iconoGuardar.png"));
                imgEliminar.setImage(new Image("/org/rodrigodiaz/image/iconoCancelar.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                break;

        }
    }

    public void eliminar() {
        switch (tipoDeOperacion) {
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("NUEVO         ");
                btnEliminar.setText("CANCELAR     ");
                imgNuevo.setImage(new Image("/org/rodrigodiaz/image/iconoAgregar.png"));
                imgEliminar.setImage(new Image("/org/rodrigodiaz/image/iconoCancelar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }

    public void guardar() {
        Usuario registro = new Usuario();
        try {
            if (txtNombre.getText().trim().length() == 0 || txtApellido.getText().trim().length() == 0 || txtUsuario.getText().trim().length() == 0 || txtContraseña.getText().trim().length() == 0 || txtRepetirContraseña.getText().trim().length() == 0) {
                JOptionPane.showMessageDialog(null, "Debe de rellenar todos los campos", "AVISO", JOptionPane.WARNING_MESSAGE);

            } else {
                registro.setNombreUsuario(txtNombre.getText().trim());
                registro.setApellidoUsuario(txtApellido.getText().trim());
                registro.setUsuarioLogin(txtUsuario.getText().trim());
                String contra = txtContraseña.getText().trim();
                String encript = DigestUtils.md5Hex(contra);
                registro.setContrasena(encript);
                if (String.valueOf(txtContraseña.getText().trim()).equals(String.valueOf(txtRepetirContraseña.getText().trim()))) {
                    PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarUsuario(?,?,?,?)}");
                    procedimiento.setString(1, registro.getNombreUsuario());
                    procedimiento.setString(2, registro.getApellidoUsuario());
                    procedimiento.setString(3, registro.getUsuarioLogin());
                    procedimiento.setString(4, registro.getContrasena());
                    procedimiento.execute();
                    postGuardar();
                } else {
                    JOptionPane.showMessageDialog(null, "La contraseña no es igual", "AVISO", JOptionPane.WARNING_MESSAGE);
                }

            }

        } catch (MysqlDataTruncation error) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Verifique el número de carácteres", "AVISO", JOptionPane.WARNING_MESSAGE);
        } catch (java.lang.NumberFormatException e) {
            Toolkit.getDefaultToolkit().beep();
            JOptionPane.showMessageDialog(null, "Valor Incorrecto", "AVISO", JOptionPane.WARNING_MESSAGE);
        } catch (MySQLIntegrityConstraintViolationException error) {
            Toolkit.getDefaultToolkit().beep();

            JOptionPane.showMessageDialog(null, "El usuario ya existe", "AVISO", JOptionPane.WARNING_MESSAGE);
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void login() {
        escenarioPrincipal.login();
    }

    public void desactivarControles() {
        txtNombre.setEditable(false);
        txtApellido.setEditable(false);
        txtUsuario.setEditable(false);
        txtContraseña.setEditable(false);
        txtRepetirContraseña.setEditable(false);
    }

    public void activarControles() {
        txtNombre.setEditable(true);
        txtApellido.setEditable(true);
        txtUsuario.setEditable(true);
        txtContraseña.setEditable(true);
        txtRepetirContraseña.setEditable(true);
    }

    public void limpiarControles() {
        txtNombre.clear();
        txtApellido.clear();
        txtUsuario.clear();
        txtContraseña.clear();
        txtRepetirContraseña.clear();
    }

    public void postGuardar() {
        limpiarControles();
        desactivarControles();
        btnNuevo.setText("NUEVO         ");
        btnEliminar.setText("ELIMINAR     ");
        imgNuevo.setImage(new Image("/org/rodrigodiaz/image/iconoAgregar.png"));
        imgEliminar.setImage(new Image("/org/rodrigodiaz/image/iconoEliminar.png"));
        tipoDeOperacion = operaciones.NINGUNO;
        login();
    }
}
