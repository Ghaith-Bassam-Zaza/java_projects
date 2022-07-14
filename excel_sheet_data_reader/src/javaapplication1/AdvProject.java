/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package javaapplication1;

/**
 *
 * @author Ghaith
 */
import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AdvProject extends Application {
    TextField textField;
    TextArea textArea;
    int depth = 1;
    FileChooser fileChooser;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        fileChooser=new FileChooser();
        stage.setTitle("advProject");
        stage.setWidth(750);
        stage.setHeight(800);

        Label label = new Label("Path:");

        textField = new TextField();
        textField.setPrefWidth(600);

        textArea = new TextArea();
        textArea.setPrefSize(700, 700);
        textArea.setEditable(false);

        Button button = new Button("Browse");
        button.setOnAction(e -> Browse());
        
        VBox vb = new VBox();
        HBox hb = new HBox();
        hb.getChildren().addAll(label, textField, button);
        vb.getChildren().addAll(textArea);
        hb.setSpacing(10);
        vb.setSpacing(10);
        hb.setPadding(new Insets(10, 10, 10, 10));
        vb.setPadding(new Insets(50, 10, 10, 10));
        Group g = new Group(vb,hb);
        Scene scene = new Scene(g);

        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

    }

    private void Browse() {
        
        File file = fileChooser.showOpenDialog(new Stage());
        textField.setText(String.valueOf(file.getPath()));
        //System.out.println(String.valueOf(fileChooser.getInitialDirectory()));
        String lt = textField.getText();
        String link = new File(lt).getAbsolutePath();
        Iterator iter = sheetReader.read(link);

        Service service = sheetReader.analyse(iter);
        Iterator<Operation> operations = service.getOperations();
        textArea.setText(null);
        while (operations.hasNext()) {
            Operation op = operations.next();
            textArea.appendText(op.getName() + "\nHTTP_opertion:\t" + op.get_HTTP_opertion() + "\nREST_URL:\t" + op.get_REST_URL());
            iterFields(op);
            textArea.appendText("end API \n");
            textArea.appendText("\n----------------------------------------------------------------------------\n");
        }
        textArea.appendText("*************Program Finish***********\n");
        textArea.appendText("Thank You For Testing");
    }

    void iterFields(FieldsCollection FC) {

        Iterator<Field> fields = FC.getFields();
        while (fields.hasNext()) {
            Field field = fields.next();
            printField(field);
            if (field instanceof ObjField objF) {
                for (int i = 0; i < depth; i++) {
                    textArea.appendText("\t");
                }
                textArea.appendText(field.getName() + ":" + "\n");
                depth++;
                iterFields(objF);
                depth--;
                for (int i = 0; i < depth; i++) {
                    textArea.appendText("\t");
                }
                textArea.appendText("end " + field.getName() + "\n\n");
            }
        }
    }

    void printField(Field F) {
        if (F == null) {
            return;
        }
        if (F instanceof StringField f) {
            for (int i = 0; i < depth; i++) {
                textArea.appendText("\t");
            }
            textArea.appendText(F.getDirection() + "\t" + F.getName() + "\t" + "string" + "\t" + Arrays.toString(f.getAllowedVals()) + "\t" + (F.isMandatory() ? "y" : "N") + "\n");
        } else {
            textArea.appendText("\n");
            for (int i = 0; i < depth; i++) {
                textArea.appendText("\t");
            }
            textArea.appendText(F.getDirection() + "\t" + F.getName() + "\t" + "object" + "\t \t" + (F.isMandatory() ? "y" : "N") + "\n");

        }
    }
}
