package demo;

import io.github.egormkn.LatexView;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.net.URL;

public class Demo extends Application {

    @FXML
    private LatexView latexView;

    @FXML
    private TextArea textArea;

    @FXML
    private void update(ActionEvent event) {
        latexView.setFormula(textArea.getText());
    }

    public static void main(String... args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        URL layout = ClassLoader.getSystemResource("demo/demo.fxml");
        Parent root = FXMLLoader.load(layout);
        Scene scene = new Scene(root, 640, 480);
        stage.setScene(scene);
        stage.show();
    }
}
