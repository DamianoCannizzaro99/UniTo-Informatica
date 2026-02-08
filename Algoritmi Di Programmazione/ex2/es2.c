#include <string.h>
#include <stdlib.h>
#include "es2.h"

int min(int a, int b, int c) {
    int min = a;
    if (b < min) min = b;
    if (c < min) min = c;
    return min;
}

int edit_distance(const char *s1, const char *s2) {
    if (strlen(s1) == 0) return strlen(s2);
    if (strlen(s2) == 0) return strlen(s1);

    int d_no_op;
    if (s1[0] == s2[0]) {
        d_no_op = edit_distance(s1 + 1, s2 + 1);
    } else {
        d_no_op = __INT_MAX__;
    }

    int d_canc = 1 + edit_distance(s1, s2 + 1);
    int d_ins = 1 + edit_distance(s1 + 1, s2);

    return min(d_no_op, d_canc, d_ins);
}
