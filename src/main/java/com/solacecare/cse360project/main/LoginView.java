package com.solacecare.cse360project.main;

import com.solacecare.cse360project.doctor.Doctor;
import com.solacecare.cse360project.doctor.DoctorRepository;
import com.solacecare.cse360project.doctor.DoctorValidator;
import com.solacecare.cse360project.nurse.Nurse;
import com.solacecare.cse360project.nurse.NurseRepository;
import com.solacecare.cse360project.nurse.NurseValidator;
import com.solacecare.cse360project.patient.Patient;
import com.solacecare.cse360project.patient.PatientRepository;
import com.solacecare.cse360project.patient.PatientService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class LoginView {
    private VBox layout;
    private HBox view;
    private Stage stage;
    private String userType;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private NurseRepository nurseRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorValidator doctorValidator;

    @Autowired
    private NurseValidator nurseValidator;

    @Autowired
    private PatientService patientService;

    public void loginUser(String email, String password) throws Exception {
        switch (userType) {
            case "Doctor":
                if(doctorRepository.findByEmailAndPassword(email, password).isPresent()){
                    MainJFX.goToDoctorView();
                }
                else{
                    throw new Exception("No user found. Email or password may be incorrect");
                }
                break;
            case "Nurse":
                Optional<Nurse> nurse = nurseRepository.findByEmailAndPassword(email, password);
                if(nurse.isPresent()){
                    MainJFX.goToNurseView(nurse.get());
                }
                else{
                    throw new Exception("No user found. Email or password may be incorrect");
                }
                break;
            case "Patient":
                Optional<Patient> patient = patientRepository.findByEmailAndPassword(email, password);
                if(patient.isPresent()){
                    MainJFX.goToPatientView(patient.get());
                    patientService.associateVisitsWithPatient(patient.get().getPatientIdentifier());
                }
                else{
                    throw new Exception("No user found. Email or password may be incorrect");
                }
                break;
            default:
                MainJFX.goToUserSelectView();
                break;
        }
    }

    public boolean createAccount(String fName, String lName, String email, String password, Long licenseOrInsuranceNum, LocalDate dateOfBirth, String phoneNum, String pharmAddr) {
        switch (userType) {
            case "Doctor":
                if(doctorRepository.findByEmail(email).isEmpty() && doctorValidator.isValidLicense(String.valueOf(licenseOrInsuranceNum))){
                    doctorRepository.save(new Doctor(fName, lName, email, password, licenseOrInsuranceNum));
                    return true;
                }
                else{
                    return false;
                }
            case "Nurse":
                if(nurseRepository.findByEmail(email).isEmpty() && nurseValidator.isValidLicense(String.valueOf(licenseOrInsuranceNum))){
                    nurseRepository.save(new Nurse(fName, lName, email, password, licenseOrInsuranceNum));
                    return true;
                }
                else{
                    return false;
                }
            case "Patient":
                Optional<Patient> patient = patientRepository.findByEmail(email);
                if(patient.isEmpty()){
                    patientRepository.save(new Patient(fName, lName, email, password, dateOfBirth, licenseOrInsuranceNum, phoneNum, pharmAddr));
                    patient = patientRepository.findByEmail(email);
                    patientService.associateVisitsWithPatient(patient.get().getPatientIdentifier());
                    return true;
                }
                else{
                    return false;
                }
            default:
                MainJFX.goToUserSelectView();
                break;
        }
        return false;
    }

    public LoginView() {
    }

    public void initializeComponents(){
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
        TextField fNameT = new TextField();
        createAccountForm.add(fNameT, 1, 0);
        createAccountForm.add(new Label("Last Name:"), 0, 1);
        TextField lNameT = new TextField();
        createAccountForm.add(lNameT, 1, 1);
        createAccountForm.add(new Label("Email:"), 0, 2);
        TextField emailT = new TextField();
        createAccountForm.add(emailT, 1, 2);
        createAccountForm.add(new Label("Password:"), 0, 3);
        TextField passwordT = new TextField();
        createAccountForm.add(passwordT, 1, 3);
        TextField licenseOrInsuranceNumT = new TextField();
        DatePicker dateOfBirth = new DatePicker();
        TextField phoneNumT = new TextField();
        TextField pharmAddrT = new TextField();
        switch (userType){
            case "Doctor":
            case "Nurse":
                createAccountForm.add(new Label("License Number: "), 0, 4);
                createAccountForm.add(licenseOrInsuranceNumT,1, 4);
                break;
            case "Patient":
                createAccountForm.add(new Label("Date of Birth: "), 0, 4);
                createAccountForm.add(dateOfBirth,1, 4);
                createAccountForm.add(new Label("Insurance Number: "), 0, 5);
                createAccountForm.add(licenseOrInsuranceNumT,1, 5);
                createAccountForm.add(new Label("Phone Number: "), 0, 6);
                createAccountForm.add(phoneNumT,1, 6);
                createAccountForm.add(new Label("Pharmacy Address: "), 0, 7);
                createAccountForm.add(pharmAddrT,1, 7);
                break;
        }

        Button createAccountButton = new Button("Create Account");
        createAccountForm.add(createAccountButton, 1, 8);
        Label message = new Label();
        createAccountForm.add(message, 0, 9);
        createAccountButton.setOnAction(e -> {
            try{
                if(createAccount(fNameT.getText(), lNameT.getText(), emailT.getText(), passwordT.getText(), Long.parseLong(licenseOrInsuranceNumT.getText()), dateOfBirth.getValue(), phoneNumT.getText(), pharmAddrT.getText())){
                    message.setTextFill(Color.LIMEGREEN);
                    message.setText("Account Successfully Created");
                }
                else{
                    message.setTextFill(Color.RED);
                    message.setText("Account with that email already exists");
                }
            }
            catch (NumberFormatException ex){
                message.setTextFill(Color.RED);
                message.setText("Please enter numbers in the corresponding fields");
            }
        });

        // Form for login
        GridPane loginForm = new GridPane();
        loginForm.setVgap(10);
        loginForm.setHgap(10);

        // Form fields for login
        loginForm.add(new Label("Email:"), 0, 0);
        TextField loginEmailT = new TextField();
        loginForm.add(loginEmailT, 1, 0);
        loginForm.add(new Label("Password:"), 0, 1);
        PasswordField loginPasswordT = new PasswordField();
        loginForm.add(loginPasswordT, 1, 1);
        Button loginButton = new Button("Login");
        loginForm.add(loginButton, 1, 2);
        Label messageL = new Label();
        loginForm.add(messageL, 0, 3);

        loginButton.setOnAction(e -> {
            try {
                loginUser(loginEmailT.getText(), loginPasswordT.getText());
            } catch (Exception ex) {
                messageL.setTextFill(Color.RED);
                messageL.setText(ex.getMessage());
            }
        });

        // Back button
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> MainJFX.goToUserSelectView());

        // Add the back button to the layout
        layout.getChildren().addAll(view, backButton);

        // Add forms to the HBox
        view.getChildren().addAll(createAccountForm, loginForm);
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void setUserType(String userType){
        this.userType = userType;
    }

    public VBox getView() {
        return layout;
    }
}
