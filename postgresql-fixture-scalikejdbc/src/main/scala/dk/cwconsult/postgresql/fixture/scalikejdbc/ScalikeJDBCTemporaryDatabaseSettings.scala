package dk.cwconsult.postgresql.fixture.scalikejdbc

import dk.cwconsult.postgresql.fixture.jdbc.DatabaseSettings
import scalikejdbc._

/**
 * Database settings for temporary database used by test fixture.
 *
 * @param templateDatabaseName name of the template database to use.
 * @param databaseSettings settings for connecting to the database.
 * @param connectionPoolSize size of the connection pool to use.
 * @param postInitialize block which gets run just after creating
 *                       the temporary database.
 */
case class ScalikeJDBCTemporaryDatabaseSettings(
  templateDatabaseName: String,
  databaseSettings: DatabaseSettings,
  connectionPoolSize: Int = 16,
  postInitialize: ConnectionPool => Unit = _ => { })
