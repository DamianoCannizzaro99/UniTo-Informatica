#include <stdio.h>
#include <stdlib.h>
#include "es1.h"
#include <time.h>

int max_records = 10000000;

typedef struct {
    int id;
    char field1[100];
    int field2;
    float field3;
} Record;
int compare_field1(const void *a, const void *b) {
    Record *recordA = (Record *)a;
    Record *recordB = (Record *)b;
    return strcmp(recordA->field1, recordB->field1);
}

int compare_field2(const void *a, const void *b) {
    Record *recordA = (Record *)a;
    Record *recordB = (Record *)b;
    return (recordA->field2 - recordB->field2);
}

int compare_field3(const void *a, const void *b) {
    Record *recordA = (Record *)a;
    Record *recordB = (Record *)b;
    if (recordA->field3 < recordB->field3) return -1;
    if (recordA->field3 > recordB->field3) return 1;
    return 0;
}
void sort_records(char *infile, char *outfile, size_t field, size_t algo) {
    FILE *file = fopen(infile, "r");
    if (file == NULL) {
        printf("Non è stato possibile aprire il file.\n");
        return;
    }

    size_t records_size = 100;  // inizia con una stima
    Record *records = (Record*) malloc(records_size * sizeof(Record));
    if (records == NULL) {
        printf("Non è stato possibile allocare la memoria.\n");
        return;
    }

    size_t i = 0;
    while (i<max_records&&fscanf(file, "%d,%[^,],%d,%f\n", &records[i].id, records[i].field1, &records[i].field2, &records[i].field3) != EOF) {
        i++;
        if (i >= records_size) {
            records_size *= 2;  // raddoppia la dimensione
            records = (Record*) realloc(records, records_size * sizeof(Record));
            if (records == NULL) {
                printf("Non è stato possibile riallocare la memoria.\n");
                return;
            }
        }
    }
    fclose(file);
   
    int (*compare)(const void *, const void *);
    switch (field) {
        case 1:
            compare = compare_field1;
            break;
        case 2:
            compare = compare_field2;
            break;
        case 3:
            compare = compare_field3;
            break;
        default:
            printf("Campo non valido.\n");
            return;
    }
   
    switch (algo) {
        case 1:
            merge_sort(records, i, sizeof(Record), compare);
            break;
        case 2:
            quick_sort(records, i, sizeof(Record), compare);
            break;
        default:
            printf("Algoritmo non valido.\n");
            return;
    }

       FILE *outFile = fopen(outfile, "w");
    if (outFile == NULL) {
        printf("Non è stato possibile aprire il file di output.\n");
        return;
    }

   
     switch (field) {
        case 1:
             for (int j = 0; j < i; j++) {
            fprintf(outFile, "%s,%d,%f\n",  records[j].field1, records[j].field2, records[j].field3);
            }
            break;
        case 2:
             for (int j = 0; j < i; j++) {
        fprintf(outFile, "%d,%s,%f\n",  records[j].field2, records[j].field1, records[j].field3);
        }
            break;
        case 3:
             for (int j = 0; j < i; j++) {
        fprintf(outFile, "%f,%s,%d\n",  records[j].field3, records[j].field1, records[j].field2);
    }
            break;
        default:
            printf("Campo non valido.\n");
            return;
    }
    fclose(outFile);
    free(records);
}


int main(int argc, char *argv[]) {
    if (argc != 5) {
        printf("Utilizzo: %s <input_file> <output_file> <field> <algo>\n", argv[0]);
        return 1;
    }

    char *infile = argv[1];
    char *outfile = argv[2];
    size_t field = atoi(argv[3]);
    size_t algo = atoi(argv[4]);

    clock_t inizio, fine;
    double tempo_impiegato;

    inizio = clock();
    
    sort_records(infile, outfile, field, algo);

    fine = clock();

    tempo_impiegato = ((double) (fine - inizio)) / CLOCKS_PER_SEC;

    printf("Il programma è stato eseguito in: %f secondi\n", tempo_impiegato);
    return 0;
}

