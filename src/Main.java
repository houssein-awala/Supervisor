public class Main {
    static Supervisor supervisor;
    static Simulator simulator;
    public static void main(String [] args)
    {
        supervisor=new Supervisor();
        supervisor.start();
        simulator=new Simulator();
        simulator.start();
    }
}
