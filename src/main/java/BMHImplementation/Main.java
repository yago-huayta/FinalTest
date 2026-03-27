package BMHImplementation;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        final String FULL_TEXT = readFileText();

    }

    private static String readFileText () {
        return "";
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
