package com.smoothstack.BatchMicroservice.generator;

import java.io.FileWriter;
import java.io.IOException;

public class FileGenerator {

    public void xmlHeader(String path, String tag) throws IOException {
        FileWriter fw = new FileWriter(path);
        fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<"+tag+">");
        fw.close();
    }

    public void xmlCloser(String path, String tag) throws IOException {
        FileWriter fw = new FileWriter(path, true);
        fw.append("</").append(tag).append(">");
        fw.close();
    }
}
