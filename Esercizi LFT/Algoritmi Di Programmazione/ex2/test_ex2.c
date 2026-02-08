#include "unity.h"
#include <string.h>
#include <stdlib.h>
#include "unity.c"
#include "unity.h"
#include "es2.h"
#include "es2dinamico.h"


void test_edit_distance_same_string(void) {
    TEST_ASSERT_EQUAL_INT(0, edit_distance("test", "test"));
}
void test_edit_distance_dyn_same_string(void) {
    TEST_ASSERT_EQUAL_INT(0, edit_distance_dyn("test", "test"));
}
void test_edit_distance_dyn_empty_strings(void) {
    TEST_ASSERT_EQUAL_INT(0, edit_distance_dyn("", ""));
}
void test_edit_distance_empty_strings(void) {
    TEST_ASSERT_EQUAL_INT(0, edit_distance_dyn("", ""));
}

void test_edit_distance_one_empty_string(void) {
    
    TEST_ASSERT_EQUAL_INT(4, edit_distance("", "test"));
    TEST_ASSERT_EQUAL_INT(4, edit_distance("test", ""));
}

void test_edit_distance_dyn_one_empty_string(void) {
    
    TEST_ASSERT_EQUAL_INT(4, edit_distance_dyn("", "test"));
    TEST_ASSERT_EQUAL_INT(4, edit_distance_dyn("test", ""));
}

void test_edit_distance_general_cases(void) {
    TEST_ASSERT_EQUAL_INT(1, edit_distance("test", "tests"));
    TEST_ASSERT_EQUAL_INT(2, edit_distance("test", "tent"));
    TEST_ASSERT_EQUAL_INT(1, edit_distance("test", "tst"));
    TEST_ASSERT_EQUAL_INT(2, edit_distance("test", "te"));
}

void test_edit_distance_dyn_general_cases(void) {
    TEST_ASSERT_EQUAL_INT(1, edit_distance_dyn("test", "tests"));
    TEST_ASSERT_EQUAL_INT(2, edit_distance_dyn("test", "tent"));
    TEST_ASSERT_EQUAL_INT(1, edit_distance_dyn("test", "tst"));
    TEST_ASSERT_EQUAL_INT(2, edit_distance_dyn("test", "te"));
}


int main(void) {
    UNITY_BEGIN();
    
    RUN_TEST(test_edit_distance_same_string);
    RUN_TEST(test_edit_distance_empty_strings);
    RUN_TEST(test_edit_distance_one_empty_string);
    RUN_TEST(test_edit_distance_general_cases);

    RUN_TEST(test_edit_distance_dyn_same_string);
    RUN_TEST(test_edit_distance_dyn_empty_strings);
    RUN_TEST(test_edit_distance_dyn_one_empty_string);
    RUN_TEST(test_edit_distance_dyn_general_cases);
    
    
    return UNITY_END();
}
