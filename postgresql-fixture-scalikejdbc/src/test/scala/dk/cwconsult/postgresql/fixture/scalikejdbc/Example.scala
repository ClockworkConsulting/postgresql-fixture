package dk.cwconsult.postgresql.fixture.scalikejdbc

import dk.cwconsult.postgresql.fixture.jdbc.DatabaseSettings
import scalikejdbc._

/**
 * This is an example of usage for easy copy/paste into documentation. It is NOT a test.
 */
class Example {

  def main(): Unit = {
    // Create the temporary database
    val temporaryDatabase = ScalikeJDBCTemporaryDatabaseFixture.setup(
      'MyConnectionPool,
      ScalikeJDBCTemporaryDatabaseSettings(
        templateDatabaseName = "template1",
        databaseSettings = DatabaseSettings(
          databaseName = "postgres",
          userName = "my-username",
          password = "my-password"),
        connectionPoolSize = 16,
        postInitialize = connectionPool => {
          // Do any post-initialization setup you want here; such as
          // applying migrations, etc.
        }))
    // Try-finally to ensure resources are freed.
    try {
      // Temporary database connections are now available
      // from the 'MyConnectionPool connection pool.
      val connection = ConnectionPool.borrow('MyConnectionPool)
      // ...
    } finally {
      // Drop the temporary database
      temporaryDatabase.close()
    }

  }

}
