#ifndef MESSENGER_C
#define MESSENGER_C

#include <cjson/cJSON.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include "../../header/inputOuput/messenger.h"


Messenger *messenger = NULL;

Messenger *get_messenger(){
    if(messenger == NULL){
        messenger = calloc(1 , sizeof(Messenger));
        messenger->previous_id = -1;
    }

    return messenger;
}

void send_result(Messenger *messenger , Message message){
    cJSON *id = cJSON_CreateNumber(message.id);
    cJSON *data_path = cJSON_CreateString(message.dataPath);

    cJSON *instruction = cJSON_CreateObject();
    cJSON_AddItemToObject(instruction , "id" , id);
    cJSON_AddItemToObject(instruction, "data path", data_path);

    char *result = cJSON_Print(instruction);
    cJSON_Delete(instruction);

    FILE *file = fopen(messenger->sending_path, "w");

    if (file == NULL)
        exit(1);

    fputs(result, file);
    fclose(file);
    free(result);
}

boolean check_new(Messenger *messenger){
    cJSON *id = NULL ;
    cJSON *data_path = NULL;
    cJSON *instruction = NULL;
    cJSON *name = NULL;
    FILE *file = NULL;
    do
    {
        if(file = fopen(messenger->reception_path , "r")){
            int lettre;
            int size = 0;
            if (file == NULL)
                exit(1);

            while ((lettre = fgetc(file)) != EOF)
                size++;
            fclose(file);

            file = fopen(messenger->reception_path , "r");
            char *buffer = calloc(size, sizeof(char));
            fread(buffer, size, 1, file);
            fclose(file);

            instruction = cJSON_Parse(buffer);
            id = cJSON_GetObjectItemCaseSensitive(instruction , "id");
            data_path = cJSON_GetObjectItemCaseSensitive(instruction, "data path");
            name = cJSON_GetObjectItemCaseSensitive(instruction, "name");

            messenger->message.dataPath = calloc(strlen(data_path->valuestring) + 1, sizeof(char));
            strcpy(messenger->message.dataPath , data_path->valuestring);

            messenger->message.name = calloc(strlen(name->valuestring) + 1, sizeof(char));
            strcpy(messenger->message.name , name->valuestring);

            messenger->message.id = id->valueint;
            cJSON_Delete(instruction);
        }else{
            printf("le fichier %s n'exite pas\n" , messenger->reception_path);
            return False;
        }

    } while (id->valueint == messenger->previous_id);

    messenger->previous_id = id->valueint;
    return True;
}

int receive_instruction(Messenger *messenger)
{
    if(strcmp(messenger->message.name , "glushkov") == 0){
        return 1;
    }
    else if (strcmp(messenger->message.name, "thomson") == 0){
        return 2;
    }
    else if (strcmp(messenger->message.name, "AFD to REG") == 0)
    {
        return 3;
    }
    else if (strcmp(messenger->message.name, "hopcroft minisation") == 0)
    {
        return 4;
    }
    else if (strcmp(messenger->message.name, "brzozowski minisation") == 0)
    {
        return 5;
    }
    else if (strcmp(messenger->message.name, "determinisation") == 0)
    {
        return 6;
    }
    else if (strcmp(messenger->message.name, "epsilone determinisation") == 0)
    {
        return 7;
    }
    else if (strcmp(messenger->message.name, "union") == 0)
    {
        return 8;
    }
    else if (strcmp(messenger->message.name, "intersection") == 0)
    {
        return 9;
    }
    else if (strcmp(messenger->message.name, "completer") == 0)
    {
        return 10;
    }
    else if (strcmp(messenger->message.name, "complementaire") == 0)
    {
        return 11;
    }
    else if (strcmp(messenger->message.name, "miroir AFD") == 0)
    {
        return 12;
    }
    else if (strcmp(messenger->message.name, "miroir AFN") == 0)
    {
        return 13;
    }
    else if (strcmp(messenger->message.name, "reconnaissance AFD") == 0)
    {
        return 14;
    }
    else if (strcmp(messenger->message.name, "reconnaissance AFN") == 0)
    {
        return 15;
    }else{
        return 0;
    }
}

#endif