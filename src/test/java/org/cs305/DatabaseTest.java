package org.cs305;

import org.junit.jupiter.api.Test;

public class DatabaseTest {
  @Test
  void testClearData() {
    Database.clearData();
  }

  @Test
  void testCloseConnection() {
    Database.getConnection();
    Database.closeConnection();
  }

  @Test
  void testGetConnection() {
    Database.getConnection();
  }

  @Test
  void testInitialize() {
    Database.initialize();
  }
}
