package texteditor;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;

public class Controller {

    boolean textAreaChanged;
    private Model model = new Model();
    @FXML
    private TextArea textArea;
    @FXML
    private MenuBar menuBar;
    @FXML
    private ColorPicker backgroundColor;
    @FXML
    private ColorPicker borderColor;
    @FXML
    private ColorPicker textColor;

    TextArea getTextArea() {
        return textArea;
    }

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

    /**
     * Lets the user choose his color preference with ColorPicker.
     * Has a bug where clicking on custom color gets a null exception.
     * Probably a conflict with the Menu container or the Menu Item itself.
     * Should also have a file in which to store/save the color preference.
     */
    @FXML
    protected void changeColor() {

        System.out.println("Mark 1");
        String color = String.valueOf(backgroundColor.getValue());
        color = color.substring(2, color.length() - 2);
        String value = "-fx-control-inner-background: #" + color;

        System.out.println("Mark 2");
        String color1 = String.valueOf(borderColor.getValue());
        color1 = color1.substring(2, color1.length() - 2);
        String value1 = "-fx-background-color: #" + color1;

        System.out.println("Mark 3");
        String color2 = String.valueOf(textColor.getValue());
        color2 = color2.substring(2, color2.length() - 2);
        String value2 = "-fx-text-fill: #" + color2;

        System.out.println("Mark 4");
        menuBar.setStyle(value1);
        textArea.setStyle(value + "; " + value1 + "; " + value2 + ";");

    }

}