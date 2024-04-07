package com.solacecare.cse360project.nurse;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
public class NurseValidator {
    private final String LICENSE_FILE_PATH = "nurse_licenses.csv";
    private final Set<String> validLicenses = new HashSet<>();

    public NurseValidator() {
        loadValidLicenses();
    }

    private void loadValidLicenses() {
        try (BufferedReader br = new BufferedReader(new FileReader(LICENSE_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                validLicenses.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isValidLicense(String licenseNumber) {
        return validLicenses.contains(licenseNumber.trim());
    }
}
