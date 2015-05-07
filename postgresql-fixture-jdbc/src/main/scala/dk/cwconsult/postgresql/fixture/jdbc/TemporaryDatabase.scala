package dk.cwconsult.postgresql.fixture.jdbc

/**
 * Temporary database.
 */
trait TemporaryDatabase extends AutoCloseable {

  /**
   * Get database settings for the temporary database.
   */
  def databaseSettings: DatabaseSettings

  /**
   * Database JDBC URL.
   */
  def databaseURL: String

  /**
   * Clean up.
   */
  override def close(): Unit

}
