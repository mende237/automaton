#ifndef THOMSON_H
#define THOMSON_H
#include "../data_structure/tree.h"
#include "../data_structure/linked_list.h"
#include "AFN.h"

typedef struct REG{
    char *initiale_state;
    int a;
    char *end_state;
    list mat_trans;
}*REG , REG_elem;


typedef struct thomson_node{
    char *value;
    char *initiale_state;
    char *end_state;
}thomson_node;

REG new_REG();
AFN thomson_algorithm(char **expression, int length, void print_info(void *src, void *lbl, void *dest));
REG construct_automate(tree t);

tree convert_post_to_thomson_tree(char **expression, int length);

REG union_reg(REG reg1, REG reg2 , char *initial , char *final);
REG concat_reg(REG reg1, REG reg2);
REG start_reg(REG reg, char *initial, char *finallist);

void free_REG(REG reg);
#endif