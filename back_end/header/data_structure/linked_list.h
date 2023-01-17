#ifndef LINKED_LIST_H
#define LINKED_LIST_H
#include "structure.h"

typedef struct List
{
    struct element *head;
    struct element *queue;
    int length;
} * list , list_element;


list new_list(void);
boolean is_empty_list(list li);
boolean search_value_in_list(list li, void *value, boolean equal(void *val1, void *val2 , ...) , ...);
int get_index_element_list(list li, void *value, boolean equal(void *val1, void *val2 , ...));
void print_list(list li, void print_value(void *value , boolean last));
void queue_insertion(list li, void *x);
void head_insertion(list li, void *x);
void *get_element_list(list li , int index);
list copy_element_list(list li);
boolean include_value_list(list li1, list li2, boolean equal(void *val1, void *val2 , ...));
void delete_element_list(list li , int index);
void free_list(list li);

#endif 