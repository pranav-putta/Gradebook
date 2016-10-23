package net.codealizer.thegradebook.assets;

/**
 * Created by Pranav on 10/21/16.
 */

public enum Grade {

    A, B, C, D, AB, BC, CD;

    public static String valueOf(Grade g) {
        if (g == A) {
            return "A";
        } else if (g == B) {
            return "B";
        } else if (g == C) {
            return "C";
        } else if (g == D) {
            return "D";
        } else if (g == AB) {
            return "A/B";
        } else if (g == BC) {
            return "B/C";
        } else if (g == CD) {
            return "C/D";
        }

        return "";
    }
}
