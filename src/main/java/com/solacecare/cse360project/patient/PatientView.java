package com.solacecare.cse360project.patient;

import com.solacecare.cse360project.main.MainJFX;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class PatientView {
    private VBox layout;
    private Stage stage;

    private Patient currentPatient;

    public PatientView() {

    }

    public void initializeComponents(){
        TabPane view = new TabPane();

        Tab tabVisits = new Tab("Visits");
        tabVisits.setClosable(false);
        //TODO: Add components for visits here...

        Tab tabEditInformation = new Tab("Edit Information");
        tabEditInformation.setClosable(false);
        //TODO: Add components for editing information here...

        Tab tabMessages = new Tab("Messages");
        tabMessages.setClosable(false);
        //TODO: Add components for messages here...

        view.getTabs().addAll(tabVisits, tabEditInformation, tabMessages);

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

    public void setPatient(Patient patient) {
        currentPatient = patient;
    }
}