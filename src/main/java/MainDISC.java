import java.util.Scanner;

public class MainDISC {

    static Scanner ler = new Scanner(System.in);

    public static void main(String[] args) {

        while(true){

            System.out.println("\n\n-------------------------- MENU --------------------------");
            System.out.println("1. Verify if a train can travel between two stations.");
            System.out.println("2. Maintenance in a railway.");
            System.out.println("3. Find the shortest route considering must pass stations.");
            System.out.println("0. Exit");

            System.out.println("\nSelect a option from above: ");
            String option = ler.nextLine();

            try {
                switch (option) {
                    case "1":
                        railway_connectivity.Main.main(new String[0]);
                        break;
                    case "2":
                        maintenance_route_finder.Main.main(new String[0]);
                        break;
                    case "3":
                        shortest_route_finder.Main.main(new String[0]);
                    case "0":
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error executing the option: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}

