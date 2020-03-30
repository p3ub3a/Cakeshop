## Runner.java
* **shouldRun** - variabila de tip boolean ce tine programul in viata (valoare default **true**), tasteaza 'X' in consola pentru a o seta cu **false**;
* **orderCount** - variabila ce este incrementata atunci cand o comanda este preluata;
* **queueMonitor** - obiect folosit la comunicarea dintre manageri si responsabilul telefonic atunci cand coada de comenzi este plina;
* **managerMonitor** - obiect folosit la comunicarea dintre manageri si curieri;
* **monitorCounter** - variabila folosita pentru a selecta indexul unui element din **monitors**.

##### Metoda main contine:
 * o coada de comenzi ce poate fi folosita de mai multe fire de executie, din moment ce este de tip ConcurrentLinkedQueue;
 * 5 ExecturService care genereaza 6 threaduri pentru manageri, 15 threaduri pentru cofetari si 5 threaduri pentru curieri;
 * o metoda *simulateShop* careia i se dau ca si argumente coada de comenzi si ExecutorService-urile.

 ##### simulateShop(Queue<Order> orders, ExecutorService managerService, ExecutorService confectionerService, ExecutorService courierService)
 * thread-ul principal reprezinta responsabilul telefonic;
 * cu ajutorul metodei *getInput()*, utilizatorul poate tasta un numar cuprins intre 1 si 12 pentru a-si alege tortul dorit (gasit in **product/Cake.java**);
 * daca in coada exista mai mult de 6 comenzi, responsabilul telefonic (thread-ul principal) asteapta ca un manager sa-l notifice in cazul in care este liber;
 * fiecare manager/thread generat de *managerService* apeleaza metoda *processOrder*;
 * in final, daca **shouldRun** este **false**, cele 3 ExecutorServices vor fi inchise prin apelarea metodei *shutdown*.
 
 ##### processOrder(Queue<Order> orders, ExecutorService confectionerService, ExecutorService courierService, Cake cake, Order order)
 * metoda responsabila cu trimiterea comenzii catre cofetari si, ulterior, livrarea acesteia;
 * **sendOrderToConfectioners** returneaza o lista **List<Future<-Order>>**. Inainte de a livra comanda, thread-ul asociat unui manager va astepta cofetarii sa termine treaba;
 * daca toti curierii sunt ocupati, managerul/thread-ul curent va astepta o notificare din partea curierului;
 * dupa ce a fost livrata comanda, aceasta este scoasa din coada de comenzi.
 
## product/Cake.java
* un enum ce continte tipurile de torturi disponibile in magazin;
* constructorul primeste, pe langa nume si id, timpii de preparare si de rulare in milisecunde.

## product/Order.java
* POJO pentru comanda.

## product/OrderStatus.java
* enum ce contine posibile statusuri ale unei comenzi.

## utils/Constants.java, utils/Messages.java
* fisiere ce contin constante.

## utils/Monitor.java
* clasa folosita pentru sincronizarea thread-urilor;
* **waiting** - indica daca thread-ul ar trebui sa ramana in asteptare;
* **pauseThread(Monitor monitor)** - suspenda un thread;
* **wakeupThread(Monitor monitor)** - trezeste un thread.

## workers/Confectioner.java
* **duration** - durata de preparare;
 * clasa abstracta ce este extinsa de catre cofetarii cu functii specifice (de preparare a cremei, blatului si decoratiunilor).
 
##### prepareCake(int duration, int orderId, String cakeName)
* fiecare cofetar suprascrie metoda;
* **orderId** si **cakeName** sunt folosite pentru a afisa mesaje in consola;
* **duration**: cat timp thread-ul curent va fi in **sleep** 

## workers/CreamConfectioner.java;
* suprascrie metoda *prepareCake*;
* afiseaza mesaje specifice prepararii cremei.

## workers/DecorationsConfectioner.java
* suprascrie metoda *prepareCake*;
* afiseaza mesaje specifice prepararii decoratiunilor.

## workers/DoughConfectioner.java
* suprascrie metoda *prepareCake*;
* afiseaza mesaje specifice prepararii blatului.

## workers/Manager.java
* instanta acestei clase poate trimite torturi catre cofetari cu ajutorul functiei *sendOrderToConfectioners*.

##### List<Future<-Order>> sendOrderToConfectioners(ExecutorService doughCService, ExecutorService creamCService, ExecutorService decosCService, Order order, Cake cake)
* trimite comanda si tipul de tort catre 3 cofetari, fiecare avand un rol specific;
* returneaza o comanda de tip Future ce va fi folosite in **Runner.java**.

##### sendOrderToConfectioner(ExecutorService confectionerService, Confectioner confectioner, Order order, Cake cake)
* confectionerService genereaza 5 thread-uri ce pot prepara torturi simultan.

## workers/Courier.java
* **busyCouriers** - incrementata atunci cand un curier incepe livrarea, decrementata cand termina livrarea;
* instanta acestei clase poate livra torturi cu ajutorul functiei *deliverCake*

##### deliverCake(Order order, Cake cake, ExecutorService courierService)
* **courierService** poate genera 5 thread-uri ce pot livra simultan comenzi;
* durata livrarii este data de **cake.getDeliveryDuration()**;
* la sfarsitul livrarii este notificat un manager ce se poate afla in asteptare.