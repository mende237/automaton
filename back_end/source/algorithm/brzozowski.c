#ifndef BRZOZOWSKI_C
#define BRZOZOWSKI_C

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "../../header/algorithm/brzozowski.h"
#include "../../header/algorithm/function.h"

AFD brzozowski_minimisation(AFD afd, boolean equal_value(void *st1, void *st2, ...))
{
    AFN afn_temp1 = miroir_AFD(afd);

    AFD afd_temp1 = determinisation(afn_temp1, equal_value);

    free_AFN(afn_temp1);

    AFD afd_temp2 = rename_states(afd_temp1, True);

    AFN afn_temp2 = miroir_AFD(afd_temp2);

    AFD afd_temp3 = determinisation(afn_temp2, equal_value);

    free_AFN(afn_temp2);

    AFD afd_result = rename_states(afd_temp3, True);

    return afd_result;
}

static boolean equal_struct_state(void *st1, void *value_st2, ...)
{
    struct state *st = st1;
    char *ch = value_st2;
    if (strcmp(st->value, ch) == 0)
        return True;

    return False;
}

static list *generate_mat_trans(AFD afd , list garbage)
{
    list *data = calloc(2, sizeof(list_element));
    typedef struct state state;
    typedef struct etiquette etiquette;
    int i = 0, j = 0, k = 0, index = 0;
    /************************************/
    list state_list = new_list();
    list mat_trans = new_list();
    /************************************/
    void **trans_temp;
    state *st = NULL;
    etiquette *et;

    for (i = 0; i < afd->nbre_state * afd->nbre_label; i++)
    {
        if (afd->mat_trans[i] != NULL)
        {
            /**********************************************/
            void **trans = calloc(3, sizeof(void *));
            /**********************************************/
            et = afd->mat_trans[i][1];
            trans[1] = et->value;
            index = get_index_element_list(state_list, afd->mat_trans[i][0], equal_struct_state);
            if (index == -1)
            {
                /*****************************************/
                st = calloc(1, sizeof(state));
                /*****************************************/

                st->value = afd->mat_trans[i][0];

                /***************************************/
                st->loop = NULL;
                /**************************************/
                queue_insertion(state_list, st);
                trans[0] = st;
            }
            else
            {
                st = get_element_list(state_list, index);
            }

            trans[0] = st;
            index = get_index_element_list(state_list, afd->mat_trans[i][2], equal_struct_state);
            if (index == -1)
            {
                /*****************************************/
                st = calloc(1, sizeof(state));
                /*****************************************/
                st->value = afd->mat_trans[i][2];

                st->loop = NULL;
                queue_insertion(state_list, st);
                trans[2] = st;
            }
            else
            {
                st = get_element_list(state_list, index);
            }
            trans[2] = st;
            queue_insertion(mat_trans, trans);
        }
        else
        {
            break;
        }
    }

    state *begin = calloc(1, sizeof(state));
    begin->value = "begin";
    /***********************************/
    begin->loop = NULL;
    /***********************************/

    state *end = calloc(1, sizeof(state));
    end->value = "end";
    end->loop = NULL;
    boolean find = False;
    char *state_begin;

    for (i = 0; i < state_list->length; i++)
    {
        st = get_element_list(state_list, i);
        if (search_state(afd->finale_state, st->value, afd->nbre_finale_state) == True)
        {
            /***********************************************************/
            void **trans = calloc(3, sizeof(void *));
            /**********************************************************/
            trans[0] = st;
            char *ep = calloc(3, sizeof(char));
            strcpy(ep, "ep");
            trans[1] = ep;
            trans[2] = end;
            queue_insertion(mat_trans, trans);
            queue_insertion(garbage , ep);
        }

        if (find == True)
            continue;

        if (strcmp(afd->initiale_state, st->value) == 0)
        {
            find = True;
            /***********************************************************/
            void **trans = calloc(3, sizeof(void *));
            /**********************************************************/
            trans[0] = begin;
            char *ep = calloc(3, sizeof(char));
            strcpy(ep, "ep");
            trans[1] = ep;
            trans[2] = st;
            queue_insertion(mat_trans, trans);
            queue_insertion(garbage, ep);
        }
    }

    // void *st1;
    // void *st2;
    // struct etiquette *et;
    // char *lbl;

    // for (i = 0; i < mat_trans->length; i++)
    // {
    //     trans_temp = get_element_list(mat_trans , i);
    //     st1 = trans_temp[0];
    //     st2 = trans_temp[2];
    //     et = trans_temp[1];
    //     lbl = et->value;
    //     printf("%p -------%s-------%p\n" , st1 , lbl , st2);

    // }

    // free_list(mat_trans);
    data[0] = state_list;
    data[1] = mat_trans;

    return data;
}

