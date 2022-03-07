#ifndef UTILITAIRE_H
#define UTILITAIRE_H
#include "../data_structure/linked_list.h"
#include "AFN.h"
#include "AFD.h"
#include "../data_structure/structure.h"

boolean or(boolean val1 , boolean val2);
list convert_inf_to_post(char **expression, int length);
int priority(const char *x);

//NB les expressions sont en representation posfixe
//determine le nombre d'etat de l'automate de thomson associe a l'expression
//reguliere
int get_nbre_state(char **expression, int length);
//determine le nombre d'etiquette de l'automate associe a l'expression 
//reguliere 
int get_nbre_label(char **expression, int length);

boolean is_operator(const char *val);

list read_expression(int length ,char *path, boolean from_file);

/*cette fonction permet de lire un AFN stocké dans un 
fichier et le stocké en memoire dans une structure AFN*/
AFN convert_file_to_AFN(char *path , list garbage);
AFD convert_file_to_AFD(char *path, list garbage);

list *detect_word(AFD afd, boolean sp_st , list word_list, void print_state(void *x, boolean l));
int calculate_length(char **word);
char** convert_to_transition(char* exp);
char** jason_to_word(char* path);
char** convert_to_word(char *word);
char* concat(char *ch1 , char *ch2 , int nbr1 , int nbr2);


AFN read_AFN(char *path);
void free_word(char** word);
void free_elem_in_list(list li);
void path_to_jason(list path_list, char *road);

void print_result(list li);
void print_info_AFN(AFN afn, void print_info(void *src, void *lbl, void *dest));
void print_info_AFD(AFD afc, boolean state_list, void print_value_list(void * , boolean last));



#endif 