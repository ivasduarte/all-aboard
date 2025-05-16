package ui.utils;


/**
 * DevelopmentTeamUI represents a user interface component designed to display
 * details about the development team via the console.
 *
 * This class implements Runnable, allowing it to be executed in a separate
 * thread if desired. Upon execution, it outputs formatted information
 * including the names and email addresses of the development team members.
 */
public class DevelopmentTeamUI implements Runnable{


    /**
     * Constructs a new instance of the DevelopmentTeamUI class.
     *
     * This constructor initializes a user interface designed to display information
     * about the development team. The information is formatted and outputs via the console
     * when the class's {@code run} method is executed.
     */
    public DevelopmentTeamUI() {}


    /**
     * Outputs the development team information to the console.
     *
     * The method prints a formatted list of the development team members' names
     * and corresponding email addresses.
     *
     * This method is primarily used to display information about the team in a
     * visually structured way. It is typically invoked as part of the user
     * interface flow.
     */
    public void run() {
        System.out.println("\n");
        System.out.println("---------------- DEVELOPMENT TEAM ----------------");
        System.out.println("   Iva Silva        --- 1241176@isep.ipp.pt");
        System.out.println("   João Ferreira    --- 1241166@isep.ipp.pt");
        System.out.println("   José Cardoso     --- 1241323@isep.ipp.pt");
        System.out.println("   Rafael Capão     --- 1241285@isep.ipp.pt");
    }
}
