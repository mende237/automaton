#ifndef STRUCTURE_H
#define STRUCTURE_H

typedef enum boolean
{
    True = 0,
    False = 1
} boolean;

struct etiquette
{
    void *value;
    int index;
};

struct element
{
    void *value;
    struct element *next;
};




#endif