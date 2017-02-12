package texteditor;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Optional;

class Model {

    //Main File
    private File File;

    //Is the File chosen from the FileChooser
    private boolean FileChosen;

    //For getting and setting strings from/in clipboard
    private Clipboard clipboard = Clipboard.getSystemClipboard();

    boolean isFileChosen() {
        return FileChosen;
    }

    File getFile() {
        return File;
    }

    void open(TextArea textArea) {

        try {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Text File");
            FileChooser.ExtensionFilter extensionFilter =
                    new FileChooser.ExtensionFilter("TXT Files (*txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extensionFilter);
            fileChooser.setInitialDirectory(
                    new File(System.getProperty("user.home") + "\\Documents"));

            File = fileChooser.showOpenDialog(new Stage());

            if (File != null) {

                FileChosen = true;

                textArea.clear();

                try (BufferedReader br = new BufferedReader(new FileReader(File))) {

                    for (String line = br.readLine(); line != null; line = br.readLine()) {
                        textArea.appendText(line + "\n");
                    }

                } catch (IOException e) {
                    System.out.println("Cannot open file ... " + e);
                }

            } else FileChosen = false;

        } catch (Exception e) {
            System.out.println("Invalid file ... " + e);
        }

    }

    void save(TextArea textArea) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(File))) {

            //Goes trough textArea lines and puts each of them in a String "line"
            //BufferedWriter writes the String to a file
            //then calls newLine() to separate them
            for (String line : textArea.getText().split("\\n")) {

                bw.write(line);
                bw.newLine();

            }

        } catch (Exception e) {
            System.out.println("Cannot save file ... " + e);
        }

    }

    void saveAs(TextArea textArea) {

        try {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Text File");
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TXT Files (*txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extensionFilter);
            fileChooser.setInitialDirectory(
                    new File(System.getProperty("user.home") + "\\Documents")
            );

            File = fileChooser.showSaveDialog(new Stage());

            if (File != null) {

                FileChosen = true;

                try (BufferedWriter bw = new BufferedWriter(new FileWriter(File))) {

                    //Goes trough textArea lines and puts each of them in a String "line"
                    //BufferedWriter writes the String to a file
                    //then calls newLine() to separate them
                    for (String line : textArea.getText().split("\\n")) {
                        bw.write(line);
                        bw.newLine();
                    }

                } catch (IOException e) {
                    System.out.println("Cannot save(As) file ... " + e);
                }

            } else FileChosen = false;

        } catch (Exception e) {
            System.out.println("Invalid file ... " + e);
        }

    }

    void copy(TextArea textArea) {

        ClipboardContent content = new ClipboardContent();
        content.putString(textArea.getSelectedText());
        clipboard.setContent(content);

    }

    void paste(TextArea textArea) {

        textArea.appendText(clipboard.getString());

    }

    //Popup that let's the user choose if he wants to save the file, not save it or cancel the operation
    String alert() {

        String save = "save";
        String dontSave = "don't save";
        String cancel = "cancel";

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Text Editor");

        String filename;

        if (File != null) filename = File.getName();

        else filename = "Untitled";

        alert.setHeaderText("Do you want to save changes to " + filename);

        ButtonType buttonTypeOne = new ButtonType("Save");
        ButtonType buttonTypeTwo = new ButtonType("Don't save");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {

            if (result.get() == buttonTypeOne) {
                // ... user chose "One"
                return save;

            } else if (result.get() == buttonTypeTwo) {
                // ... user chose "Two"
                return dontSave;

            } else {
                // ... user chose CANCEL or closed the dialog
                return cancel;
            }

        } else return cancel;

    }

}
