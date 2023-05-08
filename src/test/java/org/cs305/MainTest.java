package org.cs305;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class MainTest extends CommonTestClass {
  @Test
  void testMain() {
    String input = """
        e
        """;
    assertTrue(testInput(input, ()->{
      Main.main(null);
    }));
    assertEquals(outputLastNthLine(0), "Exit");
  }
}
