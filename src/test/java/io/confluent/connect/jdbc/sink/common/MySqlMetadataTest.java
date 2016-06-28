package io.confluent.connect.jdbc.sink.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class MySqlMetadataTest {
  private final String URI = "jdbc:mysql://localhost:3306/the_db";
  private final String user = "root";
  private final String psw = "12345678";

  //@Test
  public void shouldReturnTrueIfTheTableExists() throws SQLException {
    String table = "tutorials_tbl";
    try (Connection connection = DriverManager.getConnection(URI, user, psw)) {
      assertTrue(DatabaseMetadata.tableExists(connection, table));
    }
  }

  //@Test
  public void shouldReturnFalseIfTheTableDoesNotExists() throws SQLException {
    String table = "bibble";
    try (Connection connection = DriverManager.getConnection(URI, user, psw)) {
      assertFalse(DatabaseMetadata.tableExists(connection, table));
    }
  }

  //@Test
  public void shouldReturnFalseEvenIfTheTableIsInAnotherDatabase() throws SQLException {
    String table = "tasks";
    try (Connection connection = DriverManager.getConnection(URI, user, psw)) {
      assertFalse(DatabaseMetadata.tableExists(connection, table));
    }
  }

  //@Test
  public void shouldReturnTheTablesInfo() throws SQLException {
    String tableName = "tutorials_tbl";
    try (Connection connection = DriverManager.getConnection(URI, user, psw)) {
      DbTable table = DatabaseMetadata.getTableMetadata(connection, tableName);
      assertEquals(tableName, table.getName());
      Map<String, DbTableColumn> map = table.getColumns();
      assertEquals(4, map.size());
      assertTrue(map.containsKey("tutorial_id"));
      assertTrue(map.containsKey("tutorial_title"));
      assertTrue(map.containsKey("tutorial_author"));
      assertTrue(map.containsKey("submission_date"));

      assertTrue(map.get("tutorial_id").isPrimaryKey());
    }
  }

  /**
   * > CREATE DATABASE the_db;
   * > CREATE TABLE tutorials_tbl(
   tutorial_id INT NOT NULL AUTO_INCREMENT,
   tutorial_title VARCHAR(100) NOT NULL,
   tutorial_author VARCHAR(40) NOT NULL,
   submission_date DATE,
   PRIMARY KEY ( tutorial_id )
   );

   *
   * > create database other_db;
   * >CREATE TABLE IF NOT EXISTS tasks (
   task_id INT(11) NOT NULL AUTO_INCREMENT,
   subject VARCHAR(45) DEFAULT NULL,
   start_date DATE DEFAULT NULL,
   end_date DATE DEFAULT NULL,
   description VARCHAR(200) DEFAULT NULL,
   PRIMARY KEY (task_id)
   );
   *
   *
   *
   *
   */
}
