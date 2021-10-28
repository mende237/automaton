#ifndef HOPCROFT_C
#define HOPCROFT_C
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "../../header/algorithm/hopcroft.h"
#include "../data_structure/linked_list.c"
#include "../data_structure/stack.c"
#include "../algorithm/function.c"

AFD hopcroft_minimisation(AFD afd, boolean equal_value(void *lb1, void *lb2), void print_element_in_list(void *x, boolean last))
{
    typedef struct Breaker Breaker;
    typedef struct etiquette etiquette;
    int i = 0, j = 0;
    void **data = get_state_tab(afd, equal_value);
    int *nbr_state = data[0];
    char **tab_state = data[1];
    list final_state_list = new_list();
    list non_final_state_list = NULL;
    list state_list = new_list();
    list S = NULL;
    //pile des casseurs
    list L = new_list();

    for (i = 0; i < *nbr_state; i++)
    {
        queue_insertion(state_list, tab_state[i]);
        char *test = tab_state[i];
        // printf("%s , ", test);
    }

    for (i = 0; i < afd->nbre_finale_state; i++)
    {
        queue_insertion(final_state_list, afd->finale_state[i]);
    }

    list pi = new_list();
    non_final_state_list = complementaire_set(state_list, final_state_list);
    queue_insertion(pi, final_state_list);
    queue_insertion(pi, non_final_state_list);

    S = smallest_set(final_state_list, non_final_state_list);

    printf("l'ensemble initiale des casseurs est : {");

    for (i = 0; i < afd->nbre_label; i++)
    {
        Breaker *breaker = malloc(sizeof(Breaker));
        breaker->set = S;
        breaker->label = afd->tab_labels[i];
        queue_insertion(L, breaker);
        printf("(");
        print_list(breaker->set, print_element_in_list);
        printf(" , %s) ; ", breaker->label);
    }

    printf("}\n");

    while (is_empty_list(L) == False)
    {
        Breaker *breaker = L->head->value;
        printf("le casseur :(");
        print_list(breaker->set, print_element_in_list);
        printf(" , %s)\n", breaker->label);
        for (i = 0; i < pi->length; i++)
        {
            list B = get_element_list(pi, i);
            printf("le block a casse est :");
            print_list(B, print_element_in_list);
            printf("\n");
            list *data_temp = break_block(B, breaker, afd, equal_value, print_element_in_list);
            list B1 = data_temp[0];
            list B2 = data_temp[1];
            free(data_temp);
            if (is_empty_list(B1) == True || is_empty_list(B2) == True)
            {
                free_list(B1);
                free_list(B2);
            }
            else
            {
                delete_element_list(pi, i);
                i--;
                queue_insertion(pi, B1);
                queue_insertion(pi, B2);

                for (i = 0; i < afd->nbre_label; i++)
                {
                    Breaker *breaker_temp = malloc(sizeof(Breaker));
                    breaker_temp->set = B;
                    breaker_temp->label = afd->tab_labels[i];

                    int index = get_index_element_list(L, breaker_temp, equal_breaker);
                    if (index != -1)
                    {
                        delete_element_list(L, index);
                        Breaker *breaker1 = malloc(sizeof(Breaker));
                        breaker1->set = B1;
                        breaker1->label = afd->tab_labels[i];

                        Breaker *breaker2 = malloc(sizeof(Breaker));
                        breaker2->set = B2;
                        breaker2->label = afd->tab_labels[i];

                        queue_insertion(L, breaker1);
                        queue_insertion(L, breaker2);
                    }
                    else
                    {
                        list S0 = smallest_set(B1, B2);
                        Breaker *breaker0 = malloc(sizeof(Breaker));
                        breaker0->set = S0;
                        breaker0->label = afd->tab_labels[i];
                        queue_insertion(L, breaker0);
                    }
                    free(breaker_temp);
                }
            }
        }
        delete_element_list(L, 0);
        free(breaker);
    }

    printf("\nles classe d'equivalences sont \n");
    printf("{ ");
    for (i = 0; i < pi->length; i++)
    {
        print_list(get_element_list(pi, i), print_element_in_list);
        printf(" ; ");
    }
    printf("}\n");

    list initial_state_list;
    for (i = 0; i < pi->length; i++)
    {
        list temp_list = get_element_list(pi, i);
        if (search_value_in_list(temp_list, afd->initiale_state, equal_value) == True)
        {
            initial_state_list = temp_list;
            break;
        }
    }

    int k = 0;

    list *tab_pi = calloc(pi->length, sizeof(void *));
    for (i = 0; i < pi->length; i++)
    {
        tab_pi[i] = get_element_list(pi, i);
    }

    int cmpt_state = 1;
    stack pile = new_stack();
    void **states = calloc(pi->length, sizeof(void *));
    void ***mat_state = calloc(pi->length, sizeof(void **));

    states[0] = copy_element_list(initial_state_list);

    push(pile, states[0]);
    boolean is_well = False;
    boolean first_loop = True;

    while (is_empty_stack(pile) == False)
    {
        list state = pop(pile);
        if (search_state_list(states, state, cmpt_state, 1) == False || first_loop == True)
        {
            void **trans_temp = delta_global_automate(afd, state, True, equal_string);
            void **trans_result = calloc(afd->nbre_label, sizeof(void *));
            for (j = 0; j < afd->nbre_label; j++)
            {
                for (k = 0; k < pi->length; k++)
                {
                    if (include_value_list(tab_pi[k], trans_temp[j], equal_value) == True)
                    {
                        //cette liste ne sert plus a rien elle sera remplace par sa correspondante dans
                        //l'ensemble des classes d'equivalences
                        free_list(trans_temp[j]);
                        trans_result[j] = copy_element_list(tab_pi[k]);
                        push(pile, trans_result[j]);
                        break;
                    }
                }
            }

            if (first_loop == True)
            {
                cmpt_state--;
            }

            states[cmpt_state] = state;
            mat_state[cmpt_state] = trans_result;

            cmpt_state++;
            free(trans_temp);
        }

        first_loop = False;
    }

    free_stack(pile);

    int cmpt_final_state = 0;
    list final_state_result = new_list();

    for (i = 0; i < afd->nbre_finale_state; i++)
    {
        for (j = 0; j < pi->length; j++)
        {
            if (search_value_in_list(states[j], afd->finale_state[i], equal_value) == True)
            {
                queue_insertion(final_state_result, copy_element_list(states[j]));
                break;
            }
        }
    }

    AFD afd_result = new_AFD(pi->length, final_state_result->length, afd->nbre_label);
    for (i = 0; i < final_state_result->length; i++)
    {
        afd_result->finale_state[i] = get_element_list(final_state_result, i);
    }

    afd_result->initiale_state = states[0];
    afd_result->state_tab = states;
    afd_result->mat_state = mat_state;

    int cmpt = 0;
    for (i = 0; i < pi->length; i++)
    {
        for (j = 0; j < afd->nbre_label; j++)
        {
            add_transition_AFD(afd_result, states[i], afd->tab_labels[j], mat_state[i][j], cmpt);
            cmpt++;
        }
    }

    // for (i = 0; i < pi->length; i++)
    // {
    //     print_list(states[i], print_element_in_list);
    //     void **aux = mat_state[i];
    //     printf("\t");
    //     for (j = 0; j < afd->nbre_label; j++)
    //     {
    //         print_list(aux[j] , print_element_in_list);
    //         printf("\t");
    //     }
    //     printf("\n");
    // }

    free_list(final_state_result);
    free_list(final_state_list);
    free_list(non_final_state_list);
    free_list(state_list);
    free_list(pi);
    free(tab_pi);
    free_list(L);
    free(tab_state);
    free(nbr_state);
    free(data);

    return afd_result;
}

