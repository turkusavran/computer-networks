public class Project
{
    public final static void main(String[] argv)
    {
        DVRoute simulator;

        System.out.println("Initializing DV Route..");

        simulator = new DVRoute();

        System.out.println("Running DV Route..");

        simulator.runSimulator();
    }
}
