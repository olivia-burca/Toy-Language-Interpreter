package sample;


import Controller.Controller;
import Model.PrgState;
import Model.adt.*;
import Model.exp.ArithExp;
import Model.exp.Heap.HeapReading;
import Model.exp.RelationalExp;
import Model.exp.ValueExp;
import Model.exp.VarExp;
import Model.stmt.*;
import Model.stmt.Files.CloseRFile;
import Model.stmt.Files.OpenRFile;
import Model.stmt.Files.ReadFile;
import Model.stmt.Heap.HeapAllocation;
import Model.stmt.Heap.HeapWriting;
import Model.types.*;
import Model.value.BoolValue;
import Model.value.IValue;
import Model.value.IntValue;
import Model.value.StringValue;
import Repo.IRepo;
import Repo.Repo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.net.URL;
import java.util.ArrayList;


public class Interpretor extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Toy Language Interpreter");
        //FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample/ToyLanguageInterpreter.fxml"));
        //Pane pane = (Pane) fxmlLoader.load();

        String fxmlPath ="ToyLanguageInterpreter.fxml" ;
        URL url = getClass().getClassLoader().getResource(fxmlPath);

        FXMLLoader fxmlLoader = new FXMLLoader(url);
        Pane pane = (Pane) fxmlLoader.load();

        ToyLanguageController controller = fxmlLoader.getController();
        controller.setControllers(getControllers());
        primaryStage.setScene(new Scene(pane));
        primaryStage.show();

    }

    public static ArrayList<Controller> getControllers(){

        // ex 1:  int v; v = 2; Print(v)
        IStmt ex1= new CompStmt(new VarDeclStmt("v",new IntType()),
                new CompStmt(new AssignStmt("v",new ValueExp(new IntValue(2))),
                        new PrintStmt(new VarExp("v"))));

        //PrgState prg1 = new PrgState(ex1);
        //IRepo repo1 = new Repo(prg1,"log1.txt");
        //Controller ctr1 = new Controller(repo1);



        // ex 2: int a; int b; a=2+3*5; b=a+1; Print(b)

        IStmt ex2 = new CompStmt( new VarDeclStmt("a",new IntType()), new CompStmt(new VarDeclStmt("b",new IntType()),
                new CompStmt(new AssignStmt("a", new ArithExp('+',new ValueExp(new IntValue(2)),new ArithExp('*',
                        new ValueExp(new IntValue(3)), new ValueExp(new IntValue(5))))),  new CompStmt(
                        new AssignStmt("b",new ArithExp('+',new VarExp("a"), new ValueExp(new IntValue(1)))),
                        new PrintStmt(new VarExp("b"))))));

        //PrgState prg2 = new PrgState(ex2);
        //IRepo repo2 = new Repo(prg2,"log2.txt");
        //Controller ctr2 = new Controller(repo2);


        // ex 3: bool a; a=false; int v;(If a Then v=2 Else v=3);Print(v)

        IStmt ex3 = new CompStmt(new VarDeclStmt("a",new BoolType()), new CompStmt(new AssignStmt("a",
                new ValueExp(new BoolValue(true))),new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new IfStmt(new VarExp("a"),new AssignStmt("v",new ValueExp(new IntValue(2))),
                        new AssignStmt("v", new ValueExp(new IntValue(3)))), new PrintStmt(new
                        VarExp("v"))))));

        //PrgState prg3 = new PrgState(ex3);
        //IRepo repo3 = new Repo(prg3,"log3.txt");
        //Controller ctr3 = new Controller(repo3);


        IStmt ex4 = new CompStmt(
                new VarDeclStmt("varf",new StringType()),new CompStmt(
                new AssignStmt("varf",new ValueExp(new StringValue("test.in"))),new CompStmt(
                new OpenRFile(new VarExp("varf")),new CompStmt(
                new VarDeclStmt("varc",new IntType()),new CompStmt(
                new ReadFile(new VarExp("varf"),"varc"),new CompStmt(
                new PrintStmt(new VarExp("varc")),new CompStmt(
                new ReadFile(new VarExp("varf"),"varc") ,new CompStmt(new PrintStmt(new VarExp("varc")),new CloseRFile(new VarExp("varf"))))))))));

        //PrgState prg4 = new PrgState(ex4);
        //IRepo repo4 = new Repo(prg4,"log4.txt");
        //Controller ctr4 = new Controller(repo4);

        IStmt ex5 = new CompStmt(
                new VarDeclStmt("v",new IntType()),
                new CompStmt(
                        new AssignStmt("v",new ValueExp(new IntValue(4))),
                        new WhileStmt(
                                new RelationalExp(new VarExp("v"),new ValueExp(new IntValue(0)),">"),
                                new CompStmt(new PrintStmt(new VarExp("v")),
                                        new AssignStmt( "v",new ArithExp('-',new VarExp("v"),new ValueExp(new IntValue(1))))
                                )
                        )));

        //PrgState prg5 = new PrgState(ex5);
        //IRepo repo5 = new Repo(prg5,"log5.txt");
        //Controller ctr5 = new Controller(repo5);

        //Ref int v;new(v,20);Ref Ref int a; new(a,v);print(v);print(a)

        IStmt ex6 = new CompStmt(new CompStmt(new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),new HeapAllocation("v",
                new ValueExp(new IntValue(20))))
                ,new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),new HeapAllocation("a",
                new VarExp("v")))), new CompStmt(new PrintStmt(new VarExp("v")),new PrintStmt(new VarExp("a"))));

        //PrgState prg6 = new PrgState(ex6);
        //IRepo repo6 = new Repo(prg6,"log6.txt");
        //Controller ctr6 = new Controller(repo6);

        // Ref int v;new(v,20);Ref Ref int a; new(a,v);print(rH(v));print(rH(rH(a))+5)

        IStmt ex7 = new CompStmt(new CompStmt(new CompStmt(new CompStmt(new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),new HeapAllocation("v",
                new ValueExp(new IntValue(20)))),new VarDeclStmt("a", new RefType(new RefType(new IntType())))),
                new HeapAllocation("a", new VarExp("v"))), new PrintStmt(new HeapReading(new VarExp("v")))),
                new PrintStmt(new ArithExp('+',new HeapReading(new HeapReading(new VarExp("a"))),new ValueExp(new IntValue(5)))));

        //PrgState prg7 = new PrgState(ex7);
        //IRepo repo7 = new Repo(prg7,"log7.txt");
        //Controller ctr7 = new Controller(repo7);

        // Ref int v;new(v,20);print(rH(v)); wH(v,30);print(rH(v)+5);

        IStmt ex8 = new CompStmt(new CompStmt(new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),new HeapAllocation("v",
                new ValueExp(new IntValue(20)))),new CompStmt(new PrintStmt(new HeapReading(new VarExp("v"))),new HeapWriting("v",
                new ValueExp(new IntValue(30))))),new PrintStmt(new ArithExp('+',
                new HeapReading(new VarExp("v")),new ValueExp(new IntValue(5)))));

        //PrgState prg8 = new PrgState(ex8);
        //IRepo repo8 = new Repo(prg8,"log8.txt");
        //Controller ctr8 = new Controller(repo8);

        //Ref int v;new(v,20);Ref Ref int a; new(a,v); new(v,30);print(rH(rH(a)))

        IStmt ex9 = new CompStmt(new CompStmt(new CompStmt(new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),new HeapAllocation("v",
                new ValueExp(new IntValue(20)))),new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                new HeapAllocation("a", new VarExp("v")))),new HeapAllocation("v",
                new ValueExp(new IntValue(30)))),new PrintStmt(new HeapReading(new HeapReading(new VarExp("a")))));
        //PrgState prg9 = new PrgState(ex9);
        //IRepo repo9 = new Repo(prg9,"log9.txt");
        //Controller ctr9 = new Controller(repo9);


        // int v; Ref int a; v=10;new(a,22);
        // fork(wH(a,30);v=32;print(v);print(rH(a)));
        // print(v);print(rH(a))
        //int v; Ref int a; v=10; new(a,22); fork(wH(a,30); print(v); print(rH(a))); print(v); print(rH(a))


        IStmt a1 = new AssignStmt("v",  new ValueExp(new IntValue(10)));
        IStmt a2 = new HeapAllocation("a",  new ValueExp(new IntValue(22)));

        IStmt a3 = new HeapWriting("a",  new ValueExp(new IntValue(30)));
        IStmt a4 = new AssignStmt("v",  new ValueExp(new IntValue(32)));
        IStmt a5 = new PrintStmt(new VarExp("v"));
        IStmt a6 = new PrintStmt(new HeapReading(new VarExp("a")));
        IStmt a7 = new CompStmt(new CompStmt(a3,a4), new CompStmt(a5,a6));

        IStmt a8 = new ForkStmt(a7);

        IStmt a9 = new PrintStmt(new VarExp("v"));
        IStmt a10 = new PrintStmt(new HeapReading(new VarExp("a")));

        IStmt c1 = new CompStmt(a1, a2);
        IStmt c2 = new CompStmt(a8, new CompStmt(a9, a10));
        //Statement c2 = new CompStmt(a9,a10);
        //IStmt ex10 = new CompStmt(c1,c2);

        IStmt s1 = new VarDeclStmt("v", new IntType());
        IStmt s2 = new CompStmt(s1, new VarDeclStmt("a", new RefType(new IntType())));
        IStmt s3 = new CompStmt(s2,new AssignStmt("v", new ValueExp(new IntValue(10))));
        IStmt s4 = new CompStmt(s3, new HeapAllocation("a", new ValueExp(new IntValue(22))));

        IStmt s5 = new CompStmt(new CompStmt(new HeapWriting("a",  new ValueExp(new IntValue(30))),new PrintStmt(new VarExp("v")) ),new PrintStmt(new HeapReading(new VarExp("a"))));
        IStmt s6 = new ForkStmt(s5);

        IStmt s7 = new CompStmt( new PrintStmt(new VarExp("v")), new PrintStmt(new HeapReading(new VarExp("a"))));

        IStmt eex10 = new CompStmt(new CompStmt(s4,s6),s7);



        IStmt exx10 = new CompStmt(
                new VarDeclStmt("v", new IntType()),
                new CompStmt(
                        new VarDeclStmt("a", new RefType(new IntType())),
                        new CompStmt(
                                new AssignStmt("v", new ValueExp(new IntValue(10))),
                                new CompStmt(
                                        new HeapAllocation("a", new ValueExp(new IntValue(22))),
                                        new CompStmt(
                                                new ForkStmt(new HeapWriting("a", new ValueExp(new IntValue(30)))),
                                                new CompStmt(
                                                        new AssignStmt("v", new ValueExp(new IntValue(32))),

                                                        new CompStmt(
                                                                new PrintStmt(new VarExp("v")),
                                                                new CompStmt(
                                                                        new PrintStmt(new HeapReading(new VarExp("a"))),
                                                                        new CompStmt(
                                                                                new PrintStmt(new VarExp("v")),
                                                                                new PrintStmt(new HeapReading(new VarExp("a")))
                                                                        )
                                                                )
                                                        ))
                                        )

                                )
                        )
                )
        );

        IStmt ex10= new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new VarDeclStmt("a", new RefType(new IntType())),
                        new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(10))),
                                new CompStmt(new HeapAllocation("a", new ValueExp(new IntValue(22))),
                                        new CompStmt(new ForkStmt(new CompStmt(new HeapWriting("a", new ValueExp(new IntValue(30))),
                                                new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(32))),
                                                        new CompStmt(new PrintStmt(new VarExp("v")),
                                                                new PrintStmt(new HeapReading(new VarExp("a"))))))),
                                                new CompStmt(new PrintStmt(new VarExp("v")),
                                                        new PrintStmt(new HeapReading(new VarExp("a")))))))));




        IStack<IStmt> exestack1 = new ExeStack<IStmt>();
        exestack1.push(ex1);
        IDict<String, IValue> symTable1 = new Dict<String, IValue>();
        IList<IValue> out1 = new MyList<IValue>();
        IDict<StringValue, BufferedReader> ft1 = new Dict<StringValue,BufferedReader>();
        IMyHeap<Integer, IValue> h1 = new MyHeap<Integer,IValue>();
        PrgState prg1 = new PrgState(exestack1, symTable1, out1, ex1, ft1, h1);

        IStack<IStmt> exestack2 = new ExeStack<IStmt>();
        exestack2.push(ex2);
        IDict<String, IValue> symTable2 = new Dict<String, IValue>();
        IList<IValue> out2 = new MyList<IValue>();
        IDict<StringValue, BufferedReader> ft2 = new Dict<StringValue,BufferedReader>();
        IMyHeap<Integer, IValue> h2 = new MyHeap<Integer, IValue>();
        PrgState prg2 = new PrgState(exestack2, symTable2, out2, ex2, ft2, h2);

        IStack<IStmt> exestack3 = new ExeStack<IStmt>();
        exestack3.push(ex3);
        IDict<String, IValue> symTable3 = new Dict<String, IValue>();
        IList<IValue> out3 = new MyList<IValue>();
        IDict<StringValue, BufferedReader> ft3 = new Dict<StringValue,BufferedReader>();
        IMyHeap<Integer, IValue> h3 = new MyHeap<Integer, IValue>();
        PrgState prg3 = new PrgState(exestack3, symTable3, out3, ex3, ft3, h3);

        IStack<IStmt> exestack4 = new ExeStack<IStmt>();
        exestack4.push(ex4);
        IDict<String, IValue> symTable4 = new Dict<String, IValue>();
        IList<IValue> out4 = new MyList<IValue>();
        IDict<StringValue, BufferedReader> ft4 = new Dict<StringValue,BufferedReader>();
        IMyHeap<Integer, IValue> h4 = new MyHeap<Integer, IValue>();
        PrgState prg4 = new PrgState(exestack4, symTable4, out4, ex4, ft4, h4);

        IStack<IStmt> exestack5 = new ExeStack<IStmt>();
        exestack5.push(ex5);
        IDict<String, IValue> symTable5 = new Dict<String, IValue>();
        IList<IValue> out5 = new MyList<IValue>();
        IDict<StringValue, BufferedReader> ft5 = new Dict<StringValue,BufferedReader>();
        IMyHeap<Integer, IValue> h5 = new MyHeap<Integer, IValue>();
        PrgState prg5 = new PrgState(exestack5, symTable5, out5, ex10, ft5, h5);

        IStack<IStmt> exestack6 = new ExeStack<IStmt>();
        exestack6.push(ex6);
        IDict<String, IValue> symTable6 = new Dict<String, IValue>();
        IList<IValue> out6 = new MyList<IValue>();
        IDict<StringValue, BufferedReader> ft6 = new Dict<StringValue,BufferedReader>();
        IMyHeap<Integer, IValue> h6 = new MyHeap<Integer, IValue>();
        PrgState prg6 = new PrgState(exestack6, symTable6, out6, ex6, ft6, h6);

        IStack<IStmt> exestack7 = new ExeStack<IStmt>();
        exestack7.push(ex7);
        IDict<String, IValue> symTable7 = new Dict<String, IValue>();
        IList<IValue> out7 = new MyList<IValue>();
        IDict<StringValue, BufferedReader> ft7 = new Dict<StringValue,BufferedReader>();
        IMyHeap<Integer, IValue> h7 = new MyHeap<Integer, IValue>();
        PrgState prg7 = new PrgState(exestack7, symTable7, out7, ex7, ft7, h7);

        IStack<IStmt> exestack8 = new ExeStack<IStmt>();
        exestack8.push(ex8);
        IDict<String, IValue> symTable8 = new Dict<String, IValue>();
        IList<IValue> out8 = new MyList<IValue>();
        IDict<StringValue, BufferedReader> ft8 = new Dict<StringValue,BufferedReader>();
        IMyHeap<Integer, IValue> h8 = new MyHeap<Integer, IValue>();
        PrgState prg8 = new PrgState(exestack8, symTable8, out8, ex8, ft8, h8);

        IStack<IStmt> exestack9 = new ExeStack<IStmt>();
        exestack9.push(ex9);
        IDict<String, IValue> symTable9 = new Dict<String, IValue>();
        IList<IValue> out9 = new MyList<IValue>();
        IDict<StringValue, BufferedReader> ft9 = new Dict<StringValue,BufferedReader>();
        IMyHeap<Integer, IValue> h9 = new MyHeap<Integer, IValue>();
        PrgState prg9 = new PrgState(exestack9, symTable9, out9, ex9, ft9, h9);

        IStack<IStmt> exestack10 = new ExeStack<IStmt>();
        exestack10.push(ex10);
        IDict<String, IValue> symTable10 = new Dict<String, IValue>();
        IList<IValue> out10 = new MyList<IValue>();
        IDict<StringValue, BufferedReader> ft10 = new Dict<StringValue,BufferedReader>();
        IMyHeap<Integer, IValue> h10 = new MyHeap<Integer, IValue>();
        PrgState prg10 = new PrgState(exestack10, symTable10, out10, ex10, ft10, h10);





        Dict<String, IType> dict1 = new Dict<>();
        Dict<String, IType> dict2 = new Dict<>();
        Dict<String, IType> dict3 = new Dict<>();
        Dict<String, IType> dict4 = new Dict<>();
        Dict<String, IType> dict5 = new Dict<>();
        Dict<String, IType> dict6 = new Dict<>();
        Dict<String, IType> dict7 = new Dict<>();
        Dict<String, IType> dict8 = new Dict<>();
        Dict<String, IType> dict9 = new Dict<>();
        Dict<String, IType> dict10 = new Dict<>();

        try {
            ex1.typeCheck(dict1);
            ex2.typeCheck(dict2);
            ex3.typeCheck(dict3);
            ex4.typeCheck(dict4);
            ex5.typeCheck(dict5);
            ex6.typeCheck(dict6);
            ex7.typeCheck(dict7);
            ex8.typeCheck(dict8);
            ex9.typeCheck(dict9);
            ex10.typeCheck(dict10);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }



        IRepo repo1 = new Repo( "log1.txt");
        repo1.addProgram(prg1);
        Controller ctrl1 = new Controller(repo1);
        IRepo repo2 = new Repo( "log2.txt");
        repo2.addProgram(prg2);
        Controller ctrl2 = new Controller(repo2);
        IRepo repo3 = new Repo( "log3.txt");
        repo3.addProgram(prg3);
        Controller ctrl3 = new Controller(repo3);
        IRepo repo4 = new Repo( "log4.txt");
        repo4.addProgram(prg4);
        Controller ctrl4 = new Controller(repo4);
        IRepo repo5 = new Repo( "log5.txt");
        repo5.addProgram(prg5);
        Controller ctrl5 = new Controller(repo5);
        IRepo repo6 = new Repo( "log6.txt");
        repo6.addProgram(prg6);
        Controller ctrl6 = new Controller(repo6);
        IRepo repo7 = new Repo( "log7.txt");
        repo7.addProgram(prg7);
        Controller ctrl7 = new Controller(repo7);
        IRepo repo8 = new Repo( "log8.txt");
        repo8.addProgram(prg8);
        Controller ctrl8 = new Controller(repo8);
        IRepo repo9 = new Repo( "log9.txt");
        repo9.addProgram(prg9);
        Controller ctrl9 = new Controller(repo9);
        IRepo repo10 = new Repo( "log10.txt");
        repo10.addProgram(prg10);
        Controller ctrl10 = new Controller(repo10);



        ArrayList<Controller> controllers = new ArrayList<>();
        controllers.add(ctrl1);
        controllers.add(ctrl2);
        controllers.add(ctrl3);
        controllers.add(ctrl4);
        controllers.add(ctrl5);
        controllers.add(ctrl6);
        controllers.add(ctrl7);
        controllers.add(ctrl8);
        controllers.add(ctrl9);
        controllers.add(ctrl10);

        return controllers;
    }
}