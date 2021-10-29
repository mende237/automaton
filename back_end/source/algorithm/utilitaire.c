#ifndef UTILITAIRE_C
#define UTILITAIRE_C
#include <string.h>
#include "../../header/algorithm/utilitaire.h"
#include "../data_structure/linked_list.c"
#include "../../source/data_structure/stack.c"
#include "AFN.c"
#include "AFD.c"

list convert_inf_to_post(char **expression, int length)
{

    list li_result = new_list();
    stack pile = new_stack();

    int i = 0;
    for (i = 0; i < length; i++)
    {
        char *temp = expression[i];
        if (is_operator(temp) == False)
        {
            if (strcmp("(", temp) != 0 && strcmp(")", temp) != 0)
            {
                queue_insertion(li_result, temp);
            }
            else
            {
                if (strcmp("(", temp) == 0)
                {
                    push(pile, temp);
                }
                else if (strcmp(")", temp) == 0)
                {
                    char *val = pop(pile);
                    while (strcmp("(", val) != 0)
                    {
                        queue_insertion(li_result, val);
                        val = pop(pile);
                    }
                }
            }
        }
        else
        {

            if (is_empty_stack(pile) == True)
            {
                push(pile, temp);
            }
            else
            {
                boolean enter = False;
                char *old_operator = pop(pile);
                while (priority(old_operator) >= priority(temp))
                {
                    enter = True;
                    queue_insertion(li_result, old_operator);
                    if (is_empty_stack(pile) == False)
                    {
                        old_operator = pop(pile);
                        enter = False;
                    }
                    else
                    {
                        break;
                    }
                }
                if (enter == False)
                {
                    push(pile, old_operator);
                }
                push(pile, temp);
            }
        }
    }

    while (is_empty_stack(pile) == False)
    {
        queue_insertion(li_result, pop(pile));
    }
    return li_result;
}

boolean or (boolean val1, boolean val2)
{
    if (val1 == False && val2 == False)
    {
        return False;
    }

    return True;
}

boolean is_operator(const char *val)
{
    if (strcmp(".", val) == 0 || strcmp("+", val) == 0 || strcmp("*", val) == 0)
    {
        return True;
    }

    return False;
}

int priority(const char *x)
{
    if (strcmp("(", x) == 0)
    {
        return 0;
    }
    else if (strcmp("+", x) == 0)
    {
        return 1;
    }
    else if (strcmp(".", x) == 0)
    {
        return 2;
    }
    else if (strcmp("*", x) == 0)
    {
        return 3;
    }
}

int get_nbre_state(char **expression, int length)
{
    int i = 0;
    int cmpt = 0;
    for (i = 0; i < length; i++)
    {
        if (strcmp(".", expression[i]) != 0)
        {
            cmpt++;
        }
    }
    return 2 * cmpt;
}

int get_nbre_label(char **expression, int length)
{
    char **tab = malloc(length * sizeof(char *));
    int i = 0, j = 0;
    int cmpt = 0;
    boolean find = False;
    for (i = 0; i < length; i++)
    {
        if (strcmp("ep", expression[i]) != 0 && is_operator(expression[i]) == False)
        {
            for (j = 0; j < cmpt; j++)
            {
                if (strcmp(expression[i], tab[j]) == 0)
                {
                    find = True;
                    break;
                }
            }
            if (find == False)
            {
                tab[cmpt] = expression[i];
                cmpt++;
            }
        }
        find = False;
    }
    return cmpt;
}

list *detect_word(AFD afd, boolean sp_st , list word_list, void print_state(void *x, boolean l))
{
    int i = 0, length = 0;
    boolean verdit;
    list *result = calloc(2, sizeof(list));
    result[0] = new_list();
    result[1] = new_list();
    for (i = 0; i < word_list->length; i++)
    {
        char **word = get_element_list(word_list, i);
        length = calculate_length(word);

        //if(sp_state == False){
        verdit = detect(afd, word, length);
        // }else{
        //     verdit = detect(afd, word, length, equal_special_state, equal_label);
        // }

        if (verdit == True)
        {
            head_insertion(result[0], word);
        }
        else
        {
            head_insertion(result[1], word);
        }
    }
    return result;
}

int calculate_length(char **word)
{
    int i = 0;
    while (word[i] != NULL)
    {
        i++;
    }
    return i;
}

