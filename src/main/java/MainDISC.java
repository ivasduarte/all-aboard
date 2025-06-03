import java.util.Scanner;

public class MainDISC {

    static Scanner ler = new Scanner(System.in);

    public static void main(String[] args) {

        while(true){

            System.out.println("\n\n-------------------------- DISC MENU --------------------------");
            System.out.println("1. Verify if a train can travel between two stations.");
            System.out.println("2. Maintenance in a railway.");
            System.out.println("0. Exit");

            String option = ler.nextLine();

            try {
                switch (option) {
                    case "1":
                        railway_connectivity.Main.main(new String[0]);
                        break;
                    case "2":
                        shortest_route_finder.Main.main(new String[0]);
                        break;
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

