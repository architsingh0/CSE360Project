package com.solacecare.cse360project.main;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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

        VBox vBox = new VBox(20); // Increased spacing for visual appeal
        vBox.setAlignment(Pos.CENTER);

        // Welcome label
        Label welcomeLabel = new Label("Welcome to the Health Portal");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24)); // Large and bold font

        // Image view
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/logo.png"))); // Ensure the image path is correct
        imageView.setFitHeight(150); // Adjust the height as needed
        imageView.setPreserveRatio(true);

        // Create buttons and set their styles
        Button btnDoctor = new Button("Login as Doctor");
        btnDoctor.setStyle("-fx-background-color: deepskyblue; -fx-text-fill: white;");
        btnDoctor.setMinWidth(200); // Ensures that all buttons have the same width

        Button btnNurse = new Button("Login as Nurse");
        btnNurse.setStyle("-fx-background-color: deepskyblue; -fx-text-fill: white;");
        btnNurse.setMinWidth(200);

        Button btnPatient = new Button("Login as Patient");
        btnPatient.setStyle("-fx-background-color: deepskyblue; -fx-text-fill: white;");
        btnPatient.setMinWidth(200);

        // Define actions for buttons
        btnDoctor.setOnAction(e -> MainJFX.goToLoginView("Doctor"));
        btnNurse.setOnAction(e -> MainJFX.goToLoginView("Nurse"));
        btnPatient.setOnAction(e -> MainJFX.goToLoginView("Patient"));

        // Add components to VBox
        vBox.getChildren().addAll(imageView, welcomeLabel, btnDoctor, btnNurse, btnPatient);

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