static list *break_block(list B, struct Breaker *breaker, AFD afd, boolean equal_value(void *lb1, void *lb2), void print_element_in_list(void *x, boolean last))
{
    list *data = calloc(2, sizeof(list));
    list temp1 = left_quotient(breaker, afd, equal_value);
    //free dans la fonction hopcroft_minimisation
    /*******************************************************/
    list B1 = intersection_set(B, temp1);
    list B2 = complementaire_set(B, B1);
    /******************************************************/

    free_list(temp1);
    data[0] = B1;
    data[1] = B2;
    return data;
}

static list left_quotient(struct Breaker *breaker, AFD afd, boolean equal_value(void *lb1, void *lb2))
{
    typedef struct etiquette etiquette;
    int i = 0, j = 0;
    list state_list = new_list();

    for (j = 0; j < afd->nbre_state * afd->nbre_label; j++)
    {
        void **trans_temp = afd->mat_trans[j];
        if (trans_temp != NULL)
        {
            etiquette *et_temp = trans_temp[1];
            if (strcmp(et_temp->value, breaker->label) == 0 && search_value_in_list(breaker->set, trans_temp[2], equal_value) == True)
            {
                queue_insertion(state_list, trans_temp[0]);
            }
        }
    }

    return state_list;
}

static void **get_state_tab(AFD afd, boolean equal_value(void *lb1, void *lb2))
{
    list state_list = new_list();
    void **data = calloc(2, sizeof(void *));
    int i = 0;
    for (i = 0; i < afd->nbre_label * afd->nbre_state; i++)
    {
        void **trans_temp = afd->mat_trans[i];
        if (trans_temp != NULL)
        {
            if (search_value_in_list(state_list, trans_temp[0], equal_value) == False)
            {
                queue_insertion(state_list, trans_temp[0]);
            }

            if (search_value_in_list(state_list, trans_temp[2], equal_value) == False)
            {
                queue_insertion(state_list, trans_temp[2]);
            }
        }
    }
    //free dans la fonction hopcroft_minimisation
    /****************************************************************************/
    char **tab_state = calloc(state_list->length, sizeof(char *));
    /****************************************************************************/
    for (i = 0; i < state_list->length; i++)
    {
        tab_state[i] = get_element_list(state_list, i);
    }

    free_list(state_list);
    //free dans la fonction hopcroft_minimisation
    /**********************************************/
    int *nbr_state = malloc(sizeof(int));
    /********************************************/
    *nbr_state = state_list->length;
    data[0] = nbr_state;
    data[1] = tab_state;

    return data;
}