static void delete_loop(list mat_trans , list garbage)
{
    typedef struct state state;
    int i = 0;
    void **trans = NULL;
    state *st = NULL;
    char *ch_temp, *prevouis_ch;
    // state *st2 = NULL;

    for (i = 0; i < mat_trans->length; i++)
    {
        trans = get_element_list(mat_trans, i);
        if (trans[0] == trans[2])
        {
            ch_temp = trans[1];
            st = trans[0];

            if (st->loop == NULL)
            {
                st->loop = calloc(strlen(ch_temp), sizeof(char));
                strcat(st->loop, ch_temp);
            }
            else
            {
                // prevouis_ch = st->loop;
                // st->loop = calloc(strlen(prevouis_ch) + strlen(ch_temp) + 1 , sizeof(char));
                // strcat(st->loop , prevouis_ch);
                // strcat(st->loop , "+");
                // strcat(st->loop , ch_temp);
                // free(prevouis_ch);
                st->loop = realloc(st->loop, (strlen(st->loop) + strlen(ch_temp) + 1) * sizeof(char));
                strcat(st->loop, "+");
                strcat(st->loop, ch_temp);
            }
            //queue_insertion(garbage , st->loop);
            delete_element_list(mat_trans, i);
            i--;
        }
    }

    // void **trans_temp;
    // state *st1;
    // state *st2;
    // char *lbl;
    // for (i = 0; i < mat_trans->length; i++)
    // {
    //     trans_temp = get_element_list(mat_trans, i);
    //     st1 = trans_temp[0];
    //     st2 = trans_temp[2];

    //     char *ch1 = st1->value;
    //     char *ch2 = st2->value;

    //     lbl = trans_temp[1];
    //     printf("%s %s  -------%s------- %s %s", ch1, st1->loop, lbl, ch2, st2->loop);
    //     printf("\n");
    // }
}

static void delete_redondance(list mat_trans , list garbage)
{
    typedef struct etiquette etiquette;
    int i = 0, j = 0;
    void **trans_temp1 = NULL;
    void **trans_temp2 = NULL;
    boolean change = False;
    for (i = 0; i < mat_trans->length; i++)
    {
        trans_temp1 = get_element_list(mat_trans, i);
        change = False;
        char *lable = calloc(strlen(trans_temp1[1]) + 1, sizeof(char));
        strcpy(lable, (char *)trans_temp1[1]);
        char *old = trans_temp1[1];

        for (j = i + 1; j < mat_trans->length; j++)
        {
            trans_temp2 = get_element_list(mat_trans, j);
            if (trans_temp1[0] == trans_temp2[0] && trans_temp1[2] == trans_temp2[2])
            {
                change = True;
                lable = realloc(lable, (strlen(lable) + strlen((char *)trans_temp2[1]) + 2) * sizeof(char));
                strcat(lable, "+");
                strcat(lable, (char *)trans_temp2[1]);
                delete_element_list(mat_trans, j);
                j--;
                i--;
            }
        }

        if (change == True)
        {
            // free(trans_temp1[1]);
            trans_temp1[1] = lable;
            queue_insertion(garbage , lable);
        }
    }

    // printf("\n*********suppresion des redondances**************\n");
    // typedef struct state state;
    // void **trans_temp;
    // state *st1;
    // state *st2;
    // char *lbl;
    // for (i = 0; i < mat_trans->length; i++)
    // {
    //     trans_temp = get_element_list(mat_trans, i);
    //     st1 = trans_temp[0];
    //     st2 = trans_temp[2];

    //     char *ch1 = st1->value;
    //     char *ch2 = st2->value;

    //     lbl = trans_temp[1];
    //     printf("%s %s  -------%s------- %s %s", ch1, st1->loop, lbl, ch2, st2->loop);
    //     printf("\n");
    // }
}

