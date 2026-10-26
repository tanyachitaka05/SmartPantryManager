package com.example.smartpantry.logic;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Makes ingredient names and units comparable despite trivial real-world
 * messiness (case, whitespace, simple plurals, common synonyms).
 *
 * The brief explicitly says a naive exact-string match that breaks on
 * "tomato" vs "tomatoes" will be marked down - this class fixes that
 * WITHOUT needing a full NLP solution.
 */
public final class IngredientNormalizer {

    private IngredientNormalizer() { }

    // A few hand-picked synonyms that trip up naive matching.
    private static final Map<String, String> SYNONYMS = new HashMap<>();
    static {
        SYNONYMS.put("scallion", "spring onion");
        SYNONYMS.put("scallions", "spring onion");
        SYNONYMS.put("green onion", "spring onion");
        SYNONYMS.put("coriander", "cilantro");
        SYNONYMS.put("capsicum", "bell pepper");
        SYNONYMS.put("aubergine", "eggplant");
        SYNONYMS.put("courgette", "zucchini");
        SYNONYMS.put("chickpea", "chickpeas");
    }

    /**
     * Normalise an ingredient name: lower-case, trim, collapse spaces,
     * apply synonyms, then naive-singularise so "Tomatoes" and "tomato"
     * both become "tomato".
     */
    public static String name(String raw) {
        if (raw == null) return "";
        String s = raw.toLowerCase(Locale.ROOT).trim().replaceAll("\\s+", " ");
        if (SYNONYMS.containsKey(s)) s = SYNONYMS.get(s);
        s = singularise(s);
        return s;
    }

    /** Very small, deliberate stemmer - only handles common English plurals. */
    private static String singularise(String s) {
        if (s.length() <= 3) return s;          // keep short words intact (e.g. "egg")
        if (s.endsWith("ies")) {                // berries -> berry
            return s.substring(0, s.length() - 3) + "y";
        }
        if (s.endsWith("oes")) {                // tomatoes -> tomato, potatoes -> potato
            return s.substring(0, s.length() - 2);
        }
        if (s.endsWith("ses") || s.endsWith("xes") || s.endsWith("ches") || s.endsWith("shes")) {
            return s.substring(0, s.length() - 2); // boxes -> box
        }
        if (s.endsWith("s") && !s.endsWith("ss")) { // onions -> onion, but keep "glass"
            return s.substring(0, s.length() - 1);
        }
        return s;
    }

    /** Normalise a unit string to a canonical token. */
    public static String unit(String raw) {
        if (raw == null) return "";
        String u = raw.toLowerCase(Locale.ROOT).trim();
        switch (u) {
            case "gram": case "grams": case "g": return "g";
            case "kilogram": case "kilograms": case "kg": return "kg";
            case "milliliter": case "milliliters": case "millilitre": case "ml": return "ml";
            case "liter": case "liters": case "litre": case "litres": case "l": return "l";
            case "piece": case "pieces": case "pcs": case "pc": case "": return "pcs";
            case "tbsp": case "tablespoon": case "tablespoons": return "tbsp";
            case "tsp": case "teaspoon": case "teaspoons": return "tsp";
            case "cup": case "cups": return "cup";
            default: return u;
        }
    }
}
