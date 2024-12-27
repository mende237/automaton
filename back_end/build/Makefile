WINDOW_CC = C:\MinGW\bin\gcc
LINUX_CC = /usr/bin/gcc-11

WINDOW_DELETE_FILES = del
LINUX_DELETE_FILES = rm -rf
MAIN_DIR=..
HEARDER_DIR=$(MAIN_DIR)/header
SOURCE_DIR=$(MAIN_DIR)/source
TARGET=$(MAIN_DIR)/main

ifeq ($(OS),Windows_NT)
    CC = $(WINDOW_CC)
    DELETE_FILES = $(WINDOW_DELETE_FILES)
else
    UNAME_S := $(shell uname -s)
    ifeq ($(UNAME_S),Linux)
        CC = $(LINUX_CC)
        DELETE_FILES = $(LINUX_DELETE_FILES)
    endif
endif

interface: $(MAIN_DIR)/main_interface.c
	$(CC) -g -c -w $(MAIN_DIR)/main_interface.c -o main.o
	make temp

consol: $(MAIN_DIR)/main_consol.c  
	$(CC) -g -c -w $(MAIN_DIR)/main_consol.c -o main.o
	make temp

temp: AFN.o AFD.o linked_list.o thomson.o glushkov.o utilitaire.o brzozowski.o hopcroft.o messenger.o configuration.o
	$(CC) -g -w main.o AFN.o AFD.o linked_list.o thomson.o glushkov.o utilitaire.o messenger.o configuration.o brzozowski.o hopcroft.o function.o stack.o tree.o -lm -lcjson -o $(TARGET)


AFN.o: $(SOURCE_DIR)/algorithm/AFN.c $(HEARDER_DIR)/algorithm/AFN.h $(HEARDER_DIR)/data_structure/structure.h linked_list.o stack.o
	$(CC) -g -c -w $(SOURCE_DIR)/algorithm/AFN.c -o AFN.o

AFD.o: $(SOURCE_DIR)/algorithm/AFD.c $(HEARDER_DIR)/algorithm/AFD.h $(HEARDER_DIR)/data_structure/structure.h stack.o linked_list.o function.o
	$(CC) -g -c -w $(SOURCE_DIR)/algorithm/AFD.c -o AFD.o

linked_list.o: $(SOURCE_DIR)/data_structure/linked_list.c $(HEARDER_DIR)/data_structure/linked_list.h $(HEARDER_DIR)/data_structure/structure.h
	$(CC) -g -c -w $(SOURCE_DIR)/data_structure/linked_list.c -o linked_list.o

stack.o: $(SOURCE_DIR)/data_structure/stack.c $(HEARDER_DIR)/data_structure/stack.h $(HEARDER_DIR)/data_structure/structure.h
	$(CC) -g -c -w $(SOURCE_DIR)/data_structure/stack.c  -o stack.o

tree.o: $(SOURCE_DIR)/data_structure/tree.c $(HEARDER_DIR)/data_structure/tree.h $(HEARDER_DIR)/data_structure/structure.h
	$(CC) -g -c -w $(SOURCE_DIR)/data_structure/tree.c  -o tree.o

thomson.o: $(SOURCE_DIR)/algorithm/thomson.c $(HEARDER_DIR)/algorithm/thomson.h linked_list.o utilitaire.o stack.o tree.o
	$(CC) -g -c -w $(SOURCE_DIR)/algorithm/thomson.c -o thomson.o

glushkov.o: $(SOURCE_DIR)/algorithm/glushkov.c $(HEARDER_DIR)/algorithm/glushkov.h linked_list.o utilitaire.o stack.o tree.o
	$(CC) -g -c -w $(SOURCE_DIR)/algorithm/glushkov.c -o glushkov.o

brzozowski.o: $(SOURCE_DIR)/algorithm/brzozowski.c $(HEARDER_DIR)/algorithm/brzozowski.h
	$(CC) -g -c -W $(SOURCE_DIR)/algorithm/brzozowski.c -o brzozowski.o

hopcroft.o: $(SOURCE_DIR)/algorithm/hopcroft.c $(HEARDER_DIR)/algorithm/hopcroft.h linked_list.o stack.o function.o
	$(CC) -g -c -w $(SOURCE_DIR)/algorithm/hopcroft.c -o hopcroft.o

messenger.o: $(SOURCE_DIR)/inputOutput/messenger.c $(HEARDER_DIR)/inputOutput/messenger.h scheduler.o 
	$(CC) -g -c -w $(SOURCE_DIR)/inputOutput/messenger.c -o messenger.o

configuration.o: $(SOURCE_DIR)/inputOutput/configuration.c $(HEARDER_DIR)/inputOutput/configuration.h
	$(CC) -g -c -w $(SOURCE_DIR)/inputOutput/configuration.c -o configuration.o

scheduler.o: $(SOURCE_DIR)/inputOutput/scheduler.c $(HEARDER_DIR)/inputOutput/scheduler.h configuration.o
	$(CC) -g -c -w $(SOURCE_DIR)/inputOutput/scheduler.c -o scheduler.o

function.o: $(SOURCE_DIR)/algorithm/function.c $(HEARDER_DIR)/algorithm/function.h $(HEARDER_DIR)/algorithm/AFD.h
	$(CC) -g -c -w $(SOURCE_DIR)/algorithm/function.c -o function.o

utilitaire.o: $(SOURCE_DIR)/algorithm/utilitaire.c $(HEARDER_DIR)/algorithm/utilitaire.h AFD.o AFN.o linked_list.o stack.o
	$(CC) -g -c -w $(SOURCE_DIR)/algorithm/utilitaire.c -o utilitaire.o

clean:
	$(DELETE_FILES) *.o

mrproper:clean
	$(DELETE_FILES) $(MAIN_DIR)/main
