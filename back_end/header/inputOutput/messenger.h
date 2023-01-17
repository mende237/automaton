#ifndef MESSENGER_H
#define MESSENGER_H
#include "../data_structure/structure.h"
#include "./message.h"
#include <stdlib.h>

typedef struct Messenger
{
    char *sending_path;
    char *reception_path;
    Message message;
    int previous_id;
}Messenger;

extern Messenger *messenger;

Messenger *get_messenger();
void send_result(Messenger *messenger , Message message);
boolean check_new(Messenger *messenger);
int receive_instruction(Messenger *messenger);
void free_messenger(Messenger *messenger);

#endif