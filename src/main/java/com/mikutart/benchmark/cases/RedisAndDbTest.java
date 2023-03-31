package com.mikutart.benchmark.cases;

import org.openjdk.jmh.annotations.*;
import redis.clients.jedis.Jedis;

import java.sql.*;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@Timeout(time = 1, timeUnit = TimeUnit.MINUTES)
@Fork(value = 1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class RedisAndDbTest {
    private static final String DATABASE_ADDRESS = System.getenv("DATABASE_ADDRESS");

    private static final String MYSQL_USERNAME = "root";
    private static final String MYSQL_PASSWORD = "swordfish";

    private static final String REDIS_HOSTNAME = System.getenv("REDIS_ADDRESS");
    private static final int REDIS_PORT = 6379;

    private Connection mysqlConnection;
    private Statement mysqlStatement;
    private Jedis jedis;

    @Setup
    public void setup() throws SQLException {
        // MySQL setup
        String MYSQL_URL = "jdbc:mysql://"
            + (
                DATABASE_ADDRESS == null
                    ? "localhost"
                    : DATABASE_ADDRESS
            )
            + ":3306/test";

        mysqlConnection = DriverManager.getConnection(MYSQL_URL, MYSQL_USERNAME, MYSQL_PASSWORD);
        mysqlStatement = mysqlConnection.createStatement();

        // Redis setup
        jedis = new Jedis(
            REDIS_HOSTNAME == null 
                ? "localhost" 
                : REDIS_HOSTNAME, 
            REDIS_PORT
        );
        jedis.flushAll();

        mysqlStatement.executeUpdate("CREATE TABLE IF NOT EXISTS test_table (" +
            "id INT NOT NULL AUTO_INCREMENT, name VARCHAR(255), PRIMARY KEY (id))");
    }

    @TearDown
    public void tearDown() throws SQLException {
        String deleteQuery = "DELETE FROM test_table WHERE name = 'value'";
        mysqlStatement.executeUpdate(deleteQuery);

        // MySQL teardown
        mysqlStatement.close();
        mysqlConnection.close();

        // Redis teardown
        jedis.close();
    }

    @Benchmark
    @Measurement(iterations = 5, time = 20, timeUnit = TimeUnit.SECONDS)
    @Warmup(iterations = 2, time = 20, timeUnit = TimeUnit.SECONDS)
    public void testMySQLInsert10000() throws SQLException {
        String insertQuery = "INSERT INTO test_table (name) VALUES ('value')";
        for (int i = 0; i < 10000; i++) {
            mysqlStatement.executeUpdate(insertQuery);
        }
        mysqlStatement.executeUpdate("DELETE FROM test_table WHERE 1 = 1");
    }

    @Benchmark
    @Measurement(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
    @Warmup(iterations = 2, time = 5, timeUnit = TimeUnit.SECONDS)
    public void testRedisInsert10000() {
        for (int i = 0; i < 10000; i++) {
            jedis.set("key" + i, "value" + i);
        }
    }

    @Benchmark
    @Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
    @Warmup(iterations = 2, time = 1, timeUnit = TimeUnit.SECONDS)
    public void testRedisDelete10000() {
        for (int i = 0; i < 10000; i++) {
            jedis.del("key" + i);
        }
    }
}
