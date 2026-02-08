#ifndef ES1_H
#define ES1_H

#include <stdlib.h>
#include <string.h>

void merge(void *base, size_t size, int (*compar)(const void*, const void*), int start, int mid, int end);
void merge_sort(void *base, size_t nitems, size_t size, int (*compar)(const void*, const void*));
void swap(void *a, void *b, size_t size);
int median_of_three(void *base, size_t size, int (*compar)(const void*, const void*), int low, int high);
int partition(void *base, size_t size, int (*compar)(const void*, const void*), int low, int high);
void quick_sort(void *base, size_t nitems, size_t size, int (*compar)(const void*, const void*));

#endif 
