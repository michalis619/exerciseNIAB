package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HarvestDataProcessor {

    private static final Map<String, String> cropCodeMap = new HashMap<String, String>();

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
      StringBuilder output = new StringBuilder();
      String county = line[0];
      Map<String, Integer> cropsData = new HashMap<>();
      int totalWeight = 0;

      for (int i = 1; i < line.length - 1; i += 2) {
        String cropCode = line[i];
        if (!cropCodeMap.containsKey(cropCode)) {
          System.err.println("Unknown crop code: " + cropCode);
          output.append("Unknown crop code: " + cropCode + " ");
          continue; // Skip unknown crop codes
        }

        int weight;
        try {
          weight = Integer.parseInt(line[i + 1]);
        } catch (NumberFormatException e) {
          System.err.println("Invalid weight for crop code " + cropCode + ": " + line[i + 1]);
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
    }

}
