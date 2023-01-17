#ifndef TREE_H
#define TREE_H

typedef struct  tree
{
    struct tree *left_child;
    struct tree *right_child;
    struct tree *father;
    void *info;

}node_tree , *tree;

tree new_tree(void *val);
node_tree *create_node(void *info);

void print_tree_postfixe(tree arbre, void print_tree_info(void *value1));
void free_tree(tree arbre);


#endif