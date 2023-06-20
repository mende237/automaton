#ifndef AFN_H
#define AFN_H
#include "../data_structure/linked_list.h"
#include "../data_structure/structure.h"

typedef struct AFN
{
    int nbre_state;
    int nbre_initiale_state;
    int nbre_finale_state;
    int nbre_label;
    list mat_trans;
    void **initiale_state;
    void **finale_state;
    void **tab_labels;
    void *epsilone;
    int cmpt_label;
} *AFN, AFN_value;


AFN new_AFN(int nbre_state, int nbre_initiale_state, int nbre_finale_state , int nbre_label , void *epsilone);
AFN miroir_AFN(AFN afn);

void add_transition_AFN(AFN afn, void *begin, void *label, void *end);
void print_transitions_AFN(AFN afn, void print_elem(void *begin, void *label, void *end));
list delta_AFN(AFN afn, void *state, void *symbole);
list detect_AFN(AFN afn, void *word, int size);
void AFN_to_jason(AFN afn , char *path);
AFN jason_to_AFN(char *path, list garbage);
void print_AFN(AFN afn, char* print_state(void *state) , boolean equal_state(void* state1, void* state2));

void free_AFN(AFN afn);

#endif