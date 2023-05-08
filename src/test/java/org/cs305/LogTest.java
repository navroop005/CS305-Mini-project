package org.cs305;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class LogTest {
  @BeforeAll
  static void testClose_null(){
    Log.closeFile();
  }

  @Test
  void testError() {
    Log.error("Error1");
    Log.error("Error2");
  }

  @Test
  void testWrite() {
    Log.write("Test");
    Log.write("Test 2");
  }

  @AfterEach
  void closeFile(){
    Log.closeFile();
  }
}