char **convert_to_word(char *word)
{
    int i = 0;
    list tmp = new_list();
    char **main_word = NULL;
    for (i = 0; word[i] != '\0'; i++)
    {
        char *c = calloc(1, sizeof(char));
        *c = word[i];
        queue_insertion(tmp, c);
    }

    main_word = calloc(tmp->length + 1, sizeof(char *));

    for (i = 0; i < tmp->length; i++)
    {
        char *c = get_element_list(tmp, i);
        main_word[i] = c;
    }
    free(tmp);

    return main_word;
}

AFD convert_file_to_AFD(char *path, list garbage)
{

    int nbr_state;
    int nbr_final_state;
    int nbr_label;
    char *initial_state;
    char **final_state;
    char **trans_temp = calloc(3, sizeof(char *));
    AFD afd;

    char *state = NULL;
    char *str = NULL;

    signed char line[256];

    int cmpt = 0;
    char *token = NULL;
    int i = 0, j = 0;
    boolean exist = False;

    FILE *file = fopen(path, "r");
    if (file == NULL)
        exit(1);

    while (fgets(line, 255, file) != NULL)
    {
        line[strlen(line) - 1] = '\0';
        switch (cmpt)
        {
        case 0:
            nbr_state = atoi(line);
            break;
        case 1:
            nbr_final_state = atoi(line);
            break;
        case 2:
            nbr_label = atoi(line);
            break;
        case 3:
            initial_state = calloc(strlen(line), (sizeof(char)));
            strcpy(initial_state, line);
            break;
        case 4:
            final_state = calloc(nbr_final_state, sizeof(void *));
            token = strtok(line, ",");
            for (i = 0; token != NULL; i++)
            {
                state = calloc(strlen(token), sizeof(char));
                strcpy(state, token);
                final_state[i] = state;
                token = strtok(NULL, ",");
                head_insertion(garbage, state);
            }
            break;
        default:
            if (exist == False)
            {
                afd = new_AFD(nbr_state, nbr_final_state, nbr_label);
            }

            token = strtok(line, ",");
            for (i = 0; token != NULL; i++)
            {
                str = calloc(strlen(token), sizeof(char));
                strcpy(str, token);
                trans_temp[i] = str;

                head_insertion(garbage, str);
                token = strtok(NULL, ",");
            }

            add_transition_AFD(afd, trans_temp[0], trans_temp[1], trans_temp[2], j);
            j++;
            exist = True;
            break;
        }
        cmpt++;
    }

    afd->initiale_state = initial_state;
    for (i = 0; i < nbr_final_state; i++)
    {
        afd->finale_state[i] = final_state[i];
    }

    free(final_state);
    free(trans_temp);
    fclose(file);
    return afd;
}

AFN convert_file_to_AFN(char *path, list garbage)
{
    int nbr_state;
    int nbr_initial_state;
    int nbr_final_state;
    int nbr_label;
    void **initial_state;
    void **final_state;
    char *epsilone;
    char **trans_temp = calloc(3, sizeof(char *));
    AFN afn;

    char *state = NULL;
    char *str = NULL;

    signed char line[256];

    int cmpt = 0;
    char *token = NULL;
    int i = 0;
    boolean exist = False;

    FILE *file = fopen(path, "r");
    if (file == NULL)
        exit(1);

    while (fgets(line, 255, file) != NULL)
    {
        line[strlen(line) - 1] = '\0';
        switch (cmpt)
        {
        case 0:
            nbr_state = atoi(line);
            break;
        case 1:
            nbr_initial_state = atoi(line);
            break;
        case 2:
            nbr_final_state = atoi(line);
            break;
        case 3:
            nbr_label = atoi(line);
            break;
        case 4:
            epsilone = line;
            break;
        case 5:
            initial_state = calloc(nbr_initial_state, sizeof(void *));
            token = strtok(line, ",");
            for (i = 0; token != NULL; i++)
            {

                state = calloc(strlen(token), sizeof(char));
                strcpy(state, token);
                initial_state[i] = state;
                token = strtok(NULL, ",");
                head_insertion(garbage, state);
            }
            break;
        case 6:
            final_state = calloc(nbr_final_state, sizeof(void *));
            token = strtok(line, ",");
            for (i = 0; token != NULL; i++)
            {
                state = calloc(strlen(token), sizeof(char));
                strcpy(state, token);
                final_state[i] = state;
                //printf("%s" , state);
                token = strtok(NULL, ",");
                head_insertion(garbage, state);
            }
            break;
        default:
            if (exist == False)
            {
                afn = new_AFN(nbr_state, nbr_initial_state, nbr_final_state, nbr_label, epsilone);
            }

            token = strtok(line, ",");
            for (i = 0; token != NULL; i++)
            {
                str = calloc(strlen(token), sizeof(char));
                strcpy(str, token);
                trans_temp[i] = str;

                head_insertion(garbage, str);
                token = strtok(NULL, ",");
            }

            add_transition_AFN(afn, trans_temp[0], trans_temp[1], trans_temp[2]);
            exist = True;
            break;
        }
        cmpt++;
    }

    for (i = 0; i < nbr_initial_state; i++)
    {
        afn->initiale_state[i] = initial_state[i];
    }

    for (i = 0; i < nbr_final_state; i++)
    {
        afn->finale_state[i] = final_state[i];
    }

    free(final_state);
    free(initial_state);
    free(trans_temp);

    fclose(file);
    return afn;
}

