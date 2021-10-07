#ifndef THOMSON_C
#define THOMSON_C
#include "../../header/algorithm/thomson.h"
#include "../data_structure/linked_list.c"
#include "utilitaire.c"
#include "../data_structure/stack.c"
#include <stdlib.h>
#include <math.h>

REG new_REG()
{
    REG reg = malloc(sizeof(REG_elem));
    reg->end_state = NULL;
    reg->initiale_state = NULL;
    //reg->mat_trans = new_list();
}

AFN thomson_algorithm(char **expression, int length, void print_info(void *src, void *lbl, void *dest))
{
    int i = 0;
    list li_result = convert_inf_to_post(expression, length);

    char **result = malloc(li_result->length * sizeof(char *));

    for (i = 0; i < li_result->length; i++)
    {
        result[i] = get_element_list(li_result, i);
    }

    tree t = convert_post_to_thomson_tree(result, li_result->length);
    REG reg = construct_automate(t);

    int nbr_state = get_nbre_state(result, li_result->length);
    int nbr_label = get_nbre_label(result, li_result->length);

    AFN afn = new_AFN(nbr_state, 1, 1, nbr_label, "ep");

    for (i = 0; i < reg->mat_trans->length; i++)
    {
        char **tab = get_element_list(reg->mat_trans, i);
        add_transition_AFN(afn, tab[0], tab[1], tab[2]);
    }

    afn->initiale_state[0] = reg->initiale_state;
    afn->finale_state[0] = reg->end_state;
    afn->nbre_initiale_state = 1;
    afn->nbre_finale_state = 1;

    free_REG(reg);
    return afn;
}

REG construct_automate(tree t)
{
    if (t == NULL)
    {
        return NULL;
    }
    else
    {
        thomson_node *thom_n = t->info;
        if (t->left_child == NULL && t->right_child == NULL)
        {
            REG reg = new_REG();
            char **trans = malloc(3 * sizeof(char *));
            trans[0] = thom_n->initiale_state;
            trans[1] = thom_n->value;
            trans[2] = thom_n->end_state;

            reg->initiale_state = thom_n->initiale_state;
            reg->end_state = thom_n->end_state;
            list li = new_list();

            head_insertion(li, trans);
            reg->mat_trans = li;
            free(thom_n);
            return reg;
        }
        else if (strcmp("+", thom_n->value) == 0)
        {
            char *end_state = thom_n->end_state;
            char *initial_state = thom_n->initiale_state;
            free(thom_n);
            return union_reg(construct_automate(t->left_child), construct_automate(t->right_child), initial_state, end_state);
        }
        else if (strcmp(".", thom_n->value) == 0)
        {
            free(thom_n);
            return concat_reg(construct_automate(t->left_child), construct_automate(t->right_child));
        }
        else
        {
            char *end_state = thom_n->end_state;
            char *initial_state = thom_n->initiale_state;
            free(thom_n);

            return start_reg(construct_automate(t->left_child), initial_state, end_state);
        }
    }
}

REG union_reg(REG reg1, REG reg2, char *initial, char *final)
{
    int i = 0;
    REG reg_result = new_REG();
    list tmp = new_list();
    //if()
    for (i = 0; i < reg1->mat_trans->length; i++)
    {
        head_insertion(tmp, get_element_list(reg1->mat_trans, i));
    }

    for (i = 0; i < reg2->mat_trans->length; i++)
    {
        head_insertion(tmp, get_element_list(reg2->mat_trans, i));
    }
   
    //***********a free**************************************
    char **trans1 = malloc(3 * sizeof(char *));
    char **trans2 = malloc(3 * sizeof(char *));
    char **trans3 = malloc(3 * sizeof(char *));
    char **trans4 = malloc(3 * sizeof(char *));

    trans1[0] = initial;
    trans1[1] = "ep";
    trans1[2] = reg1->initiale_state;

    trans2[0] = initial;
    trans2[1] = "ep";
    trans2[2] = reg2->initiale_state;

    trans3[0] = reg1->end_state;
    trans3[1] = "ep";
    trans3[2] = final;

    trans4[0] = reg2->end_state;
    trans4[1] = "ep";
    trans4[2] = final;

    head_insertion(tmp, trans1);
    head_insertion(tmp, trans2);
    head_insertion(tmp, trans3);
    head_insertion(tmp, trans4);

    reg_result->initiale_state = initial;
    reg_result->end_state = final;
    reg_result->mat_trans = tmp;

    free(reg1);
    free(reg2);
    return reg_result;
}

REG concat_reg(REG reg1, REG reg2)
{
    int i = 0;
    REG reg_result = new_REG();
    list tmp = new_list();
    for (i = 0; i < reg1->mat_trans->length; i++)
    {
        head_insertion(tmp, get_element_list(reg1->mat_trans, i));
    }

    for (i = 0; i < reg2->mat_trans->length; i++)
    {
        head_insertion(tmp, get_element_list(reg2->mat_trans, i));
    }

    char **trans = malloc(3 * sizeof(char *));

    trans[0] = reg1->end_state;
    trans[1] = "ep";
    trans[2] = reg2->initiale_state;
    head_insertion(tmp, trans);

    reg_result->initiale_state = reg1->initiale_state;
    reg_result->end_state = reg2->end_state;
    reg_result->mat_trans = tmp;

    free(reg1);
    free(reg2);
    return reg_result;
}

