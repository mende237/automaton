#ifndef AFD_H
#define AFD_H
#include "AFN.h"
#include "../data_structure/linked_list.h"

typedef struct AFD
{
    int nbre_state;
    int nbre_label;
    int nbre_finale_state;
    void ***mat_trans;
    void ***mat_state;
    void **state_tab;
    void *initiale_state;
    void **finale_state;
    void **tab_labels;
    int cmpt_label;
} * AFD, AFD_value;

/*permet de créer un nouveau automate */
AFD new_AFD(int nbre_state, int nbre_finale_state, int nbre_label);
/*cette fonction permet d'evaluer si un mot donné appartient est reconnue par un automate 
elle prend en paramettre la fonction  equal_state qui permet de dire quand deux etats
sont egaux , la fonction equal_lable qui joue presque le meme role que la fonction equal_state , elle
permet de dire lorsque deux ettiquettes sont egaux*/
list detect_AFD(AFD afd, void *word, int size);

// static void **delta_global_AFN(AFN afn, list state, boolean equal_value(void *lb1, void *lb2));

static void **delta_global_AFD(AFD afd, void *state);

void *delta_AFD(AFD afd, void *state, void *symbole);
/*cette fonction permet d'ajouter une transition a l'automate elle prend en paramettre l'automate 
sur lequel on veut ajouter la transition la source l'etiquette la destination 
et la fonction equal_label qui permet de dire lorsque deux etiquettes sont egales*/
void add_transition_AFD(AFD afd, void *begin, void *label, void *end, int index);
/**/
static list *union_trans(AFD afd1, AFD afd2, void **trans1, void **trans2, void **tab_label, int nbre_label, void print_value(void *value, boolean last));

static void **delta_global_AFD(AFD afn, void *state);

AFD union_AFD(AFD afd1, AFD afd2, void print_value(void *x, boolean last));

AFD intersection_AFD(AFD afd1, AFD afd2, void print_value(void *x, boolean last));

void completer_AFD(AFD afd, char *well_state, boolean equal_value(void *lb1, void *lb2 , ...));

AFD complementaire_AFD(AFD afd, char *well_state, boolean equal_value(void *lb1, void *lb2 , ...));

AFN miroir_AFD(AFD afd);
/*cette fonction renome tous les etats de l'AFD et supprime l'ancien AFD*/
AFD rename_states(AFD afd, boolean permut);

/*cette fonction permet de faire l'union de deux ensemble sous forme de liste 
chainé elle prend en paramettre les deux ensembles li1 et li2 ; et une fonction
permettant de dire quand est ce que deux elements contenu dans les deux listes 
sont égaux*/
static list union_set(const list li1, const list li2);

static void **product_AFD(AFD afd1, AFD afd2, void print_value(void *x, boolean last));
/*cette fonction prend en paramettre un automate non-deterministe sans epsilone transition
, avec un unique etat initiale retourne un automate deterministe 
elle prend aussi en paremettre d'autres fonctions comme equal_state qui permet de dire quand deux etats de
sont egaux , la fonction equal_lable qui joue presque le meme role que la fonction equal_state , elle
permet de dire lorsque deux ettiquettes sont egaux*/
AFD determinisation(AFN afn, boolean equal_label(void *lb1, void *lb2 , ...));
/*cette fonction prend en paramettre un automate non-deterministe avec epsilone transition
, avec un unique etat initiale retourne un automate deterministe 
elle prend aussi en paremettre d'autres fonctions comme equal_state qui permet de dire quand deux etats de
sont egaux , la fonction equal_lable qui joue presque le meme role que la fonction equal_state , elle
permet de dire lorsque deux ettiquettes sont egaux*/
AFD epsilone_determinisation(AFN afn, boolean equal_label(void *lb1, void *lb2 , ...), void print_value(void *x, boolean last));
/*cette fonction permet de donner tous les etats partant d'un etat donné que l'on peut atteindre en ne lisant que 
les epsilone*/
list epsilone_transition(AFN afn, void *state, void print_value(void *x, boolean last));
/*cette fonction retourne l'epsilone fermerture d'un etat donné*/
list epsilone_closure(AFN afn, void *state, void print_value(void *x, boolean last));
/*cette fonction retourne l'epsilone fermerture d'un ensemble d'etat donné*/
list epsilone_closure_set(AFN afn, list set_state, void print_value(void *x, boolean last));

void print_transitions_AFD(AFD afd, void print_elem(void *begin, void *label, void *end));
/*cette fonction permet d'afficher la table de transition d'un afd elle prend en parametre une fonction print_state
qui permet d'afficher un etat sous forme de liste , la fonction print_label permet d'afficher une etiquette d'un automate
la fonction length_state permet de calculer le nombre de caractere qu'un etat occupe si l'on l'affiche en console et en fin 
la fonction length_label permet de de calculer le nombre de caractere qu'une etiquette occupe si l'on l'affiche en console*/
void print_AFD(AFD afd, boolean is_state_list, boolean is_special_state, void print_state(void *x, boolean last), int length_state(void *x, boolean is_state_list));


void AFD_to_jason(AFD afd, char *path);

AFD jason_to_AFD(char *path, list garbage);

/*libere la memoire*/
void free_AFD(AFD afd, boolean is_state_list);

#endif