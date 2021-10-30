package com.howtodoinjava.core.basic;

import java.util.ArrayList;
import java.util.List;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.infra.Blackhole;

public class ForLoopPerformanceTest 
{
    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }
    
    private static List<Integer> list = new ArrayList<>();
    static
    {
        for(int i=0; i < 1_000_000; i++)
        {
            list.add(i);
        }
    }
    
    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    public void usingStream(Blackhole blackhole) {
        list.stream().forEach(i -> blackhole.consume(i));
    }
    
    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    public void usingIterator(Blackhole blackhole) {
        list.listIterator().forEachRemaining(i -> blackhole.consume(i));
    }
    
    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    public void usingForEachLoop(Blackhole blackhole) {
        for(Integer i : list)
        {
           blackhole.consume(i);
        }
    }
    
    @Benchmark
    @Fork(value = 1, warmups = 1)
    @BenchmarkMode(Mode.Throughput)
    public void usingSimpleForLoop(Blackhole blackhole) {
        for(int i = 0; i < list.size() ; i++)
        {
            blackhole.consume(i);
        }
    }
}
