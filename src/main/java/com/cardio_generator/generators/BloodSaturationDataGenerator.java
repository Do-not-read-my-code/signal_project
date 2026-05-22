package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

public class BloodSaturationDataGenerator implements PatientDataGenerator {
    private static final Random random = new Random();
    private int[] lastSaturationValues;

    public BloodSaturationDataGenerator(int patientCount) {
        lastSaturationValues = new int[patientCount + 1];

        // start each patient with a baseline saturation value
        for (int i = 1; i <= patientCount; i++) {
            lastSaturationValues[i] = 95 + random.nextInt(6); // somewhere between 95 and 100
        }
    }

    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            // simulate small fluctuations in blood saturation
            int variation = random.nextInt(3) - 1; // -1, 0, or 1
            int newSaturationValue = lastSaturationValues[patientId] + variation;

            // keep saturation in a realistic and healthy range
            newSaturationValue = Math.min(Math.max(newSaturationValue, 90), 100);
            lastSaturationValues[patientId] = newSaturationValue;
            outputStrategy.output(patientId, System.currentTimeMillis(), "Saturation", Double.toString(newSaturationValue) + "%");
        } catch (Exception e) {
            System.err.println("An error occurred while generating blood saturation data for patient " + patientId);
            e.printStackTrace(); // print the stack trace to help track down where the error came from
        }
    }
}
