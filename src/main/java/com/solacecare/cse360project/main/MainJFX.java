package com.solacecare.cse360project.main;

import com.solacecare.cse360project.nurse.NurseView;
import com.solacecare.cse360project.doctor.DoctorView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import com.solacecare.cse360project.patient.PatientView;

public class MainJFX extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        primaryStage.setTitle("Healthcare System");
        goToUserSelectView();
        primaryStage.show();
    }

    public static void goToUserSelectView() {
        UserSelectView userSelectView = new UserSelectView(primaryStage);
        Scene scene = new Scene(userSelectView.getView(), 800, 600);
        primaryStage.setScene(scene);
    }

    public static void goToPatientView() {
        PatientView patientView = new PatientView(primaryStage);
        Scene scene = new Scene(patientView.getView(), 800, 600);
        primaryStage.setScene(scene);
    }

    public static void goToNurseView() {
        NurseView patientView = new NurseView(primaryStage);
        Scene scene = new Scene(patientView.getView(), 800, 600);
        primaryStage.setScene(scene);
    }

    public static void goToDoctorView() {
        DoctorView patientView = new DoctorView(primaryStage);
        Scene scene = new Scene(patientView.getView(), 800, 600);
        primaryStage.setScene(scene);
    }

    public static void goToLoginView(String userType){
        LoginView loginView = new LoginView(primaryStage, userType);
        Scene scene = new Scene(loginView.getView(), 800, 600);
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
        SpringApplication.run(Main.class, args);
    }
}