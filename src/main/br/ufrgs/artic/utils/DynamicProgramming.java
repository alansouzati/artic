package br.ufrgs.artic.utils;

/**
 * This class is responsible for adding code related to dynamic programming
 */
public final class DynamicProgramming {

    private DynamicProgramming() {
    }

    public static int distance(String source, String target) {
        if (source == null && target == null) {
            return 0;
        }

        if (source == null) {
            return target.length();
        }

        if (target == null) {
            return source.length();
        }

        int m = source.length();
        int n = target.length();
        int[][] d = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            d[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            d[0][j] = j;
        }
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (source.charAt(i - 1) == target.charAt(j - 1)) {
                    d[i][j] = d[i - 1][j - 1];
                } else {
                    d[i][j] = min((d[i - 1][j] + 1), (d[i][j - 1] + 1),
                            (d[i - 1][j - 1] + 1));
                }
            }
        }
        return (d[m][n]);
    }

    public static int min(int a, int b, int c) {
        return (Math.min(Math.min(a, b), c));
    }
}
