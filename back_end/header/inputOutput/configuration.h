#ifndef CONFIGURATION_H
#define CONFIGURATION_H

typedef struct Configuration{
    char *request_path;
    char *response_path;
    char *data_request_path;
    char *data_response_path;
    char *path_sem_request;
    char *path_sem_response;
} Configuration;


extern Configuration *config;

Configuration *get_config(char *path);
void print_config(Configuration *config);
void free_config(Configuration *config);

#endif // CONFIGURATION_H
