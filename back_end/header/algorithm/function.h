#ifndef FUNCTION_H
#define FUNCTION_H
#include "../data_structure/structure.h"
#include "../data_structure/linked_list.h"

/*cette fonction permet de verifier si deux etats sous forme de liste sont egaux
elle prend en parametre la fonction equal_value qui permet de dire quand deux elements
de ces etats la sont egaux.deux etats sont egaux dans ce cas lorsque tous les elements 
de chaque etats sont egaux . si permut = 1 deux les element constituants ces deux etats doivent 
etre egaux dans l'ordre*/
boolean equal_state(void *st1, void *st2 , ...);

boolean search_state(void **state_tab, void *state, int n);
/*cette fonction permet de rechercher un etat dans la sous forme de liste dans un tableau formé 
d'etat sous forme de liste et la fonction equal_value permet de dire quand deux elements des listes 
differentes sont egaux */
boolean search_state_list(void **state_tab, list state, int n, int permut);

boolean search_state_list_in_list(list list_state, list state , int permut);

boolean is_well_state_in_AFD(AFD afd);

/*cette fonction donne le nouvelle etat ou l'on doit aller quittant d'un etat */
void **delta_global_automate(void *automate, list state, boolean is_AFD, boolean equal_label(void *lb1, void *lb2 , ...));

#endif