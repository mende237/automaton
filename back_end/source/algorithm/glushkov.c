#ifndef GLUSHKOV_C
#define GLUSHKOV_C

#include "../../header/algorithm/glushkov.h"
#include "utilitaire.c"
#include "../data_structure/linked_list.c"
#include "../data_structure/stack.c"
#include "../data_structure/tree.c"
#include <string.h>

AFN glushkov_algorithm(char **expression, int length , list garbage2)
{
    int i = 0, j = 0;
    stack pile = new_stack();
    list garbage = new_list();
    list li_result = convert_inf_to_post(expression, length);

    char **result = malloc(li_result->length * sizeof(char *));
    for (i = 0; i < li_result->length; i++)
    {
        result[i] = get_element_list(li_result, i);
    }
    tree t = convert_post_to_glushkov_tree(result, li_result->length);
    list mat_succ = new_list();

    list state_list = new_list();
    queue_insertion(state_list, NULL);

    list initiale_list = first(t, garbage);

    for (i = 0; i < initiale_list->length; i++)
    {
        push(pile, get_element_list(initiale_list, i));
    }

    //on insert la liste des symboles que l'on peut lire initialement
    queue_insertion(mat_succ, initiale_list);

    while (is_empty_stack(pile) == False)
    {
        linear_element *tmp = pop(pile);
        if (include_special(tmp, state_list) == False)
        {
            queue_insertion(state_list, tmp);
            list succ = follow(t, tmp->index, garbage);
            queue_insertion(mat_succ, succ);
            for (i = 0; i < succ->length; i++)
            {
                push(pile, get_element_list(succ, i));
            }
        }
    }

    // printf("%d" , mat_succ->length);
    // printf("%d" , state_list->length);
    // list test = new_list();
    // for (i = 0; i < mat_succ->length; i++)
    // {

    //     list tmp = get_element_list(mat_succ , i);
    //     linear_element *elem = get_element_list(state_list , i);
    //     if(elem != NULL){

    //         printf("%s%d--->" , elem->value , elem->index);
    //     }else{
    //         printf("0---->");
    //     }
    //     print_list(tmp , print_tree_info);
    //     printf("\n");

    // }

    list last_state = last(t, garbage);
    int nbre_label = get_nbre_label(result, li_result->length);
    /*apres l'utilisation de cette AFN toutes variables alloués doivent etre liberé
    chaque element de la liste des etats fianaux doit etre libere de meme que chaque 
    element de la matrice de transition doit etre liberé ainsi que le seul etat 
    initiale*/
    AFN afn = NULL;

    if (strcmp(t->info, "*") == 0)
    {
        afn = new_AFN(state_list->length, 1, (last_state->length + 1), nbre_label, "ep");
    }
    else
    {
        afn = new_AFN(state_list->length, 1, last_state->length, nbre_label, "ep");
    }

    char *initiale = malloc(20 * sizeof(char));
    sprintf(initiale, "%d", 0);
    afn->initiale_state[0] = initiale;

    /*on renplie les etats finaux de l'automate obtenue*/
    for (i = 0; i < last_state->length; i++)
    {
        char *state = malloc(20 * sizeof(char));
        linear_element *elem = get_element_list(last_state, i);
        sprintf(state, "%d", elem->index);
        afn->finale_state[i] = state;
    }
    // printf("%d", last_state->length);
    // char *state = malloc(20 * sizeof(char));
    // sprintf(state, "%d", 0);
    afn->finale_state[last_state->length] = initiale;

    for (i = 0; i < mat_succ->length; i++)
    {
        list tmp = get_element_list(mat_succ, i);
        linear_element *elem = get_element_list(state_list, i);
        for (j = 0; j < tmp->length; j++)
        {
            linear_element *l_elem = get_element_list(tmp, j);
            char *label = l_elem->value;
            /***************a free****************************/
            char *end = malloc(20 * sizeof(char));
            /***********************************************/
            queue_insertion(garbage2 , end);
            sprintf(end, "%d", l_elem->index);
            if (elem != NULL)
            {
                /***************a free****************************/
                char *begin = malloc(20 * sizeof(char));
                /*************************************************/
                queue_insertion(garbage2, begin);
                sprintf(begin, "%d", elem->index);
                add_transition_AFN(afn, begin, label, end);
            }
            else
            {
                //***************a free***************
                char *begin = malloc(sizeof(char));
                begin = "0";
                add_transition_AFN(afn, begin, label, end);
            }
        }
    }
    //print_transitions_AFN(afn , print_info);
    for (i = 0; i < state_list->length; i++)
    {
        linear_element *val = get_element_list(state_list, i);
        if (val != NULL)
        {
            free(val);
        }
    }

    for (i = 0; i < garbage->length; i++)
    {
        free_list(get_element_list(garbage, i));
    }

    free_list(garbage);
    return afn;
}

