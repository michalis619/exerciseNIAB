package org.example;

import static org.example.HarvestDataProcessor.processHarvestData;

public class Main {
  public static void main(String[] args) {
    String inputFilePath = "src/main/resources/harvestData/clean.csv";
    String validationFilePath = "path/to/harvest data - validation needed.csv";
    String errorLogFilePath = "path/to/error_log.txt";

    HarvestDataProcessor.processHarvestData(inputFilePath);
    //processHarvestDataWithValidation(validationFilePath, errorLogFilePath);

  }
}