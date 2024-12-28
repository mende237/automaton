#ifndef AFD_C
#define AFD_C

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <cjson/cJSON.h>

#include "../../header/algorithm/AFD.h"
#include "../../header/data_structure/structure.h"
#include "../../header/data_structure/stack.h"
#include "../../header/data_structure/linked_list.h"
#include "../../header/algorithm/function.h"

AFD new_AFD(int nbre_state, int nbre_finale_state, int nbre_label)
{
    AFD afd = malloc(sizeof(AFD_value));
    afd->cmpt_label = 0;
    afd->nbre_label = nbre_label;
    afd->nbre_state = nbre_state;
    afd->nbre_finale_state = nbre_finale_state;
    afd->mat_state = NULL;
    afd->state_tab = NULL;
    afd->mat_trans = calloc(nbre_label * nbre_state, sizeof(void **));
    afd->finale_state = calloc(nbre_finale_state, sizeof(void *));
    afd->tab_labels = calloc(nbre_label, sizeof(void *));
    return afd;
}

void add_transition_AFD(AFD afd, void *begin, void *label, void *end, int index)
{
    typedef struct etiquette etiquette;
    int i = 0;
    boolean find = False;
    int index1 = 0;

    for (i = 0; i < afd->cmpt_label; i++)
    {
        if (strcmp(afd->tab_labels[i], label) == 0)
        {
            char *test = label;
            index1 = i;
            find = True;
        }
    }

    etiquette *lbl = malloc(sizeof(etiquette));
    lbl->value = label;
    if (find == False)
    {
        afd->tab_labels[afd->cmpt_label] = label;
        afd->cmpt_label++;
        lbl->index = afd->cmpt_label - 1;
    }
    else
    {
        lbl->index = index1;
    }

    void **trans = malloc(3 * sizeof(void *));
    trans[0] = begin;
    trans[1] = lbl;
    trans[2] = end;
    afd->mat_trans[index] = trans;
}

void *delta_AFD(AFD afd, void *state, void *symbole)
{
    typedef struct etiquette etiquette;
    int i = 0;
    void **transition;

    for (i = 0; i < afd->nbre_label * afd->nbre_state; i++)
    {
        transition = afd->mat_trans[i];
        if (transition != NULL)
        {
            etiquette *lbl = transition[1];
            if (strcmp(transition[0], state) == 0 && strcmp(lbl->value, symbole) == 0)
            {
                return transition[2];
            }
        }
    }
    return NULL;
}

/*cette fonction teste si un mot donné est reconnu par un AFD (automate
fini deterministe)*/
list detect_AFD(const AFD afd, void *word, int size)
{
    list path = new_list();
    list tab_path = new_list();

    void **mot = word;
    void *q0 = afd->initiale_state;
    int i = 0;
    void *q = q0;

    for (i = 0; i < size; i++)
    {
        void *symbole = mot[i];
        queue_insertion(path, q);
        queue_insertion(path, symbole);
        q = delta_AFD(afd, q, symbole);

        if (q == NULL)
        {
            queue_insertion(tab_path, path);
            return tab_path;
        }
        // else
        // {
        // }
    }

    if (q != NULL)
    {
        queue_insertion(path, q);
    }

    queue_insertion(tab_path, path);
    return tab_path;
}

/*la fonction equal value permet de verifier l'egaliter entre l'element val et un
element dans la liste st2*/
static boolean include(void *val, list st2)
{
    int i = 0;
    for (i = 0; i < st2->length; i++)
    {
        if (strcmp(val, get_element_list(st2, i)) == 0)
        {
            return True;
        }
    }

    return False;
}

static list union_set(const list li1, const list li2)
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
        if (include(temp, result) == False)
        {
            head_insertion(result, temp);
        }
    }

    return result;
}

static void **delta_global_AFD(AFD afd, void *state)
{
    typedef struct etiquette etiquette;
    int i = 0, j = 0;
    void **trans = NULL;
    etiquette *et = NULL;
    void **trans_result = calloc(afd->nbre_label, sizeof(void *));

    if (state != NULL)
    {
        while (i < afd->nbre_label * afd->nbre_state && afd->mat_trans[i] != NULL)
        {
            trans = afd->mat_trans[i];
            if (strcmp(trans[0], state) == True)
            {
                // char *ch = trans[0];
                // char *ch1 = trans[2];
                et = trans[1];
                // char *ch3 = et->value;

                // printf("%s %s  %s" , ch , ch3 , ch1);
                // printf("\n");
                trans_result[et->index] = trans[2];
            }
            i++;
        }
    }
    else
    {

        for (i = 0; i < afd->nbre_label; i++)
        {
            trans_result[i] = NULL;
        }
    }

    // for (i = 0; i < afd->nbre_label; i++)
    // {
    //     char *ch = trans_result[i];
    //     printf("%s ", ch);
    // }

    // printf("\n");

    return trans_result;
}

static list *union_trans(AFD afd1, AFD afd2, void **trans1, void **trans2, void **tab_label, int nbre_label)
{
    list *trans_result = calloc(nbre_label, sizeof(list));
    list *test = calloc(nbre_label, sizeof(list));
    int i = 0, j = 0, index_max = afd1->nbre_label;
    list temp;
    boolean equal = False;

    for (i = 0; i < afd1->nbre_label; i++)
    {
        temp = new_list();
        queue_insertion(temp, trans1[i]);
        // queue_insertion(temp , NULL);
        trans_result[i] = temp;
    }

    for (i = 0; i < afd2->nbre_label; i++)
    {
        for (j = 0; j < afd1->nbre_label; j++)
        {
            if (strcmp(afd1->tab_labels[j], afd2->tab_labels[i]) != 0)
            {

                equal = False;
            }
            else
            {
                equal = True;
                queue_insertion(trans_result[j], trans2[i]);
                break;
            }
        }

        if (equal == False)
        {
            char *well = NULL;
            temp = new_list();
            queue_insertion(temp, well);
            queue_insertion(temp, trans2[i]);
            trans_result[index_max] = temp;
            index_max++;
            // char *ch = afd2->tab_labels[i];
            // printf("%s " , ch);
            // print_list(temp , print_value);
            // printf("\n");
        }
    }

    for (i = 0; i < afd1->nbre_label; i++)
    {

        if (trans_result[i]->length < 2)
        {
            char *well = NULL;
            queue_insertion(trans_result[i], well);
        }
    }

    free(trans1);
    free(trans2);
    return trans_result;
}

