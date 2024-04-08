package com.solacecare.cse360project.nurse;

import com.solacecare.cse360project.generic.Message;
import com.solacecare.cse360project.generic.MessageRepository;
import com.solacecare.cse360project.generic.User;
import com.solacecare.cse360project.generic.UserRepository;
import com.solacecare.cse360project.patient.PatientService;
import com.solacecare.cse360project.patient.PatientVisit;
import com.solacecare.cse360project.patient.Prescription;
import com.solacecare.cse360project.patient.PatientVisitRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import com.solacecare.cse360project.main.MainJFX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class NurseView {
    private VBox layout;
    private Stage stage;
    private ObservableList<Message> messagePreviews;
    TextArea messageDetailView;
    private Nurse currentNurse;

    private String patientIdentifier;

    @Autowired
    PatientVisitRepository patientVisitRepository;

    @Autowired
    PatientService patientService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageRepository messageRepository;

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
            this.patientIdentifier = firstName.substring(0,3) + lastName.substring(0,3) + dateOfBirth.getMonthValue() + dateOfBirth.getDayOfMonth() + dateOfBirth.getYear();
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

                    patientVisitRepository.saveAndFlush(new PatientVisit(dateOfBirth, LocalDate.now(), LocalTime.now(), patientIdentifier, weight, height, bodyTemperature, bloodPressure, "\n" + allergies + "\n" + concerns));

                    message.setTextFill(Color.LIMEGREEN);
                    message.setText("Patient with identifier: " + patientIdentifier + " saved successfully");
                    patientService.associateVisitsWithPatient(patientIdentifier);
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
        VBox patientVisits = new VBox();
        patientVisits.setSpacing(10);
        patientVisits.setPadding(new Insets(10));
        TextField searchField = new TextField();
        if(patientIdentifier != null){
            searchField.setText(patientIdentifier);
        }
        searchField.setPromptText("Enter Patient Identifier");
        Accordion accordion = new Accordion();
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> searchPatientVisits(accordion, searchField));

        patientVisits.getChildren().addAll(searchField, searchButton, accordion);

        tabPatientHistory.setContent(patientVisits);


        Tab tabMessages = new Tab("Messages");
        tabMessages.setClosable(false);
        //TODO: Add components for messages here...
        BorderPane root = new BorderPane();
        HBox topBar = new HBox();
        Button composeButton = new Button("Compose");
        Button refreshButton = new Button("Refresh");
        topBar.getChildren().addAll(composeButton, refreshButton);
        root.setTop(topBar);

        // SplitPane for message list and detail view
        SplitPane mainArea = new SplitPane();
        messagePreviews = FXCollections.observableArrayList();
        ListView<Message> messageListView = new ListView<>(messagePreviews);
        messageListView.setCellFactory(lv -> new ListCell<Message>() {
            @Override
            protected void updateItem(Message message, boolean empty) {
                super.updateItem(message, empty);
                if (empty || message == null) {
                    setText(null);
                } else {
                    setText(message.getTitle() + " - " + message.getContent().substring(0, Math.min(message.getContent().length(), 50)) + "...");
                }
            }
        });

        messageDetailView = new TextArea();
        VBox messageListContainer = new VBox(new Label("Messages"), messageListView);
        VBox messageDetailContainer = new VBox(new Label("Message Detail"), messageDetailView);
        mainArea.getItems().addAll(messageListContainer, messageDetailContainer);
        root.setCenter(mainArea);

        composeButton.setOnAction(e -> openComposeDialog());
        refreshButton.setOnAction(e -> refreshMessages());

        messageListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                displayMessageDetails(newSelection.getId());
            }
        });


        view.getTabs().addAll(tabPatientVitals, tabPatientHistory, tabMessages);

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> MainJFX.goToUserSelectView());

        layout = new VBox(10);
        layout.getChildren().addAll(view, logoutButton);
        tabMessages.setContent(root);
    }

    private void searchPatientVisits(Accordion accordion, TextField searchField) {
        accordion.getPanes().clear(); // Clear previous results
        String identifier = searchField.getText();

        List<PatientVisit> patientVisits = patientVisitRepository.findByPatientIdentifier(identifier);

        if (patientVisits != null && !patientVisits.isEmpty()) {
            // Grouping by date and time
            Map<String, List<PatientVisit>> visitsByDateTime = patientVisits.stream()
                    .collect(Collectors.groupingBy(visit -> visit.getDate().toString() + " " + visit.getTime().toString()));

            visitsByDateTime.forEach((dateTime, visits) -> {
                VBox contentBox = new VBox(5);
                for (PatientVisit visit : visits) {
                    // Format the patient visit details
                    String visitDetails = formatPatientVisitDetails(visit);
                    Label detailsLabel = new Label(visitDetails);
                    contentBox.getChildren().add(detailsLabel);
                }

                TitledPane titledPane = new TitledPane(dateTime, contentBox);
                accordion.getPanes().add(titledPane);
            });
        }
    }


    private String formatPatientVisitDetails(PatientVisit visit) {
        return String.format("Time: %s\nWeight: %.2f\nHeight: %.2f\nBody Temp: %.2f\nBlood Pressure: %.2f\nNurse Notes: %s\nSymptoms: %s\nDoctor's Notes: %s\nPrescriptions: %s",
                visit.getTime().toString(),
                visit.getWeight(),
                visit.getHeight(),
                visit.getBodyTemp(),
                visit.getBloodPressure(),
                visit.getNurseNotes(),
                visit.getSymptoms(),
                visit.getDrNotes(),
                visit.getPrescriptions().stream().map(Prescription::toString).collect(Collectors.joining(", ")));
    }

    private void openComposeDialog() {
        Dialog<Message> dialog = new Dialog<>();
        dialog.setTitle("Compose Message");

        // Setup form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField recipientField = new TextField();
        TextField titleField = new TextField();
        TextArea messageBodyField = new TextArea();

        grid.add(new Label("Recipient:"), 0, 0);
        grid.add(recipientField, 1, 0);
        grid.add(new Label("Title:"), 0, 1);
        grid.add(titleField, 1, 1);
        grid.add(new Label("Message:"), 0, 2);
        grid.add(messageBodyField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        ButtonType sendButtonType = new ButtonType("Send", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(sendButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == sendButtonType) {
                Message message = new Message();
                message.setTitle(titleField.getText());
                message.setContent(messageBodyField.getText());
                User recipient = userRepository.findByEmail(recipientField.getText());
                message.setRecipient(recipient);
                message.setSender(currentNurse);
                messageRepository.save(message);
                return message;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(message -> {
            System.out.println("Message sent: " + message.getTitle());
        });
    }



    private void refreshMessages() {
        List<Message> messages = Stream.concat(
                messageRepository.findBySenderEmail(currentNurse.getEmail()).stream(),
                messageRepository.findByRecipientEmail(currentNurse.getEmail()).stream()
        ).distinct().collect(Collectors.toList());

        messagePreviews.clear();
        messagePreviews.addAll(messages);
    }




    private void displayMessageDetails(Long messageId) {
        messageRepository.findById(messageId).ifPresent(message -> {
            String details = String.format("Title: %s\nFrom: %s\nTo: %s\n\n%s",
                    message.getTitle(),
                    message.getSender().getEmail(),
                    message.getRecipient().getEmail(),
                    message.getContent());
            messageDetailView.setText(details);
        });
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void setCurrentNurse(Nurse nurse){
        currentNurse = nurse;
    }

    public VBox getView() {
        return layout;
    }
}