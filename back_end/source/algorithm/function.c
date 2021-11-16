#ifndef FUNCTION_C
#define FUNCTION_C

#include "../../header/algorithm/function.h"
#include "../../header/algorithm/AFD.h"
#include <stdlib.h>
#include <stdarg.h>

boolean search_state(void **state_tab, void *state, int n)
{
    int i = 0;
    for (i = 0; i < n; i++)
    {
        if (state_tab[i] != NULL && state != NULL)
        {
            if (strcmp(state_tab[i], state) == 0)
            {
                return True;
            }
        }
        else if (state_tab[i] == state)
        {
            return True;
        }
    }
    return False;
}

boolean equal_state(void  *state1, void *state2, ...)
{
    list st1 = state1;
    list st2 = state2;

    va_list lst;
    va_start(lst, state2);
    int permut = va_arg(lst, int);
    va_end(lst);
    
    boolean rep = False;
    int i = 0, j = 0;
    if (st1->length != st2->length)
    {
        return False;
    }
    else if (is_empty_list(st1) == False && is_empty_list(st2) == False)
    {
        if (permut == 1)
        {
            for (i = 0; i < st1->length; i++)
            {
                rep = False;
                for (j = 0; j < st2->length; j++)
                {
                    if (strcmp(get_element_list(st1, i), get_element_list(st2, j)) == 0)
                    {
                        rep = True;
                    }
                }
                if (rep == False)
                {
                    return False;
                }
            }
            return True;
        }
        else
        {
            for (i = 0; i < st1->length; i++)
            {
                char *ch1 = get_element_list(st1, i);
                char *ch2 = get_element_list(st2, i);
                if (ch1 != NULL && ch2 != NULL)
                {
                    if (strcmp(ch1, ch2) != 0)
                    {
                        return False;
                    }
                }
                else if (ch1 != ch2)
                {
                    return False;
                }
            }
            return True;
        }
    }
    else if (is_empty_list(st1) == True && is_empty_list(st2) == True)
    {
        return True;
    }
    else
    {
        return False;
    }
}

boolean search_state_list(void **state_tab, list state, int n, int permut)
{
    int i = 0;
    for (i = 0; i < n; i++)
    {
        if (equal_state(state_tab[i], state, permut) == True)
        {
            return True;
        }
    }

    return False;
}

boolean search_state_list_in_list(list list_state, list state, int permut)
{
}

void **delta_global_automate(void *automate, list state, boolean is_AFD, boolean equal_label(void *lb1, void *lb2 , ...))
{
    typedef struct etiquette etiquette;
    int i = 0, j = 0;
    void **trans;
    int nbre_label;
    int mat_trans_length;

    AFD afd;
    AFN afn;
    etiquette *et;
    if (is_AFD == True)
    {
        afd = automate;
        nbre_label = afd->nbre_label;
        mat_trans_length = afd->nbre_label * afd->nbre_state;
    }
    else
    {
        afn = automate;
        nbre_label = afn->nbre_label;
        mat_trans_length = afn->mat_trans->length;
    }

    void **trans_result = malloc(nbre_label * sizeof(void *));

    //on alloue l'espace devant contenir chaque nouveau etat ou
    //peut aller suivant chaque symbole de l'alphabet
    for (i = 0; i < nbre_label; i++)
    {
        trans_result[i] = new_list();
    }

    for (i = 0; i < state->length; i++)
    {
        for (j = 0; j < mat_trans_length; j++)
        {
            //on recupere la transition a la j emme ligne
            if (is_AFD == True)
            {
                trans = afd->mat_trans[j];
            }
            else
            {
                trans = get_element_list(afn->mat_trans, j);
            }

            if (trans != NULL)
            {
                if (strcmp(get_element_list(state, i), trans[0]) == 0)
                {
                    et = trans[1];
                    if (et->index != -1)
                    {
                        /*dans met le nouvel etat dans le tableau correspondant a l'étiquette lu 
                    chaque etiquette connais son index dans le nouveau tableau de transition*/
                        if (search_value_in_list(trans_result[et->index], trans[2], equal_label , 1) == False)
                        {
                            head_insertion(trans_result[et->index], trans[2]);
                        }
                    }
                }
            }
        }
    }

    return trans_result;
}

#endif