static list generate_follow(list mat_trans, struct state *st)
{
    typedef struct state *state;
    int i = 0;
    struct pseudo_transition *p_trans;
    void **trans_temp;
    char *ch;
    state st1;
    /*********************************/
    list follow = new_list(); // free dans la fonction generate_new_trans
    /*********************************/
    printf("les suivants de %s:\n", st->value);
    for (i = 0; i < mat_trans->length; i++)
    {
        trans_temp = get_element_list(mat_trans, i);
        if (trans_temp[0] == st)
        {
            /*****************************************************************/
            p_trans = calloc(1, sizeof(struct pseudo_transition)); // free dans la fonction generate_new_trans
            /*****************************************************************/
            p_trans->lbl = trans_temp[1];
            p_trans->st = trans_temp[2];
            st1 = trans_temp[2];
            printf("%s %s ", st1->value, st1->loop);
            queue_insertion(follow, p_trans);
        }
    }
    printf("\n");
    return follow;
}

static list generate_precedent(list mat_trans, struct state *st)
{
    typedef struct state *state;
    int i = 0;
    void **trans_temp;
    struct pseudo_transition *p_trans;
    char *ch;
    state st1;
    /*********************************/
    list precedent = new_list();
    /*********************************/
    printf("les precedents de %s:\n", st->value);
    for (i = 0; i < mat_trans->length; i++)
    {
        trans_temp = get_element_list(mat_trans, i);
        if (trans_temp[2] == st)
        {
            /*******************************************************************/
            p_trans = calloc(1, sizeof(struct pseudo_transition));
            /******************************************************************/
            p_trans->lbl = trans_temp[1];
            p_trans->st = trans_temp[0];
            st1 = trans_temp[0];
            printf("%s %s ", st1->value, st1->loop);
            queue_insertion(precedent, p_trans);
        }
    }
    printf("\n");

    return precedent;
}
/*cette fonction retourne une matrice de transition et la liste des etats*/
static list generate_new_trans(list mat_trans, struct state *st, list garbage)
{
    typedef struct pseudo_transition pseudo_transition;
    pseudo_transition *p_trans_p;
    pseudo_transition *p_trans_f;

    int i = 0, j = 0;
    list new_trans_list = new_list();
    list follow_list = generate_follow(mat_trans, st);
    list precedent_list = generate_precedent(mat_trans, st);
    char *lbl, *loop_lbl;
    void **new_trans;

    void **follow = copy_data_list_to_tab(follow_list);
    void **precedent = copy_data_list_to_tab(precedent_list);

    boolean paranthese_p = False;
    boolean paranthese_f = False;
    
    for (i = 0; i < precedent_list->length; i++)
    {
        paranthese_p = False;
        p_trans_p = precedent[i];
        char *lbl_p = NULL;
        for (j = 0; j < follow_list->length; j++)
        {
            lbl_p = bracket(p_trans_p->lbl, True);
            paranthese_f = False;
            p_trans_f = follow[j];
            char *lbl_f = NULL;
            // dans le cas ou l'etat n'a pas de boucle
            if (st->loop == NULL)
            {
                if (strcmp(p_trans_p->lbl, "ep") != 0 && strcmp(p_trans_f->lbl, "ep") != 0)
                {
                    lbl_f = bracket(p_trans_f->lbl , True);
                    lbl = calloc(strlen(lbl_p) + strlen(lbl_f) + 2, sizeof(char));
                    strcpy(lbl, lbl_p);
                    strcat(lbl, ".");
                    strcat(lbl, lbl_f);
                }
                else if (strcmp(p_trans_p->lbl, "ep") == 0 && strcmp(p_trans_f->lbl, "ep") != 0)
                {
                    lbl_f = bracket(p_trans_f->lbl, False);
                    lbl = calloc(strlen(lbl_f) + 1, sizeof(char));
                    strcpy(lbl, lbl_f);
                }
                else if (strcmp(p_trans_p->lbl, "ep") != 0 && strcmp(p_trans_f->lbl, "ep") == 0)
                {
                    lbl_f = bracket(p_trans_f->lbl, False);
                    lbl_p = remove_bracket(lbl_p);
                    lbl = calloc(strlen(lbl_p) + 1, sizeof(char));
                    strcpy(lbl, lbl_p);
                }
                else
                {
                    lbl_f = bracket(p_trans_f->lbl, False);
                    lbl = calloc(3, sizeof(char));
                    strcpy(lbl, "ep");
                }
            }
            else
            {
                if (strchr(st->loop, '+') != NULL || strchr(st->loop, '.') != NULL)
                {
                    loop_lbl = calloc(strlen(st->loop) + 4, sizeof(char));
                    strcpy(loop_lbl, "(");
                    strcat(loop_lbl, st->loop);
                    strcat(loop_lbl, ")");
                    strcat(loop_lbl, "*");
                }
                else
                {
                    loop_lbl = calloc(strlen(st->loop) + 2, sizeof(char));
                    strcat(loop_lbl, st->loop);
                    strcat(loop_lbl, "*");
                }

                if (strcmp(p_trans_p->lbl, "ep") != 0 && strcmp(p_trans_f->lbl, "ep") != 0)
                {
                    lbl_f = bracket(p_trans_f->lbl, True);
                    lbl = calloc(strlen(lbl_p) + strlen(loop_lbl) + strlen(lbl_f) + 3, sizeof(char));
                    strcpy(lbl, lbl_p);
                    strcat(lbl, ".");
                    strcat(lbl, loop_lbl);
                    strcat(lbl, ".");
                    strcat(lbl, lbl_f);
                }
                else if (strcmp(p_trans_p->lbl, "ep") == 0 && strcmp(p_trans_f->lbl, "ep") != 0)
                {
                    lbl_f = bracket(p_trans_f->lbl, False);
                    lbl = calloc(strlen(loop_lbl) + strlen(lbl_f) + 2, sizeof(char));
                    strcpy(lbl, loop_lbl);
                    strcat(lbl, ".");
                    strcat(lbl, lbl_f);
                }
                else if (strcmp(p_trans_p->lbl, "ep") != 0 && strcmp(p_trans_f->lbl, "ep") == 0)
                {
                    lbl_f = bracket(p_trans_f->lbl, False);
                    // lbl_p = remove_bracket(lbl_p);
                    lbl = calloc(strlen(lbl_p) + strlen(loop_lbl) + 2, sizeof(char));
                    strcpy(lbl, lbl_p);
                    strcat(lbl, ".");
                    strcat(lbl, loop_lbl);
                }
                else
                {
                    lbl_f = bracket(p_trans_f->lbl, False);
                    lbl = calloc(strlen(loop_lbl) + 1, sizeof(char));
                    strcpy(lbl, loop_lbl);
                }
                free(loop_lbl);
            }

            // dans le cas ou il n'y a pas une nouvelle boucle qui se forme
            if (p_trans_p->st != p_trans_f->st)
            {
                new_trans = calloc(3, sizeof(void *));
                new_trans[0] = p_trans_p->st;
                new_trans[1] = lbl;
                new_trans[2] = p_trans_f->st;
                queue_insertion(new_trans_list, new_trans);
            }
            else
            {
                // on cree la nouvelle boucle
                if (p_trans_p->st->loop == NULL)
                {

                    p_trans_p->st->loop = calloc(strlen(lbl) + 1 , sizeof(char));
                    strcpy(p_trans_p->st->loop , lbl);
                }
                else
                {
                    int old_length = strlen(p_trans_p->st->loop);
                    int lbl_length = strlen(lbl);

                    // char *temp = calloc(strlen(p_trans_p->st->loop) + strlen(lbl) + 1 , sizeof(char));
                    // strcat(temp, p_trans_p->st->loop);
                    // strcat(temp , "+");
                    // strcat(temp , lbl);
                    // free(p_trans_p->st->loop);
                    // p_trans_p->st->loop = temp;
                    p_trans_f->st->loop = realloc(p_trans_f->st->loop, (old_length + lbl_length + 2) * sizeof(char));
                    strcat(p_trans_p->st->loop, "+");
                    strcat(p_trans_p->st->loop, lbl);
                }
            }

            queue_insertion(garbage, lbl);
            queue_insertion(garbage, lbl_f);
            queue_insertion(garbage, lbl_p);
        }
    }

    //                    freeing memory
    for (i = 0; i < follow_list->length; i++)
    {
        free(follow[i]);
    }

    for (i = 0; i < precedent_list->length; i++)
    {
        free(precedent[i]);
    }

    free_list(follow_list);
    free_list(precedent_list);
    free(follow);
    free(precedent);
    return new_trans_list;
}

