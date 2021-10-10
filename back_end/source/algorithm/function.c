#include "../../header/algorithm/function.h"
#include <stdlib.h>

boolean search_state(void **state_tab, void *state, int n){
    int i = 0;
    for (i = 0; i < n; i++)
    {
        if (state_tab[i] != NULL && state != NULL)
        {
            if (strcmp(state_tab[i], state) == 0)
            {
                return True;
            }
        }
        else if (state_tab[i] == state)
        {
            return True;
        }
    }
    return False;
}



