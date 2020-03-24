## Runner.java

* **shouldRun** - variabila de tip boolean ce tine programul in viata (valoare default **true**), tasteaza 'X' in consola pentru a o seta cu **false**.

#### Metoda main contine:
 * o coada de comenzi ce poate fi folosita de mai multe fire de executie, din moment ce este de tip ConcurrentLinkedQueue;
 * 3 ExecturService care genereaza 6 threaduri pentru manageri, 15 threaduri pentru cofetari si 5 threaduri pentru curieri;
 * o metoda *simulateShop* method careia i se dau ca si argumente coada de comenzi si ExecutorService-urile.

 #### simulateShop(Queue<Order> orders, ExecutorService managerService, ExecutorService confectionerService, ExecutorService courierService)
 * thread-ul principal reprezinta responsabilul telefonic;
 * utilizatorul poate tasta un numar cuprins intre 1 si 12 pentru a-si alege tortul dorit (gasit in **product/Cake.java**);
 * daca in coada exista mai mult de 6 comenzi, responsabilul telefonic (thread-ul principal) asteapta ca un manager sa fie liber;
 * fiecare manager/thread generat de **managerService** apeleaza metoda **processOrder**
 * in final, daca **shouldRun** este **false**, cele 3 ExecutorServices vor fi inchise prin apelarea metodei *shutdown*
 
 #### processOrder(Queue<Order> orders, ExecutorService confectionerService, ExecutorService courierService, Cake cake, Order order)
 * va urma... maine :D