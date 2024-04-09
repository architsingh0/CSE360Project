package com.solacecare.cse360project.doctor;

import com.solacecare.cse360project.generic.Message;
import com.solacecare.cse360project.generic.MessageRepository;
import com.solacecare.cse360project.generic.User;
import com.solacecare.cse360project.generic.UserRepository;
import com.solacecare.cse360project.main.MainJFX;
import com.solacecare.cse360project.patient.PatientVisit;
import com.solacecare.cse360project.patient.PatientVisitRepository;
import com.solacecare.cse360project.patient.Prescription;
import com.solacecare.cse360project.patient.PrescriptionRepository;
import jakarta.transaction.Transactional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DoctorView {
    private BorderPane layout;
    private Stage stage;
    private Doctor currentDoctor;
    private ObservableList<Message> messagePreviews;
    private VBox messageDetailContainer;
    private VBox repliesContainer;
    TextArea messageDetailView;

    @Autowired
    private PatientVisitRepository patientVisitRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    public DoctorView() {
    }

    public void initializeComponents() {
        layout = new BorderPane();
        layout.setPadding(new Insets(10));
        TabPane view = new TabPane();

        HBox topBar = new HBox();
        topBar.setAlignment(Pos.TOP_RIGHT);
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> MainJFX.goToUserSelectView());

        Label nurseNameLabel = new Label();
        nurseNameLabel.setText("Hello ".concat(currentDoctor.getFirstName().concat(" ").concat(currentDoctor.getLastName())));
        topBar.getChildren().addAll(nurseNameLabel, logoutButton);
        HBox.setMargin(logoutButton, new Insets(0, 0, 0, 10));

        Tab patientVisitHistory = new Tab("Patient Visit History");
        patientVisitHistory.setClosable(false);
        //TODO: Add components for patient vitals here...
        setupPatientHistoryTab(patientVisitHistory);


        Tab physicalExam = new Tab("Physical Exam");
        physicalExam.setClosable(false);
        //TODO: Add components for patient health here...
        setupPhysicalExamTab(physicalExam);

        Tab tabPrescribe = new Tab("Prescribe");
        tabPrescribe.setClosable(false);
        //TODO: Add components for prescribing medication here...
        setupPrescribeTab(tabPrescribe);

        Tab tabMessages = new Tab("Messages");
        tabMessages.setClosable(false);
        //TODO: Add components for messages here...
        setupMessagesTab(tabMessages);

        view.getTabs().addAll(patientVisitHistory, physicalExam, tabPrescribe, tabMessages);

        logoutButton.setOnAction(e -> MainJFX.goToUserSelectView());

        layout.setTop(topBar);
        layout.setCenter(view);
        VBox.setVgrow(view, Priority.ALWAYS);
    }

    private void setupPatientHistoryTab(Tab tab) {
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10));
        TextField searchField = new TextField();
        searchField.setPromptText("Enter Patient Identifier");
        Accordion accordion = new Accordion();
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            accordion.getPanes().clear();
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
        });

        vBox.getChildren().addAll(searchField, searchButton, accordion);
        tab.setContent(vBox);
    }

    private void setupPhysicalExamTab(Tab physicalExam) {
        VBox layout = new VBox(10);
        TextField patientIdentifierField = new TextField();
        patientIdentifierField.setPromptText("Enter patient identifier");
        Button searchButton = new Button("Search");
        GridPane detailsGrid = new GridPane();
        detailsGrid.setVgap(10);
        detailsGrid.setHgap(10);
        searchButton.setOnAction(e -> {
            String patientIdentifier = patientIdentifierField.getText();
            List<PatientVisit> visits = patientVisitRepository.findByPatientIdentifier(patientIdentifier);
            if (!visits.isEmpty()) {
                PatientVisit latestVisit = visits.get(visits.size() - 1); // Assuming the list is ordered by date
                TextField symptomsField = new TextField(latestVisit.getSymptoms());
                TextArea drNotesArea = new TextArea(latestVisit.getDrNotes());
                Button saveButton = new Button("Save");
                saveButton.setOnAction(ev -> {
                    latestVisit.setSymptoms(symptomsField.getText());
                    latestVisit.setDrNotes(drNotesArea.getText());
                    patientVisitRepository.save(latestVisit);
                });
                detailsGrid.getChildren().clear(); // Clear previous search
                detailsGrid.addRow(0, new Label("Symptoms:"), symptomsField);
                detailsGrid.addRow(1, new Label("Doctor's Notes:"), drNotesArea);
                detailsGrid.add(saveButton, 1, 2);
            }
        });
        layout.getChildren().addAll(new Label("Search Patient:"), patientIdentifierField, searchButton, detailsGrid);
        physicalExam.setContent(layout);
    }

    private void setupPrescribeTab(Tab tabPrescribe) {
        VBox layout = new VBox(10);
        TextField patientIdentifierField = new TextField();
        patientIdentifierField.setPromptText("Enter patient identifier");
        Button searchButton = new Button("Search");
        ListView<Prescription> prescriptionsList = new ListView<>();
        prescriptionsList.setCellFactory(param -> new ListCell<Prescription>() {
            @Override
            protected void updateItem(Prescription item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
            }
        });
        searchButton.setOnAction(e -> {
            String patientIdentifier = patientIdentifierField.getText();
            List<Prescription> prescriptions = prescriptionRepository.findAllByPatientVisitPatientIdentifier(patientIdentifier);
            prescriptionsList.getItems().clear();
            prescriptionsList.getItems().addAll(prescriptions);
        });
        Button prescribeButton = new Button("Prescribe New Medicine");

        prescribeButton.setOnAction(e -> {
            String patientIdentifier = patientIdentifierField.getText();
            List<PatientVisit> visits = patientVisitRepository.findByPatientIdentifier(patientIdentifier);
            if (visits.isEmpty()) {
                return;
            }
            PatientVisit latestVisit = visits.get(visits.size() - 1);

            Dialog<Prescription> dialog = new Dialog<>();
            dialog.setTitle("Prescribe New Medicine");
            dialog.setHeaderText("Enter prescription details");

            ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField pharmacyAddr = new TextField();
            TextField medicineName = new TextField();
            TextField medicineDosage = new TextField();
            TextField drSignature = new TextField();

            grid.add(new Label("Pharmacy Address:"), 0, 0);
            grid.add(pharmacyAddr, 1, 0);
            grid.add(new Label("Medicine Name:"), 0, 1);
            grid.add(medicineName, 1, 1);
            grid.add(new Label("Medicine Dosage:"), 0, 2);
            grid.add(medicineDosage, 1, 2);
            grid.add(new Label("Doctor's Signature:"), 0, 3);
            grid.add(drSignature, 1, 3);

            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == saveButtonType) {
                    Prescription newPrescription = new Prescription();
                    newPrescription.setPharmacyAddr(pharmacyAddr.getText());
                    newPrescription.setMedicineName(medicineName.getText());
                    newPrescription.setMedicineDosage(medicineDosage.getText());
                    newPrescription.setDrSignature(drSignature.getText());
                    newPrescription.setPatientVisit(latestVisit);
                    return newPrescription;
                }
                return null;
            });

            Optional<Prescription> result = dialog.showAndWait();
            result.ifPresent(prescription -> {
                prescriptionRepository.save(prescription);
                prescriptionsList.getItems().add(prescription);
            });
        });

        layout.getChildren().addAll(new Label("Search Patient:"), patientIdentifierField, searchButton, prescriptionsList, prescribeButton);
        tabPrescribe.setContent(layout);
    }

    private void setupMessagesTab(Tab tab) {
        BorderPane root = new BorderPane();
        HBox topBar = new HBox();
        Button composeButton = new Button("Compose");
        Button refreshButton = new Button("Refresh");
        topBar.getChildren().addAll(composeButton, refreshButton);
        root.setTop(topBar);

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
        Button replyButton = new Button("Reply");
        replyButton.setOnAction(e -> {
            if (messageListView.getSelectionModel().getSelectedItem() != null) {
                openReplyDialog(messageListView.getSelectionModel().getSelectedItem());
            }
        });
        messageDetailContainer = new VBox(new Label("Message Detail"), messageDetailView, replyButton);

        mainArea.getItems().addAll(messageListContainer, messageDetailContainer);
        root.setCenter(mainArea);

        composeButton.setOnAction(e -> openComposeDialog());
        refreshButton.setOnAction(e -> refreshMessages());

        messageListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                messageDetailContainer.getChildren().remove(repliesContainer);
                displayMessageDetails(newSelection.getId());
            }
        });
        refreshMessages();
        tab.setContent(root);
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
                message.setSender(currentDoctor);
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
        List<Message> messages = messageRepository.findByRecipientEmail(currentDoctor.getEmail());

        messagePreviews.clear();
        messagePreviews.addAll(messages);
    }

    @Transactional
    public void displayMessageDetails(Long messageId) {
        messageRepository.findById(messageId).ifPresent(message -> {
            String details = String.format("Title: %s\nFrom: %s\nTo: %s\n\n%s",
                    message.getTitle(),
                    message.getSender().getEmail(),
                    message.getRecipient().getEmail(),
                    message.getContent());
            messageDetailView.setText(details);
            messageDetailContainer.getChildren().remove(repliesContainer);
            repliesContainer = null;
            repliesContainer = new VBox();
            if (message.getReplies() != null) {
                message.getReplies().forEach(reply -> {
                    String replyText = String.format("From: %s\n%s",
                            reply.getSender().getEmail(),
                            reply.getContent());
                    Label replyLabel = new Label(replyText);
                    repliesContainer.getChildren().add(replyLabel);
                });
            }

            messageDetailContainer.getChildren().add(repliesContainer);
        });
    }

    private void openReplyDialog(Message originalMessage) {
        Dialog<Message> dialog = new Dialog<>();
        dialog.setTitle("Reply to Message");

        // Setup form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextArea messageBodyField = new TextArea();

        grid.add(new Label("Message:"), 0, 0);
        grid.add(messageBodyField, 1, 0);

        dialog.getDialogPane().setContent(grid);
        ButtonType sendButtonType = new ButtonType("Send", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(sendButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == sendButtonType) {
                Message reply = new Message();
                reply.setContent(messageBodyField.getText());
                reply.setTitle("Re: " + originalMessage.getTitle());
                reply.setRecipient(originalMessage.getSender());
                reply.setSender(currentDoctor);
                // Set the original message as the parent of this reply
                reply.setParentMessage(originalMessage);
                messageRepository.save(reply);
                return reply;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(message -> {
            System.out.println("Reply sent: " + message.getTitle());
            refreshMessages();
        });
    }

    private String formatPatientVisitDetails(PatientVisit visit) {
        return String.format("Time: %s\nWeight: %.2f\nHeight: %.2f\nBody Temp: %.2f\nBlood Pressure: %.2f\nImmunization Record: %s\nNurse Notes: %s\nSymptoms: %s\nDoctor's Notes: %s\nPrescriptions: %s",
                visit.getTime().toString(),
                visit.getWeight(),
                visit.getHeight(),
                visit.getBodyTemp(),
                visit.getBloodPressure(),
                visit.getImmunizationRecord(),
                visit.getNurseNotes(),
                visit.getSymptoms(),
                visit.getDrNotes(),
                visit.getPrescriptions().stream().map(Prescription::toString).collect(Collectors.joining(", ")));
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public BorderPane getView() {
        return layout;
    }

    public void setDoctor(Doctor doctor) {
        this.currentDoctor = doctor;
    }
}