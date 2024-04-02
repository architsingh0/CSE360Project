package com.solacecare.cse360project.main;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginView {
    private VBox layout;
    private HBox view;
    private Stage stage;
    private String userType;

    public void loginUser() {
        switch (userType) {
            case "Doctor":
                MainJFX.goToDoctorView();
                break;
            case "Nurse":
                MainJFX.goToNurseView();
                break;
            case "Patient":
                MainJFX.goToPatientView();
                break;
            default:
                MainJFX.goToUserSelectView();
                break;
        }
    }

    public LoginView(Stage stage, String userType) {
        this.stage = stage;
        this.userType = userType;
        view = new HBox(20);
        layout = new VBox(10); // Create a VBox for vertical layout
        view.setPadding(new Insets(15, 12, 15, 12));
        layout.setPadding(new Insets(15, 12, 15, 12));

        // Form for account creation
        GridPane createAccountForm = new GridPane();
        createAccountForm.setVgap(10);
        createAccountForm.setHgap(10);

        // Form fields for account creation
        createAccountForm.add(new Label("First Name:"), 0, 0);
        createAccountForm.add(new TextField(), 1, 0);
        createAccountForm.add(new Label("Last Name:"), 0, 1);
        createAccountForm.add(new TextField(), 1, 1);
        // Add more fields as necessary

        Button createAccountButton = new Button("Create Account");
        createAccountButton.setOnAction(e -> loginUser());
        createAccountForm.add(createAccountButton, 1, 6);

        // Form for login
        GridPane loginForm = new GridPane();
        loginForm.setVgap(10);
        loginForm.setHgap(10);

        // Form fields for login
        loginForm.add(new Label("Email:"), 0, 0);
        loginForm.add(new TextField(), 1, 0);
        loginForm.add(new Label("Password:"), 0, 1);
        loginForm.add(new PasswordField(), 1, 1);

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> loginUser());
        loginForm.add(loginButton, 1, 2);

        // Back button
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> MainJFX.goToUserSelectView());

        // Add the back button to the layout
        layout.getChildren().addAll(view, backButton);

        // Add forms to the HBox
        view.getChildren().addAll(createAccountForm, loginForm);
    }

    public VBox getView() {
        return layout;
    }
}