static void **product_AFD(AFD afd1, AFD afd2)
{
    void **result = calloc(6, sizeof(void *));
    int i = 0, j = 0, cmpt_state = 1;
    boolean well_state = False;
    list label_list = new_list();

    list state_list = new_list();
    list mat_state_list = new_list();
    stack pile = new_stack();

    for (i = 0; i < afd1->nbre_label; i++)
    {
        queue_insertion(label_list, afd1->tab_labels[i]);
    }

    for (i = 0; i < afd2->nbre_label; i++)
    {
        char *lbl = afd2->tab_labels[i];
        while (j < afd1->nbre_label && strcmp(afd1->tab_labels[j], afd2->tab_labels[i]) != 0)
        {
            j++;
        }

        if (j >= afd1->nbre_label)
        {
            queue_insertion(label_list, afd2->tab_labels[i]);
        }
        j = 0;
    }

    void **tab_label = calloc(label_list->length, sizeof(void *));

    for (i = 0; i < label_list->length; i++)
    {
        tab_label[i] = get_element_list(label_list, i);
    }

    list initial_state_list = new_list();
    queue_insertion(initial_state_list, afd1->initiale_state);
    queue_insertion(initial_state_list, afd2->initiale_state);

    // state_tab[0] = initial_state_list;
    queue_insertion(state_list, initial_state_list);

    list *trans = union_trans(afd1, afd2, delta_global_AFD(afd1, afd1->initiale_state), delta_global_AFD(afd2, afd2->initiale_state), tab_label, label_list->length);
    queue_insertion(mat_state_list, trans);
    // mat_state[0] = trans;
    for (i = 0; i < label_list->length; i++)
    {
        if (get_element_list(trans[i], 0) != NULL || get_element_list(trans[i], 1) != NULL)
        {
            push(pile, trans[i]);
        }
        else
        {
            well_state = True;
        }
    }

    while (is_empty_stack(pile) == False)
    {
        list state = pop(pile);
        if (search_value_in_list(state_list, state, equal_state, 0) == False)
        {
            queue_insertion(state_list, state);
            // state_tab[cmpt_state] = state;
            trans = union_trans(afd1, afd2, delta_global_AFD(afd1, get_element_list(state, 0)), delta_global_AFD(afd2, get_element_list(state, 1)), tab_label, label_list->length);
            queue_insertion(mat_state_list, trans);
            // mat_state[cmpt_state] = trans;

            for (i = 0; i < label_list->length; i++)
            {
                if (get_element_list(trans[i], 0) != NULL || get_element_list(trans[i], 1) != NULL)
                {
                    push(pile, trans[i]);
                }
                else
                {
                    well_state = True;
                }
            }

            cmpt_state++;
        }
    }

    free_stack(pile);

    /*************************************************/
    int *nbr_state = calloc(1, sizeof(int));
    int *nbr_label = calloc(1, sizeof(int));
    /************************************************/
    *nbr_label = label_list->length;
    if (well_state == True)
    {
        *nbr_state = state_list->length + 1;
        list temp1_list = new_list();
        queue_insertion(temp1_list, NULL);
        queue_insertion(temp1_list, NULL);
        queue_insertion(state_list, temp1_list);

        list *temp_trans = calloc(*nbr_label, sizeof(list));
        for (i = 0; i < *nbr_label; i++)
        {
            list temp2_list = new_list();
            queue_insertion(temp2_list, NULL);
            queue_insertion(temp2_list, NULL);
            temp_trans[i] = temp2_list;
        }
        queue_insertion(mat_state_list, temp_trans);
    }
    else
    {
        *nbr_state = state_list->length;
    }

    /*****************************************************************************************/
    void **state_tab = malloc(*nbr_state * sizeof(void *));
    void **mat_state = malloc(*nbr_state * sizeof(void *));
    /*****************************************************************************************/

    for (i = 0; i < *nbr_state; i++)
    {
        state_tab[i] = get_element_list(state_list, i);
        mat_state[i] = get_element_list(mat_state_list, i);
    }

    result[0] = state_tab;
    result[1] = nbr_state;
    result[2] = mat_state;
    result[3] = nbr_label;
    result[4] = initial_state_list;
    result[5] = tab_label;

    free_list(state_list);
    free_list(mat_state_list);
    free_list(label_list);
    return result;
}

AFD union_AFD(AFD afd1, AFD afd2)
{
    void **trans = NULL;
    int cmpt = 0;
    list final_states = new_list();
    int i = 0, j = 0;
    void **data = product_AFD(afd1, afd2);
    void **state_tab = data[0];
    int *nbr_state = data[1];
    void ***mat_state = data[2];
    int *nbr_label = data[3];
    list initiale_state = data[4];
    void **tab_labels = data[5];

    // determination des etats finaux
    for (i = 0; i < *nbr_state; i++)
    {
        list state = state_tab[i];
        void *substate1 = get_element_list(state, 0);
        void *substate2 = get_element_list(state, 1);
        char *ch1 = substate1;
        char *ch2 = substate2;

        if (search_state(afd1->finale_state, substate1, afd1->nbre_finale_state) == True)
        {
            queue_insertion(final_states, state);
        }
        else if (search_state(afd2->finale_state, substate2, afd2->nbre_finale_state) == True)
        {
            queue_insertion(final_states, state);
        }
    }

    void **tab_final_state = calloc(final_states->length, sizeof(void *));

    for (i = 0; i < final_states->length; i++)
    {
        tab_final_state[i] = get_element_list(final_states, i);
    }

    AFD afd_result = new_AFD(*nbr_state, final_states->length, *nbr_label);
    afd_result->finale_state = tab_final_state;
    afd_result->state_tab = state_tab;
    afd_result->mat_state = mat_state;
    afd_result->initiale_state = initiale_state;
    for (i = 0; i < *nbr_state; i++)
    {

        trans = mat_state[i];
        for (j = 0; j < *nbr_label; j++)
        {
            add_transition_AFD(afd_result, state_tab[i], tab_labels[j], trans[j], cmpt);
            cmpt++;
        }
    }

    free_list(final_states);
    free(nbr_state);
    free(nbr_label);
    free(data);

    return afd_result;
}

