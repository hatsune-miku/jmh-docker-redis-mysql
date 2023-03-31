package com.mikutart.benchmark.cases;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Warmup(iterations = 2, time = 1, timeUnit = TimeUnit.SECONDS)
@Timeout(time = 1, timeUnit = TimeUnit.MINUTES)
@Fork(value = 1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class MemoryTest {
    private static final int ARRAY_SIZE = 1_000_000;
    private ArrayList<Integer> arrayList;

    @Setup
    public void setup() {
        arrayList = new ArrayList<>(ARRAY_SIZE);
        for (int i = 0; i < ARRAY_SIZE; i++) {
            arrayList.add(i);
        }
    }

    @Benchmark
    public void sortArrayList(Blackhole blackhole) {
        arrayList.sort(Integer::compare);
        blackhole.consume(arrayList);
    }
}
