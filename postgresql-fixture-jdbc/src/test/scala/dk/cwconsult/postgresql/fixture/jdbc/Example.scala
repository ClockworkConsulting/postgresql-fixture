package dk.cwconsult.postgresql.fixture.jdbc

/**
 * This is an example of usage for easy copy/paste into documentation. It is NOT a test.
 */
class Example {

  def main(): Unit = {
    // Create the temporary database
    val temporaryDatabase = PostgreSQLFixtureSupport.createTemporaryDatabase(
      DatabaseSettings(
        databaseName = "postgres",
        userName = "my-username",
        password = "my-password"),
      "template1")
    // Try-finally to ensure resources are freed.
    try {
      // Temporary database settings are now available
      // to use for establishing a connection, etc.
      Console.println(s"temporary database settings: ${temporaryDatabase.databaseSettings}")
    } finally {
      // Drop the temporary database
      temporaryDatabase.close()
    }

  }

}
