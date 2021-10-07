#include "../data_structure/linked_list.h"
#include "../data_structure/stack.h"
#include "../algorithm/AFD.h"

struct Breaker{
    list set;
    char *label;
};

void* break_block(list state , struct Breaker breaker);

list union_set(list state1 , list state2);

AFD hopcroft_minimisation(AFD afd);