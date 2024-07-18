package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HarvestDataProcessor {


    // A static, unmodifiable map to store crop codes and their corresponding full names.
    private static final Map<String, String> cropCodeMap = new HashMap<String, String>();

    // A non-static map to store override data for crop weights by county and crop code.
    // The outer map uses county names as keys and maps each to another map.
    // The inner map uses crop codes as keys and stores the corresponding weights as values.
    private Map<String, Map<String, Integer>> overrideData = new HashMap<>();

    private static final String ERROR_PATH = "src/main/resources/logs/errorsLog.txt";

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    static {
        cropCodeMap.put("W", "Wheat");
        cropCodeMap.put("B", "Barley");
        cropCodeMap.put("M", "Maize");
        cropCodeMap.put("BE", "Beetroot");
        cropCodeMap.put("C", "Carrot");
        cropCodeMap.put("PO", "Potatoes");
        cropCodeMap.put("PA", "Parsnips");
        cropCodeMap.put("O", "Oats");
        cropCodeMap.get("W");
    }

  public void processHarvestDataWithOverride(String filePath, String overridePath) {
    readOverrideData(overridePath);
    processHarvestData(filePath) ;
  }

  public void processHarvestData(String filePath) {
    processFile(filePath, false);
  }

  public void readOverrideData(String filePath) {
    processFile(filePath, true);
  }

  /**
   * Processes a file line by line, by passing an array of strings for each line to the
   * {@link #processLine(String[], boolean)} to handle.
   * The array is created by splitting each line by a comma and space (", ").
   * Depending on the `isOverride` flag, each line is processed either as regular data or override data.
   * @param filePath The filepath of the file we are processing.
   * @param isOverride a boolean flag indicating whether the file contains override data.
   */
  private void processFile(String filePath, boolean isOverride) {
      try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
          String line;
          // Read each line from the file
          while ((line = reader.readLine()) != null) {
              processLine(line.split(", "), isOverride);
          }
      } catch (IOException e) {
          e.printStackTrace();
      }
  }

  /**
   * Processes a single line of data, which is split into an array of strings.
   * Each line is expected to start with a County, followed by pairs of crop codes and weights.
   * The method validates the data, handles errors by logging them, and applies overrides if specified.
   * When there is an error like a wrong crop code the method logs that error but
   * still process the rest data that is correct .
   *
   * @param line an array of strings representing the data in one line of the data file
   * @param isOverride a boolean flag indicating if this line should be processed as override data
   * @return a formatted string representation of the processed line for testing purposes
   */
  public String processLine(String[] line, boolean isOverride) {
    StringBuilder output = new StringBuilder(); //we return this string, so we can easily test this method
    try (FileWriter errorLog = new FileWriter(ERROR_PATH, true)) {
      int length = line.length;
      // Check if the line length is valid (at least 3 and odd number), so you can have a county and crop/weight pairs.
      if (length < 3 || length % 2 == 0) {
        errorLog.write(timestamp() + " The number of information on the line that starts with " + line[0] +
                " are not valid each line should start with a County and then couples of crop code/weight." + "\n");
        return "";
      }
      String county = line[0];
      Map<String, Integer> cropsData = new HashMap<>();
      int totalWeight = 0;
      for (int i = 1; i < length - 1; i += 2) {
        String cropCode = line[i];
        // Check if the crop code is valid
        if (!cropCodeMap.containsKey(cropCode)) {
          errorLog.write(timestamp() + " Unknown crop code: '" + cropCode + "' in county: " + county + "\n");
          output.append("Unknown crop code: " + cropCode + " ");
          continue; // Skip unknown crop codes
        }

        int weight;
        try {
          weight = Integer.parseInt(line[i + 1]);
        } catch (NumberFormatException e) {
          errorLog.write(timestamp() + " Invalid weight for crop code " + cropCode + " in county: " + county + ": " + line[i + 1] + "\n");
          output.append("Invalid weight for crop code " + cropCode + ": " + line[i + 1] + " ");
          continue; // Skip invalid weights
        }
        cropsData.put(cropCode, weight);
        totalWeight += weight;
      }
      if (isOverride) {
        // If override, store the cropsData in overrideData map.
        overrideData.put(county, cropsData);
      } else {
        // Apply any override data.
        if (overrideData.containsKey(county)) {
          for (Map.Entry<String, Integer> entry : overrideData.get(county).entrySet()) {
            String cropCode = entry.getKey();
            int weight = entry.getValue();

            if (cropsData.containsKey(cropCode)) {
              totalWeight -= cropsData.get(cropCode);//Remove the old weight from total weight if the crop already existed.
            }
            cropsData.put(cropCode, weight); //update the data with the override crop.
            totalWeight += weight; // add the new weight to total weight.
          }
        }

        System.out.println("County: " + county);
        output.append(String.format("County: %s\n", county));
        // Calculate and print the weight as a percentage of the total for each crop.
        for (Map.Entry<String, Integer> entry : cropsData.entrySet()) {
          String cropName = cropCodeMap.get(entry.getKey());
          int weight = entry.getValue();
          double percentage = (double) weight / totalWeight * 100;
          System.out.printf("Crop: %s, Weight as a percentage: %.2f%%\n", cropName, percentage);
          output.append(String.format("Crop: %s, Weight as a percentage: %.2f%%\n", cropName, percentage));
        }
        System.out.println();
        output.append("\n");
        return output.toString();
      }
    } catch(IOException e){
      e.printStackTrace();
    }
    return output.toString();
  }


  private static String timestamp() {
    return LocalDateTime.now().format(dtf);
  }

}
