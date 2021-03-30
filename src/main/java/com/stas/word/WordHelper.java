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
                // get each paragraph
                .map(XWPFParagraph::getParagraphText)
                // delete all breaks
                .map(string -> string.replaceAll("\n", " "))
                // split a sentence for the 'words'
                .flatMap(string -> Arrays.stream(string.split(" ")))
                // make all words lower case stated
                .map(String::toLowerCase)
                // delete all punctuation symbols except '
                .map(string -> string.replaceAll("[\\p{Punct}&&[^']]", " "))
                // collect to a list
                .collect(Collectors.toList());
    }

    public void addResultToFile(String result) {
        XWPFParagraph p = document.createParagraph();
        XWPFRun pRun = p.createRun();
        pRun.addBreak();
        pRun.setText(result);
        pRun.setFontFamily("Times New Roman");
        pRun.setFontSize(12);
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
