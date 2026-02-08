#ifndef EDIT_DISTANCE_DYN_H
#define EDIT_DISTANCE_DYN_H

int min_dyn(int a, int b, int c);
int edit_distance_dyn_rec(const char *s1, const char *s2, int i, int j, int** dp);
int edit_distance_dyn(const char *s1, const char *s2);

#endif 