AFD intersection_AFD(AFD afd1, AFD afd2)
{

    void **trans = NULL;
    int cmpt = 0;
    list final_states = new_list();
    int i = 0, j = 0;
    void **data = product_AFD(afd1, afd2);
    void **state_tab = data[0];
    int *nbr_state = data[1];
    void ***mat_state = data[2];
    int *nbr_label = data[3];
    list initiale_state = data[4];
    void **tab_labels = data[5];

    // determination des etats finaux
    for (i = 0; i < *nbr_state; i++)
    {
        list state = state_tab[i];
        void *substate1 = get_element_list(state, 0);
        void *substate2 = get_element_list(state, 1);
        char *ch1 = substate1;
        char *ch2 = substate2;

        if (search_state(afd1->finale_state, substate1, afd1->nbre_finale_state) == True && search_state(afd2->finale_state, substate2, afd2->nbre_finale_state) == True)
        {
            queue_insertion(final_states, state);
        }
    }

    void **tab_final_state = calloc(final_states->length, sizeof(void *));

    for (i = 0; i < final_states->length; i++)
    {
        tab_final_state[i] = get_element_list(final_states, i);
    }

    AFD afd_result = new_AFD(*nbr_state, final_states->length, *nbr_label);
    afd_result->finale_state = tab_final_state;
    afd_result->state_tab = state_tab;
    afd_result->mat_state = mat_state;
    afd_result->initiale_state = initiale_state;

    for (i = 0; i < *nbr_state; i++)
    {

        trans = mat_state[i];
        for (j = 0; j < *nbr_label; j++)
        {
            add_transition_AFD(afd_result, state_tab[i], tab_labels[j], trans[j], cmpt);
            cmpt++;
        }
    }

    free_list(final_states);
    free(nbr_state);
    free(nbr_label);
    free(data);

    return afd_result;
}

void completer_AFD(AFD afd, char *well_state, boolean equal_value(void *lb1, void *lb2, ...))
{
    typedef struct etiquette etiquette;
    int i = 0, j = 0, index = 0, cmpt = 0;
    int cmpt_state = 0;
    void *state = NULL;
    list state_list = new_list();
    list mat = new_list();

    void **trans = NULL;
    void **trans_temp = NULL;
    etiquette *lbl = NULL;

    for (i = 0; i < afd->nbre_state * afd->nbre_label; i++)
    {
        trans = afd->mat_trans[i];
        if (trans != NULL)
        {
            lbl = trans[1];
            state = trans[0];
            // char *str = state;
            // printf("etat :%s\n", str);
            index = get_index_element_list(state_list, state, equal_value);
            if (index == -1)
            {
                trans_temp = calloc(afd->nbre_label, sizeof(void *));
                trans_temp[lbl->index] = trans[2];
                queue_insertion(mat, trans_temp);
                queue_insertion(state_list, state);
            }
            else
            {
                trans_temp = get_element_list(mat, index);
                trans_temp[lbl->index] = trans[2];
            }
        }
        else
        {
            cmpt = i;
            break;
        }
    }

    printf("*****************************************\n");
    for (i = 0; i < afd->nbre_state * afd->nbre_label; i++)
    {
        trans = afd->mat_trans[i];
        if (trans != NULL)
        {

            state = trans[2];
            // char *str = state;
            // printf("etat :%s\n" ,str);
            index = get_index_element_list(state_list, state, equal_value);
            if (index == -1)
            {
                trans_temp = calloc(afd->nbre_label, sizeof(void *));
                queue_insertion(mat, trans_temp);
                queue_insertion(state_list, state);
            }
        }
    }

    boolean is_well = False;
    for (i = 0; i < mat->length; i++)
    {
        trans = get_element_list(mat, i);
        for (j = 0; j < afd->nbre_label; j++)
        {
            if (trans[j] == NULL)
            {
                is_well = True;
            }
        }
    }

    int nbre_state = state_list->length;
    if (is_well == True)
    {
        nbre_state++;
    }

    afd->nbre_state = nbre_state;

    void ***mat_state = calloc(nbre_state, sizeof(void **));
    void **state_tab = calloc(nbre_state, sizeof(void *));

    for (i = 0; i < mat->length; i++)
    {
        trans = get_element_list(mat, i);
        state_tab[i] = get_element_list(state_list, i);
        if (is_well == True)
        {
            for (j = 0; j < afd->nbre_label; j++)
            {
                if (trans[j] == NULL)
                {
                    trans[j] = well_state;
                }
            }
        }
        mat_state[i] = trans;
    }

    if (is_well == True)
    {
        afd->mat_trans = realloc(afd->mat_trans, (afd->cmpt_label * afd->nbre_state) * sizeof(void **));

        state_tab[state_list->length] = well_state;
        /************************************************************/
        trans_temp = calloc(afd->nbre_label, sizeof(void *));
        /***********************************************************/
        char *test;
        for (i = 0; i < afd->nbre_label; i++)
        {
            trans_temp[i] = well_state;
            test = well_state;
        }

        mat_state[mat->length] = trans_temp;

        for (i = 0; i < nbre_state; i++)
        {
            trans = mat_state[i];
            for (j = 0; j < afd->nbre_label; j++)
            {
                if (strcmp(trans[j], well_state) == 0)
                {
                    add_transition_AFD(afd, state_tab[i], afd->tab_labels[j], trans[j], cmpt);
                    cmpt++;
                }
            }
        }
    }

    afd->mat_state = mat_state;
    afd->state_tab = state_tab;

    free_list(state_list);
    free_list(mat);
}

