
#include <stdlib.h> // per size_t
#include <string.h> // per memcpy
#include "unity.h"
#include "unity.c"

#include "es1.h"

// Funzione di confronto per ordinamento crescente
int compare_int(const void *a, const void *b) {
    int int_a = *((int*) a);
    int int_b = *((int*) b);

    if (int_a == int_b) return 0;
    else if (int_a < int_b) return -1;
    else return 1;
}
int compare_string(const void *a, const void *b) {
    char *str_a = *((char**) a);
    char *str_b = *((char**) b);

    return strcmp(str_a, str_b);
}
int compare_float(const void *a, const void *b) {
    float float_a = *((float*) a);
    float float_b = *((float*) b);

    if (float_a == float_b) return 0;
    else if (float_a < float_b) return -1;
    else return 1;
}

void test_merge_sort_string(void) {
    char *array[] = {"banana", "apple", "cherry", "date"};
    char *sorted_array[] = {"apple", "banana", "cherry", "date"};
    merge_sort(array, 4, sizeof(char*), compare_string);
    TEST_ASSERT_EQUAL_STRING_ARRAY(sorted_array, array, 4);
}

void test_quick_sort_string(void) {
    char *array[] = {"banana", "apple", "cherry", "date"};
    char *sorted_array[] = {"apple", "banana", "cherry", "date"};
    quick_sort(array, 4, sizeof(char*), compare_string);
    TEST_ASSERT_EQUAL_STRING_ARRAY(sorted_array, array, 4);
}

void test_merge_sort_float(void) {
    float array[] = {1.2f, 3.4f, 2.2f, 0.1f};
    float sorted_array[] = {0.1f, 1.2f, 2.2f, 3.4f};
    merge_sort(array, 4, sizeof(float), compare_float);
    TEST_ASSERT_EQUAL_FLOAT_ARRAY(sorted_array, array, 4);
}

void test_quick_sort_float(void) {
    float array[] = {1.2f, 3.4f, 2.2f, 0.1f};
    float sorted_array[] = {0.1f, 1.2f, 2.2f, 3.4f};
    quick_sort(array, 4, sizeof(float), compare_float);
    TEST_ASSERT_EQUAL_FLOAT_ARRAY(sorted_array, array, 4);
}
void test_merge_sort_single_string(void) {
    char *array[] = {"apple"};
    char *sorted_array[] = {"apple"};
    merge_sort(array, 1, sizeof(char*), compare_string);
    TEST_ASSERT_EQUAL_STRING_ARRAY(sorted_array, array, 1);
}

void test_quick_sort_single_string(void) {
    char *array[] = {"apple"};
    char *sorted_array[] = {"apple"};
    quick_sort(array, 1, sizeof(char*), compare_string);
    TEST_ASSERT_EQUAL_STRING_ARRAY(sorted_array, array, 1);
}

void test_merge_sort_same_strings(void) {
    char *array[] = {"apple", "apple", "apple"};
    char *sorted_array[] = {"apple", "apple", "apple"};
    merge_sort(array, 3, sizeof(char*), compare_string);
    TEST_ASSERT_EQUAL_STRING_ARRAY(sorted_array, array, 3);
}

void test_quick_sort_same_strings(void) {
    char *array[] = {"apple", "apple", "apple"};
    char *sorted_array[] = {"apple", "apple", "apple"};
    quick_sort(array, 3, sizeof(char*), compare_string);
    TEST_ASSERT_EQUAL_STRING_ARRAY(sorted_array, array, 3);
}

void test_merge_sort_single_float(void) {
    float array[] = {1.2f};
    float sorted_array[] = {1.2f};
    merge_sort(array, 1, sizeof(float), compare_float);
    TEST_ASSERT_EQUAL_FLOAT_ARRAY(sorted_array, array, 1);
}

void test_quick_sort_single_float(void) {
    float array[] = {1.2f};
    float sorted_array[] = {1.2f};
    quick_sort(array, 1, sizeof(float), compare_float);
    TEST_ASSERT_EQUAL_FLOAT_ARRAY(sorted_array, array, 1);
}

void test_merge_sort_same_floats(void) {
    float array[] = {1.2f, 1.2f, 1.2f};
    float sorted_array[] = {1.2f, 1.2f, 1.2f};
    merge_sort(array, 3, sizeof(float), compare_float);
    TEST_ASSERT_EQUAL_FLOAT_ARRAY(sorted_array, array, 3);
}

void test_quick_sort_same_floats(void) {
    float array[] = {1.2f, 1.2f, 1.2f};
    float sorted_array[] = {1.2f, 1.2f, 1.2f};
    quick_sort(array, 3, sizeof(float), compare_float);
    TEST_ASSERT_EQUAL_FLOAT_ARRAY(sorted_array, array, 3);
}


