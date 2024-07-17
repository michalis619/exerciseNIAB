package org.example;

public class Main {
  public static void main(String[] args) {
    String inputFilePath = "src/main/resources/harvestData/clean.csv";
    String validationFilePath = "src/main/resources/harvestData/harvestData-validation needed.csv";
    String overridePath = "src/main/resources/harvestData/override.csv";

    HarvestDataProcessor processor1 = new HarvestDataProcessor();
    processor1.processHarvestData(inputFilePath);
    processor1.processHarvestData(validationFilePath);

    HarvestDataProcessor processor2 = new HarvestDataProcessor();
    processor2.processHarvestDataWithOverride(validationFilePath, overridePath);
  }
}