AFD complementaire_AFD(AFD afd, char *well_state, boolean equal_value(void *lb1, void *lb2, ...))
{
    typedef struct etiquette etiquette;

    int i = 0, j = 0;
    // avant de faire le complementaire on complete d'abord l'automate
    completer_AFD(afd, well_state, equal_value);

    list final_state_list = new_list();

    void ***mat_state = calloc(afd->nbre_state, sizeof(void **));
    void **state_tab = calloc(afd->nbre_state, sizeof(void *));
    void **trans = NULL;
    void **trans_temp = NULL;
    void *temp = NULL;

    for (i = 0; i < afd->nbre_state; i++)
    {
        temp = afd->state_tab[i];
        char *str = temp;
        if (search_state(afd->finale_state, temp, afd->nbre_finale_state) == False)
        {
            queue_insertion(final_state_list, temp);
        }

        state_tab[i] = temp;

        trans = calloc(afd->nbre_state, sizeof(void *));
        trans_temp = afd->mat_state[i];

        for (j = 0; j < afd->nbre_label; j++)
        {
            trans[j] = trans_temp[j];
        }
        mat_state[i] = trans;
    }

    AFD afd_com = new_AFD(afd->nbre_state, final_state_list->length, afd->nbre_label);
    afd_com->initiale_state = afd->initiale_state;

    for (i = 0; i < afd->nbre_label; i++)
    {
        afd_com->tab_labels[i] = afd->tab_labels[i];
    }

    for (i = 0; i < afd->nbre_state * afd->nbre_label; i++)
    {
        afd_com->mat_trans[i] = calloc(3, sizeof(void *));
    }

    for (i = 0; i < afd->nbre_state * afd->nbre_label; i++)
    {
        for (j = 0; j < 3; j++)
        {
            if (j == 1)
            {
                etiquette *et = calloc(1, sizeof(etiquette));
                etiquette *tmp = afd->mat_trans[i][j];
                et->index = tmp->index;
                et->value = tmp->value;
                afd_com->mat_trans[i][j] = et;
            }
            else
            {
                afd_com->mat_trans[i][j] = afd->mat_trans[i][j];
            }
        }
    }

    for (i = 0; i < final_state_list->length; i++)
    {
        char *str = get_element_list(final_state_list, i);

        afd_com->finale_state[i] = str;
    }

    afd_com->initiale_state = afd->initiale_state;
    afd_com->mat_state = mat_state;
    afd_com->state_tab = state_tab;
    // afd_com->nbre_finale_state = final_state_list->length;

    // char *st1;
    // char *begin;
    // for (i = 0; i < afd_com->nbre_state; i++)
    // {
    //     begin = afd_com->state_tab[i];
    //     printf("%s--", begin);
    //     trans = afd_com->mat_state[i];
    //     for (j = 0; j < afd_com->nbre_label; j++)
    //     {
    //         st1 = trans[j];
    //         printf("%s  ", st1);
    //     }
    //     printf("\n");
    // }

    return afd_com;
}

AFN miroir_AFD(AFD afd)
{
    int i = 0;
    typedef struct etiquette etiquette;
    AFN afn = new_AFN(afd->nbre_state, afd->nbre_finale_state, 1, afd->nbre_label, "ep");
    for (i = 0; i < afd->nbre_state * afd->nbre_label; i++)
    {

        void **trans_temp = afd->mat_trans[i];
        if (trans_temp != NULL)
        {
            etiquette *et = trans_temp[1];
            add_transition_AFN(afn, trans_temp[2], et->value, trans_temp[0]);
        }
    }

    afn->finale_state[0] = afd->initiale_state;
    for (i = 0; i < afd->nbre_finale_state; i++)
    {
        afn->initiale_state[i] = afd->finale_state[i];
    }

    return afn;
}

AFD rename_states(AFD afd, boolean permut)
{
    printf("number state %d\n", afd->nbre_state);
    typedef struct etiquette etiquette;
    int cmpt = 0, nbr_line = 0;
    int i = 0, j = 0, k = 0, q = 0;
    list state_list;
    list *trans;
    void **trans_temp;
    void **final_states = calloc(afd->nbre_finale_state, sizeof(void *));
    void **state_tab = calloc(afd->nbre_state, sizeof(void *));
    void **finale_state = calloc(afd->nbre_finale_state, sizeof(void *));
    void ***mat_state = calloc(afd->nbre_state, sizeof(void **));
    void ***mat_trans = calloc(afd->nbre_label * afd->nbre_state, sizeof(void **));

    int p = 0;
    if (permut == False)
    {
        p = 0;
    }
    else
    {
        p = 1;
    }

    for (q = 0; q < afd->nbre_label * afd->nbre_state; q++)
    {
        trans_temp = afd->mat_trans[q];
        if (trans_temp != NULL)
        {
            nbr_line++;
        }
    }

    for (i = 0; i < nbr_line; i++)
    {
        mat_trans[i] = calloc(3, sizeof(void *));
    }

    for (i = 0; i < afd->nbre_state; i++)
    {
        mat_state[i] = calloc(afd->nbre_label, sizeof(void *));
    }

    // //il s'agit ici d'un etat puit
    // /**********************************************/
    // char *st = calloc(15, sizeof(char));
    // /**********************************************/
    // sprintf(st, "%d", afd->nbre_state);
    // state_tab[afd->nbre_state] = st;

    for (i = 0; i < afd->nbre_state; i++)
    {
        /**********************************************/
        char *state = calloc(15, sizeof(char));
        /**********************************************/
        sprintf(state, "%d", cmpt);
        state_list = afd->state_tab[i];
        state_tab[i] = state;

        for (q = 0; q < afd->nbre_finale_state; q++)
        {
            if (equal_state(state_list, afd->finale_state[q], p) == True)
            {
                finale_state[q] = state;
                break;
            }
        }

        for (q = 0; q < afd->nbre_label * afd->nbre_state; q++)
        {
            trans_temp = afd->mat_trans[q];
            if (trans_temp != NULL)
            {
                if (equal_state(state_list, trans_temp[0], p) == True)
                {
                    mat_trans[q][0] = state;
                }

                if (equal_state(state_list, trans_temp[2], p) == True)
                {
                    mat_trans[q][2] = state;
                }
            }
        }

        cmpt++;
        void *help = NULL;
        for (j = 0; j < afd->nbre_state; j++)
        {
            help = afd->mat_state[j];
            trans = help;
            for (k = 0; k < afd->nbre_label; k++)
            {
                if (equal_state(state_list, trans[k], p) == True)
                {
                    mat_state[j][k] = state;
                }
            }
        }
    }

    char *ch = NULL;
    etiquette *lbl_tmp = NULL;
    cmpt = 0;
    for (q = 0; q < nbr_line; q++)
    {
        trans_temp = afd->mat_trans[q];
        if (trans_temp != NULL)
        {
            lbl_tmp = afd->mat_trans[q][1];

            etiquette *lbl = calloc(1, sizeof(etiquette));
            lbl->index = lbl_tmp->index;
            lbl->value = lbl_tmp->value;
            mat_trans[q][1] = lbl;
            // ch = et->value;
            // printf("label %s\n" , ch);
            cmpt++;
        }
        // else
        // {
        //     printf("enter\n");
        // }
    }

    AFD afd_result = new_AFD(afd->nbre_state, afd->nbre_finale_state, afd->nbre_label);
    char *begin_state = calloc(1, sizeof(char));
    for (i = 0; i < afd->nbre_label; i++)
    {
        afd_result->tab_labels[i] = afd->tab_labels[i];
    }

    afd_result->initiale_state = state_tab[0];
    afd_result->mat_state = mat_state;
    afd_result->state_tab = state_tab;
    free(afd_result->mat_trans);
    afd_result->mat_trans = mat_trans;
    free(afd_result->finale_state);
    afd_result->finale_state = finale_state;
    list test = afd->state_tab[afd->nbre_state - 1];
    free_AFD(afd, True);
    // char *st1;
    // char *begin;
    // for (i = 0; i < afd->nbre_state; i++)
    // {
    //     begin = state_tab[i];
    //     printf("%s--\t", begin);
    //     trans_temp = mat_state[i];
    //     for (j = 0; j < afd->nbre_label; j++)
    //     {
    //         st1 = trans_temp[j];
    //         printf("%s\t", st1);
    //     }
    //     printf("\n");
    // }
    return afd_result;
}

