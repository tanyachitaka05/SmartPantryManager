package com.example.smartpantry.logic;

/**
 * Minimal, deliberate unit handling so quantity comparison is robust to
 * kg/g and l/ml differences without pretending to be a full unit library.
 */
public final class UnitConverter {

    private UnitConverter() { }

    private static final int MASS = 1;   // base: grams
    private static final int VOLUME = 2; // base: millilitres
    private static final int COUNT = 3;  // base: pieces
    private static final int OTHER = 0;  // cup/tbsp/tsp etc - not converted

    private static int family(String unit) {
        String u = IngredientNormalizer.unit(unit);
        switch (u) {
            case "g": case "kg": return MASS;
            case "ml": case "l": return VOLUME;
            case "pcs": return COUNT;
            default: return OTHER;
        }
    }

    /** Convert a quantity to its family's base unit (g, ml or pcs). */
    public static double toBase(double value, String unit) {
        String u = IngredientNormalizer.unit(unit);
        switch (u) {
            case "kg": return value * 1000.0;
            case "l":  return value * 1000.0;
            default:   return value; // g, ml, pcs, or non-convertible - leave as-is
        }
    }

    /** True if two units belong to the same convertible family. */
    public static boolean comparable(String a, String b) {
        int fa = family(a);
        int fb = family(b);
        return fa != OTHER && fa == fb;
    }
}