static list intersection_set(list state1, list state2)
{
    int i = 0, j = 0;
    list result = new_list();
    for (i = 0; i < state1->length; i++)
    {
        char *temp1 = get_element_list(state1, i);
        for (j = 0; j < state2->length; j++)
        {
            char *temp2 = get_element_list(state2, j);
            if (strcmp(temp1, temp2) == 0)
            {
                queue_insertion(result, temp1);
            }
        }
    }

    return result;
}

static list complementaire_set(list set_A, list set_B)
{
    int i = 0, j = 0;
    boolean find = False;
    list result = new_list();
    for (i = 0; i < set_A->length; i++)
    {
        char *temp1 = get_element_list(set_A, i);
        for (j = 0; j < set_B->length; j++)
        {
            char *temp2 = get_element_list(set_B, j);
            if (strcmp(temp1, temp2) == 0)
            {
                find = True;
                break;
            }
        }

        if (find == False)
        {
            queue_insertion(result, temp1);
        }

        find = False;
    }

    return result;
}

static list smallest_set(list set1, list set2)
{
    if (set1->length < set2->length)
    {
        return set1;
    }

    return set2;
}

static boolean equal_breaker(void *b1, void *b2)
{
    typedef struct Breaker Breacker;

    Breacker *breaker1 = b1;
    Breacker *breaker2 = b2;
    int i = 0, j = 0;
    list set1 = breaker1->set;
    list set2 = breaker2->set;
    boolean equal = True;

    if (strcmp(breaker2->label, breaker2->label) == 0)
    {
        equal = True;
    }
    else
    {
        equal = False;
    }

    if (equal == True)
    {
        for (i = 0; i < set1->length; i++)
        {
            if (search_value_in_list(set2, get_element_list(set1, i), equal_string) == False)
            {
                equal = False;
            }
        }
    }

    if (equal == False)
    {
        return False;
    }

    return True;
}

static boolean equal_string(void *ch1, void *ch2)
{
    char *val1 = ch1;
    char *val2 = ch2;
    if (strcmp(val1, val2) == 0)
    {
        return True;
    }

    return False;
}

#endif