REG start_reg(REG reg, char *initial, char *final)
{
    int i = 0;
    REG reg_result = new_REG();
    list tmp = new_list();

    for (i = 0; i < reg->mat_trans->length; i++)
    {
        head_insertion(tmp, get_element_list(reg->mat_trans, i));
    }

    //**************************a free************************
    char **trans1 = malloc(3 * sizeof(char *));
    char **trans2 = malloc(3 * sizeof(char *));
    char **trans3 = malloc(3 * sizeof(char *));
    char **trans4 = malloc(3 * sizeof(char *));

    //**********************a free***********************

    trans1[0] = initial;
    trans1[1] = "ep";
    trans1[2] = reg->initiale_state;

    trans2[0] = initial;
    trans2[1] = "ep";
    trans2[2] = final;

    trans3[0] = reg->end_state;
    trans3[1] = "ep";
    trans3[2] = reg->initiale_state;

    trans4[0] = reg->end_state;
    trans4[1] = "ep";
    trans4[2] = final;

    head_insertion(tmp, trans1);
    head_insertion(tmp, trans2);
    head_insertion(tmp, trans3);
    head_insertion(tmp, trans4);

    reg_result->initiale_state = initial;
    reg_result->end_state = final;

    reg_result->mat_trans = tmp;
    free(reg);
    return reg_result;
}

tree convert_post_to_thomson_tree(char **expression, int length)
{
    int max_state = 0;
    stack pile = new_stack();
    int i = 0, index = 0, nbr_operande = 0;

    for (i = 0; i < length; i++)
    {
        if (is_operator(expression[i]) == False && strcmp("ep", expression[i]) != 0)
        {
            nbr_operande++;
        }
    }
    char *initial_state = malloc(20 * sizeof(char));
    char *final_state = malloc(20 * sizeof(char));
    //il s'agit de l'etat initiale et l'etat finale de l'automate de thomson

    thomson_node *node = malloc(sizeof(thomson_node));
    node->value = expression[length - 1];
    if (strcmp(expression[length - 1], ".") == 0)
    {
        node->initiale_state = NULL;
        node->end_state = NULL;
        max_state = -1;
    }
    else
    {
        printf("enter");
        sprintf(initial_state, "%d", 0);
        sprintf(final_state, "%d", 1);
        node->initiale_state = initial_state;
        node->end_state = final_state;
        max_state = 1;
    }

    node_tree *root = create_node(node);

    push(pile, root);

    for (i = length - 2; i >= 0; i--)
    {
        if (is_operator(expression[i]) == True)
        {
            if (is_empty_stack(pile) == True)
            {
                push(pile, create_node(expression[i]));
            }
            else
            {
                node_tree *old_op = pop(pile);
                //determinisation des valeurs de la valeur de l'etat initiale et de l'etat
                //finale d'un noeud de thomson
                char *bg_state;
                char *fn_state;
                if (strcmp(expression[i], ".") == 0)
                {
                    //printf("%s", expression[i]);
                    bg_state = NULL;
                    fn_state = NULL;
                }
                else
                {
                    //**************a free*********************
                    bg_state = malloc(20 * sizeof(char));
                    fn_state = malloc(20 * sizeof(char));
                    sprintf(bg_state, "%d", max_state + 1);
                    sprintf(fn_state, "%d", max_state + 2);
                    max_state += 2;
                }
                //************a free***********************
                thomson_node *thom_n_tmp = malloc(sizeof(thomson_node));

                thom_n_tmp->value = expression[i];
                thom_n_tmp->initiale_state = bg_state;
                thom_n_tmp->end_state = fn_state;

                node_tree *new_op = create_node(thom_n_tmp);
                //creation de la liaison entre l'ancien noeud et le nouveau noeud de l'arbre
                new_op->father = old_op;
                thomson_node *thom_n = old_op->info;
                if (strcmp(thom_n->value, "*") == 0)
                {
                    old_op->left_child = new_op;
                    push(pile, new_op);
                }
                else
                {
                    if (old_op->right_child != NULL)
                    {
                        old_op->left_child = new_op;
                        push(pile, new_op);
                    }
                    else
                    {
                        old_op->right_child = new_op;
                        push(pile, old_op);
                        push(pile, new_op);
                    }
                }
            }
        }
        else
        {
            //************a free**************************
            char *bg_state = malloc(20 * sizeof(char));
            char *fn_state = malloc(20 * sizeof(char));

            sprintf(bg_state, "%d", max_state + 1);
            sprintf(fn_state, "%d", max_state + 2);

            //*************a free*************************
            thomson_node *thom_n_tmp = malloc(sizeof(thomson_node));

            thom_n_tmp->value = expression[i];
            thom_n_tmp->initiale_state = bg_state;
            thom_n_tmp->end_state = fn_state;

            max_state += 2;

            node_tree *val = create_node(thom_n_tmp);
            node_tree *op = pop(pile);
            val->father = op;
            thomson_node *thom_n = op->info;
            if (strcmp(thom_n->value, "*") == True)
            {
                op->left_child = val;
            }
            else
            {
                if (op->right_child != NULL)
                {
                    op->left_child = val;
                }
                else
                {
                    op->right_child = val;
                    push(pile, op);
                }
            }
        }
    }

    return root;
}

void free_REG(REG reg)
{
    int i = 0;
    for (i = 0; i < reg->mat_trans->length; i++)
    {
        free(get_element_list(reg->mat_trans, i));
    }
    free(reg);
}

#endif