static char* bracket(char *exp , boolean put_bracket){
    char *result = NULL;
    if(put_bracket == True){
        if (strchr(exp, '+') != NULL)
        {
            result = calloc(strlen(exp) + 3, sizeof(char));
            strcpy(result, "(");
            strcat(result, exp);
            strcat(result, ")");
        }
        else
        {
            result = calloc(strlen(exp) + 1, sizeof(char));
            strcpy(result, exp);
        }
    }else{
        result = calloc(strlen(exp) + 1, sizeof(char));
        strcpy(result, exp);
    }

    return result;
}

static char *remove_bracket(char *ch){
    if (strchr(ch, '(') != NULL || strchr(ch, ')') != NULL)
    {
        int i = 0;
        char *result = calloc(strlen(ch) - 1 , sizeof(char));
        ch[strlen(ch) - 1] = '\0';
        for (i = 1; i < (int) strlen(ch); i++)
        {
            result[i-1] = ch[i];
        }
        free(ch);
        return result;
    }
    else
    {
        return ch;
    }
}

static void **copy_data_list_to_tab(const list li)
{
    int i = 0;
    void **tab = calloc(li->length, sizeof(void *));
    for (i = 0; i < li->length; i++)
    {
        tab[i] = get_element_list(li, i);
    }

    return tab;
}