/*cette fonction permet de lire l'expression reguliere que l'utilisateur va entre
au clavier*/
list read_expression(int length ,char *path, boolean from_file)
{
    int i = 0, j = 0;
    char *exp = NULL;
    list expression_list = new_list();
    list operande = new_list();
    boolean was_operator = False;

    if(from_file == True){
        FILE *file = fopen(path , "r");
        char *line = calloc(length , sizeof(char));
        fgets(line , length - 1 , file);
        line[strlen(line) - 1] = '\0';
        printf("%s\n" , line);
        exp = calloc(strlen(line) , sizeof(char));
        strcpy(exp , line);
        fclose(file);
    }else{
        scanf("%s", exp);
    }


    for (i = 0; exp[i] != '\0'; i++)
    {
        if (exp[i] == '+' || exp[i] == '.' || exp[i] == '*' || exp[i] == '(' || exp[i] == ')')
        {
            was_operator = True;
            char *tmp = calloc(operande->length, sizeof(char));

            for (j = 0; j < operande->length; j++)
            {
                char *tmp_ch = get_element_list(operande, j);
                tmp[j] = *tmp_ch;
                free(tmp_ch);
                tmp_ch = NULL;
            }

            if (is_empty_list(operande) == False)
            {
                queue_insertion(expression_list, tmp);
            }

            char *c = calloc(1, sizeof(char));
            *c = exp[i];

            free_list(operande);
            operande = new_list();
            queue_insertion(operande, c);
        }
        else
        {
            char *c = calloc(1, sizeof(char));
            if (was_operator == False)
            {
                *c = exp[i];
                queue_insertion(operande, c);
            }
            else
            {
                char *tmp = calloc((operande->length), sizeof(char));
                int len_op = operande->length;
                for (j = 0; j < operande->length; j++)
                {
                    char *tmp_ch = get_element_list(operande, j);
                    tmp[j] = *tmp_ch;
                    free(tmp_ch);
                    tmp_ch = NULL;
                }

                queue_insertion(expression_list, tmp);
                free_list(operande);
                operande = new_list();
                *c = exp[i];
                queue_insertion(operande, c);
            }
            was_operator = False;
        }
    }

    char *tmp = calloc(operande->length, sizeof(char));
    for (j = 0; j < operande->length; j++)
    {
        char *tmp_ch = get_element_list(operande, j);
        tmp[j] = *tmp_ch;
        free(tmp_ch);
        tmp_ch = NULL;
    }

    queue_insertion(expression_list, tmp);
    free(exp);
    exp = NULL;
    free_list(operande);
    return expression_list;
}

