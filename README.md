# Intro

`postgresql-fixture` provides a simple way to create temporary PostgreSQL
databases for testing purposes in your Scala projects.

To use it, you'll need the credentials for user who can **create databases**
on the PostgreSQL server you're targeting.

# Usage (JDBC)

Example of usage:

```scala
    // Create the temporary database
    val temporaryDatabase = PostgreSQLFixtureSupport.createTemporaryDatabase(
      DatabaseSettings(
        databaseName = "postgres",
        userName = "my-user",
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
```

# Usage (ScalikeJDBC)

```scala
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
```

# Copyright and License

This code is provided under the [BSD 2-clause license](https://github.com/ClockworkConsulting/postgresql-fixture/blob/master/LICENSE)
