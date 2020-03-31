import java.util.*;

public class ConsoleApplication {

    List<Process> priorityQueue = new ArrayList<Process>();

    static int counter = 0;
    Map<Integer, Integer> processCounter = new HashMap<Integer, Integer>();


    public void start() {
        boolean flag = true;
        while (flag) {
            System.out.println("--------------------------------------------------");
            System.out.println("Opcje:");
            System.out.println("1. Wyswietl kolejke procesow");
            System.out.println("2. Dodaj nowy proces");
            System.out.println("3. Usun wybrany proces");
            System.out.println("4. Zmien priorytet wybranego procesu");
            System.out.println("5. Zmien stan wybranego procesu");
            System.out.println("6. Przejdz dalej (wykonanie 1 kwantu czasu)");
            System.out.println("0. Wyjdz z symulacji");
            System.out.println("--------------------------------------------------");
            Scanner scanner = new Scanner(System.in);
            Integer option = scanner.nextInt();
            switch (option) {
                case 1:
                    displayAllProcesses();
                    break;
                case 2:
                    try {
                        addProcessToPriorityQueue(addProcess(scanner));
                    } catch (AlreadyExistsException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    removeProcess(scanner);
                    break;
                case 4:
                    editProcessPriority(scanner);
                    break;
                case 5:
                    editProcessState(scanner);
                    break;
                case 6:
                    try {
                        makeOneStep(scanner);
                    } catch (EmptyListException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0:
                    System.out.println("Czy na pewno chcesz opuscic program?");
                    System.out.println("Wpisz '0' jesli tak lub '1' jesli nie");
                    int exitInput = scanner.nextInt();
                    if (exitInput == 0) {
                        System.out.println("wyjscie z programu");
                        flag = false;
                    } else if (exitInput == 1) {
                        break;
                    } else {
                        System.out.println("wybrales nieprawidlowa opcje");
                        break;
                    }
                    break;
                default:
                    System.out.println("Wybrales niewspierana opcje");
                    break;
            }
        }
    }

    public void displayAllProcesses() {
        System.out.print("\n\nID procesu \t Priorytet \t\t Stan \t\t Czas wykonania \n");
        if (priorityQueue.isEmpty()) {
            System.out.print("\n   " + 0 + "\t\t    " + 0 + "\t\t   "
                    + "IDLE" + "\t\t    " + " - " + "\n");
        } else {
            //  Collections.sort(priorityQueue);
            //Collections.reverse(priorityQueue);
            for (int i = 0; i < priorityQueue.size(); i++) {
                Process process1 = priorityQueue.get(i);
                System.out.print("\n   " + process1.getProcessId() + "\t\t    " + process1.getPriority() + "\t\t   "
                        + process1.getState() + "\t\t    " + process1.getStartBurstTime() + "\n");
            }
        }
    }


    public Process addProcess(Scanner scanner) throws AlreadyExistsException {
        Process process1 = new Process();
        System.out.println("Podaj ID watku (int 1-100):");
        int id = scanner.nextInt();

        if (priorityQueue.contains(getThisProcessByID(id))) {
            throw new AlreadyExistsException
                    ("Istnieje juz proces o takim ID!");
        } else {
            process1.setProcessId(id);

            System.out.println("Podaj priorytet watku (1-31): ");
            int priority = scanner.nextInt();
            if (priority > 31) {
                System.out.println("Nie mozna ustawic takiego priorytetu");
            } else {
                process1.setPriority(priority);

                System.out.println("Podaj stan watku (w-WAITING, r-RUNNING, t-TERMINATED):");
                String state = scanner.next();
                PCB.processState stateEnum = PCB.getValue(state);
                process1.setState(addingState(process1, stateEnum));
            }
        }
        return process1;
    }

    public void addProcessToPriorityQueue(Process process) {
        priorityQueue.add(process);
        System.out.println("dodano proces do kolejki");
    }


    public void removeProcess(Scanner scanner) {
        System.out.println("Podaj ID procesu, ktory chcesz usunac: ");
        int id = scanner.nextInt();
        Process p1 = getThisProcessByID(id);
        priorityQueue.remove(p1);
        System.out.println("usunieto proces o id: " + id);
    }

    public Process getThisProcessByID(int i) {
        for (Process p1 : priorityQueue) {
            if (p1.getProcessId() == i) {
                return p1;
            }
        }
        return null;
    }

    public void editProcessPriority(Scanner scanner) {
        int max = checkHighestPriority(priorityQueue);

        System.out.println("Podaj ID procesu, ktorego priorytet chcesz zmienic: ");
        int pr = scanner.nextInt();
        Process p1 = getThisProcessByID(pr);
        System.out.println("Proces o id: " + p1.getProcessId() + " i priorytecie: " + p1.getPriority());
        System.out.println("Podaj na jaki priorytet chcesz zmienic ten proces: ");
        int newPriority = scanner.nextInt();
        //zalezy od stanu
        if (p1.getState().equals(PCB.processState.RUNNING)) {
            if (p1.getPriority() == max) {
                System.out.println("nie ma sensu zmieniac priorytetu");
            } else if (p1.getPriority() > max) {
                if (newPriority > 31) {
                    System.out.println("Nie mozna ustawic takiego priorytetu");
                } else {
                    p1.setPriority(newPriority);
                }
            } else {
                if (newPriority > 31) {
                    System.out.println("Nie mozna ustawic takiego priorytetu");
                } else {
                    p1.setPriority(newPriority);
                }
            }
        } else if (p1.getState().equals(PCB.processState.WAITING)) {
            if (p1.getPriority() == max) {
                //dwa takie same
            } else if (p1.getPriority() > max) {
                if (newPriority > 31) {
                    System.out.println("Nie mozna ustawic takiego priorytetu");
                } else {
                    p1.setPriority(newPriority);
                    p1.setState(PCB.processState.RUNNING);
                }
            } else if (p1.getPriority() < max) {
                if (newPriority > 31) {
                    System.out.println("Nie mozna ustawic takiego priorytetu");
                } else {
                    p1.setPriority(newPriority);
                }
            }
        }

        System.out.println("zmieniono priorytet");
    }

    public void editProcessState(Scanner scanner) {
        System.out.println("Podaj ID procesu, ktorego priorytet chcesz zmienic: ");
        int pr = scanner.nextInt();
        Process p1 = getThisProcessByID(pr);
        System.out.println("Proces o id: " + p1.getProcessId() + " , priorytecie: " + p1.getPriority() + " i stanie: " + p1.getState());
        System.out.println("Podaj na jaki stan (w-WAITING, r-RUNNING, t-TERMINATED) chcesz zmienic ten proces: ");
        String newState = scanner.next();
        PCB.processState stateEnum = PCB.getValue(newState);
        p1.setState(addingState(p1, stateEnum));

        System.out.println("zmieniono stan");
    }

    public int checkHowManyRunning(List<Process> list) {
        List<Process> runList = new ArrayList<>();
        for (Process p1 : list) {
            if (p1.getState().equals(PCB.processState.RUNNING)) {
                runList.add(p1);
            } else {
                //System.out.println("problem z lista");
            }
        }
        //System.out.println("liczba running: " + runList.size());
        return runList.size();
    }


    public void makeOneStep(Scanner scanner) throws EmptyListException {
        if (scanner != null) {
            List<Process> toRemove = new ArrayList<>();
            int max = checkHighestPriority(priorityQueue);
            int howManyR = checkHowManyRunning(priorityQueue);
            // Process p2 = getThisProcessByID(max);
            if (howManyR == 0) {
                if (priorityQueue.isEmpty()) {
                    throw new EmptyListException("brak procesow");
                } else {
                    for (Process p1 : priorityQueue) {
                        if (p1.getState().equals(PCB.processState.TERMINATED)) {
                            toRemove.add(p1);
                        } else if (max == p1.getPriority()) {
                            //p2.setPriority(p1.getPriority() + 1);
                            //naprawic!
                            p1.setState(PCB.processState.RUNNING);
                        } else {
                            processWaiting(p1);
                            // System.out.println("process waiting zadzialal przy braku running");
                        }
                    }
                }
            } else if (howManyR == 1) {
                for (Process p1 : priorityQueue) {
                    if (p1.getState().equals(PCB.processState.RUNNING)) {
                        processRunning(p1);
                        // System.out.println("process runing zadzialal przy running");
                    } else if (p1.getState().equals(PCB.processState.TERMINATED)) {
                        toRemove.add(p1);
                    } else if (p1.getState().equals(PCB.processState.WAITING)) {
                        processWaiting(p1);
                        // System.out.println("proces waiting zadzialal przy running");
                    }
                }
            }
//            else if (howManyR == 2) {
//                Process p1 = priorityQueue.get(this.process.getPriority());
//                Process p2 = priorityQueue.get(process.getThisPriority(p1.getPriority() + 1));
//                p2.setState(PCB.processState.RUNNING);
//            }
            else {
                // System.out.println("za duzo running");

            }
            priorityQueue.removeAll(toRemove);
            counter = counter + 1;
        }
    }

    public PCB.processState addingState(Process p1, PCB.processState state) {
        int max = checkHighestPriority(priorityQueue);
        int howManyR = checkHowManyRunning(priorityQueue);

        if (state.equals(p1.getEnumSTate(PCB.processState.TERMINATED))) {
            return p1.setState(PCB.processState.TERMINATED);
        } else if (state.equals(p1.getEnumSTate(PCB.processState.WAITING))) {
            if (howManyR > 0) {
                if (p1.getPriority() < max || p1.getPriority() == max) {
                    return p1.setState(PCB.processState.WAITING);
                } else if (p1.getPriority() > max) {
                    changeStateFromRunningToWaiting();
                    return p1.setState(PCB.processState.RUNNING);
                } else {
                    // System.out.println("cos nie tak");
                }
            }
            p1.setStartCounter(0);
            return p1.setState(PCB.processState.WAITING);
        } else if (state.equals(p1.getEnumSTate(PCB.processState.RUNNING))) {
            if (howManyR == 0) {
                return p1.setState(PCB.processState.RUNNING);
            } else if (howManyR == 1) {
                if (p1.getPriority() < max || p1.getPriority() == max) {
                    System.out.println("inny proces ma wyzszy priorytet lub byl pierwszy");
                    return p1.setState(PCB.processState.WAITING);
                } else if (p1.getPriority() > max) {
                    changeStateFromRunningToWaiting();
                    return p1.setState(PCB.processState.RUNNING);
                } else {
                    //  System.out.println("cos nie tak");
                }
            }
        }
        return null;
    }

    public void handleTwoProcessesWithTheSamePriority(Process p1, int prio) {

        int c1 = p1.getStartCounter();
        Process p2 = getThisProcessByID(prio);
        int c2 = p2.getStartCounter();
        if (c1 > c2) {
            p1.setState(PCB.processState.RUNNING);
            p2.setState(PCB.processState.WAITING);
        } else if (c1 == c2) {
            c2++;
        } else if (c1 < c2) {
            p2.setState(PCB.processState.RUNNING);
            p1.setState(PCB.processState.WAITING);
        }
    }


    public void changeStateFromRunningToWaiting() {
        for (Process p1 : priorityQueue) {
            if (p1.getState().equals(PCB.processState.RUNNING)) {
                p1.setState(PCB.processState.WAITING);
            }
        }
    }

    public void processRunning(Process p1) {
        if (p1.getStartBurstTime() == 0) {
            p1.setState(PCB.processState.TERMINATED);
        } else if (p1.getStartBurstTime() > 0) {
            int temp = p1.getStartBurstTime();
            p1.setStartBurstTime(temp - 1);
            System.out.println("proces sie wykonuje");
        }
    }

    public void processWaiting(Process p1) {
        int localCounter = p1.getStartCounter();
        if (p1.getPriority() < 16) {
            if (localCounter < 3) {
                p1.setStartCounter(p1.getStartCounter() + 1);
                processCounter.put(p1.getProcessId(), processCounter.get(p1.getProcessId() + 1));
                System.out.println("dodano licznik");
            } else {
                p1.setPriority(p1.getPriority() + 1);
                p1.setStartCounter(0);
                processCounter.replace(p1.getProcessId(), p1.getStartCounter());
                System.out.println("wyzerowano licznik");
            }
        } else {
            System.out.println("ok");
        }
    }

    public Integer checkHighestPriority(List<Process> list) {
        int max = 0;
        for (Process p1 : list) {
            if (max < p1.getPriority()) {
                max = p1.getPriority();
            }
        }
        return max;
    }
}
