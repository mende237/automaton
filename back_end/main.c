#include <stdarg.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "./source/algorithm/AFN.c"
#include "./source/algorithm/AFD.c"
#include "./source/data_structure/linked_list.c"
#include "./source/algorithm/thomson.c"
#include "./source/algorithm/glushkov.c"
#include "./source/algorithm/utilitaire.c"

char **add_data(int n, ...);
void clearScreen();
void print_label(void *val);
void print_element_in_list(void *x, boolean last);
void print_tree_info(void *x);
void print_trans_info(void *src, void *lbl, void *dest);
void print_trans_list_info(void *src, void *lbl, void *dest);
int length_state_list(void *x);
int length_label(void *c);

boolean confirm_expression(char **reg_expression, int length);
boolean confirm(int user_rep);
boolean equal_special_state(void *st1, void *st2);
boolean equal_st(void *st1, void *st2);

void clearScreen()
{
#ifdef __unix__

    system("clear");

#else

    system("CLS");

#endif
}

boolean confirm_expression(char **reg_expression, int length)
{
    int rep = 1, i = 0;
    for (i = 0; i < length; i++)
    {
        printf("%s ", reg_expression[i]);
    }
    do
    {
        printf("\nCONFIRMER QUE C'EST BIEN VOTRE EXPRESSION (OUI = 1 NON = 0) : ");
        scanf("%d", &rep);
    } while (rep != 0 && rep != 1);

    if (rep == 0)
        return False;

    return True;
}

boolean confirm(int user_rep)
{

    while (user_rep != 0 && user_rep != 1)
    {
        printf("ENTRER OUI = 1 NON = 0\n");
        scanf("%d", &user_rep);
    }
    if (user_rep == 0)
        return False;

    return True;
}

int length_state(void *x, boolean is_state_list)
{
    if (is_state_list == True)
    {
        list li = x;
        if (is_empty_list(li) == False)
        {
            int i = 0, length = 0;
            for (i = 0; i < li->length; i++)
            {
                char *tmp = get_element_list(li, i);
                length += strlen(tmp);
            }

            length += li->length + 1;
            return length;
        }
        return 4;
    }

    char *str = x;
    return strlen(x) + 2;
}

int length_label(void *c){
    char *lbl = c;
    return strlen(lbl);
}

void print_label(void *val){
    char *ch = val;
    printf("%s" , ch);
}

void print_trans_info(void *src, void *lbl, void *dest)
{
    typedef struct etiquette etiquette;

    char *d = src;
    char *f = dest;
    etiquette *et = lbl;
    char *val = et->value;
    printf("%s---------%s---------->%s\n", d, val, f);
}

void print_trans_list_info(void *src, void *lbl, void *dest)
{
    typedef struct etiquette etiquette;
    list d = src;
    list f = dest;
    etiquette *et = lbl;
    char *val = et->value;
    print_list(d, print_element_in_list);
    printf("---------");
    printf("%s", val);
    printf("---------->");
    print_list(f, print_element_in_list);
    printf("\n");
}

void print_info2(void *src, void *lbl, void *dest)
{
    typedef struct etiquette etiquette;

    list d = src;
    list f = dest;
    etiquette *et = lbl;
    char *val = et->value;
    print_list(d, print_element_in_list);
    printf("---------%s index %d-----------", val, et->index);
    print_list(f, print_element_in_list);
    printf("\n");
}

void print_element_in_list(void *x, boolean last)
{
    char *value = x;
    if (last == False)
    {
        printf("%s,", value);
    }
    else
    {
        printf("%s", value);
    }
}

void print_tree_info(void *x)
{

    if (x != NULL)
    {
        node_tree *node = x;
        thomson_node *elem = node->info;
        char *value = elem->value;
        char *bg = elem->initiale_state;
        char *end = elem->end_state;
        printf("%s %s %s\n", value, bg, end);
    }
    else
    {
        printf("0");
    }
}

boolean equal_st(void *st1, void *st2)
{
    char *ch1 = st1;
    char *ch2 = st2;
    if (strcmp(ch1, ch2) == 0)
    {
        return True;
    }
    else
    {
        return False;
    }
}

