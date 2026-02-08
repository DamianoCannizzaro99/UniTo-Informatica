
#include <string.h>
#include <stdlib.h>
#include "es2dinamico.h"
int min_dyn(int a, int b, int c) {
    int min = a;
    if (b < min) min = b;
    if (c < min) min = c;
    return min; 
}

int edit_distance_dyn_rec(const char *s1, const char *s2, int i, int j, int** dp) {
    if (i == 0) return j;
    if (j == 0) return i;

    if (dp[i][j] != -1) return dp[i][j];

    int d_no_op = __INT_MAX__;
    if (s1[i-1] == s2[j-1]) {
        d_no_op = edit_distance_dyn_rec(s1, s2, i - 1, j - 1, dp);
    }

    int d_canc = 1 + edit_distance_dyn_rec(s1, s2, i, j - 1, dp);
    int d_ins = 1 + edit_distance_dyn_rec(s1, s2, i - 1, j, dp);

    dp[i][j] = min_dyn(d_no_op, d_canc, d_ins);

    return dp[i][j];
}

int edit_distance_dyn(const char *s1, const char* s2) {
    int len1 = strlen(s1);
    int len2 = strlen(s2);
    int** dp = (int**)malloc((len1 + 1) * sizeof(int*));
    for (int i = 0; i <= len1; i++) {
        dp[i] = (int*)malloc((len2 + 1) * sizeof(int));
        for (int j = 0; j <= len2; j++) {
            dp[i][j] = -1;
        }
    }
    int result = edit_distance_dyn_rec(s1, s2, len1, len2, dp);

    for (int i = 0; i <= len1; i++) {
        free(dp[i]);
    }
    free(dp);
    return result;
}
