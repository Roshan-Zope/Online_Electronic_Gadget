package com.example.onlineelectronicgadget.util;

import android.util.Log;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class QuerySimplifier {
    private static final String[] CATEGORIES = {"laptop", "tv", "watch", "tablet"};
    private static final String[] ATTRIBUTES = {"batteryLife", "brand", "category", "color", "description", "dimension", "display", "graphics", "model", "os",
            "ports", "price", "processor", "ram", "rating", "stocks", "warranty", "weight", "sound", "smart features", "connectivity", "displayTechnology", "resolution",
            "screenSize", "sensor", "waterResistance", "camera"
    };

    public static String[] parseQueryArray(String query) {
        String[] words = query.toLowerCase().split("\\s+");
        return words;
    }

    public static Map<String, Object> parseQuery(String query) {
        Map<String, Object> specifications = new HashMap<>();
        String[] words = query.toLowerCase().split("\\s+");
        String category = null;

        Log.d("myTag", Arrays.toString(words));

        for (String s : words) {
            String word = s.trim();
            if (isCategory(word)) {
                category = word;
                break;
            }
        }

        if (category != null) {
            specifications.put("category", category);
            parseAttributes(words, specifications);
        }

        Log.d("myTag", specifications.toString());

        return specifications;
    }

    private static void parseAttributes(String[] words, Map<String, Object> specifications) {
        for (int i = 0; i < words.length; i++) {
            String word = words[i];

            if (isNumeric(word)){
                int val = Integer.parseInt(word);

                if (i+1 < words.length && isAttribute(words[i+1])) {
                    specifications.put(words[i+1], val);
                    i++;
                }
            } else if (word.equals("under") && i+1 < words.length && isNumeric(words[i+1])) {
                specifications.put("maxPrice", Integer.parseInt(words[i+1]));
                i++;
            } else if (word.equals("over") && i+1 < words.length && isNumeric(words[i+1])) {
                specifications.put("minPrice", Integer.parseInt(words[i+1]));
                i++;
            } else if (word.equals("between") && i + 2 < words.length && isNumeric(words[i + 1]) && words[i + 2].equals("and") && i + 3 < words.length && isNumeric(words[i + 3])) {
                specifications.put("minPrice", Integer.parseInt(words[i + 1]));
                specifications.put("maxPrice", Integer.parseInt(words[i + 3]));
                i += 3;
            }
        }
        Log.d("myTag", specifications.toString());
    }

    private static boolean isAttribute(String word) {
        for (String s : ATTRIBUTES) {
            if (s.equalsIgnoreCase(word)) return true;
        }
        return false;
    }

    private static boolean isNumeric(String word) {
        try {
            Integer.parseInt(word);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isCategory(String word) {
        for (String s : CATEGORIES) {
            if (s.equals(word)) return true;
        }
        return false;
    }
}