AFD determinisation(AFN afn, boolean equal_value(void *st1, void *st2, ...))
{
    typedef struct etiquette etiquette;
    boolean is_well = False;
    int i = 0, cmpt_state = 1;

    list state_tab_list = new_list();
    list mat_state_list = new_list();

    list initial_state_list = new_list();
    for (i = 0; i < afn->nbre_initiale_state; i++)
    {
        head_insertion(initial_state_list, afn->initiale_state[i]);
    }

    stack pile = new_stack();
    void **trans = delta_global_automate(afn, initial_state_list, False, equal_value);

    for (i = 0; i < afn->nbre_label; i++)
    {
        if (is_empty_list(trans[i]) == False)
        {
            push(pile, trans[i]);
        }
        else
        {
            is_well = True;
        }
    }

    queue_insertion(state_tab_list, initial_state_list);
    queue_insertion(mat_state_list, trans);
    while (is_empty_stack(pile) == False)
    {

        list state = pop(pile);
        if (search_value_in_list(state_tab_list, state, equal_state, 1) == False)
        {
            queue_insertion(state_tab_list, state);
            trans = delta_global_automate(afn, state, False, equal_value);
            queue_insertion(mat_state_list, trans);
            for (i = 0; i < afn->nbre_label; i++)
            {
                if (is_empty_list(trans[i]) == False)
                {
                    push(pile, trans[i]);
                }
                else
                {
                    is_well = True;
                }
            }
            cmpt_state++;
        }
    }

    free_stack(pile);

    void **state_tab = malloc(state_tab_list->length * sizeof(void *));
    void ***mat_state = malloc(mat_state_list->length * sizeof(void **));

    for (i = 0; i < state_tab_list->length; i++)
    {
        state_tab[i] = get_element_list(state_tab_list, i);
        mat_state[i] = get_element_list(mat_state_list, i);
    }

    int j = 0, nbre_finale_state = 0, k = 0;
    int cmpt = 0, cmpt_finale_state = 0;
    list list_finale_state = new_list();
    boolean in = False;

    for (i = 0; i < afn->nbre_finale_state; i++)
    {
        for (j = 0; j < state_tab_list->length; j++)
        {
            list temp_list = get_element_list(state_tab_list, j);
            if (search_value_in_list(temp_list, afn->finale_state[i], equal_value, 1) == True)
            {
                head_insertion(list_finale_state, temp_list);
                delete_element_list(state_tab_list, j);
                cmpt_finale_state++;
                j--;
            }
        }
    }

    AFD afd;
    if (is_well == True)
    {
        afd = new_AFD(cmpt_state + 1, cmpt_finale_state, afn->nbre_label);
        /******************************************/
        list well_state = new_list();
        /*****************************************/
        state_tab = realloc(state_tab, (cmpt_state + 1) * sizeof(void *));
        mat_state = realloc(mat_state, (cmpt_state + 1) * sizeof(void **));

        state_tab[cmpt_state] = well_state;
        trans = calloc(afd->nbre_label, sizeof(void *));
        for (i = 0; i < afd->nbre_label; i++)
        {
            trans[i] = new_list();
        }
        mat_state[cmpt_state] = trans;
    }
    else
    {
        afd = new_AFD(cmpt_state, cmpt_finale_state, afn->nbre_label);
    }

    afd->mat_state = mat_state;
    afd->state_tab = state_tab;

    for (i = 0; i < cmpt_finale_state; i++)
    {
        afd->finale_state[i] = get_element_list(list_finale_state, i);
    }

    afd->initiale_state = initial_state_list;

    for (i = 0; i < afd->nbre_state; i++)
    {
        trans = mat_state[i];
        for (j = 0; j < afn->nbre_label; j++)
        {
            add_transition_AFD(afd, state_tab[i], afn->tab_labels[j], trans[j], cmpt);
            cmpt++;
        }
    }

    free_list(state_tab_list);
    free_list(list_finale_state);
    free_list(mat_state_list);
    return afd;
}

