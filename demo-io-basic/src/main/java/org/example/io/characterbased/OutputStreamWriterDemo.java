package org.example.io.characterbased;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class OutputStreamWriterDemo {
    public static void main(String[] args) throws IOException {
        OutputStream os =  new FileOutputStream("demo-io-basic/src/main/resources/files/观沧海");
        OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
        osw.write("东临碣石 以观沧海\n水何澹澹 山岛竦峙\n树木丛生 百草丰茂\n秋风萧瑟 洪波涌起\n日月之行 若出其中\n星汉灿烂 若出其里\n幸甚至哉 歌以咏志");
        osw.close();
    }
}
