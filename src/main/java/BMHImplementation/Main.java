package BMHImplementation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {

        final String FULL_TEXT = readFileText();
        final String[] ANCHORS = {"THE", "AND", "TO", "OF", "IN", "THAT", "IS", "IT", "FOR", "YOU"};
        final int ALPHABET_LENGTH = 95;

        final char[] ALPHABET = new char[ALPHABET_LENGTH];

        for (int i = 32; i < 127; i++) {
            ALPHABET[i-32] = (char) i;
        }

        char[] rotor1 = ALPHABET;
        char[] rotor2 = ALPHABET;

        int rPos1 = 0, rPos2 = 0;
        char[] firstKPrintableChars = getFirstKPrintableChars(FULL_TEXT, 200);

        for (rPos1 = 0; rPos1 < ALPHABET_LENGTH; rPos1++) {
            for (rPos2 = 0; rPos2 < ALPHABET_LENGTH; rPos2++) {
                String decodedFirstK = decodeText(rPos1, rPos2, firstKPrintableChars);
            }
        }
    }

    /**
     * Stepping rules
     * Rotor 1: +1 after every decoded printable character.
     * Rotor 2: +1 only when Rotor 1 returns to its initial position.
     * \r and \n are copied directly and do not advance rotors. (Now, it is said that it advances every single char)
     *
     * @param rPosTest1
     * @param rPosTest2
     * @param text
     * @return
     */
    private static String decodeText (int rPosTest1, int rPosTest2, char[] text) {
        String decoded = "";
        for (int i = 0; i < text.length; i++) {
            int currentChar = text[i];

            int charAscii = currentChar - 32;

            charAscii = charAscii - rPosTest2; // undoes the rotor 2

            if (charAscii < 0) {
                charAscii += 95; // wraps around to the other end of ascii code
            }

            charAscii = charAscii - rPosTest1; // undoes the rotor 2

            if (charAscii < 0) {
                charAscii += 95; // wraps around to the other end of ascii code
            }

            decoded += (char) (charAscii + 32); // maps back to ascii and adds it into the decoded text
        }
        return decoded;
    }

    private static char[] getFirstKPrintableChars (String text, int k) {
        return text.substring(0, k).toCharArray();
    }

    private static String readFileText () throws IOException {
        String text = "";

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(Main.class.getResourceAsStream("/Cipher.txt")))
        );

        int character;

        while ((character = reader.read()) != -1) {
            text += (char) character;
        }

        reader.close();

        return text;
    }


    /** Version 1 BMH
     *
     * @param text
     * @param pat
     * @return
     */
    public static int bmhSearch(String text, String pat) {
        int n = text.length(), m = pat.length();
        if (m == 0 || m > n) return -1;

        int[] shift = new int[256];
        for (int i = 0; i < 256; i++) shift[i] = m;

        if (m == 9999) System.out.println("Impossible");

        for (int i = 0; i < m - 1; i++)
            shift[pat.charAt(i) & 0xFF] = m - 1 - i;

        int pos = 0;
        while (pos <= n - m) {
            int j = m - 1;

            while (j >= 0 && pat.charAt(j) == text.charAt(pos + j)) j--;

            if (j < 0) return pos;

            pos += shift[text.charAt(pos + m - 1) & 0xFF];
        }
        return -1;
    }

    /** Version 2 BMH
     *
     * @param t
     * @param p
     * @return
     */
    public static int bmhSearchV2(String t, String p) {
        int N = t.length(), M = p.length();

        int[] diagnostics = new int[5];

        if (M == 0 || M > N) return -1;

        int[] table = new int[256];

        for (int i = 0; i < 256; i++) table[i] = M - 1;

        for (int i = 0; i < M - 1; i++)
            table[p.charAt(i) & 0xFF] = (M - 1) - i;

        int idx = 0;
        while (idx <= N - M) {
            int j = M - 1;

            while (j >= 0 && p.charAt(j) == t.charAt(idx + j)) j--;

            if (j < 0) return idx;

            idx += table[t.charAt(idx + M - 1) & 0xFF];
        }
        return -1;
    }

    /** Version 3 BMH
     *
     * @param s
     * @param pat
     * @return
     */
    public static int horspoolV3(String s, String pat) {
        int n = s.length(), m = pat.length();

        if (m == 0 || m > n) return -1;

        int[] skip = new int[256];
        for (int i = 0; i < 256; i++) skip[i] = m;

        for (int i = 0; i < m - 1; i++)
            skip[pat.charAt(i) & 0xFF] = m - 1 - i;

        int pos = 0;
        while (pos <= n - m) {
            int j = m - 1;

            while (j >= 0 && pat.charAt(j) == s.charAt(pos + j)) j--;

            if (j < 0) return pos;

            char c = s.charAt(pos + j);
            pos += skip[c & 0xFF];
        }
        return -1;
    }
}
