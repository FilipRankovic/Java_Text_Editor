package texteditor;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Controller {

    private Model model = new Model();

    @FXML
    private TextArea textArea;

    TextArea getTextArea() {
        return textArea;
    }

    boolean textAreaChanged;

    @FXML
    protected void newFile() {
        System.out.println(textAreaChanged);
        //If there is no text in textArea, does nothing
        if (!textArea.getText().isEmpty() && textAreaChanged) {

            switch (model.alert()) {

                case "save":
                    save();
                    if (!model.isFileChosen()) return;
                    textArea.clear();
                    break;

                case "don't save":
                    textArea.clear();
                    break;

                case "cancel":
                    break;

            }

        } else textArea.clear();

    }

    @FXML
    protected void open() {

        //Prompts to save or not save a file if there is text
        if (!textArea.getText().isEmpty()) {

            switch (model.alert()) {

                case "save":
                    save();
                    if (!model.isFileChosen()) return;
                    break;

                case "don't save":
                    break;

                case "cancel":
                    return;

            }

        }

        model.open(textArea);
        textAreaChanged = false;

    }

    @FXML
    protected void save() {

        if (model.getFile() != null) {

            model.save(textArea);

        } else saveAs();

    }

    @FXML
    protected void saveAs() {

        model.saveAs(textArea);

    }

    @FXML
    protected void close() {

        if (!textArea.getText().isEmpty() && textAreaChanged) {

            switch (model.alert()) {
                case "save":
                    save();

                    if (!model.isFileChosen()) return;

                    Platform.exit();
                    break;

                case "don't save":
                    Platform.exit();
                    break;

                case "cancel":
                    break;

            }

        }else Platform.exit();

    }

    @FXML
    protected void copy() {

        model.copy(textArea);

    }

    @FXML
    protected void paste() {

        model.paste(textArea);

    }

    @FXML
    protected void delete() {

        //Deletes selected text by replacing it
        textArea.replaceSelection("");

    }


}