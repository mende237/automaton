#ifndef GLUSHKOV_H
#define GLUSHKOV_H
#include "../data_structure/tree.h"
#include "../data_structure/structure.h"
#include "utilitaire.h"
#include "../data_structure/linked_list.h"
#include "AFN.h"

typedef struct linear_element
{
    char *value;
    int index;
} linear_element;

AFN glushkov_algorithm(char **expression, int length , list garbage);

list first(tree T, list garbage);
list last(tree T, list garbage);
char *null(tree T);
list follow(tree T, int x, list garbage);
boolean is_in_set_of_pos(tree T, int x);
boolean is_in_set_of_last_pos(tree T, int x, list garbage);

list product(char *val, list li, list garbage);
boolean include_special(linear_element *val, list li);
list special_union(list li1, list li2);

tree convert_post_to_glushkov_tree(char **expression, int length);

#endif