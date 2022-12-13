#ifndef SCHEDULER_C
#define SCHEDULER_C

#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include "../../header/inputOutput/scheduler.h"
#include "../../header/inputOutput/configuration.h"

void DOWN_SEM_REQUEST(){
    int S = 0;
    do{
        S = -1;
        FILE *file = fopen(config->path_sem_request , "r");
        fread(&S, sizeof(int), 1, file);
        fclose(file);
        printf("block\n");
    }while (S < 0);
    FILE *file = fopen(config->path_sem_request, "w");
    fwrite(&S, sizeof(int), 1, file);
    fclose(file);
}

void UP_SEM_REQUEST(){
    int S = 1;
    FILE *file = fopen(config->path_sem_request, "r");
    fwrite(&S, sizeof(int), 1, file);
    fclose(file);
}


void DOWNS_SEM_RESPONSE(){
    int S = 0;
    do
    {
        FILE *file = fopen(config->path_sem_response, "r");
        fread(&S, sizeof(int), 1, file);
        S -= 1;
        fwrite(&S, sizeof(int), 1, file);
        fclose(file);
    } while (S < 0);
}


void UP_SEM_RESPONSE(){
    int S = 1;
    FILE *file = fopen(config->path_sem_response, "r");
    fwrite(&S, sizeof(int), 1, file);
    fclose(file);
}

#endif