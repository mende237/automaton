#ifndef FUNCTION_C
#define FUNCTION_C

#include "../../header/algorithm/function.h"
#include "../../header/algorithm/AFD.h"
#include <stdlib.h>

boolean search_state(void **state_tab, void *state, int n){
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

static boolean search_state_list(void **state_tab, list state, int n, int permut)
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

void **delta_global_automate(void *automate, list state, boolean is_AFD, boolean equal_label(void *lb1, void *lb2))
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
                        if (search_value_in_list(trans_result[et->index], trans[2], equal_label) == False)
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