package com.mikutart.benchmark;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            // Run all benchmarks
            org.openjdk.jmh.Main.main(args);
        }
        catch (IOException e) {
            System.err.println("Benchmark error: " + e.getMessage());
        }
    }
}
