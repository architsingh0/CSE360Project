package com.solacecare.cse360project.main;

import com.solacecare.cse360project.Main;
import com.solacecare.cse360project.nurse.Nurse;
import com.solacecare.cse360project.nurse.NurseView;
import com.solacecare.cse360project.doctor.DoctorView;
import com.solacecare.cse360project.patient.Patient;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import com.solacecare.cse360project.patient.PatientView;
import org.springframework.context.ConfigurableApplicationContext;

public class MainJFX extends Application {
    private static Stage primaryStage;
    private static ConfigurableApplicationContext springContext;

    @Override
    public void init() {
        springContext = Main.getContext();
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Healthcare System");
        goToUserSelectView();
        primaryStage.show();
    }

    public static void goToUserSelectView() {
        UserSelectView userSelectView = springContext.getBean(UserSelectView.class);
        userSelectView.setStage(primaryStage);
        userSelectView.initializeComponents();
        Scene scene = new Scene(userSelectView.getView(), 800, 600);
        primaryStage.setScene(scene);
    }

    public static void goToPatientView(Patient patient) {
        PatientView patientView = springContext.getBean(PatientView.class);
        patientView.setStage(primaryStage);
        patientView.setPatient(patient);
        patientView.initializeComponents();
        Scene scene = new Scene(patientView.getView(), 800, 600);
        primaryStage.setScene(scene);
    }

    public static void goToNurseView(Nurse nurse) {
        NurseView nurseView = springContext.getBean(NurseView.class);
        nurseView.setStage(primaryStage);
        nurseView.initializeComponents();
        nurseView.setCurrentNurse(nurse);
        Scene scene = new Scene(nurseView.getView(), 800, 600);
        primaryStage.setScene(scene);
    }

    public static void goToDoctorView() {
        DoctorView doctorView = springContext.getBean(DoctorView.class);
        doctorView.setStage(primaryStage);
        doctorView.initializeComponents();
        Scene scene = new Scene(doctorView.getView(), 800, 600);
        primaryStage.setScene(scene);
    }

    public static void goToLoginView(String userType){
        LoginView loginView = springContext.getBean(LoginView.class);
        loginView.setStage(primaryStage);
        loginView.setUserType(userType);
        loginView.initializeComponents();
        Scene scene = new Scene(loginView.getView(), 800, 600);
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
        SpringApplication.run(Main.class, args);
    }
}