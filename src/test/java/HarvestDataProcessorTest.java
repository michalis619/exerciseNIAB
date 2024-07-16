import org.example.HarvestDataProcessor;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class HarvestDataProcessorTest {

  @Test
  /**
   * Testing the method processLine in HarvestData with valid data.
   */
  public void testProcessLine() throws IOException {
    String[] input = {"Derbyshire", "W", "75", "B", "15", "M", "10"};
    String output = HarvestDataProcessor.processLine(input);
    Assert.assertTrue(output.contains("County: Derbyshire"));
    Assert.assertTrue(output.contains("Barley, Weight as a percentage: 15.00%"));
    Assert.assertTrue(output.contains("Wheat, Weight as a percentage: 75.00%"));
    Assert.assertTrue(output.contains("Maize, Weight as a percentage: 10.00%"));
  }

  @Test
  /**
   * Testing the method processLine in HarvestData with invalid data.
   */
  public void testProcessLineInvalid() throws IOException {
    String[] input = {"Derbyshire", "WW", "15", "B", "abc", "M", "10"};
    String output = HarvestDataProcessor.processLine(input);
    Assert.assertTrue(output.contains("Unknown crop code: WW"));
    Assert.assertTrue(output.contains("Invalid weight for crop code B: abc"));
  }

}