AFD epsilone_determinisation(AFN afn, boolean equal_value(void *lb1, void *lb2, ...))
{
    typedef struct etiquette etiquette;

    int i = 0, cmpt_state = 1;
    list initiale = new_list();
    boolean is_well = False;
    for (i = 0; i < afn->nbre_initiale_state; i++)
    {
        head_insertion(initiale, afn->initiale_state[i]);
    }

    list state_tab_list = new_list();
    list mat_state_list = new_list();

    list garbage = new_list();

    stack pile = new_stack();

    list e_clo = epsilone_closure_set(afn, initiale);
    void **trans = delta_global_automate(afn, e_clo, False, equal_value);

    list old_list;
    for (i = 0; i < afn->nbre_label; i++)
    {
        old_list = trans[i];
        trans[i] = epsilone_closure_set(afn, trans[i]);
        //**************************************
        head_insertion(garbage, old_list);
        //**************************************
    }

    for (i = 0; i < afn->nbre_label; i++)
    {
        if (is_empty_list(trans[i]) == False)
        {
            push(pile, trans[i]);
        }
        else
        {
            is_well = True;
        }
    }

    queue_insertion(state_tab_list, e_clo);
    queue_insertion(mat_state_list, trans);

    while (is_empty_stack(pile) == False)
    {

        list state = pop(pile);
        if (search_value_in_list(state_tab_list, state, equal_state, 1) == False)
        {
            queue_insertion(state_tab_list, state);
            trans = delta_global_automate(afn, state, False, equal_value);

            for (i = 0; i < afn->nbre_label; i++)
            {
                old_list = trans[i];
                trans[i] = epsilone_closure_set(afn, trans[i]);
                //***************************************************
                head_insertion(garbage, old_list);
                //***************************************************
            }
            queue_insertion(mat_state_list, trans);
            for (i = 0; i < afn->nbre_label; i++)
            {
                if (is_empty_list(trans[i]) == False)
                {
                    push(pile, trans[i]);
                }
                else
                {
                    is_well = True;
                }
            }
            cmpt_state++;
        }
    }

    free_stack(pile);

    void **state_tab = malloc(state_tab_list->length * sizeof(void *));
    void ***mat_state = malloc(mat_state_list->length * sizeof(void **));

    for (i = 0; i < state_tab_list->length; i++)
    {
        state_tab[i] = get_element_list(state_tab_list, i);
        mat_state[i] = get_element_list(mat_state_list, i);
    }

    int j = 0, nbre_finale_state = 0, k = 0;
    int cmpt = 0, cmpt_finale_state = 0;
    list list_finale_state = new_list();
    boolean in = False;

    for (i = 0; i < afn->nbre_finale_state; i++)
    {
        for (j = 0; j < state_tab_list->length; j++)
        {
            list temp_list = get_element_list(state_tab_list, j);
            if (search_value_in_list(temp_list, afn->finale_state[i], equal_value, 1) == True)
            {
                head_insertion(list_finale_state, temp_list);
                delete_element_list(state_tab_list, j);
                cmpt_finale_state++;
                j--;
            }
        }
    }

    AFD afd;
    if (is_well == True)
    {
        afd = new_AFD(cmpt_state + 1, cmpt_finale_state, afn->nbre_label);
        /******************************************/
        list well_state = new_list();
        /*****************************************/
        state_tab = realloc(state_tab, (cmpt_state + 1) * sizeof(void *));
        mat_state = realloc(mat_state, (cmpt_state + 1) * sizeof(void **));

        state_tab[cmpt_state] = well_state;
        trans = calloc(afd->nbre_label, sizeof(void *));
        for (i = 0; i < afd->nbre_label; i++)
        {
            trans[i] = new_list();
        }
        mat_state[cmpt_state] = trans;
    }
    else
    {
        afd = new_AFD(cmpt_state, cmpt_finale_state, afn->nbre_label);
    }

    afd->mat_state = mat_state;
    afd->state_tab = state_tab;

    for (i = 0; i < cmpt_finale_state; i++)
    {
        afd->finale_state[i] = get_element_list(list_finale_state, i);
    }

    afd->initiale_state = state_tab[0];

    for (i = 0; i < afd->nbre_state; i++)
    {
        trans = mat_state[i];
        for (j = 0; j < afn->nbre_label; j++)
        {
            add_transition_AFD(afd, state_tab[i], afn->tab_labels[j], trans[j], cmpt);
            cmpt++;
        }
    }

    free_list(state_tab_list);
    free_list(list_finale_state);
    free_list(mat_state_list);
    return afd;
}

list epsilone_transition(AFN afn, void *state)
{
    list states = new_list();
    typedef struct etiquette etiquette;
    etiquette *lbl;
    int i = 0;
    void **transition;
    void *q;

    for (i = 0; i < afn->mat_trans->length; i++)
    {
        transition = get_element_list(afn->mat_trans, i);
        lbl = transition[1];
        if (strcmp(transition[0], state) == 0 && strcmp(lbl->value, afn->epsilone) == 0)
        {
            head_insertion(states, transition[2]);
        }
    }

    return states;
}

list epsilone_closure(AFN afn, void *state)
{
    int i = 0;

    stack pile = new_stack();
    push(pile, state);
    void *temp;
    list list_states;
    list e_fermerture = new_list();
    head_insertion(e_fermerture, state);
    while (is_empty_stack(pile) == False)
    {
        temp = pop(pile);
        list_states = epsilone_transition(afn, temp);
        for (i = 0; i < list_states->length; i++)
        {
            void *state = get_element_list(list_states, i);
            if (include(state, e_fermerture) == False)
            {
                push(pile, get_element_list(list_states, i));
                head_insertion(e_fermerture, state);
            }
        }
        free_list(list_states);
    }

    // char *test = state;
    // printf("fermeture de %s\n" , test);
    // print_list(e_fermerture, print_value);
    // printf("\n");
    // free_stack(pile);

    return e_fermerture;
}

