package com.mikutart.benchmark.cases;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.*;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@Measurement(iterations = 5, batchSize = 100, time = 10, timeUnit = TimeUnit.SECONDS)
@Warmup(iterations = 2, batchSize = 100, time = 10, timeUnit = TimeUnit.SECONDS)
@Timeout(time = 1, timeUnit = TimeUnit.MINUTES)
@Fork(value = 1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class HardDriveTest {
    private static final int BUFFER_SIZE = 1024;
    private static final int FILE_SIZE = 1_000_000;
    private File file;

    @Setup
    public void setup() throws IOException {
        file = File.createTempFile("tempfile", ".tmp");
        byte[] buffer = new byte[BUFFER_SIZE];
        try (FileOutputStream fos = new FileOutputStream(file)) {
            for (int i = 0; i < FILE_SIZE / BUFFER_SIZE; i++) {
                fos.write(buffer);
            }
        }
    }

    @TearDown
    public void tearDown() {
        file.delete();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void readFromFile(Blackhole blackhole) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        try (FileInputStream fis = new FileInputStream(file)) {
            while (fis.read(buffer) != -1) {
                blackhole.consume(buffer);
            }
        }
    }
}
