#ifndef AFN_C
#define AFN_C
#include "../../header/algorithm/AFN.h"
#include "../../source/data_structure/linked_list.c"
#include "../../source/data_structure/stack.c"
#include <stdlib.h>
#include <stdio.h>

AFN new_AFN(int nbre_state, int nbre_initiale_state, int nbre_finale_state, int nbre_label, void *epsilone)
{

    AFN afn = malloc(sizeof(AFN_value));
    afn->mat_trans = new_list();
    afn->nbre_initiale_state = nbre_initiale_state;
    afn->nbre_finale_state = nbre_finale_state;
    afn->nbre_label = nbre_label;
    afn->tab_labels = malloc(nbre_label * sizeof(void *));
    afn->initiale_state = malloc(nbre_initiale_state * sizeof(void *));
    afn->finale_state = malloc(nbre_finale_state * sizeof(void *));
    afn->epsilone = epsilone;
    afn->nbre_state = nbre_state;
    afn->cmpt_label = 0;
    return afn;
}

void add_transition_AFN(AFN afn, void *begin, void *label, void *end)
{

    typedef struct etiquette etiquette;
    int i = 0;
    boolean find = False;
    int index = 0;
    etiquette *lbl = malloc(sizeof(etiquette));
    if (strcmp(afn->epsilone, label) != 0)
    {

        for (i = 0; i < afn->cmpt_label; i++)
        {
            if (strcmp(afn->tab_labels[i], label) == True)
            {
                index = i;
                find = True;
            }
        }

        lbl->value = label;
        if (find == False)
        {
            afn->tab_labels[afn->cmpt_label] = label;
            afn->cmpt_label++;
            lbl->index = afn->cmpt_label - 1;
        }
        else
        {
            lbl->index = index;
        }
    }
    else
    {
        lbl->value = label;
        lbl->index = -1;
    }

    void **trans = malloc(3 * sizeof(void *));
    trans[0] = begin;
    trans[1] = lbl;
    trans[2] = end;
    // char *b = begin;
    // char *l = lbl->value;
    // char *e = end;
    // printf("%s --------- %s ------ %s\n", b, l, e);
    head_insertion(afn->mat_trans, trans);
}

void print_transitions_AFN(AFN afn, void print_elem(void *begin, void *label, void *end))
{
    typedef struct etiquette etiquette;
    int i = 0;
    void **transition;
    for (i = 0; i < afn->mat_trans->length; i++)
    {
        transition = get_element_list(afn->mat_trans, i);
        etiquette *lbl = transition[1];
        print_elem(transition[0], lbl, transition[2]);
    }
}

AFN miroir_AFN(AFN afn)
{
    int i = 0;
    list li = afn->mat_trans;
    typedef struct etiquette etiquette;
    int nbr_state = afn->nbre_state;
    int nbr_initial_state = afn->nbre_finale_state;
    int nbr_final_state = afn->nbre_initiale_state;
    AFN afn_result = new_AFN(afn->nbre_state, afn->nbre_finale_state, afn->nbre_initiale_state, afn->nbre_label, afn->epsilone);
    for (i = 0; i < afn->mat_trans->length; i++)
    {
        void **trans_temp = get_element_list(li, i);
        if (trans_temp != NULL)
        {
            etiquette *et = trans_temp[1];
            add_transition_AFN(afn_result, trans_temp[2], et->value, trans_temp[0]);
        }
    }

    for (i = 0; i < afn->nbre_finale_state; i++)
    {
        afn_result->initiale_state[i] = afn->finale_state[i];
    }

    for (i = 0; i < afn->nbre_initiale_state; i++)
    {
        afn_result->finale_state[i] = afn->initiale_state[i];
    }

    return afn_result;
}

void free_AFN(AFN afn)
{
    if (afn != NULL)
    {
        typedef struct etiquette etiquette;
        int i = 0;
        void **trans;
        for (i = 0; i < afn->mat_trans->length; i++)
        {
            trans = get_element_list(afn->mat_trans, i);
            free(trans[1]);

            free(get_element_list(afn->mat_trans, i));
        }

        free(afn->mat_trans);
        free(afn->tab_labels);
        free(afn->initiale_state);
        free(afn->finale_state);
        free(afn);
    }
}

list delta_AFN(AFN afn, void *state, void *symbole)
{
    typedef struct etiquette etiquette;
    int i = 0;
    void **transition;
    list result = new_list();

    for (i = 0; i < afn->mat_trans->length; i++)
    {
        transition = get_element_list(afn->mat_trans, i);
        etiquette *lbl = transition[1];
        if (strcmp(transition[0], state) == 0 && strcmp(lbl->value, symbole) == 0)
        {
            queue_insertion(result, transition[2]);
        }
    }

    return result;
}

boolean detect_AFN(const AFN afn, void *word, int size)
{

    typedef struct container
    {
        int index;
        void *state;
    } container;

    stack pile = new_stack();
    void **mot = word;
    int i = 0, cmpt = 0;

    for (i = 0; i < afn->nbre_initiale_state; i++)
    {
        container *c = calloc(1, sizeof(container));
        c->index = 0;
        c->state = afn->initiale_state[i];
        push(pile, c);
    }

    while (is_empty_stack(pile) == False)
    {
        list state_list;
        container *c = pop(pile);
        cmpt = c->index;
        void *state = c->state;

        while (cmpt < size)
        {
            void *symbole = mot[cmpt];
            state_list = delta_AFN(afn, state, symbole);
            if(is_empty_list(state_list) == True){
                break;
            }
            cmpt++;
            for (i = 0; i < state_list->length; i++)
            {
                container *temp = calloc(1, sizeof(container));
                temp->index = cmpt;
                temp->state = get_element_list(state_list, i);
                push(pile, temp);
            }
            container *temp = pop(pile);
            state = temp->state;
            free(temp);
        }

        for (i = 0; i < afn->nbre_finale_state; i++)
        {
            if (strcmp(state, afn->finale_state[i]) == 0)
            {
                return True;
            }
        }

        free(c);
    }
    
    free_stack(pile);
    return False;
}

#endif