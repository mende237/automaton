#ifndef TREE_C
#define TREE_C
#include "../../header/data_structure/tree.h"
#include <stdlib.h>

tree new_tree(void *val){
    node_tree *root = malloc(sizeof(node_tree));
    root->info = val;
    root->left_child = NULL;
    root->right_child = NULL;
    root->father = NULL;

    return root;
}

node_tree *create_node(void *info){
    return new_tree(info);
}

void print_tree_postfixe(tree arbre ,void print_tree_info(void *value)){
    if(arbre == NULL)
        return;

    print_tree_postfixe(arbre->left_child, print_tree_info);
    print_tree_postfixe(arbre->right_child, print_tree_info);
    print_tree_info(arbre); 
}


void free_tree(tree arbre){

    if(arbre == NULL)
        return;
    
    free_tree(arbre->left_child);
    free_tree(arbre->right_child);

    free(arbre);
}

#endif