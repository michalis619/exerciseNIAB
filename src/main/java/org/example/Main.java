package org.example;

public class Main {
  public static void main(String[] args) {
    // Paths to the input data files
    String inputFilePath = "src/main/resources/harvestData/clean.csv";
    String validationFilePath = "src/main/resources/harvestData/harvestData-validation needed.csv";
    String overridePath = "src/main/resources/harvestData/override.csv";

    HarvestDataProcessor processor1 = new HarvestDataProcessor();
    processor1.processHarvestData(inputFilePath); //process and outputting the data for the clean.csv
    processor1.processHarvestData(validationFilePath); //process and outputting the data for the validation needed data file

    HarvestDataProcessor processor2 = new HarvestDataProcessor();
    //process and outputting the data for the validation needed data file with using the override file as override.
    processor2.processHarvestDataWithOverride(validationFilePath, overridePath);
  }
}