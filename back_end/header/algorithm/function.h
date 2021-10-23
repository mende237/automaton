#ifndef FUNCTION_H
#define FUNCTION_H
#include "../data_structure/structure.h"

boolean search_state(void **state_tab, void *state, int n);
/*cette fonction permet de rechercher un etat dans la sous forme de liste dans un tableau formé 
d'etat sous forme de liste et la fonction equal_value permet de dire quand deux elements des listes 
differentes sont egaux */
static boolean search_state_list(void **state_tab, list state, int n, int permut);
/*cette fonction donne le nouvelle etat ou l'on doit aller quittant d'un etat */
void **delta_global_automate(void *automate, list state, boolean is_AFD, boolean equal_label(void *lb1, void *lb2));

#endif