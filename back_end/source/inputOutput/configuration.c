#ifndef CONFIGURATION_C
#define CONFIGURATION_C

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <cjson/cJSON.h>
#include "../../header/inputOuput/configuration.h"

Configuration *config = NULL;

Configuration *get_config(char *path)
{
    if (config == NULL)
    {
        config = calloc(1, sizeof(Configuration));
        FILE *file = fopen(path, "r");
        int lettre;
        int size = 0;
        if (file == NULL)
            exit(1);

        while ((lettre = fgetc(file)) != EOF)
            size++;
        fclose(file);

        file = fopen(path, "r");
        char *buffer = calloc(size, sizeof(char));
        fread(buffer, size, 1, file);
        fclose(file);

        cJSON *conf_json = cJSON_Parse(buffer);
        cJSON *request = cJSON_GetObjectItemCaseSensitive(conf_json, "request path");
        cJSON *response = cJSON_GetObjectItemCaseSensitive(conf_json, "response path");

        cJSON *data_request_path = cJSON_GetObjectItemCaseSensitive(conf_json, "data request path");
        cJSON *data_response_path = cJSON_GetObjectItemCaseSensitive(conf_json, "data reponse path");

        cJSON *path_sem_request = cJSON_GetObjectItemCaseSensitive(conf_json, "path semaphore request");
        cJSON *path_sem_response = cJSON_GetObjectItemCaseSensitive(conf_json, "path semaphore response");

        config->data_request_path = calloc(strlen(data_request_path->valuestring) + 1, sizeof(char));
        strcpy(config->data_request_path , data_request_path->valuestring);


        config->data_response_path = calloc(strlen(data_response_path->valuestring) + 1, sizeof(char));
        strcpy(config->data_response_path, data_response_path->valuestring);

        config->path_sem_request = calloc(strlen(path_sem_request->valuestring) + 1, sizeof(char));
        strcpy(config->path_sem_request, path_sem_request->valuestring);

        config->path_sem_response = calloc(strlen(path_sem_response->valuestring) + 1, sizeof(char));
        strcpy(config->path_sem_response, path_sem_response->valuestring);

        config->request_path = calloc(strlen(request->valuestring) + 1, sizeof(char));
        strcpy(config->request_path, request->valuestring);



        config->response_path = calloc(strlen(response->valuestring) + 1, sizeof(char));
        strcpy(config->response_path, response->valuestring);

        cJSON_Delete(conf_json);
    }

    return config;
}

void print_config(Configuration *config)
{
    if (config == NULL)
    {
        printf("pas de configution defini");
    }
    else
    {
        printf(" data request -> %s\n", config->data_request_path);
        printf(" data response -> %s\n", config->data_response_path);
        printf(" path semaphore request -> %s\n", config->path_sem_request);
        printf(" path semaphore response -> %s\n", config->path_sem_response);
        printf(" request -> %s\n", config->request_path);
        printf(" response -> %s\n", config->response_path);
    }
}

void free_config(Configuration *config){
    free(config->data_request_path);
    free(config->data_response_path);
    free(config->request_path);
    free(config->response_path);
    free(config->path_sem_request);
    free(config->path_sem_response);
    free(config);
}

#endif