package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

public class BloodLevelsDataGenerator implements PatientDataGenerator {
    private static final Random random = new Random();
    private final double[] baselineCholesterol;
    private final double[] baselineWhiteCells;
    private final double[] baselineRedCells;

    public BloodLevelsDataGenerator(int patientCount) {
        // initialize arrays to hold baseline values per patient
        baselineCholesterol = new double[patientCount + 1];
        baselineWhiteCells = new double[patientCount + 1];
        baselineRedCells = new double[patientCount + 1];

        // pick a random baseline for each patient
        for (int i = 1; i <= patientCount; i++) {
            baselineCholesterol[i] = 150 + random.nextDouble() * 50; // random starting baseline
            baselineWhiteCells[i] = 4 + random.nextDouble() * 6; // random starting baseline
            baselineRedCells[i] = 4.5 + random.nextDouble() * 1.5; // random starting baseline
        }
    }

    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            // small variation around the baseline to keep values realistic
            double cholesterol = baselineCholesterol[patientId] + (random.nextDouble() - 0.5) * 10; // small variation
            double whiteCells = baselineWhiteCells[patientId] + (random.nextDouble() - 0.5) * 1; // small variation
            double redCells = baselineRedCells[patientId] + (random.nextDouble() - 0.5) * 0.2; // small variation

            // output the generated values
            outputStrategy.output(patientId, System.currentTimeMillis(), "Cholesterol", Double.toString(cholesterol));
            outputStrategy.output(patientId, System.currentTimeMillis(), "WhiteBloodCells", Double.toString(whiteCells));
            outputStrategy.output(patientId, System.currentTimeMillis(), "RedBloodCells", Double.toString(redCells));
        } catch (Exception e) {
            System.err.println("An error occurred while generating blood levels data for patient " + patientId);
            e.printStackTrace(); // print the stack trace to help track down where the error came from
        }
    }
}
