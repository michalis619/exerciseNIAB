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

    private static final Map<String, String> cropCodeMap = new HashMap<String, String>();

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

    public static void processHarvestData(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                processLine(line.split(", "));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String processLine(String[] line) {
      StringBuilder output = new StringBuilder(); //we return this string, so we can easily test this method
      try (FileWriter errorLog = new FileWriter(ERROR_PATH, true)) {
        int length = line.length;
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
        System.out.println("County: " + county);
        output.append(String.format("County: %s\n", county));
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
      } catch (IOException e) {
        e.printStackTrace();
      }
      return output.toString();
    }

  private static String timestamp() {
    return LocalDateTime.now().format(dtf);
  }

}
