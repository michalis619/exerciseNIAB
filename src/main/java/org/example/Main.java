package org.example;

public class Main {
  public static void main(String[] args) {
    String inputFilePath = "src/main/resources/harvestData/clean.csv";
    String validationFilePath = "src/main/resources/harvestData/harvestData-validation needed.csv";

    HarvestDataProcessor.processHarvestData(inputFilePath);
    HarvestDataProcessor.processHarvestData(validationFilePath);
  }
}