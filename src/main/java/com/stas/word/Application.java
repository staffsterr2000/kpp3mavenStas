package com.stas.word;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Main class with some static util methods
 */
public class Application {

    public static Map<String, Integer> getWordsAndTheirCountMap(Collection<String> words) {
        Map<String, Integer> wordAndItAppearingCountMap = new LinkedHashMap<>();

        for (String word : words) {
            Integer value = wordAndItAppearingCountMap.get(word);
            if (value == null)
                wordAndItAppearingCountMap.put(word, 1);
            else
                wordAndItAppearingCountMap.put(word, ++value);
        }

        return wordAndItAppearingCountMap;
    }

    // deletes all empty and null strings in collection
    public static void trimCollection(Collection<String> collection) {
        collection.removeIf(value -> value == null || value.isEmpty());
    }

    // gets the last word in collection
    public static String getLastWord(Collection<String> collection) {
        trimCollection(collection);

        String value = null;
        for (String s : collection) {
            value = s;
        }

        if (value == null) throw new IllegalStateException();

        return value;
    }

    public static boolean isAppearsMoreThanOnce(String word, Map<String, Integer> wordAndItAppearingCountMap) {
        return wordAndItAppearingCountMap.get(word) > 1;
    }

    public static void main(String[] args) {

        try (BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Enter the file's path:");
            Path path = Paths.get(console.readLine());
            WordHelper helper = new WordHelper(path);

            List<String> words = helper.readWordsFromFile();
            String lastWord = getLastWord(words);
            Map<String, Integer> map = getWordsAndTheirCountMap(words);
            boolean bool = isAppearsMoreThanOnce(lastWord, map);

            helper.addResultToFile(bool);
            helper.saveDocument();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
