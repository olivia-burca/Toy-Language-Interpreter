package Repo;
import Model.PrgState;

import java.io.IOException;
import java.util.List;

public interface IRepo {
    void addProgram(PrgState newPrg);
    PrgState getCrtPrg();
    void logPrgStateExec(PrgState state) throws IOException;
    List<PrgState> getPrgList() ;
    void setPrgList(List<PrgState> list);
}
