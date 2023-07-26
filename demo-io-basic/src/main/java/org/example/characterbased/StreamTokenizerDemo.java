package org.example.characterbased;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;

public class StreamTokenizerDemo {
    public static void main(String[] args) throws IOException {
        StreamTokenizer streamTokenizer = new StreamTokenizer(new StringReader("Mary had 1 little lamb..."));
        while(streamTokenizer.nextToken() != StreamTokenizer.TT_EOF){
            if(streamTokenizer.ttype == StreamTokenizer.TT_WORD) {
                System.out.println(streamTokenizer.sval);
            } else if(streamTokenizer.ttype == StreamTokenizer.TT_NUMBER) {
                System.out.println(streamTokenizer.nval);
            } else if(streamTokenizer.ttype == StreamTokenizer.TT_EOL) {
                System.out.println();
            }
        }
    }
}
