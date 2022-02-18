package Controller;

import Model.PrgState;
import Model.types.RefType;
import Model.value.IValue;
import Model.value.RefValue;
import Repo.IRepo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Controller {

    //constructor
    private IRepo repo;
    private ExecutorService executor;

    public Controller(IRepo _repo) {
        repo = _repo;
    }

    public void addProgram(PrgState newPrg) {
        repo.addProgram(newPrg);
    }

    public List<PrgState> removeCompletedPrg(List<PrgState> l) {
        return l.stream().filter(e -> e.isNotCompleted())
                .collect(Collectors.toList());
    }

    /*public void oneStep() throws IOException {
        PrgState program = repo.getCrtPrg();
        IStack<IStmt> stack = program.getStack();
        if(stack.isEmpty())
            throw new EmptyStack("Stack is empty!\n");
        //repo.logPrgStateExec();
        IStmt st = stack.pop();
        st.execute(program);
        //System.out.println(program);

        //program.getHeapTable().setContent(this.unsafeGarbageCollector(program.getSymTable().getAllValues(),program.getHeapTable()));
        program.getHeapTable().setContent(unsafeGarbageCollector(
                getAddrFromSymTable(program.getSymTable().getContent().values(),
                program.getHeapTable().getContent().values()), program.getHeapTable().getContent()));

        repo.logPrgStateExec();

    }*/

    public void oneStepForAll(List<PrgState> list) throws InterruptedException, IOException {


        //prepare list of callables
        List<Callable<PrgState>> callList = list.stream()
                .map((PrgState p) -> (Callable<PrgState>) (() -> {
                    return p.oneStep();
                }))
                .collect(Collectors.toList());


        //start the execution of the callables
        // returns the list of new created program states (threads)

        List<PrgState> newList = executor.invokeAll(callList).stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        System.out.println("End of program");
                        return null;
                    }

                }).filter(p -> p != null)
                .collect(Collectors.toList());

        // add the new threads to the list of existing threads
        list.addAll(newList);


        // print the program state list into the file after the execution
        for (PrgState prg : list) {
            repo.logPrgStateExec(prg);
        }

        // save the current programs in the repo
        List<PrgState> list2 = new ArrayList<>(list);
        repo.setPrgList(list2);

    }

    public void executeOneStep() {
        executor = Executors.newFixedThreadPool(8);
        removeCompletedPrg(repo.getPrgList());
        List<PrgState> programStates = repo.getPrgList();
        if (programStates.size() > 0) {
            try {
                oneStepForAll(repo.getPrgList());
            } catch (InterruptedException | IOException e) {
                System.out.println();
            }
            programStates.forEach(e -> {
                try {
                    repo.logPrgStateExec(e);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            removeCompletedPrg(repo.getPrgList());
            executor.shutdownNow();
        }
    }


    public void allStep() throws InterruptedException, IOException {
        executor = Executors.newFixedThreadPool(2);
        List<PrgState> list = repo.getPrgList();
        List<PrgState> prgList = removeCompletedPrg(repo.getPrgList());


        while (prgList.size() > 0) {

            for (PrgState prg : prgList) {
                prg.getHeapTable().setContent(garbageCollector(
                        getAddrFromSymTable(prg.getSymTable().values()),
                        (Map<Integer, IValue>) prg.getHeapTable().getContent()));

            }
            oneStepForAll(prgList);
            list = repo.getPrgList();
            prgList = removeCompletedPrg(repo.getPrgList());

        }
        executor.shutdownNow();

        List<PrgState> prgList2 = new ArrayList<>(prgList);

        repo.setPrgList(prgList2);
    }


    Map<Integer, IValue> garbageCollector(List<Integer> symTableAddr, Map<Integer, IValue> heap) {


        List<Integer> addr = getAddrFromSymTable(heap.values());
        return heap.entrySet().stream()
                .filter(e -> symTableAddr.contains(e.getKey()) || addr.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    List<Integer> getAddrFromSymTable(Collection<IValue> symTableValues) {//, Collection<IValue> heap){

        return symTableValues.stream().filter(v -> v.getType() instanceof RefType)
                .map(v -> {
                    RefValue v1 = (RefValue) v;
                    return v1.getAddress();
                })
                .collect(Collectors.toList());


    }


    public PrgState getProgram() {
        return repo.getCrtPrg();
    }

    public List<String> getIdentifiers() {
        List<String> identifiers = new ArrayList<>();
        repo.getPrgList().forEach(x -> identifiers.add(String.valueOf(x.getid())));
        return identifiers;
    }
}
