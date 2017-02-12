package texteditor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("TextEditorView.fxml"));
        Parent root = loader.load();
        primaryStage.getIcons().add(new Image("res/icon.png"));
        primaryStage.setTitle("Text Editor");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();

        Controller controller = loader.getController();

        //Checks for changes to textArea
        controller.getTextArea().textProperty().addListener((anyChange) -> controller.textAreaChanged = true);

        primaryStage.setOnCloseRequest(event -> controller.close());

    }
}