list first(tree T, list garbage)
{
    if (T == NULL)
    {
        list tmp = new_list();
        head_insertion(garbage, tmp);
        return tmp;
    }
    else if (T->left_child == NULL && T->right_child == NULL)
    {
        linear_element *elem = T->info;
        if (strcmp("ep", elem->value) == 0)
        {
            list tmp = new_list();
            head_insertion(garbage, tmp);
            return tmp;
        }
        else
        {
            list li = new_list();
            head_insertion(li, elem);
            head_insertion(garbage, li);
            return li;
        }
    }
    else if (strcmp("+", T->info) == 0)
    {
        list tmp = special_union(first(T->left_child, garbage), first(T->right_child, garbage));
        head_insertion(garbage, tmp);
        return tmp;
    }
    else if (strcmp(".", T->info) == 0)
    {
        list tmp = special_union(first(T->left_child, garbage), product(null(T->left_child), first(T->right_child, garbage), garbage));
        head_insertion(garbage, tmp);
        return tmp;
    }
    else
    {
        return first(T->left_child, garbage);
    }
}

list last(tree T, list garbage)
{
    if (T == NULL)
    {
        list tmp = new_list();
        head_insertion(garbage, tmp);
        return tmp;
    }
    else if (T->left_child == NULL && T->right_child == NULL)
    {
        linear_element *elem = T->info;
        if (strcmp("ep", elem->value) == 0)
        {
            list tmp = new_list();
            head_insertion(garbage, tmp);
            return tmp;
        }
        else
        {
            list li = new_list();
            head_insertion(li, elem);
            head_insertion(garbage, li);
            return li;
        }
    }
    else if (strcmp("+", T->info) == 0)
    {
        list tmp = special_union(last(T->left_child, garbage), last(T->right_child, garbage));
        head_insertion(garbage, tmp);
        return tmp;
    }
    else if (strcmp(".", T->info) == 0)
    {
        list tmp = special_union(last(T->right_child, garbage), product(null(T->right_child), last(T->left_child, garbage), garbage));
        head_insertion(garbage, tmp);
        return tmp;
    }
    else
    {
        return last(T->left_child, garbage);
    }
}

list follow(tree T, int x, list garbage)
{
    if (T == NULL)
    {
        list tmp = new_list();
        head_insertion(garbage, tmp);
        return tmp;
    }
    else if (T->left_child == NULL && T->right_child == NULL)
    {
        list tmp = new_list();
        head_insertion(garbage, tmp);
        return tmp;
    }
    else if (strcmp("+", T->info) == 0)
    {

        if (is_in_set_of_pos(T->left_child, x) == True)
        {
            return follow(T->left_child, x, garbage);
        }
        else
        {
            return follow(T->right_child, x, garbage);
        }
    }
    else if (strcmp(".", T->info) == 0)
    {
        if (is_in_set_of_pos(T->left_child, x) == True)
        {
            if (is_in_set_of_last_pos(T->left_child, x, garbage) == True)
            {
                list tmp = special_union(follow(T->left_child, x, garbage), first(T->right_child, garbage));
                head_insertion(garbage, tmp);
                return tmp;
            }
            else
            {
                return follow(T->left_child, x, garbage);
            }
        }
        else
        {
            return follow(T->right_child, x, garbage);
        }
    }
    else
    {
        if (is_in_set_of_last_pos(T->left_child, x, garbage) == True)
        {
            list tmp = special_union(follow(T->left_child, x, garbage), first(T->left_child, garbage));
            head_insertion(garbage, tmp);
            return tmp;
        }
        else
        {
            return follow(T->left_child, x, garbage);
        }
    }
}

