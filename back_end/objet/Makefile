final:test.o
	gcc test.o -lcjson -o backend

test.o: ../test.c linked_list.o tree.o stack.o AFD.o AFN.o glushkov.o thomson.o brzozowski.o utilitaire.o hopcroft.o
	gcc -c ../test.c

#compilation des structures de données
linked_list.o: ../source/data_structure/linked_list.c ../header/data_structure/linked_list.h
	gcc -c -w ../source/data_structure/linked_list.c
tree.o: ../source/data_structure/tree.c ../header/data_structure/tree.h
	gcc -c -w ../source/data_structure/tree.c
stack.o: ../source/data_structure/stack.c  ../header/data_structure/stack.h
	gcc -c -w ../source/data_structure/stack.c

#compilation des algorithmes et des utilitaires
AFN.o: ../source/algorithm/AFN.c ../header/algorithm/AFN.h linked_list.o
	gcc -c -w ../source/algorithm/AFN.c

AFD.o: ../source/algorithm/AFD.c ../header/algorithm/AFD.h ../header/data_structure/structure.h linked_list.o stack.o function.o
	gcc -c -w ../source/algorithm/AFD.c

glushkov.o: ../source/algorithm/glushkov.c ../header/algorithm/glushkov.h linked_list.o stack.o utilitaire.o tree.o
	gcc -c -w ../source/algorithm/glushkov.c

brzozowski.o: ../source/algorithm/brzozowski.c ../header/algorithm/brzozowski.h
	gcc -c -w ../source/algorithm/brzozowski.c

thomson.o: ../source/algorithm/thomson.c ../header/algorithm/thomson.h linked_list.o stack.o stack.o utilitaire.o tree.o
	gcc -c -w ../source/algorithm/thomson.c

utilitaire.o: ../source/algorithm/utilitaire.c ../header/algorithm/utilitaire.h linked_list.o stack.o AFN.o AFD.o
	gcc -c -w ../source/algorithm/utilitaire.c

function.o: ../source/algorithm/function.c ../header/algorithm/function.h ../header/algorithm/AFD.h
	gcc -c -w ../source/algorithm/function.c

hopcroft.o: ../source/algorithm/hopcroft.c ../header/algorithm/hopcroft.h linked_list.o stack.o function.o
	gcc -c -w ../source/algorithm/hopcroft.c


	
