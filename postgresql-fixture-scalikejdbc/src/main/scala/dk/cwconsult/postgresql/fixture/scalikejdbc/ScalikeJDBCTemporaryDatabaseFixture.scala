package dk.cwconsult.postgresql.fixture.scalikejdbc

import dk.cwconsult.postgresql.fixture.jdbc.PostgreSQLFixtureSupport
import scalikejdbc._

/**
 * Fixture for using a temporary database during tests.
 */
object ScalikeJDBCTemporaryDatabaseFixture {

  private[this] val mutex = new Object()

  /**
   * Set up the temporary database and add a connection pool which is connected to it.
   * Returns a closeable which will take care of cleanup when its `close` method is invoked.
   */
  def setup(connectionPoolName: Any, temporaryDatabaseSettings: ScalikeJDBCTemporaryDatabaseSettings): AutoCloseable = {
    // Acquire semaphore for the full duration of the setup; PostgreSQL returns an
    // error if you try to clone a template database concurrently. This won't save
    // us from other *processes*, but it's the best we can do for now.
    mutex.synchronized {
      // Create the temporary database
      val temporaryDatabase = PostgreSQLFixtureSupport.createTemporaryDatabase(
        temporaryDatabaseSettings.databaseSettings,
        temporaryDatabaseSettings.templateDatabaseName)

      // Set up ScalikeJDBC connection pool
      ConnectionPool.add(
        connectionPoolName,
        temporaryDatabase.databaseURL,
        temporaryDatabase.databaseSettings.userName,
        temporaryDatabase.databaseSettings.password,
        ConnectionPoolSettings(validationQuery = "SELECT 1", maxSize = temporaryDatabaseSettings.connectionPoolSize))

      // Perform initialization
      temporaryDatabaseSettings.postInitialize(ConnectionPool.get(connectionPoolName))

      // Return the closeable which lets us remove the temporary database when we're done.
      new AutoCloseable {
        override def close(): Unit = {
          // Close all the connections in the pool.
          ConnectionPool.close(connectionPoolName)
          // Cleanup the temporary database.
          temporaryDatabase.close()
        }
      }
    }
  }

}
