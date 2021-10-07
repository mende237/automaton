#ifndef STACK_H
#define STACK_H
#include "structure.h"

typedef struct stack
{
    int nbre_element;
    struct element *head;

}*stack , stack_element;

stack new_stack(void);
boolean is_empty_stack(stack stk);
void *pop(stack stk);
void push(stack stk , void *value);
void print_stack(stack stk , void print_value(void *value));
void free_stack(stack stk);

#endif