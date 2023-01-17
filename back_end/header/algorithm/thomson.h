#ifndef THOMSON_H
#define THOMSON_H
#include "../data_structure/tree.h"
#include "../data_structure/linked_list.h"
#include "AFN.h"

typedef struct pseudo_AFN{
    char *initiale_state;
    int a;
    char *end_state;
    list mat_trans;
} * pseudo_AFN, pseudo_AFN_elem;

typedef struct thomson_node{
    char *value;
    char *initiale_state;
    char *end_state;
}thomson_node;

pseudo_AFN new_pseudo_AFN();
AFN thomson_algorithm(char **expression, int length, list garbage);
pseudo_AFN construct_automate(tree t);

tree convert_post_to_thomson_tree(char **expression, int length , list garbage);

pseudo_AFN union_p_AFN(pseudo_AFN p_afn1, pseudo_AFN p_afn2, char *initial, char *final);
pseudo_AFN concat_p_AFN(pseudo_AFN p_afn1, pseudo_AFN p_afn2);
pseudo_AFN start_p_AFN(pseudo_AFN p_afn, char *initial, char *finallist);

void free_REG(pseudo_AFN reg);
#endif