char *null(tree T)
{
    if (T == NULL)
    {
        return "vide";
    }
    else if (T->left_child == NULL && T->right_child == NULL)
    {
        linear_element *elem = T->info;
        if (strcmp("ep", elem->value) == 0)
        {
            return "ep";
        }

        return "vide";
    }
    else if (strcmp("+", T->info) == 0)
    {
        if (strcmp(null(T->left_child), "ep") == 0 || strcmp(null(T->right_child), "ep") == 0)
        {
            return "ep";
        }
        else
        {
            return "vide";
        }
    }
    else if (strcmp(".", T->info) == 0)
    {
        if (strcmp(null(T->left_child), "vide") == 0 || strcmp(null(T->right_child), "vide") == 0)
        {
            return "vide";
        }
        else
        {
            return "ep";
        }
    }
    else
    {

        return "ep";
    }
}

boolean is_in_set_of_pos(tree T, int x)
{
    if (T == NULL)
    {
        return 1;
    }
    else if (T->left_child == NULL && T->right_child == NULL)
    {
        linear_element *elem = T->info;
        if (elem->index == x)
        {
            return True;
        }
        else
        {
            return False;
        }
    }
    else
    {
        return or (is_in_set_of_pos(T->left_child, x), is_in_set_of_pos(T->right_child, x));
    }
}

boolean is_in_set_of_last_pos(tree T, int x, list garbage)
{
    list last_list = last(T, garbage);
    linear_element *tmp;
    int i = 0;
    if (is_empty_list(last_list) == False)
    {
        for (i = 0; i < last_list->length; i++)
        {
            tmp = get_element_list(last_list, i);
            if (tmp->index == x)
            {
                return True;
            }
        }
        return False;
    }
    else
    {
        return False;
    }
}

list product(char *val, list li, list garbage)
{

    if (strcmp("ep", val) == 0)
    {
        return li;
    }
    else
    {
        list tmp_list = new_list();
        head_insertion(garbage, tmp_list);
        return tmp_list;
    }
}

boolean include_special(linear_element *val, list li)
{
    int i = 0;
    for (i = 0; i < li->length; i++)
    {
        linear_element *elem = get_element_list(li, i);
        if (elem != NULL)
        {
            if (strcmp(val->value, elem->value) == 0 && val->index == elem->index)
            {
                return True;
            }
        }
    }

    return False;
}

list special_union(list li1, list li2)
{
    int i = 0;
    list result = new_list();
    for (i = 0; i < li1->length; i++)
    {
        head_insertion(result, get_element_list(li1, i));
    }

    void *temp;
    for (i = 0; i < li2->length; i++)
    {
        temp = get_element_list(li2, i);
        if (include_special(temp, result) == False)
        {
            head_insertion(result, temp);
        }
    }

    return result;
}

tree convert_post_to_glushkov_tree(char **expression, int length)
{

    stack pile = new_stack();
    int i = 0, index = 0, nbr_operande = 0;
    for (i = 0; i < length; i++)
    {
        if (is_operator(expression[i]) == False && strcmp("ep", expression[i]) != 0)
        {
            nbr_operande++;
        }
    }

    node_tree *root = create_node(expression[length - 1]);

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
                node_tree *new_op = create_node(expression[i]);
                new_op->father = old_op;
                if (strcmp(old_op->info, "*") == 0)
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

            linear_element *elem = malloc(sizeof(linear_element));
            if (strcmp("ep", expression[i]) == 0)
            {
                elem->index = -1;
            }
            else
            {
                elem->index = nbr_operande - index;
            }
            index++;
            elem->value = expression[i];

            node_tree *val = create_node(elem);
            node_tree *op = pop(pile);
            val->father = op;
            if (strcmp(op->info, "*") == True)
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
#endif