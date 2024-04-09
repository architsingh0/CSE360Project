package com.solacecare.cse360project.patient;

import com.solacecare.cse360project.generic.Message;
import com.solacecare.cse360project.generic.MessageRepository;
import com.solacecare.cse360project.generic.User;
import com.solacecare.cse360project.generic.UserRepository;
import com.solacecare.cse360project.main.MainJFX;
import jakarta.transaction.Transactional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class PatientView {
    private VBox layout;
    private Stage stage;
    private Patient currentPatient;
    private ObservableList<Message> messagePreviews;
    private VBox messageDetailContainer;
    private VBox repliesContainer;
    TextArea messageDetailView;

    @Autowired
    private PatientVisitRepository patientVisitRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    public PatientView() {

    }

    public void initializeComponents(){
        TabPane view = new TabPane();

        Tab tabVisits = new Tab("Visits");
        tabVisits.setClosable(false);
        //TODO: Add components for visits here...
        setupVisitsTab(tabVisits);

        Tab tabEditInformation = new Tab("Edit Information");
        tabEditInformation.setClosable(false);
        //TODO: Add components for editing information here...
        setupEditInformationTab(tabEditInformation);

        Tab tabMessages = new Tab("Messages");
        tabMessages.setClosable(false);
        //TODO: Add components for messages here...
        setupMessagesTab(tabMessages);

        view.getTabs().addAll(tabVisits, tabEditInformation, tabMessages);

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> MainJFX.goToUserSelectView());

        layout = new VBox(10);
        layout.getChildren().addAll(view, logoutButton);
    }

    private void setupVisitsTab(Tab tabVisits) {
        VBox patientVisits = new VBox();
        patientVisits.setSpacing(10);
        patientVisits.setPadding(new Insets(10));
        Accordion accordion = new Accordion();

        // Fetch patient visits directly, as there's no search functionality
        List<PatientVisit> visits = patientVisitRepository.findByPatientIdentifier(currentPatient.getPatientIdentifier());
        if (visits != null && !visits.isEmpty()) {
            Map<LocalDate, List<PatientVisit>> visitsByDate = visits.stream()
                    .collect(Collectors.groupingBy(PatientVisit::getDate));

            visitsByDate.forEach((date, visitsForDate) -> {
                VBox contentBox = new VBox(5);
                visitsForDate.forEach(visit -> {
                    String visitDetails = formatPatientVisitDetails(visit); // Assume similar implementation as in NurseView
                    contentBox.getChildren().add(new Label(visitDetails));
                });
                TitledPane titledPane = new TitledPane(date.toString(), contentBox);
                accordion.getPanes().add(titledPane);
            });
        }

        patientVisits.getChildren().add(accordion);
        tabVisits.setContent(patientVisits);
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


    private void setupEditInformationTab(Tab tabEditInformation) {
        GridPane editForm = new GridPane();
        editForm.setVgap(10);
        editForm.setHgap(10);

        // Example: First Name and Last Name fields
        TextField firstNameField = new TextField(currentPatient.getFirstName());
        TextField lastNameField = new TextField(currentPatient.getLastName());
        TextField emailField = new TextField(currentPatient.getEmail());
        PasswordField password = new PasswordField();
        DatePicker dateOfBirth = new DatePicker(currentPatient.getDateOfBirth());
        TextField phoneNum = new TextField(currentPatient.getPhoneNum());
        TextField insuranceNum = new TextField(String.valueOf(currentPatient.getInsuranceNum()));
        TextField pharmacyAddr = new TextField(currentPatient.getPharmacyAddr());

        // Add other fields for date of birth, insurance number, etc., initializing them with currentPatient's data

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            currentPatient.setFirstName(firstNameField.getText());
            currentPatient.setLastName(lastNameField.getText());
            currentPatient.setEmail(emailField.getText());
            currentPatient.setPassword(password.getText());
            currentPatient.setDateOfBirth(dateOfBirth.getValue());
            currentPatient.setPhoneNum(phoneNum.getText());
            currentPatient.setInsuranceNum(Long.parseLong(insuranceNum.getText()));
            currentPatient.setPharmacyAddr(pharmacyAddr.getText());

            patientRepository.save(currentPatient);
        });

        // Add components to editForm and set it as the content of tabEditInformation
        editForm.add(new Label("First Name:"), 0, 0);
        editForm.add(firstNameField, 1, 0);
        editForm.add(new Label("Last Name:"), 0, 1);
        editForm.add(lastNameField, 1, 1);
        editForm.add(new Label("Email:"), 0, 2);
        editForm.add(emailField, 1, 2);
        editForm.add(new Label("Password:"), 0, 3);
        editForm.add(password, 1, 3);
        editForm.add(new Label("Date of Birth:"), 0, 4);
        editForm.add(dateOfBirth, 1, 4);
        editForm.add(new Label("Phone Number:"), 0, 5);
        editForm.add(phoneNum, 1, 5);
        editForm.add(new Label("Insurance Number:"), 0, 6);
        editForm.add(insuranceNum, 1, 6);
        editForm.add(new Label("Pharmacy Address:"), 0, 7);
        editForm.add(pharmacyAddr, 1, 7);

        editForm.add(saveButton, 1, 9); // Adjust grid positions as necessary

        tabEditInformation.setContent(editForm);
    }


    private void setupMessagesTab(Tab tab) {
        // Similar setup as provided in the messages tab of NurseView,
        // adjusted for PatientView specifics
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
        Button replyButton = new Button("Reply");
        replyButton.setOnAction(e -> {
            if (messageListView.getSelectionModel().getSelectedItem() != null) {
                openReplyDialog(messageListView.getSelectionModel().getSelectedItem());
            }
        });
        messageDetailContainer = new VBox(new Label("Message Detail"), messageDetailView, replyButton);

//        messageDetailContainer.getChildren().add(replyButton);

        mainArea.getItems().addAll(messageListContainer, messageDetailContainer);
        root.setCenter(mainArea);

        composeButton.setOnAction(e -> openComposeDialog());
        refreshButton.setOnAction(e -> refreshMessages());

        messageListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                messageDetailContainer.getChildren().remove(repliesContainer);
//                repliesContainer = null;
                displayMessageDetails(newSelection.getId());
            }
        });
        refreshMessages();
//        VBox.setVgrow(root, Priority.ALWAYS);
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
                message.setSender(currentPatient);
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
        List<Message> messages = messageRepository.findByRecipientEmail(currentPatient.getEmail());

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

            // Prepare a container specifically for replies, which does not disturb the "Reply" button
//            repliesContainer = null;
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

//            ScrollPane repliesScrollPane = new ScrollPane(repliesContainer);
//            repliesScrollPane.setFitToWidth(true);
//
//            messageDetailContainer.getChildren().add(repliesScrollPane);

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
                reply.setSender(currentPatient);
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