void test_merge_sort_single_element(void) {/**/
    int array[] = {1};
    int sorted_array[] = {1};
    merge_sort(array, 1, sizeof(int), compare_int);
    TEST_ASSERT_EQUAL_INT_ARRAY(sorted_array, array, 1);
}

void test_quick_sort_single_element(void) {/**/
    int array[] = {1};
    int sorted_array[] = {1};
    quick_sort(array, 1, sizeof(int), compare_int);
    TEST_ASSERT_EQUAL_INT_ARRAY(sorted_array, array, 1);
}

void test_merge_sort_duplicate_elements(void) {/**/
    int array[] = {5, 2, 2, 6, 1, 1};
    int sorted_array[] = {1, 1, 2, 2, 5, 6};
    merge_sort(array, 6, sizeof(int), compare_int);
    TEST_ASSERT_EQUAL_INT_ARRAY(sorted_array, array, 6);
}
void test_quick_sort_duplicate_elements(void) {/**/
    int array[] = {5, 2, 2, 6, 1, 1};
    int sorted_array[] = {1, 1, 2, 2, 5, 6};
    quick_sort(array, 6, sizeof(int), compare_int);
    TEST_ASSERT_EQUAL_INT_ARRAY(sorted_array, array, 6);
}

void test_merge_sort_already_sorted(void) {/**/
    int array[] = {1, 2, 3, 4, 5, 6};
    int sorted_array[] = {1, 2, 3, 4, 5, 6};
    merge_sort(array, 6, sizeof(int), compare_int);
    TEST_ASSERT_EQUAL_INT_ARRAY(sorted_array, array, 6);
}
void test_quick_sort_already_sorted(void) {/**/
    int array[] = {1, 2, 3, 4, 5, 6};
    int sorted_array[] = {1, 2, 3, 4, 5, 6};
    quick_sort(array, 6, sizeof(int), compare_int);
    TEST_ASSERT_EQUAL_INT_ARRAY(sorted_array, array, 6);
}

void test_merge_sort_reverse_sorted(void) {/**/
    int array[] = {6, 5, 4, 3, 2, 1};
    int sorted_array[] = {1, 2, 3, 4, 5, 6};
    merge_sort(array, 6, sizeof(int), compare_int);
    TEST_ASSERT_EQUAL_INT_ARRAY(sorted_array, array, 6);
}

void test_quick_sort_reverse_sorted(void) {/**/
    int array[] = {6, 5, 4, 3, 2, 1};
    int sorted_array[] = {1, 2, 3, 4, 5, 6};
    quick_sort(array, 6, sizeof(int), compare_int);
    TEST_ASSERT_EQUAL_INT_ARRAY(sorted_array, array, 6);
}




void test_merge_sort(void) {/**/
    int array[] = {5, 2, 4, 6, 1, 3};
    int sorted_array[] = {1, 2, 3, 4, 5, 6};
    merge_sort(array, 6, sizeof(int), compare_int);
    TEST_ASSERT_EQUAL_INT_ARRAY(sorted_array, array, 6);
}

void test_quick_sort(void) {/**/
    int array[] = {5, 2, 4, 6, 1, 3};
    int sorted_array[] = {1, 2, 3, 4, 5, 6};
    quick_sort(array, 6, sizeof(int), compare_int);
    TEST_ASSERT_EQUAL_INT_ARRAY(sorted_array, array, 6);
}

int main(void) { 
    UNITY_BEGIN();
    RUN_TEST(test_merge_sort_string);
    RUN_TEST(test_quick_sort_string);
    RUN_TEST(test_merge_sort_float);
    RUN_TEST(test_quick_sort_float);
    RUN_TEST(test_merge_sort_single_string);
    RUN_TEST(test_quick_sort_single_string);
    RUN_TEST(test_merge_sort_same_strings);
    RUN_TEST(test_quick_sort_same_strings);
    RUN_TEST(test_merge_sort_single_float);
    RUN_TEST(test_quick_sort_single_float);
    RUN_TEST(test_merge_sort_same_floats);
    RUN_TEST(test_quick_sort_same_floats);
    RUN_TEST(test_merge_sort);
    RUN_TEST(test_quick_sort);
    RUN_TEST(test_merge_sort_reverse_sorted);
    RUN_TEST(test_merge_sort_already_sorted);
    RUN_TEST(test_merge_sort_duplicate_elements);
    RUN_TEST(test_merge_sort_single_element);
    RUN_TEST(test_quick_sort_reverse_sorted);
    RUN_TEST(test_quick_sort_already_sorted);
    RUN_TEST(test_quick_sort_duplicate_elements);
    RUN_TEST(test_quick_sort_single_element);

    return UNITY_END();
}
