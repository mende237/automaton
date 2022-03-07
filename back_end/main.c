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
#include "./source/algorithm/brzozowski.c"
#include "./source/algorithm/hopcroft.c"

#include "./source/inputOutput/messenger.c"
#include "./source/inputOutput/configuration.c"

char **add_data(int n, ...);
void clearScreen();
void print_label(void *val);
void print_element_in_list(void *x, boolean last);
void print_tree_info(void *x);
void print_trans_info(void *src, void *lbl, void *dest);
void print_trans_list_info(void *src, void *lbl, void *dest);
int length_state(void *x, boolean is_state_list);
int length_label(void *c);

boolean confirm_expression(char **reg_expression, int length);
boolean confirm(int user_rep);
boolean equal_special_state(void *st1, void *st2);
boolean equal_label(void *lbl1, void *lbl2, ...);
boolean equal_st(void *st1, void *st2, ...);

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
                if (tmp != NULL)
                {
                    length += strlen(tmp);
                }
                else
                {
                    length += 4;
                }
            }

            length += li->length + 1;
            return length;
        }
        return 4;
    }

    char *str = x;
    return strlen(x) + 2;
}

int length_label(void *c)
{
    char *lbl = c;
    return strlen(lbl);
}

void print_label(void *val)
{
    char *ch = val;
    printf("%s", ch);
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
    if (value == NULL)
    {
        value = "vide";
    }

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

boolean equal_st(void *st1, void *st2, ...)
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

boolean equal_label(void *lbl1, void *lbl2, ...)
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
    char *test1 = calloc(6 , sizeof(char));
    test1[0] = 'a';
    test1[1] = 'b';
    test1[2] = 'c';
    test1[3] = 'd';
    test1[4] = 'e';
    printf("before %s %d\n",test1 , (int)strlen(test1));
    test1[strlen(test1)-1] = '\0';
    test1[0] = '\n';
    printf("after  %s %d\n", test1, (int)strlen(test1));

    //test1 += 1;
    // free(test1);
    // char *test = calloc((strlen(test1)+5) , sizeof(char));
    // strcpy(test , test1);
    // strcat(test, " adad");
    // free(test1);
    // printf("%s\n" , test);

    int user_rep = 12;
    scanf("%d" , &user_rep);
    printf("user rep %d\n", user_rep);
    boolean restart = False;
    boolean rep = False;

    int length = 0;
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
    char *path = NULL;
    char *well_state;
    char *word_path = NULL;

    list expression_list = NULL;
    list garbage = NULL;
    list word_list = NULL;
    list *result = NULL;
    list list_path = NULL;
    boolean is_afd = True;

    AFD afd = NULL;
    AFN afn = NULL;

    AFD afd_result = NULL;
    AFN afn_result = NULL;
    AFD old_afd = NULL;

    switch (user_rep)
    {
    case 1:
        garbage = new_list();
        expression_list = read_expression(255, "/home/dimitri/Bureau/expression.txt", True);
        
        //reg_expression = malloc(expression_list->length * sizeof(char *));

        for (i = 0; i < expression_list->length; i++)
        {
            //reg_expression[i] = get_element_list(expression_list, i);
            printf("%s", (char *)get_element_list(expression_list, i));
        }

        // afn = glushkov_algorithm(reg_expression, expression_list->length, garbage);
        // print_info_AFN(afn, print_trans_info);

        // AFN_to_jason(afn, "afn.json");
        // afd = determinisation(afn, equal_st);

        // afd = rename_states(afd, True);

        // AFD_to_jason(afd, "afd.json");

        // print_info_AFD(afd, False, print_element_in_list);
        // print_AFD(afd, False, False, print_element_in_list, length_state);
        // free_AFD(afd, False);
        // free_elem_in_list(expression_list);
        // free_list(expression_list);
        // free(reg_expression);
        // free_AFN(afn);
        break;
    case 2:
        garbage = new_list();
        expression_list = read_expression(255, "/home/dimitri/Bureau/expression.txt", True);

        reg_expression = malloc(expression_list->length * sizeof(char *));

        for (i = 0; i < expression_list->length; i++)
        {
            reg_expression[i] = get_element_list(expression_list, i);
        }

        afn = thomson_algorithm(reg_expression, expression_list->length, garbage);
        print_info_AFN(afn, print_trans_info);
        AFN_to_jason(afn, "afn.json");

        afd = epsilone_determinisation(afn, equal_st, print_element_in_list);

        afd = rename_states(afd, True);
        AFD_to_jason(afd, "afd.json");

        print_info_AFD(afd, False, print_element_in_list);
        print_AFD(afd, False, False, print_element_in_list, length_state);
        free_AFD(afd, False);
        free_elem_in_list(expression_list);
        free_list(expression_list);
        free(reg_expression);
        free_AFN(afn);
        break;
    case 3:
        garbage = new_list();
        afd = jason_to_AFD("afd.json", garbage);

        // print_info_AFD(afd, False, print_element_in_list);
        // print_transitions_AFD(afd, print_trans_info);

        exp = brzozowski_AFD_to_REG(afd);
        free_AFD(afd, False);
        free(exp);
        break;
    case 4:
        garbage = new_list();
        afd = jason_to_AFD("afd.json", garbage);
        print_info_AFD(afd, False, print_element_in_list);
        print_transitions_AFD(afd, print_trans_info);
        afd_result = hopcroft_minimisation(afd, equal_label, print_element_in_list);

        afd_result = rename_states(afd_result, True);
        print_info_AFD(afd_result, False, print_element_in_list);
        print_AFD(afd_result, False, False, print_element_in_list, length_state);
        AFD_to_jason(afd_result, "afd_min.json");

        free_AFD(afd, False);
        free_AFD(afd_result, False);
        break;
    case 5:
        garbage = new_list();
        afd = jason_to_AFD("afd.json", garbage);
        print_info_AFD(afd, False, print_element_in_list);
        print_transitions_AFD(afd, print_trans_info);
        afd_result = brzozowski_minimisation(afd, equal_label);

        print_info_AFD(afd_result, False, print_element_in_list);
        print_AFD(afd_result, False, False, print_element_in_list, length_state);
        AFD_to_jason(afd_result, "afd_min.json");
        free_AFD(afd, False);
        free_AFD(afd_result, False);
        break;
    case 6:
    case 7:
        garbage = new_list();
        // afn = convert_file_to_AFN(path, garbage);
        afn = jason_to_AFN("afn.json", garbage);
        print_info_AFN(afn, print_trans_info);
        if (user_rep == 6)
        {
            // afd = rename_states(afd, True);
            afd = determinisation(afn, equal_st);
        }
        else
        {
            afd = epsilone_determinisation(afn, equal_st, print_element_in_list);
        }

        print_info_AFD(afd, True, print_element_in_list);
        print_AFD(afd, True, False, print_element_in_list, length_state);

        afd = rename_states(afd, True);
        AFD_to_jason(afd, "afd.json");
        printf("L'AUTOMATE DETERMINISTE CORRESPONDANT EST :\n");

        print_info_AFD(afd, False, print_element_in_list);
        print_AFD(afd, False, False, print_element_in_list, length_state);

        free_AFD(afd, False);
        free_AFN(afn);
        break;
    case 8:
    case 9:
        garbage = new_list();
        int nbr_automate = 3;
        char **tab_path = calloc(nbr_automate, sizeof(char *));
        tab_path[0] = "/home/dimitri/Bureau/afd1.txt";
        tab_path[1] = "/home/dimitri/Bureau/afd_test.txt";
        tab_path[2] = "/home/dimitri/Bureau/afd2.txt";

        boolean possible = True;
        AFD *afd_tab = calloc(nbr_automate, sizeof(AFD));

        for (i = 0; i < nbr_automate; i++)
        {
            afd_tab[i] = convert_file_to_AFD(tab_path[i], garbage);
        }

        if (user_rep == 8)
        {
            afd_result = union_AFD(afd_tab[0], afd_tab[1], print_element_in_list);

            // print_info_AFD(afd_result, True, print_element_in_list);
            // print_AFD(afd_result, True, True, print_element_in_list, length_state);

            afd_result = rename_states(afd_result, False);
            // print_info_AFD(afd_result, False, print_element_in_list);
            // print_AFD(afd_result, False, False, print_element_in_list, length_state);

            for (i = 0; i < afd_result->nbre_state; i++)
            {
                queue_insertion(garbage, afd_result->state_tab[i]);
            }

            old_afd = afd_result;

            for (i = 2; i < nbr_automate; i++)
            {
                afd_result = union_AFD(old_afd, afd_tab[i], print_element_in_list);
                afd_result = rename_states(afd_result, False);
                for (i = 0; i < afd_result->nbre_state + 1; i++)
                {
                    queue_insertion(garbage, afd_result->state_tab[i]);
                }

                free_AFD(old_afd, False);
                old_afd = afd_result;
            }
        }
        else
        {
            afd_result = intersection_AFD(afd_tab[0], afd_tab[1], print_element_in_list);
            if (afd_result->nbre_finale_state > 0)
            {
                afd_result = rename_states(afd_result, False);

                for (i = 0; i < afd_result->nbre_state; i++)
                {
                    queue_insertion(garbage, afd_result->state_tab[i]);
                }
            }
            else
            {
                possible = False;
            }

            if (possible == True)
            {
                old_afd = afd_result;
                for (i = 2; i < nbr_automate; i++)
                {
                    afd_result = intersection_AFD(old_afd, afd_tab[i], print_element_in_list);
                    if (afd_result->nbre_finale_state > 0)
                    {
                        afd_result = rename_states(afd_result, False);

                        for (i = 0; i < afd_result->nbre_state + 1; i++)
                        {
                            queue_insertion(garbage, afd_result->state_tab[i]);
                        }
                        free_AFD(old_afd, False);
                        old_afd = afd_result;
                    }
                    else
                    {
                        possible = False;
                        free_AFD(afd_result, True);
                        break;
                    }
                }
            }
        }

        if (possible == True)
        {
            print_info_AFD(afd_result, False, print_element_in_list);
            print_AFD(afd_result, False, False, print_element_in_list, length_state);
        }

        for (i = 0; i < nbr_automate; i++)
        {
            free_AFD(afd_tab[i], False);
        }

        free_AFD(afd_result, False);
        free(afd_tab);
        break;
    case 10:
        well_state = "puit";
        garbage = new_list();

        afd = jason_to_AFD("afd.json", garbage);
        completer_AFD(afd, well_state, equal_st);
        print_info_AFD(afd, False, print_element_in_list);
        print_AFD(afd, False, False, print_element_in_list, length_state);
        AFD_to_jason(afd, "afd.json");
        free_AFD(afd, False);
        break;
    case 11:
        well_state = "puit";
        garbage = new_list();
        afd = jason_to_AFD("afd.json", garbage);
        afd_result = complementaire_AFD(afd, well_state, equal_st);

        print_info_AFD(afd_result, False, print_element_in_list);
        print_AFD(afd_result, False, False, print_element_in_list, length_state);

        AFD_to_jason(afd_result, "afd.json");
        free_AFD(afd_result, False);
        free_AFD(afd, False);
        break;
    case 12:
    case 13:
        garbage = new_list();
        if (user_rep == 12)
        {
            afd = jason_to_AFD("afd.json", garbage);
            print_info_AFD(afd, False, print_element_in_list);
            print_transitions_AFD(afd, print_trans_info);

            afn_result = miroir_AFD(afd);
            free_AFD(afd, False);
        }
        else
        {
            afn = jason_to_AFN(messenger->message.dataPath, garbage);
            print_info_AFN(afn, print_trans_info);
            afn_result = miroir_AFN(afn);
            free_AFN(afn);
        }

        AFN_to_jason(afn_result, "afn.json");
        print_info_AFN(afn_result, print_trans_info);
        free_AFN(afn_result);
        break;
    case 14:
    case 15:
        garbage = new_list();
        word = jason_to_word("word.json");
        if (user_rep == 14)
        {
            afd = jason_to_AFD("afd_min.json", garbage);
            print_info_AFD(afd, False, print_element_in_list);
            print_transitions_AFD(afd, print_trans_info);
            list_path = detect_AFD(afd, word, calculate_length(word));
            
            for (i = 0; i < list_path->length; i++)
            {
                list temp_list = get_element_list(list_path, i);
                print_list(temp_list, print_element_in_list);
                printf("\n");
            }
        }
        else
        {
            afn = jason_to_AFN("afn.json", garbage);
            print_info_AFN(afn, print_trans_info);
            list_path = detect_AFN(afn, word, calculate_length(word));

            for (i = 0; i < list_path->length; i++)
            {
                list temp_list = get_element_list(list_path, i);
                print_list(temp_list, print_element_in_list);
                printf("\n");
            }
        }

        path_to_jason(list_path, "test.json");

        for (i = 0; i < list_path->length; i++)
        {
            free_list(get_element_list(list_path, i));
        }
        free_list(list_path);
        free(path);
        free(word_path);
        free_word(word);
        break;
    default:
        user_rep = -1;
        break;
    }

    for (i = 0; i < garbage->length; i++)
    {
        //printf("%s \n" , (char*)get_element_list(garbage, i));
        free(get_element_list(garbage, i));
    }
    free_list(garbage);

    return EXIT_SUCCESS;
}