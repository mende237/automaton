ifeq ($(OS),Windows_NT)
    CC = C:\MinGW\bin\gcc
else
    UNAME_S := $(shell uname -s)
    ifeq ($(UNAME_S),Linux)
        CC = /usr/bin/gcc-9
    endif
endif


interface: main_interface.c 
	$(CC) -g -c -w main_interface.c -o main.o

consol: main_consol.c
	$(CC) -g -c -w main_consol.c -o main.o

temp: AFN.o AFD.o linked_list.o thomson.o glushkov.o utilitaire.o brzozowski.o hopcroft.o messenger.o configuration.o
	$(CC) -g main.o AFN.o AFD.o linked_list.o thomson.o glushkov.o utilitaire.o messenger.o configuration.o brzozowski.o hopcroft.o function.o stack.o tree.o -lm -lcjson -o out


AFN.o: ./source/algorithm/AFN.c ./header/algorithm/AFN.h ./header/data_structure/structure.h linked_list.o stack.o
	$(CC) -g -c -w ./source/algorithm/AFN.c -o AFN.o

AFD.o: ./source/algorithm/AFD.c ./header/algorithm/AFD.h ./header/data_structure/structure.h stack.o linked_list.o function.o
	$(CC) -g -c -w ./source/algorithm/AFD.c -o AFD.o

linked_list.o: ./source/data_structure/linked_list.c ./header/data_structure/linked_list.h ./header/data_structure/structure.h
	$(CC) -g -c -w ./source/data_structure/linked_list.c -o linked_list.o

stack.o: source/data_structure/stack.c header/data_structure/stack.h ./header/data_structure/structure.h
	$(CC) -g -c -w source/data_structure/stack.c  -o stack.o

tree.o: source/data_structure/tree.c header/data_structure/tree.h ./header/data_structure/structure.h
	$(CC) -g -c -w source/data_structure/tree.c  -o tree.o

thomson.o: ./source/algorithm/thomson.c ./header/algorithm/thomson.h linked_list.o utilitaire.o stack.o tree.o
	$(CC) -g -c -w ./source/algorithm/thomson.c -o thomson.o

glushkov.o: ./source/algorithm/glushkov.c ./header/algorithm/glushkov.h linked_list.o utilitaire.o stack.o tree.o
	$(CC) -g -c -w ./source/algorithm/glushkov.c -o glushkov.o

brzozowski.o: ./source/algorithm/brzozowski.c ./header/algorithm/brzozowski.h
	$(CC) -g -c -W ./source/algorithm/brzozowski.c -o brzozowski.o

hopcroft.o: ./source/algorithm/hopcroft.c ./header/algorithm/hopcroft.h linked_list.o stack.o function.o
	$(CC) -g -c -w ./source/algorithm/hopcroft.c -o hopcroft.o

messenger.o: ./source/inputOutput/messenger.c ./header/inputOutput/messenger.h scheduler.o 
	$(CC) -g -c -w ./source/inputOutput/messenger.c -o messenger.o

configuration.o: ./source/inputOutput/configuration.c ./header/inputOutput/configuration.h
	$(CC) -g -c -w ./source/inputOutput/configuration.c -o configuration.o

scheduler.o: ./source/inputOutput/scheduler.c ./header/inputOutput/scheduler.h configuration.o
	$(CC) -g -c -w ./source/inputOutput/scheduler.c -o scheduler.o

function.o: ./source/algorithm/function.c ./header/algorithm/function.h ./header/algorithm/AFD.h
	$(CC) -g -c -w ./source/algorithm/function.c -o function.o

utilitaire.o: ./source/algorithm/utilitaire.c ./header/algorithm/utilitaire.h AFD.o AFN.o linked_list.o stack.o
	$(CC) -g -c -w ./source/algorithm/utilitaire.c -o utilitaire.o

clean:
	rm -rf *.o

mrproper:clean
	rm -rf main