list epsilone_closure_set(AFN afn, list set_state)
{
    list result = new_list();
    list old_result;
    int i = 0;

    for (i = 0; i < set_state->length; i++)
    {
        old_result = result;
        result = union_set(result, epsilone_closure(afn, get_element_list(set_state, i)));
        //****************************************
        free_list(old_result);
        //**************************************
    }

    // print_list(result , print_value);
    return result;
}

void print_transitions_AFD(AFD afd, void print_trans(void *begin, void *label, void *end))
{
    typedef struct etiquette etiquette;
    int i = 0;
    void **transition;
    for (i = 0; i < afd->nbre_label * afd->nbre_state; i++)
    {
        transition = afd->mat_trans[i];
        if (transition != NULL)
        {
            etiquette *lbl = transition[1];
            print_trans(transition[0], lbl, transition[2]);
        }
    }
}

void print_AFD(AFD afd, boolean is_state_list, boolean is_special_state, void print_state(void *x, boolean last), int length_state(void *x, boolean is_state_list))
{
    int i = 0, j = 0, k = 0, l_lbl = 0, l_state = 0, space_to_add;
    int max = 0;
    char *test = afd->state_tab[0];

    int max1 = length_state(afd->state_tab[0], is_state_list);

    for (i = 0; i < afd->nbre_state; i++)
    {
        int tmp = length_state(afd->state_tab[i], is_state_list);
        if (max1 < tmp)
        {
            max1 = tmp;
        }
    }

    int max2 = strlen(afd->tab_labels[0]);

    for (i = 0; i < afd->nbre_label; i++)
    {
        int tmp = strlen(afd->tab_labels[i]);
        if (max2 < tmp)
        {
            max2 = tmp;
        }
    }

    if (max1 < max2)
    {
        max = max2;
    }
    else
    {
        max = max1;
    }

    if (is_special_state == True)
    {
        // printf("le max est %d\n" , max);
        if (max < 11)
            max = 11;
    }
    else
    {
        if (max < 4)
            max = 4;
    }

    for (i = 0; i < max; i++)
    {
        printf(" ");
    }
    printf(" | ");
    for (i = 0; i < afd->nbre_label; i++)
    {
        l_lbl = strlen(afd->tab_labels[i]);

        space_to_add = max - l_lbl;
        char *str_tmp = afd->tab_labels[i];
        printf("%s", str_tmp);
        for (k = 0; k < space_to_add; k++)
        {
            printf(" ");
        }
        printf(" | ");
    }

    printf("\n");
    for (i = 0; i < (afd->nbre_label + 1) * max + afd->nbre_label * 3 + 2; i++)
    {
        printf("-");
    }
    printf("\n");
    void **trans;

    for (i = 0; i < afd->nbre_state; i++)
    {
        trans = afd->mat_state[i];
        if (is_state_list == True)
        {
            print_list(afd->state_tab[i], print_state);
        }
        else
        {
            char *str_tmp = afd->state_tab[i];
            if (str_tmp == NULL)
            {
                printf("vide");
            }
            else
            {
                printf("(%s)", str_tmp);
            }
        }
        l_state = length_state(afd->state_tab[i], is_state_list);
        space_to_add = max - l_state;

        for (k = 0; k < space_to_add; k++)
        {
            printf(" ");
        }
        printf(" | ");
        for (j = 0; j < afd->nbre_label; j++)
        {
            l_state = length_state(trans[j], is_state_list);
            if (is_state_list == True)
            {
                print_list(trans[j], print_state);
            }
            else
            {
                char *str_tmp = trans[j];
                if (str_tmp == NULL)
                {
                    printf("vide");
                }
                else
                {
                    printf("(%s)", str_tmp);
                }
            }

            space_to_add = max - l_state;
            for (k = 0; k < space_to_add; k++)
            {
                printf(" ");
            }
            printf(" | ");
        }
        printf("\n");
        for (k = 0; k < (afd->nbre_label + 1) * max + afd->nbre_label * 3 + 2; k++)
        {
            printf("-");
        }
        printf("\n");
    }
}

void AFD_to_jason(AFD afd, char *path)
{
    typedef struct etiquette etiquette;
    int i = 0, j = 0;
    char *result = NULL;
    cJSON *alphabet = NULL;
    cJSON *string = NULL;
    cJSON *initial_state = NULL;
    cJSON *final_states = NULL;
    cJSON *transitions = NULL;
    cJSON *transition = NULL;
    cJSON *state = NULL;
    cJSON *nbr_state = NULL;

    cJSON *automate = cJSON_CreateObject();

    alphabet = cJSON_CreateArray();

    cJSON_AddItemToObject(automate, "alphabet", alphabet);
    for (i = 0; i < afd->nbre_label; i++)
    {
        char *c = afd->tab_labels[i];
        string = cJSON_CreateString(c);
        cJSON_AddItemToArray(alphabet, string);
    }

    nbr_state = cJSON_CreateNumber(afd->nbre_state);
    cJSON_AddItemToObject(automate, "number state", nbr_state);

    initial_state = cJSON_CreateString(afd->initiale_state);
    cJSON_AddItemToObject(automate, "initial state", initial_state);

    final_states = cJSON_CreateArray();
    cJSON_AddItemToObject(automate, "final states", final_states);
    for (i = 0; i < afd->nbre_finale_state; i++)
    {
        char *c = afd->finale_state[i];
        string = cJSON_CreateString(c);
        cJSON_AddItemToArray(final_states, string);
    }

    transitions = cJSON_CreateArray();
    cJSON_AddItemToObject(automate, "transitions", transitions);
    for (i = 0; i < afd->nbre_state * afd->nbre_label; i++)
    {
        void **trans = afd->mat_trans[i];
        if (trans != NULL)
        {
            transition = cJSON_CreateArray();
            cJSON_AddItemToArray(transitions, transition);

            char *temp_ch = trans[0];
            string = cJSON_CreateString(temp_ch);
            cJSON_AddItemToArray(transition, string);

            etiquette *et = trans[1];
            temp_ch = et->value;
            string = cJSON_CreateString(temp_ch);
            cJSON_AddItemToArray(transition, string);

            temp_ch = trans[2];
            string = cJSON_CreateString(temp_ch);
            cJSON_AddItemToArray(transition, string);
        }
    }

    result = cJSON_Print(automate);
    cJSON_Delete(automate);

    FILE *file = fopen(path, "w");

    if (file == NULL)
        exit(1);

    fputs(result, file);
    fclose(file);
    free(result);
}

