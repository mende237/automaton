<<<<<<< HEAD
#ifndef MESSENGER_C
#define MESSENGER_C

#include <cjson/cJSON.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <unistd.h>
#include "../../header/inputOutput/messenger.h"
#include "../../header/inputOutput/scheduler.h"

Messenger *messenger = NULL;

Messenger *get_messenger()
{
    if (messenger == NULL)
    {
        messenger = calloc(1, sizeof(Messenger));
        messenger->previous_id = -1;
        messenger->reception_path = NULL;
        messenger->sending_path = NULL;
    }

    return messenger;
}

void send_result(Messenger *messenger, Message message)
{
    cJSON *id = cJSON_CreateNumber(message.id);
    cJSON *data_path = cJSON_CreateString(message.dataPath);

    cJSON *instruction = cJSON_CreateObject();
    cJSON_AddItemToObject(instruction, "id", id);
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

boolean check_new(Messenger *messenger)
{
    cJSON *id = NULL;
    cJSON *data_path = NULL;
    cJSON *instruction = NULL;
    cJSON *name = NULL;
    FILE *file = NULL;
    do
    {
        // DOWN_SEM_REQUEST();
        file = fopen(messenger->reception_path, "r+");
        if (file != NULL)
        {

            fseek(file, 0, SEEK_END);
            long fsize = ftell(file);
            rewind(file); 

            char *buffer = malloc(fsize + 1);
            fread(buffer, sizeof(char), fsize, file);
            fclose(file);
            buffer[fsize] = 0;

            instruction = cJSON_Parse(buffer);
            free(buffer);
            id = cJSON_GetObjectItemCaseSensitive(instruction, "id");
            data_path = cJSON_GetObjectItemCaseSensitive(instruction, "data path");
            name = cJSON_GetObjectItemCaseSensitive(instruction, "name");

            // printf("%d\n" , (int)strlen(data_path->valuestring));
            if (data_path == NULL)
            {
                printf("coool erreur detecter la taille du buffer est %d\n", strlen(buffer));
                //printf("number of byte %d\n" , a);
                printf("boite de reception %s\n", messenger->reception_path);
                printf("la position du curseur %d\n", ftell(file));
                printf("%s\n", buffer);
                printf("size %d\n", fsize);
                if(file == NULL){
                    printf("*******/////////null file************//////\n");
                }
                exit(1);
            }


            if (id->valueint != messenger->previous_id)
            {
                messenger->message.name = malloc((strlen(name->valuestring) + 1) * sizeof *messenger->message.name);
                strcpy(messenger->message.name, name->valuestring);

                messenger->message.dataPath = malloc((strlen(data_path->valuestring) + 1) * sizeof *messenger->message.dataPath);
                strcpy(messenger->message.dataPath, data_path->valuestring);

                messenger->message.id = id->valueint;
            }

            cJSON_Delete(instruction);
            printf("%d   %d  %d\n", messenger->previous_id, id->valueint , (int)fsize);

            // sleep(1)
        }
        else
        {
            printf("le fichier %s n'exite pas\n", messenger->reception_path);
            return False;
        }
        // UP_SEM_REQUEST();
    } while (id->valueint == messenger->previous_id);

    messenger->previous_id = messenger->message.id;
    printf("%s\n", messenger->message.dataPath);
    return True;
}

int receive_instruction(Messenger *messenger)
{
    if (strcmp(messenger->message.name, "glushkov") == 0)
    {
        return 1;
    }
    else if (strcmp(messenger->message.name, "thomson") == 0)
    {
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
    else if (strcmp(messenger->message.name, "completion") == 0)
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
    }
    else
    {
        return 0;
    }
}

void free_messenger(Messenger *messenger)
{
    free(messenger->message.dataPath);
    free(messenger->message.name);
    free(messenger);
}

=======
#ifndef MESSENGER_C
#define MESSENGER_C

#include <cjson/cJSON.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <unistd.h>
#include "../../header/inputOutput/messenger.h"
#include "../../header/inputOutput/scheduler.h"

Messenger *messenger = NULL;

Messenger *get_messenger()
{
    if (messenger == NULL)
    {
        messenger = calloc(1, sizeof(Messenger));
        messenger->previous_id = -1;
        messenger->reception_path = NULL;
        messenger->sending_path = NULL;
    }

    return messenger;
}

void send_result(Messenger *messenger, Message message)
{
    cJSON *id = cJSON_CreateNumber(message.id);
    cJSON *data_path = cJSON_CreateString(message.dataPath);

    cJSON *instruction = cJSON_CreateObject();
    cJSON_AddItemToObject(instruction, "id", id);
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

boolean check_new(Messenger *messenger)
{
    cJSON *id = NULL;
    cJSON *data_path = NULL;
    cJSON *instruction = NULL;
    cJSON *name = NULL;
    FILE *file = NULL;
    do
    {
        // DOWN_SEM_REQUEST();
        file = fopen(messenger->reception_path, "r+");
        if (file != NULL)
        {

            fseek(file, 0, SEEK_END);
            long fsize = ftell(file);
            rewind(file); 

            char *buffer = malloc(fsize + 1);
            fread(buffer, sizeof(char), fsize, file);
            fclose(file);
            buffer[fsize] = 0;

            instruction = cJSON_Parse(buffer);
            free(buffer);
            id = cJSON_GetObjectItemCaseSensitive(instruction, "id");
            data_path = cJSON_GetObjectItemCaseSensitive(instruction, "data path");
            name = cJSON_GetObjectItemCaseSensitive(instruction, "name");

            // printf("%d\n" , (int)strlen(data_path->valuestring));
            if (data_path == NULL)
            {
                printf("coool erreur detecter la taille du buffer est %d\n", strlen(buffer));
                //printf("number of byte %d\n" , a);
                printf("boite de reception %s\n", messenger->reception_path);
                printf("la position du curseur %d\n", ftell(file));
                printf("%s\n", buffer);
                printf("size %d\n", fsize);
                if(file == NULL){
                    printf("*******/////////null file************//////\n");
                }
                exit(1);
            }


            if (id->valueint != messenger->previous_id)
            {
                messenger->message.name = malloc((strlen(name->valuestring) + 1) * sizeof *messenger->message.name);
                strcpy(messenger->message.name, name->valuestring);

                messenger->message.dataPath = malloc((strlen(data_path->valuestring) + 1) * sizeof *messenger->message.dataPath);
                strcpy(messenger->message.dataPath, data_path->valuestring);

                messenger->message.id = id->valueint;
            }

            cJSON_Delete(instruction);
            printf("%d   %d  %d\n", messenger->previous_id, id->valueint , (int)fsize);

            // sleep(1)
        }
        else
        {
            printf("le fichier %s n'exite pas\n", messenger->reception_path);
            return False;
        }

        sleep(1);
        // UP_SEM_REQUEST();
    } while (id->valueint == messenger->previous_id);

    messenger->previous_id = messenger->message.id;
    printf("%s\n", messenger->message.dataPath);
    return True;
}

int receive_instruction(Messenger *messenger)
{
    if (strcmp(messenger->message.name, "glushkov") == 0)
    {
        return 1;
    }
    else if (strcmp(messenger->message.name, "thomson") == 0)
    {
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
    else if (strcmp(messenger->message.name, "completion") == 0)
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
    }
    else
    {
        return 0;
    }
}

void free_messenger(Messenger *messenger)
{
    free(messenger->message.dataPath);
    free(messenger->message.name);
    free(messenger);
}

>>>>>>> 5710915a1bc5463788acfcb41d154c4319588417
#endif