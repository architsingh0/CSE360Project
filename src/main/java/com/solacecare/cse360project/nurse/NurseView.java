package com.solacecare.cse360project.nurse;

import com.solacecare.cse360project.patient.PatientVisit;
import com.solacecare.cse360project.patient.PatientVisitRepository;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import com.solacecare.cse360project.main.MainJFX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;

@Component
public class NurseView {
    private VBox layout;
    private Stage stage;

    @Autowired
    PatientVisitRepository patientVisitRepository;

    public NurseView() {

    }

    public void initializeComponents(){
        TabPane view = new TabPane();

        Tab tabPatientVitals = new Tab("Patient Vitals");
        tabPatientVitals.setClosable(false);
        GridPane patientVitals = new GridPane();
        Label fNameL = new Label("First Name: ");
        TextField fNameT = new TextField();
        Label lNameL = new Label("Last Name: ");
        TextField lNameT = new TextField();
        Label datePickerL = new Label("Date of Birth: ");
        DatePicker datePicker = new DatePicker();
        Label weightL = new Label("Weight: ");
        TextField weightT = new TextField();
        Label heightL = new Label("Height: ");
        TextField heightT = new TextField();
        Label bodyTempL = new Label("Body Temp: ");
        TextField bodyTempT = new TextField();
        Label bloodPressL = new Label("Blood Pressure: ");
        TextField bloodPressT = new TextField();
        Label allergiesL = new Label("Allergies: ");
        TextArea allergiesT = new TextArea();
        Label concernsL = new Label("Concerns: ");
        TextArea concernsT = new TextArea();
        Label message = new Label();
        Button saveB = new Button("Save");
        saveB.setOnAction(event -> {
            String firstName = fNameT.getText();
            String lastName = lNameT.getText();
            LocalDate dateOfBirth =  datePicker.getValue();
            String patientIdentifier = firstName.substring(0,3) + lastName.substring(0,3) + dateOfBirth.getMonthValue() + dateOfBirth.getDayOfMonth() + dateOfBirth.getYear();
            int age = Period.between(dateOfBirth, LocalDate.now()).getYears();
            try {
                if (age > 12) {
                    // Ensure conversion from String to float is safe
                    double weight = Double.parseDouble(weightT.getText());
                    double height = Double.parseDouble(heightT.getText());
                    double bodyTemperature = Double.parseDouble(bodyTempT.getText());
                    double bloodPressure = Double.parseDouble(bloodPressT.getText());
                    String allergies = allergiesT.getText();
                    String concerns = concernsT.getText();

                    patientVisitRepository.save(new PatientVisit(dateOfBirth, LocalTime.now(), patientIdentifier, weight, height, bodyTemperature, bloodPressure, allergies + "\n" + concerns));

                    message.setTextFill(Color.LIMEGREEN);
                    message.setText("Patient with identifier: " + patientIdentifier + " saved successfully");
                } else {
                    throw new Exception("The patient is NOT over the age of 12\nPatient's age is: " + age);
                }
            }
            catch (NumberFormatException e){
                message.setTextFill(Color.RED);
                message.setText("Please enter correct numerical values");
            }
            catch (Exception e){
                message.setTextFill(Color.RED);
                message.setText(e.getMessage());
            }
        });

        patientVitals.add(fNameL, 0, 0);
        patientVitals.add(fNameT, 1, 0);
        patientVitals.add(lNameL, 0, 1);
        patientVitals.add(lNameT, 1, 1);
        patientVitals.add(datePickerL, 0, 2);
        patientVitals.add(datePicker, 1, 2);
        patientVitals.add(weightL, 0, 3);
        patientVitals.add(weightT, 1, 3);
        patientVitals.add(heightL, 0, 4);
        patientVitals.add(heightT, 1, 4);
        patientVitals.add(bodyTempL, 0, 5);
        patientVitals.add(bodyTempT, 1, 5);
        patientVitals.add(bloodPressL, 0, 6);
        patientVitals.add(bloodPressT, 1, 6);
        patientVitals.add(allergiesL, 0, 7);
        patientVitals.add(allergiesT, 1, 7);
        patientVitals.add(concernsL, 0, 8);
        patientVitals.add(concernsT, 1, 8);
        patientVitals.add(saveB, 0, 9);
        patientVitals.add(message, 0, 10);


        tabPatientVitals.setContent(patientVitals);

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

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public VBox getView() {
        return layout;
    }
}