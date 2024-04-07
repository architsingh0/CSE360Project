package com.solacecare.cse360project.main;

import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class UserSelectView {
    private BorderPane view;
    private Stage stage;

    public UserSelectView() {

    }

    public void initializeComponents(){
        view = new BorderPane();

        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.CENTER);

        // Create buttons
        Button btnDoctor = new Button("Login as Doctor");
        btnDoctor.setOnAction(e -> MainJFX.goToLoginView("Doctor"));

        Button btnNurse = new Button("Login as Nurse");
        btnNurse.setOnAction(e -> MainJFX.goToLoginView("Nurse"));

        Button btnPatient = new Button("Login as Patient");
        btnPatient.setOnAction(e -> MainJFX.goToLoginView("Patient"));

        // Add buttons to VBox
        vBox.getChildren().addAll(btnDoctor, btnNurse, btnPatient);

        // Set the VBox to be the center of the BorderPane
        view.setCenter(vBox);

        BorderPane.setAlignment(vBox, Pos.CENTER);
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public BorderPane getView() {
        return view;
    }
}