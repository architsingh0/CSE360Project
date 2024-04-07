package com.solacecare.cse360project.doctor;

import com.solacecare.cse360project.main.MainJFX;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class DoctorView {
    private VBox layout;
    private Stage stage;

    public DoctorView() {
    }

    public void initializeComponents(){
        TabPane view = new TabPane();

        Tab tabPatientVitals = new Tab("Patient Vitals");
        tabPatientVitals.setClosable(false);
        //TODO: Add components for patient vitals here...

        Tab tabPatientHealth = new Tab("Patient Health");
        tabPatientHealth.setClosable(false);
        //TODO: Add components for patient health here...

        Tab tabPatientHistory = new Tab("Patient History");
        tabPatientHistory.setClosable(false);
        //TODO: Add components for patient history here...

        Tab tabPrescribe = new Tab("Prescribe");
        tabPrescribe.setClosable(false);
        //TODO: Add components for prescribing medication here...

        Tab tabMessages = new Tab("Messages");
        tabMessages.setClosable(false);
        //TODO: Add components for messages here...

        view.getTabs().addAll(tabPatientVitals, tabPatientHealth, tabPatientHistory, tabPrescribe, tabMessages);

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> MainJFX.goToUserSelectView());

        layout = new VBox(10);
        layout.getChildren().addAll(view, logoutButton);
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public VBox getView() {
        return layout;
    }
}