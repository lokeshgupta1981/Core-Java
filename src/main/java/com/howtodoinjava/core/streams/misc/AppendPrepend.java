package com.howtodoinjava.core.streams.misc;

import java.util.List;
import java.util.stream.Stream;

public class AppendPrepend {
    public static void main(String[] args) {
        Stream<Integer> stream = Stream.of(1, 2, 3, 4);
        
        //Append 5 to the stream
        Stream<Integer> appenededStream = Stream.concat(stream, Stream.of(5,6));
        //Verify Stream
        appenededStream.forEach(System.out::print);
        
        //Stream cannot be reused to create new for example
        stream = Stream.of(1, 2, 3, 4);
        System.out.println("\n");
        
        //Append more items to the stream
        appenededStream = Stream.concat(stream, List.of(5,6,7,8).stream());
        //Verify Stream
        appenededStream.forEach(System.out::print);
        
        stream = Stream.of(1, 2, 3, 4);
        System.out.println("\n");
        
        //Prepend 0 to the stream
        Stream<Integer> prependedStream = Stream.concat(Stream.of(0), stream);
        //Verify Stream
        prependedStream.forEach(System.out::print);
        
    }
}
