#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>
#include "es2dinamico.h"

#define INITIAL_WORDS 100

void to_lowercase(char *str) {
    for(int i = 0; str[i]; i++){
        str[i] = tolower(str[i]);
    }
}

char **allocate_words(int num_words) {
    return (char **) malloc(num_words * sizeof(char *));
}

void free_words(char **words, int num_words) {
    for (int i = 0; i < num_words; i++) {
        free(words[i]);
    }
    free(words);
}

int read_words(const char *filename, char ***words_ptr) {
    FILE *file = fopen(filename, "r");
    if (file == NULL) {
        printf("Non riesco ad aprire il file %s\n", filename);
        return 0;
    }

    int capacity = INITIAL_WORDS;
    char **words = allocate_words(capacity);

    char word[100];  // Assumiamo che una parola non sarà più lunga di 100 caratteri
    int i = 0;
    while (fscanf(file, "%s", word) != EOF) {
        if (i >= capacity) {
            capacity *= 2;  // Raddoppia la capacità
            words = (char **) realloc(words, capacity * sizeof(char *));
        }
        words[i] = strdup(word);
        to_lowercase(words[i]);
        i++;
    }

    fclose(file);

    *words_ptr = words;
    return i;  // Restituisce il numero di parole lette.
}

int main(int argc, char *argv[]) {
    if (argc != 3) {
        printf("Uso: %s <dictionary_file> <correctme_file>\n", argv[0]);
        return 1;
    }

    const char *dictionary_file = argv[1];
    const char *correctme_file = argv[2];

    char **dictionary;
    int dictionary_size = read_words(dictionary_file, &dictionary);

    char **correctme;
    int correctme_size = read_words(correctme_file, &correctme);

    for (int i = 0; i < correctme_size; i++) {
        int min_distance = __INT_MAX__;
        char **closest_words = allocate_words(dictionary_size);
        int closest_words_size = 0;

        for (int j = 0; j < dictionary_size; j++) {
            int distance = edit_distance_dyn(correctme[i], dictionary[j]);
            if (distance < min_distance) {
                min_distance = distance;
                closest_words_size = 0;
            }
            if (distance == min_distance) {
                closest_words[closest_words_size] = strdup(dictionary[j]);
                closest_words_size++;
            }
        }

        printf("\033[0;34mLe parole nel dizionario più vicine a '%s' sono:\033[0m\n", correctme[i]);
        for (int k = 0; k < closest_words_size; k++) {
            printf("\033[0;32m%s\033[0m (distanza di modifica: %d)\n", closest_words[k], min_distance);
        }

        free_words(closest_words, closest_words_size);
    }

    free_words(dictionary, dictionary_size);
    free_words(correctme, correctme_size);

    return 0;
}
