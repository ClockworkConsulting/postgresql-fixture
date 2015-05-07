package dk.cwconsult.postgresql.fixture.jdbc

import java.sql.DriverManager
import java.util.UUID
import scala.collection.mutable

/**
 * Support code for creating temporary PostgreSQL databases in test fixtures.
 */
object PostgreSQLFixtureSupport {

  /**
   * Escape a plain database name.
   */
  private[this] def escapeId(id: String): String =
    if (id.contains('"')) {
      throw new IllegalArgumentException(s"Template database names containing double-quotes not permitted")
    } else {
      "\"" + id + "\""
    }

  /**
   * Convert data base name to URL.
   */
  private[this] def toURL(databaseName: String): String =
    if (databaseName.startsWith("jdbc:postgresql:")) {
      databaseName
    } else {
      s"jdbc:postgresql:$databaseName"
    }

  /**
   * Close all resources in a stack of allocated resources. We're not going
   * to be super-pedantic about exception-handling here since you should only
   * ever be using this in tests anyway.
   */
  private[this] def closeAll(resources: mutable.Stack[AutoCloseable]): Unit = {
    while (resources.nonEmpty) {
      resources.pop().close()
    }
  }

  /**
   * Create a temporary database based on the given template database
   * name. '''Make sure you call `close` on the returned database
   * to ensure proper cleanup.'''
   *
   * @param postgreSQLDatabaseSettings are the database settings to use to connect
   *                                   to the database. Read-only access
   *                                   to this database is sufficient.
   * @param templateDatabase is the name of the template database to use for the
   *                         temporary database. '''Names containing double-quotes
   *                         are NOT supported.'''
   */
  def createTemporaryDatabase(postgreSQLDatabaseSettings: DatabaseSettings, templateDatabase: String): TemporaryDatabase = {
    // Make sure the driver is loaded.
    Class.forName("org.postgresql.Driver")
    // Generate almost-guaranteed unique database name.
    val temporaryDatabase = "temp_" + UUID.randomUUID().toString().replace("-","_")
    // Resources opened so far. We keep this in stack order.
    val resources = mutable.Stack[AutoCloseable]()
    // Connect to the template database since we need a connection to
    // issue the CREATE DATABASE statement.
    val connection = DriverManager.getConnection(
        toURL(postgreSQLDatabaseSettings.databaseName),
        postgreSQLDatabaseSettings.userName,
        postgreSQLDatabaseSettings.password)
    resources.push(connection)
    try {
      // Open statement
      val statement = connection.createStatement()
      resources.push(statement)
      // Create the temporary database
      statement.execute(s"""CREATE DATABASE ${escapeId(temporaryDatabase)} WITH TEMPLATE ${escapeId(templateDatabase)} """)
      // Return the temporary database
      new TemporaryDatabase {
        override val databaseSettings: DatabaseSettings =
          postgreSQLDatabaseSettings.copy(databaseName = toURL(temporaryDatabase))
        override val databaseURL: String =
          toURL(temporaryDatabase)
        override def close() {
          try {
            // Drop the temporary database.
            statement.execute(s"DROP DATABASE ${escapeId(temporaryDatabase)}")
          } finally {
            // Try to close all the resources.
            closeAll(resources)
          }
        }
      }
    } catch {
      case e: Exception =>
        // Try to close everything else and hope we succeed
        closeAll(resources)
        throw e // Re-throw original exception
    }
  }

}
