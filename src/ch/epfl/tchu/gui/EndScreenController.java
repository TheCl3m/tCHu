package ch.epfl.tchu.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class EndScreenController implements Initializable {

    @FXML
    private Text text;

    public void setText(String message) {
        text.setText(message);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        text.setText("");
    }
}
