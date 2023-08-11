package org.rodrigodiaz.controller;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import org.rodrigodiaz.main.Principal;


public class ProgramadorController implements Initializable {
    private Principal escenarioPrincipal;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public void openLinkGit() throws URISyntaxException, IOException{
        Desktop.getDesktop().browse(new URI("https://github.com/rdiaz-2022178"));
    }
    
    public void openLinkIg() throws URISyntaxException, IOException{
        Desktop.getDesktop().browse(new URI("https://instragram.com/rodrigo.edg"));
    }
    
    public void openLinkFb() throws URISyntaxException, IOException{
        Desktop.getDesktop().browse(new URI("https://facebook.com/"));
    }
    
    public void openLinkLinkedin() throws URISyntaxException, IOException{
        Desktop.getDesktop().browse(new URI("https://linkedin.com"));
    }
    
        
}
