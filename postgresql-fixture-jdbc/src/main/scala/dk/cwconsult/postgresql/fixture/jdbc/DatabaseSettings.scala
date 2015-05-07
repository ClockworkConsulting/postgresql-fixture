package dk.cwconsult.postgresql.fixture.jdbc

/**
 * Database settings to use for connecting to the database.
 * The given user must be able to '''create''' new databases
 * and must be able to '''read''' the database given in
 * [[databaseName]].
 *
 * @param databaseName the name of the PostgreSQL database
 *                     to connect to. Usually `postgres` will
 *                     work here.
 * @param userName the user name to use to connect.
 * @param password the password to use to connect.
 */
case class DatabaseSettings(
  databaseName: String,
  userName: String,
  password: String)
