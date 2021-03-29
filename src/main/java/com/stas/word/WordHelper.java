package com.stas.word;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class WordHelper {

    private XWPFDocument document;
    private Path path;

    public WordHelper(Path path) {
        try(InputStream fis = Files.newInputStream(path)) {
            this.document = new XWPFDocument(OPCPackage.open(fis));
            this.path = path;

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public XWPFDocument getDocument() {
        return document;
    }

    public Path getPath() {
        return path;
    }

    public List<String> readWordsFromFile() {
        return document.getParagraphs().stream()
                .map(XWPFParagraph::getParagraphText)
                .flatMap(string -> Arrays.stream(string.split(" ")))
                .collect(Collectors.toList());
    }

    public void addResultToFile(boolean isAppearsMoreThanOnce) {
        String builder = "Результат: " + isAppearsMoreThanOnce;
        XWPFParagraph p = document.createParagraph();
        XWPFRun pRun = p.createRun();
        pRun.setText(builder);
    }

    public void saveDocument() throws IOException {
        try (
                OutputStream out = Files.newOutputStream(path);
        ) {
            document.write(out);
            document.close();
        }
    }
}