char **convert_to_transition(char *exp)
{
    int i = 0, j = 0, cmpt = 0;
    char *temp;
    list temp_list = new_list();

    char **trans = calloc(3, sizeof(char *));
    for (i = 0; exp[i] != '\0'; i++)
    {
        if (exp[i] == ',')
        {
            temp = calloc(temp_list->length, sizeof(char));
            for (j = 0; j < temp_list->length; j++)
            {
                char *t = get_element_list(temp_list, j);
                temp[j] = *t;
                free(t);
            }
            trans[cmpt] = temp;
            cmpt++;
            free_list(temp_list);
            temp_list = new_list();
        }
        else
        {
            char *c = calloc(1, sizeof(char));
            *c = exp[i];
            queue_insertion(temp_list, c);
        }
    }

    temp = calloc(temp_list->length, sizeof(char));
    for (j = 0; j < temp_list->length; j++)
    {
        char *t = get_element_list(temp_list, j);
        temp[j] = *t;
        free(t);
    }
    trans[cmpt] = temp;
    free_list(temp_list);
    // for ( i = 0; i < 3; i++)
    // {
    //     printf("%s " , trans[i]);
    // }

    return trans;
}

char *concat(char *ch1, char *ch2, int length1, int length2)
{
    char *ch_result = malloc((length1 + length2) * sizeof(char));
    strcat(ch_result, ch1);
    strcat(ch_result, ch2);

    return ch_result;
}

void free_elem_in_list(list li)
{
    int i = 0;
    if (li != NULL)
    {
        for (i = 0; i < li->length; i++)
        {
            char *tmp = get_element_list(li, i);
            //printf("suppression de %s" , tmp);
            free(tmp);
            tmp = NULL;
        }
    }
}

void free_word(char **word)
{
    int i = 0;
    int length = calculate_length(word);
    for (i = 0; i < length + 1; i++)
    {
        free(word[i]);
    }
    free(word);
}

void print_result(list li)
{
    int i = 0, length = 0, j = 0;
    if (li->length == 0)
    {
        printf("vide!!\n");
    }

    for (i = 0; i < li->length; i++)
    {
        char **temp = get_element_list(li, i);
        length = calculate_length(temp);
        for (j = 0; j < length; j++)
        {
            printf("%s", temp[j]);
        }
        printf("\n");
    }
}

void print_info_AFN(AFN afn, void print_info(void *src, void *lbl, void *dest))
{
    int i = 0;
    printf("\nle nombre d'etat est : %d\n", afn->nbre_state);

    if (afn->nbre_initiale_state == 1)
    {
        char *initial = afn->initiale_state[0];
        printf("l'etat initiale est : %s\n", initial);
    }
    else
    {
        printf("le nombre d'etat initiaux %d\n", afn->nbre_initiale_state);
    }

    if (afn->nbre_finale_state == 1)
    {
        char *final = afn->finale_state[0];
        printf("l'etat finale est : %s\n", final);
    }
    else
    {
        printf("l'ensemble des etats finaux est : {");
        for (i = 0; i < afn->nbre_finale_state; i++)
        {
            char *tmp_final = afn->finale_state[i];
            if (i < afn->nbre_finale_state - 1)
            {
                printf("%s , ", tmp_final);
            }
            else
            {
                printf("%s", tmp_final);
            }
        }
        printf("}\n");
    }
    
    printf("les differentes transitions sont :\n\n");
    print_transitions_AFN(afn, print_info);
}

void print_info_AFD(AFD afd, boolean state_list, void print_value_list(void *, boolean last))
{
    int i = 0;
    if (state_list == False)
    {
        char *initial = afd->initiale_state;
        //char **finale_state = afc->finale_state;
        printf("l'etat initiale est : %s\n", initial);
        printf("l'ensemble des etat finaux est : {");
        for (i = 0; i < afd->nbre_finale_state; i++)
        {
            char *tmp_final = afd->finale_state[i];
            printf("%s", tmp_final);
            if (i < afd->nbre_finale_state - 1)
            {
                printf(" , ");
            }
        }
        printf("}\n");
    }
    else
    {
        printf("\n");
        list initial = afd->initiale_state;
        // list finale_state = new_list();
        // for (i = 0; i < afd->nbre_finale_state; i++)
        // {
        //     head_insertion(finale_state, afd->finale_state[i]);
        // }

        printf("l'etat initiale est : ");
        print_list(initial, print_value_list);
        printf("\n");

        printf("l'ensemble des etat finaux est : { ");
        for (i = 0; i < afd->nbre_finale_state; i++)
        {
            print_list(afd->finale_state[i] , print_value_list);
            if (i < afd->nbre_finale_state - 1)
            {
                printf(" , ");
            }
        }

        printf("}\n");
    }
    printf("la table de transition est :\n\n");
    //print_transitions_AFD(afd, print_trans);
}

#endif