boolean equal_special_state(void *li1, void *li2)
{
    list st1 = li1;
    list st2 = li2;
    boolean rep = False;
    int i = 0, j = 0;
    if (st1->length != st2->length)
    {
        return False;
    }
    else
    {
        for (i = 0; i < st1->length; i++)
        {
            rep = False;
            for (j = 0; j < st2->length; j++)
            {
                if (equal_st(get_element_list(st1, i), get_element_list(st2, j)) == True)
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
}

boolean equal_label(void *lbl1, void *lbl2)
{
    char *ch1 = lbl1;
    char *ch2 = lbl2;
    if (strcmp(ch1, ch2) == 0)
    {
        return True;
    }
    else
    {
        return False;
    }
}

char **add_data(int n, ...)
{
    int i = 0;
    char **data = malloc(n * sizeof(char *));
    va_list lst;
    va_start(lst, n);
    for (i = 0; i < n; i++)
    {
        data[i] = va_arg(lst, char *);
    }
    va_end(lst);

    return data;
}

int main()
{
MENU:
    clearScreen();
    printf("***************************************TP_AUTOMATE*****************************************\n");
    printf("****************                                                           ****************\n");
    printf("*******                          1-ALGORITHME DE GLUSHKOV                         *********\n");
    printf("*****                                                                                ******\n");
    printf("**                               2-ALGORITHME DE THOMSON                                 **\n");
    printf("*                                                                                         *\n");
    printf("*                                3-DETERMINISATION                                        *\n");
    printf("*                                                                                         *\n");
    printf("**                               4-EPSILONE DETERMINISATION                              **\n");
    printf("*****                                                                                ******\n");
    printf("*******                          5-RECONNAISSANCE D'UN MOT                        *********\n");
    printf("****************                                                           ****************\n");
    printf("*******************************************************************************************\n");

    boolean restart = False;
    boolean rep = False;

    int length = 0;
    int user_rep = 1;
    int word_length = 0;

    int nbr_state = 2;
    int nbr_initiale_state = 1;
    int nbr_finale_state = 1;
    int nbr_label = 1;
    int i = 0;
    int cmpt = 0;

    char *exp = NULL;
    char *state = NULL;
    char **reg_expression = NULL;
    char **word = NULL;
    char **trans = NULL;

    list expression_list = NULL;
    list garbage = NULL;
    list word_list = NULL;
    list *result = NULL;
    list good_word = NULL;
    list bad_word = NULL;

    AFD afd = NULL;
    AFN afn = NULL;

    do
    {
        printf("QUE VOULEZ EXECUTER : ");
        scanf("%d", &user_rep);
    } while (user_rep < 1 && user_rep > 5);

    // do
    // {
    switch (user_rep)
    {
    case 1:
    READ_EXPRESSION_G:
        printf("\nENTRER L'EXPRESSION REGULIERE EN N'OMETTANT PAS LE SYMBOLE '.' \n");
        expression_list = read_expression(255);

        reg_expression = malloc(expression_list->length * sizeof(char *));

        for (i = 0; i < expression_list->length; i++)
        {
            reg_expression[i] = get_element_list(expression_list, i);
        }
        rep = confirm_expression(reg_expression, expression_list->length);
        if (rep == False)
            goto READ_EXPRESSION_G;

        printf("\n");
        afn = glushkov_algorithm(reg_expression, expression_list->length);
        print_info_AFN(afn, print_trans_info);

        printf("VOULEZ VOUS DETERMINISER CET AUTOMATE ?");
        scanf("%d", &user_rep);

        if (user_rep == 1)
        {
            afd = determinisation(afn,equal_st ,print_element_in_list);

            print_transitions_AFD(rename_states(afd, 1) , print_trans_info);
        }
        else
        {

            restart = True;
            free_elem_in_list(expression_list);
            free_list(expression_list);
            free(reg_expression);
            free_AFN(afn);
            clearScreen();
            goto MENU;
        }
        print_AFD(afd ,True, print_element_in_list , length_state);
        break;
        print_info_AFD(afd, True, print_element_in_list, print_trans_list_info);

        //print_transitions_AFD(afd, print_trans_list_info);
        printf("VOULEZ VOUS VERIFIER SI UN MOT EST RECONNU PAR CET AUTOMATE? :");
        scanf("%d", &user_rep);
        rep = confirm(user_rep);

        if (user_rep == 1)
        {
        READ_WORD1:
            word_list = new_list();
            printf("ENTRER UNE LISTE DE MOTS DONT VOUS VOULEZ RECONNAITRE ENTRER end POUR ARRETER:\n");
            do
            {

                exp = calloc(255, sizeof(char));
                scanf("%s", exp);
                if (strcmp(exp, "end") != 0)
                {
                    //on doit free chaque element de ce tableau
                    word = convert_to_word(exp);
                    head_insertion(word_list, word);
                }
            } while (strcmp(exp, "end") != 0);

            result = detect_word(afd, True , equal_special_state, equal_label, word_list , print_element_in_list);
            printf("\nL'ENSEMBLE DES MOTS RECONNU PAR CET AUTOMATE SONT :\n");
            print_result(result[0]);
            printf("L'ENSEMBLE DES MOTS QUI NE SONT PAS RECONNUS SONT :\n");
            print_result(result[1]);
            printf("VOULEZ VOUS RECONNAITRE D'AUTRE MOTS ? OUI = 1 NON = 0 ? : ");

            scanf("%d", &user_rep);
            if (confirm(user_rep) == False)
            {
                restart = True;
            }
            else
            {
                restart = False;
            }

            for (i = 0; i < word_list->length; i++)
            {
                free_word(get_element_list(word_list, i));
            }

            free_list(word_list);
            free(result[0]);
            free(result[1]);
        }
        else
        {
            restart = True;
        }

        clearScreen();
        if (restart == True)
        {
            free_elem_in_list(expression_list);
            free_list(expression_list);
            free(reg_expression);
            free_AFD(afd , True);
            free_AFN(afn);
            goto MENU;
        }
        else
        {
            clearScreen();
            goto READ_WORD1;
        }

        break;

    case 2:
    READ_EXPRESSION_T:
        printf("\nENTRER L'EXPRESSION REGULIERE EN OMETTANT PAS LE SYMBOLE '.' \n");

        expression_list = read_expression(255);
        reg_expression = malloc(expression_list->length * sizeof(char *));

        for (i = 0; i < expression_list->length; i++)
        {
            reg_expression[i] = get_element_list(expression_list, i);
        }
        rep = confirm_expression(reg_expression, expression_list->length);
        if (rep == False)
            goto READ_EXPRESSION_T;

        afn = thomson_algorithm(reg_expression, expression_list->length,print_trans_info);
        print_info_AFN(afn, print_trans_info);

        printf("VOULEZ VOUS DETERMINISER CET AUTOMATE ?");
        scanf("%d", &user_rep);

        if (user_rep == 1)
        {
            afd = epsilone_determinisation(afn,equal_st,print_element_in_list);
            rename_states(afd, 1);
        }
        else
        {

            free_elem_in_list(expression_list);
            free_list(expression_list);
            free(reg_expression);
            free_AFN(afn);
            clearScreen();
            goto MENU;
        }

        print_info_AFD(afd, True, print_element_in_list, print_trans_list_info);
        print_AFD(afd,True, print_element_in_list, length_state);

        printf("VOULEZ VOUS VERIFIER SI UN MOT EST RECONNU PAR CET AUTOMATE? :");
        scanf("%d", &user_rep);
        rep = confirm(user_rep);

        if (user_rep == 1)
        {
        READ_WORD2:
            word_list = new_list();
            printf("ENTRER UNE LISTE DE MOTS DONT VOUS VOULEZ RECONNAITRE ENTRER end POUR ARRETER:\n");
            do
            {

                exp = calloc(255, sizeof(char));
                scanf("%s", exp);
                if (strcmp(exp, "end") != 0)
                {
                    //on doit free chaque element de ce tableau
                    word = convert_to_word(exp);
                    head_insertion(word_list, word);
                }
            } while (strcmp(exp, "end") != 0);

            result = detect_word(afd,True, equal_special_state, equal_label, word_list , print_element_in_list);
            printf("\nL'ENSEMBLE DES MOTS RECONNU PAR CET AUTOMATE SONT :\n");
            print_result(result[0]);
            printf("L'ENSEMBLE DES MOTS QUI NE SONT PAS RECONNUS SONT :\n");
            print_result(result[1]);
            printf("VOULEZ VOUS RECONNAITRE D'AUTRE MOTS ? OUI = 1 NON = 0 ? : ");

            scanf("%d", &user_rep);
            if (confirm(user_rep) == False)
            {
                restart = True;
            }
            else
            {
                restart = False;
            }

            for (i = 0; i < word_list->length; i++)
            {
                free_word(get_element_list(word_list, i));
            }

            free_list(word_list);
            free(result[0]);
            free(result[1]);
        }
        else
        {
            restart = True;
        }

        if (restart == True)
        {
            free_elem_in_list(expression_list);
            free_list(expression_list);
            free(reg_expression);
            free_AFD(afd , True);
            free_AFN(afn);
            goto MENU;
        }
        else
        {
            clearScreen();
            goto READ_WORD2;
        }
        break;
    case 3:
    case 4:
    READ_AFN:
        garbage = new_list();
        printf("ENTRER LE NOMBRE D'ETAT : ");
        scanf("%d", &nbr_state);
        printf("ENTRER LE NOMBRE D'ETAT INITIAUX : ");
        scanf("%d", &nbr_initiale_state);
        printf("ENTRER LE NOMBRE D'ETAT FINAUX : ");
        scanf("%d", &nbr_finale_state);
        printf("ENTRER LE NOMBRE D'ETIQUETTES DIFFERENTS : ");
        scanf("%d", &nbr_label);

        printf("\nENTRER VOS TRANSITIONS EXEMPLE 0,a,1 OU 0 ET 1 SONT DES ETATS ET A UN SYMBOLE\n");
        printf("ENTRER end QUAND VOUS NE VOULEZ ARRETER\n");

        afn = new_AFN(nbr_state, nbr_initiale_state, nbr_finale_state, nbr_label, "ep");

        do
        {
            exp = calloc(25, sizeof(char));
            scanf("%s", exp);
            if (strcmp(exp, "end") != 0)
            {
                trans = convert_to_transition(exp);

                head_insertion(garbage, trans[0]);
                head_insertion(garbage, trans[1]);
                head_insertion(garbage, trans[2]);
                add_transition_AFN(afn, trans[0], trans[1], trans[2]);
            }
        } while (strcmp(exp, "end") != 0);

        for (i = 0; i < nbr_initiale_state; i++)
        {
            state = calloc(25, sizeof(char));
            printf("ENTRER L'ETAT INTITIALE : ");
            scanf("%s", state);
            afn->initiale_state[i] = state;
            head_insertion(garbage, state);
        }

        for (i = 0; i < nbr_finale_state; i++)
        {
            state = calloc(25, sizeof(char));
            printf("ENTRER L'ETAT FINALE : ");
            scanf("%s", state);
            afn->finale_state[i] = state;
            head_insertion(garbage, state);
        }

        print_info_AFN(afn, print_trans_info);
        printf("CONFIRMER QU'IL S'AGI BIEN DE VOTRE AUTOMATE OUI = 1 NON = 0 : ");
        scanf("%d", &user_rep);
        if (confirm(user_rep) == False)
        {
            for (i = 0; i < garbage->length; i++)
            {
                free(get_element_list(garbage, i));
            }

            free_list(garbage);
            free_AFN(afn);
            clearScreen();
            goto READ_AFN;
        }

        if (user_rep == 3)
        {
            afd = determinisation(afn,equal_st,print_element_in_list);
        }
        else
        {
            afd = epsilone_determinisation(afn, equal_st,print_element_in_list);
        }

        printf("L'AUTOMATE DETERMINISTE CORRESPONDANT EST :\n");

        print_info_AFD(afd, True, print_element_in_list, print_trans_list_info);
        print_AFD(afd, True , print_element_in_list, length_state);

        printf("VOULEZ VOUS VERIFIER SI UN MOT EST RECONNU PAR CET AUTOMATE? :");
        scanf("%d", &user_rep);
        rep = confirm(user_rep);

        if (user_rep == 1)
        {
        READ_WORD3:
            word_list = new_list();
            printf("ENTRER UNE LISTE DE MOTS DONT VOUS VOULEZ RECONNAITRE ENTRER end POUR ARRETER:\n");
            do
            {

                exp = calloc(255, sizeof(char));
                scanf("%s", exp);
                if (strcmp(exp, "end") != 0)
                {
                    //on doit free chaque element de ce tableau
                    word = convert_to_word(exp);
                    head_insertion(word_list, word);
                }
            } while (strcmp(exp, "end") != 0);

            result = detect_word(afd ,True, equal_special_state, equal_label, word_list , print_element_in_list);
            printf("\nL'ENSEMBLE DES MOTS RECONNU PAR CET AUTOMATE SONT :\n");
            print_result(result[0]);
            printf("L'ENSEMBLE DES MOTS QUI NE SONT PAS RECONNUS SONT :\n");
            print_result(result[1]);
            printf("VOULEZ VOUS RECONNAITRE D'AUTRE MOTS ? OUI = 1 NON = 0 ? : ");

            for (i = 0; i < word_list->length; i++)
            {
                free_word(get_element_list(word_list, i));
            }
            free_list(word_list);
            free(result[0]);
            free(result[1]);

            scanf("%d", &user_rep);
            if (confirm(user_rep) == False)
            {
                restart = True;
            }
            else
            {
                restart = False;
            }
        }
        else
        {
            restart = False;
        }

        if (restart == True)
        {
            for (i = 0; i < garbage->length; i++)
            {
                free(get_element_list(garbage, i));
            }
            free_list(garbage);
            free(afd);
            free_AFN(afn);
            goto MENU;
        }
        else
        {
            clearScreen();
            goto READ_WORD3;
        }
        break;
    default:
    READ_AFD:
        cmpt = 0;
        garbage = new_list();
        printf("ENTRER LE NOMBRE D'ETAT : ");
        scanf("%d", &nbr_state);
        printf("ENTRER LE NOMBRE D'ETAT FINAUX : ");
        scanf("%d", &nbr_finale_state);
        printf("ENTRER LE NOMBRE D'ETIQUETTES DIFFERENTS : ");
        scanf("%d", &nbr_label);

        printf("\nENTRER VOS TRANSITIONS EXEMPLE 0,a,1 OU 0 ET 1 SONT DES ETATS ET A UN SYMBOLE\n");
        printf("ENTRER end QUAND VOUS NE VOULEZ ARRETER\n");

        afd = new_AFD(nbr_state, nbr_label, nbr_finale_state);



        do
        {
            exp = calloc(25, sizeof(char));
            scanf("%s", exp);
            if (strcmp(exp, "end") != 0)
            {
                trans = convert_to_transition(exp);

                head_insertion(garbage, trans[0]);
                head_insertion(garbage, trans[1]);
                head_insertion(garbage, trans[2]);
                add_transition_AFD(afd, trans[0], trans[1], trans[2], cmpt);
                cmpt++;
            }
        } while (strcmp(exp, "end") != 0 && cmpt < nbr_label * nbr_state);

        state = calloc(25, sizeof(char));
        printf("ENTRER L'ETAT INTITIALE : ");
        scanf("%s", state);
        afd->initiale_state = state;
        head_insertion(garbage, state);

        for (i = 0; i < nbr_finale_state; i++)
        {
            state = calloc(25, sizeof(char));
            printf("ENTRER L'ETAT FINALE : ");
            scanf("%s", state);
            afd->finale_state[i] = state;
            head_insertion(garbage, state);
        }
        printf("\n");
        print_info_AFD(afd, False, print_element_in_list, print_trans_info);
        printf("CONFIRMER QU'IL S'AGI BIEN DE VOTRE AUTOMATE OUI = 1 NON = 0 : ");
        scanf("%d", &user_rep);
        
        // miroir_AFD(afd);
        // print_transitions_AFD(afd , print_trans_info);

        if (confirm(user_rep) == False)
        {
            for (i = 0; i < garbage->length; i++)
            {
                free(get_element_list(garbage, i));
            }

            free_list(garbage);
            free_AFD(afd , True);
            clearScreen();
            goto READ_AFD;
        }

    READ_WORD4:
        word_list = new_list();
        printf("ENTRER UNE LISTE DE MOTS DONT VOUS VOULEZ RECONNAITRE ENTRER end POUR ARRETER:\n");
        do
        {

            exp = calloc(255, sizeof(char));
            scanf("%s", exp);
            if (strcmp(exp, "end") != 0)
            {
                //on doit free chaque element de ce tableau
                word = convert_to_word(exp);
                head_insertion(word_list, word);
            }
        } while (strcmp(exp, "end") != 0);

        result = detect_word(afd,False ,equal_st, equal_label, word_list , print_element_in_list);
        printf("\nL'ENSEMBLE DES MOTS RECONNU PAR CET AUTOMATE SONT :\n");
        print_result(result[0]);
        printf("L'ENSEMBLE DES MOTS QUI NE SONT PAS RECONNUS SONT :\n");
        print_result(result[1]);
        printf("VOULEZ VOUS RECONNAITRE D'AUTRE MOTS ? OUI = 1 NON = 0 ? : ");

        scanf("%d", &user_rep);
        if (confirm(user_rep) == False)
        {
            restart = True;
        }
        else
        {
            restart = False;
        }

        goto FREE_ALL3;
        //avant de faire ca il faut free tout les elements de cette liste
    FREE_ALL3:
        for (i = 0; i < word_list->length; i++)
        {
            free_word(get_element_list(word_list, i));
        }

        free_list(word_list);
        free(result[0]);
        free(result[1]);
        if (restart == True)
        {
            clearScreen();
            for (i = 0; i < garbage->length; i++)
            {
                free(get_element_list(garbage, i));
            }
            free_list(garbage);
            free(afd);
            goto MENU;
        }
        else
        {
            clearScreen();
            goto READ_WORD4;
        }
        break;
    }
    //user_rep = 0;
    // } while (user_rep == 1);

    return 0;
}