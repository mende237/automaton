#ifndef BRZOZOWSKI_H
#define BRZOZOWSKI_H

#include "./AFN.h"
#include "./AFD.h"

struct state
{
    char *loop;
    char *value;
};

struct pseudo_transition
{
    struct state *st;
    char *lbl;
};

AFD brzozowski_minimisation(AFD afd, boolean equal_value(void *st1, void *st2));

static boolean equal_struct_state(void *st1, void *value_st2);

static list *generate_mat_trans(AFD afd);

static list generate_follow(list mat_trans, struct state *st);

static list generate_precedent(list mat_trans, struct state *st);

static list generate_new_trans(list mat_trans, struct state *st, list garbage);

static void delete_loop(list mat_trans);

static void delete_redondance(list mat_trans);

static void **copy_data_list_to_tab(const list li);

/*cette fonction retourne l'expresion reguliere associe au langage reconnue par
l'AFD passe en parametre*/
char *brzozowski_AFD_to_REG(AFD afd);

#endif