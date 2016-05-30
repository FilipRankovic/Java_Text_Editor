package texteditor;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.util.Optional;

public class Controller {

    @FXML
    private TextArea textArea;

    //Main File
    //Initialized in open() and saveAs() methods
    private File mFile;

    //Is the File chosen form the FileChooser
    private boolean isFileChosen;

    @FXML
    protected void newFile() {

        if (textArea.getText().isEmpty());
            //Does nothing, file is already empty
        else {

            switch (alert()) {

                case "save":
                    save();
                    if (!isFileChosen) return;
                    textArea.clear();
                    break;

                case "don't save":
                    textArea.clear();
                    break;

                case "cancel":
                    break;
            }
        }
    }

    @FXML
    protected void open() {

        if (!textArea.getText().isEmpty()) {

            switch (alert()) {

                case "save":
                    save();
                    if (!isFileChosen) return;
                    break;

                case "don't save":
                    break;

                case "cancel":
                    return;

            }

        }

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Text File");
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TXT Files (*txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extensionFilter);
            fileChooser.setInitialDirectory(
                    new File(System.getProperty("user.home") + "\\Documents")
            );

            mFile = fileChooser.showOpenDialog(new Stage());

            if (mFile != null) {

                isFileChosen = true;
                textArea.clear();

                try (BufferedReader br = new BufferedReader(new FileReader(mFile))) {

                    for(String line = br.readLine(); line != null; line = br.readLine()) {
                        textArea.appendText(line + "\n");
                    }

                } catch (Exception e) {
                    System.out.println(e);
                }

            } else isFileChosen = false;

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    @FXML
    protected void save() {

        if (mFile != null) {

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(mFile))) {

                for (String line : textArea.getText().split("\\n")) {
                    bw.write(line);
                    bw.newLine();
                }

            } catch (Exception e) {
                System.out.println(e);
            }

        } else saveAs();

    }

    @FXML
    protected void saveAs() {

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Text File");
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TXT Files (*txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extensionFilter);
            fileChooser.setInitialDirectory(
                    new File(System.getProperty("user.home") + "\\Documents")
            );

            mFile = fileChooser.showSaveDialog(new Stage());

            if (mFile != null) {

                isFileChosen = true;

                try (BufferedWriter bw = new BufferedWriter(new FileWriter(mFile))){

                    for (String line : textArea.getText().split("\\n")) {
                        bw.write(line);
                        bw.newLine();
                    }

                } catch (Exception e) {
                    System.out.println(e);
                }
            } else isFileChosen = false;

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    @FXML
    protected void close() {

        if (!textArea.getText().isEmpty()) {

            switch (alert()) {
                case "save":
                    save();

                    if (!isFileChosen) return;

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

    //Popup that let's the user choose if he wants to save the file, not save it or cancel the operation
    //It's returning the users choice in a form of a String
    //The method is used in newFile() and cancel() methods
    private String alert() {

        String save = "save";
        String dontSave = "don't save";
        String cancel = "cancel";

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Text Editor");

        String filename;

        if (mFile != null) filename = mFile.getName();
        else filename = "Untitled";

        alert.setHeaderText("Do you want to save changes to " + filename);

        ButtonType buttonTypeOne = new ButtonType("Save");
        ButtonType buttonTypeTwo = new ButtonType("Don't save");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne){
            // ... user chose "One"
            return save;

        } else if (result.get() == buttonTypeTwo) {
            // ... user chose "Two"
            return dontSave;

        }  else {
            // ... user chose CANCEL or closed the dialog
            return cancel;
        }

    }

    @FXML
    protected void copy() {

        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();

        content.putString(textArea.getSelectedText());
        clipboard.setContent(content);

    }

    @FXML
    protected void paste() {

        final ClipboardContent content = new ClipboardContent();

        //TODO: Fix paste
        if (content.hasString()) {
            textArea.appendText(content.getString());
        }

    }

}