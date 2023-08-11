package org.rodrigodiaz.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javax.swing.JOptionPane;
import org.apache.commons.codec.cli.Digest;
import org.apache.commons.codec.digest.DigestUtils;
import org.rodrigodiaz.bean.Login;
import org.rodrigodiaz.bean.Usuario;
import org.rodrigodiaz.db.Conexion;
import org.rodrigodiaz.main.Principal;

public class LoginController implements Initializable {
    
    private Principal escenarioPrincipal;
    private ObservableList<Usuario> listaUsuario;
    
    @FXML
    private JFXPasswordField txtContrasena;
    @FXML
    private JFXTextField txtUsuarioLogin;
    @FXML
    private Button btnIniciar;
    @FXML
    private Button btnRegistrarse;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaUsuario() {
        escenarioPrincipal.ventanaUsuario();
    }
    
    public ObservableList<Usuario> getUsuario() {
        ArrayList<Usuario> lista = new ArrayList<Usuario>();
        try {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarUsuarios()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Usuario(resultado.getInt("codigoUsuario"),
                        resultado.getString("nombreUsuario"),
                        resultado.getString("apellidoUsuario"),
                        resultado.getString("usuarioLogin"),
                        resultado.getString("contrasena")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaUsuario = FXCollections.observableArrayList(lista);
    }
    
    @FXML
    private void sesion() {
        Login login = new Login();
        int x = 0;
        boolean bandera = false;
        login.setUsuarioMaster(txtUsuarioLogin.getText().trim());
        String contra = txtContrasena.getText().trim();
        String encript = DigestUtils.md5Hex(contra);
        login.setPasswordLogin(String.valueOf(encript));
        while (x < getUsuario().size()) {            
            String user = getUsuario().get(x).getUsuarioLogin();
            String pass = getUsuario().get(x).getContrasena();
            if (user.equals(login.getUsuarioMaster()) && pass.equals(login.getPasswordLogin())) {
                JOptionPane.showMessageDialog(null, "Bienvenido "+getUsuario().get(x).getNombreUsuario(), "SESIÖN INICIADA", JOptionPane.INFORMATION_MESSAGE);
                escenarioPrincipal.menuPrincipal();
                x = getUsuario().size();
                bandera = true;
            }
            x++;
        }
        if (bandera == false) {
            JOptionPane.showMessageDialog(null, "Verifique el usuario o la contraseña", "AVISO", JOptionPane.WARNING_MESSAGE);
            
        }
    }
    
}
