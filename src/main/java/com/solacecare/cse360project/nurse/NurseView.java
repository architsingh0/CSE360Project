package com.solacecare.cse360project.nurse;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.solacecare.cse360project.main.MainJFX;

public class NurseView {
    private VBox layout;
    private Stage stage;

    public NurseView(Stage stage) {
        this.stage = stage;
        TabPane view = new TabPane();

        Tab tabPatientVitals = new Tab("Patient Vitals");
        tabPatientVitals.setClosable(false);
        //TODO: Add components for patient vitals here...

        Tab tabPatientHistory = new Tab("Patient History");
        tabPatientHistory.setClosable(false);
        //TODO: Add components for patient history here...

        Tab tabMessages = new Tab("Messages");
        tabMessages.setClosable(false);
        //TODO: Add components for messages here...

        view.getTabs().addAll(tabPatientVitals, tabPatientHistory, tabMessages);

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> MainJFX.goToUserSelectView());

        layout = new VBox(10);
        layout.getChildren().addAll(view, logoutButton);
    }

    public VBox getView() {
        return layout;
    }
}