char *brzozowski_AFD_to_REG(AFD afd)
{
    typedef struct state *state;
    int i = 0, j = 0, k = 0;
    list garbage = new_list();
    list new_trans_list = NULL;
    list *data = generate_mat_trans(afd , garbage);
    list state_list = data[0];
    list mat_trans = data[1];

    void **trans_temp;
    int length = 0;

    state st1;
    state st2;
    char *lbl;
    char *expression_result;

   
    state st;
    delete_loop(mat_trans , garbage);
    delete_redondance(mat_trans , garbage);
    void **tab_state = copy_data_list_to_tab(state_list);
    for (i = 0; i < state_list->length; i++)
    {
        st = tab_state[i];
        new_trans_list = generate_new_trans(mat_trans, tab_state[i], garbage);
        for (j = 0; j < mat_trans->length; j++)
        {
            trans_temp = get_element_list(mat_trans, j);
            if (tab_state[i] == trans_temp[0] || tab_state[i] == trans_temp[2])
            {
                delete_element_list(mat_trans, j);
                free(trans_temp);
                j--;
            }
        }

        for (j = 0; j < new_trans_list->length; j++)
        {
            queue_insertion(mat_trans, get_element_list(new_trans_list, j));
        }
        free_list(new_trans_list);
        delete_redondance(mat_trans , garbage);

        printf("***********************************************\n");
        printf("apres la suppresion de l'etat %s\n", st->value);
        printf("************************************************\n");
        for (k = 0; k < mat_trans->length; k++)
        {
            trans_temp = get_element_list(mat_trans, k);
            st1 = trans_temp[0];
            st2 = trans_temp[2];
            lbl = trans_temp[1];
            printf("%s %s----- %s ------ %s %s\n", st1->value, st1->loop, lbl, st2->value, st2->loop);
        }
    }

    for (i = 0; i < mat_trans->length; i++)
    {
        trans_temp = get_element_list(mat_trans, i);
        lbl = trans_temp[1];
        length += strlen(lbl);
    }

    if (mat_trans->length == 1)
    {
        trans_temp = get_element_list(mat_trans, 0);
        lbl = trans_temp[1];
        expression_result = calloc(length, sizeof(char));
        strcpy(expression_result, lbl);
    }
    else
    {
        expression_result = calloc(length + mat_trans->length - 1, sizeof(char));
        for (i = 0; i < mat_trans->length - 1; i++)
        {
            trans_temp = get_element_list(mat_trans, i);
            lbl = trans_temp[1];
            strcat(expression_result, lbl);
            strcat(expression_result, "+");
        }
        trans_temp = get_element_list(mat_trans, i);
        lbl = trans_temp[1];
        strcat(expression_result, lbl);
    }

    printf("l'expression final est :%s\n", expression_result);

    for (i = 0; i < garbage->length; i++)
    {
        lbl = get_element_list(garbage, i);
        printf("suppression de : %s\n" , lbl);
        free(lbl);
    }

    for (i = 0; i < state_list->length; i++)
    {
        st = (state) tab_state[i];
        printf("%s      %s\n", (char *)st->loop, (char *)st->value);
        if (st->loop != NULL){
            free(st->loop);
        }
        free(tab_state[i]);
    }

    free(tab_state);
    free_list(state_list);
    free_list(mat_trans);
    free(data);
    free_list(garbage);
    return expression_result;
}

#endif