AFD jason_to_AFD(char *path, list garbage)
{
    FILE *file = NULL;
    file = fopen(path, "r");
    int lettre;
    int size = 0;
    if (file == NULL)
        exit(1);

    while ((lettre = fgetc(file)) != EOF)
        size++;
    fclose(file);

    file = fopen(path, "r");
    char *buffer = calloc(size, sizeof(char));
    fread(buffer, size, 1, file);

    fclose(file);

    int i = 0;
    cJSON *alphabet = NULL;
    cJSON *numbre_state = NULL;
    cJSON *initial_state = NULL;
    cJSON *final_states = NULL;
    cJSON *transitions = NULL;
    cJSON *transition = NULL;
    cJSON *string = NULL;
    cJSON *state = NULL;

    list mat_trans = new_list();
    list final_state_list = new_list();

    cJSON *automate = cJSON_Parse(buffer);
    if (automate == NULL)
    {
        goto end;
    }

    int nbre_label;
    alphabet = cJSON_GetObjectItemCaseSensitive(automate, "alphabet");
    if (cJSON_IsArray(alphabet))
    {
        nbre_label = cJSON_GetArraySize(alphabet);
    }
    else
    {
        goto end;
    }

    int nbr_state;
    numbre_state = cJSON_GetObjectItemCaseSensitive(automate, "number state");
    if (cJSON_IsNumber(numbre_state))
    {
        nbr_state = numbre_state->valueint;
    }

    char *init_state = NULL;
    initial_state = cJSON_GetObjectItemCaseSensitive(automate, "initial state");
    if (cJSON_IsString(initial_state))
    {
        init_state = calloc(strlen(initial_state->valuestring), sizeof(char));
        strcpy(init_state, initial_state->valuestring);
        queue_insertion(garbage, init_state);
    }
    else
    {
        goto end;
    }

    final_states = cJSON_GetObjectItemCaseSensitive(automate, "final states");
    if (cJSON_IsArray(final_states))
    {
        cJSON_ArrayForEach(string, final_states)
        {
            char *temp_ch = calloc(strlen(string->valuestring), sizeof(char));
            strcpy(temp_ch, string->valuestring);
            queue_insertion(final_state_list, temp_ch);
            queue_insertion(garbage, temp_ch);
        }
    }
    else
    {
        goto end;
    }

    transitions = cJSON_GetObjectItemCaseSensitive(automate, "transitions");
    if (cJSON_IsArray(transitions))
    {
        char *temp_ch = NULL;
        cJSON_ArrayForEach(transition, transitions)
        {
            if (cJSON_IsArray(transition))
            {
                void **trans = malloc(3 * sizeof(void *));
                temp_ch = cJSON_GetArrayItem(transition, 0)->valuestring;
                trans[0] = calloc(strlen(temp_ch), sizeof(char));
                strcpy(trans[0], temp_ch);
                temp_ch = cJSON_GetArrayItem(transition, 1)->valuestring;
                trans[1] = calloc(strlen(temp_ch), sizeof(char));
                strcpy(trans[1], temp_ch);
                temp_ch = cJSON_GetArrayItem(transition, 2)->valuestring;
                trans[2] = calloc(strlen(temp_ch), sizeof(char));
                strcpy(trans[2], temp_ch);

                queue_insertion(mat_trans, trans);
                queue_insertion(garbage, trans[0]);
                queue_insertion(garbage, trans[1]);
                queue_insertion(garbage, trans[2]);
            }
            else
            {
                goto end;
            }
        }
    }
    else
    {
        goto end;
    }

    AFD afd = new_AFD(nbr_state, final_state_list->length, nbre_label);
    afd->initiale_state = init_state;

    for (i = 0; i < final_state_list->length; i++)
    {
        char *temp = get_element_list(final_state_list, i);
        afd->finale_state[i] = temp;
    }

    for (i = 0; i < mat_trans->length; i++)
    {
        void **trans = get_element_list(mat_trans, i);
        add_transition_AFD(afd, trans[0], trans[1], trans[2], i);
    }

end:
    cJSON_Delete(automate);
    free(buffer);
    free_list(final_state_list);
    free_list(mat_trans);
    // exit(1);

    return afd;
}

void free_transition(void **trans, int nbre_label)
{
    int i = 0;
    for (i = 0; i < nbre_label; i++)
    {
        free_list(trans[i]);
        trans[i] = NULL;
    }
    free(trans);
}

void free_AFD(AFD afd, boolean is_state_list)
{
    typedef struct etiquette etiquette;
    int i = 0;
    void **trans;
    if (afd != NULL)
    {
        if (afd->mat_state != NULL)
        {
            if (is_state_list == True)
            {
                list temp_list = afd->state_tab[afd->nbre_state - 1];
                // on libere l'etat puit qui est dans le tableau des etats
                if (is_empty_list(temp_list) == True || (temp_list->length >= 2 && get_element_list(temp_list, 0) == NULL && get_element_list(temp_list, 1) == NULL))
                {
                    free_list(afd->state_tab[afd->nbre_state - 1]);
                }

                for (i = 0; i < afd->nbre_state; i++)
                {
                    free_transition(afd->mat_state[i], afd->nbre_label);
                }
                free_list(afd->initiale_state);
            }
            else
            {
                for (i = 0; i < afd->nbre_state; i++)
                {
                    free(afd->mat_state[i]);
                }
            }
            free(afd->state_tab);
        }

        for (i = 0; i < afd->nbre_label * afd->nbre_state; i++)
        {
            trans = afd->mat_trans[i];
            if (trans != NULL)
            {
                free(trans[1]);
                free(afd->mat_trans[i]);
            }
        }

        free(afd->mat_trans);
        free(afd->mat_state);
        free(afd->tab_labels);
        free(afd->finale_state);
        free(afd);
    }
}

#endif