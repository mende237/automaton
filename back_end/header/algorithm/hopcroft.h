#ifndef HOPCROFT_H
#define HOPCROFT_H
#include "../data_structure/linked_list.h"
#include "../data_structure/stack.h"
#include "../algorithm/AFD.h"
#include "../data_structure/structure.h"

struct Breaker{
    list set;
    char *label;
};

static list *break_block(list state, struct Breaker *breaker, AFD afd, boolean equal_value(void *lb1, void *lb2 ,...), void print_element_in_list(void *x, boolean last));

static list intersection_set(list state1, list state2);

static list complementaire_set(list set_A , list set_B);

static list left_quotient(struct Breaker *breaker, AFD afd, boolean equal_value(void *lb1, void *lb2 , ...));

static void **get_state_tab(AFD afd, boolean equal_value(void *lb1, void *lb2 , ...));

static list smallest_set(list set1 , list set2);

static boolean equal_breaker(void *b1, void *b2 , ...);

static boolean equal_string(void *ch1 , void *ch2 , ...);

AFD hopcroft_minimisation(AFD afd, boolean equal_value(void *lb1, void *lb2 , ...), void print_element_in_list(void *x, boolean last));

#endif