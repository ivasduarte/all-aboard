package ui.utils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * The Utils class provides a collection of utility methods for common tasks.
 * These include reading input from the console, displaying lists, and handling
 * user selections.
 */
public class Utils {

    /**
     * Reads a line of input from the console after displaying the provided prompt.
     *
     * @param prompt    The message to be displayed to the user before reading input
     * @return          The input line entered by the user, or null if an exception occurs
     */
    public static String readLineFromConsole(String prompt) {
        try {
            System.out.print("\n" + prompt);

            InputStreamReader converter = new InputStreamReader(System.in);
            BufferedReader in = new BufferedReader(converter);

            return in.readLine();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Reads an integer value from the console after displaying the provided prompt.
     * This method repeatedly prompts the user until a valid integer is entered.
     * If a non-integer value is entered, an error is logged, and the user is asked
     * to provide input again.
     *
     * @param prompt    The message to be displayed to the user before reading input.
     * @return          The integer value entered by the user.
     */
    public static int readIntegerFromConsole(String prompt) {
        do {
            try {
                String input = readLineFromConsole(prompt);

                int value = Integer.parseInt(input);

                return value;
            } catch (NumberFormatException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (true);
    }


    /**
     * Reads a double value from the console after displaying the provided prompt.
     * This method repeatedly prompts the user until a valid double value is entered.
     * If an invalid value is entered, an error is logged, and the user is prompted again.
     *
     * @param prompt    The message to be displayed to the user before reading input.
     * @return          The double value entered by the user.
     */
    public static double readDoubleFromConsole(String prompt) {
        do {
            try {
                String input = readLineFromConsole(prompt);

                double value = Double.parseDouble(input);

                return value;
            } catch (NumberFormatException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (true);
    }


    /**
     * Reads a date from the console based on the format "dd-MM-yyyy" after displaying
     * the provided prompt. This method repeatedly prompts the user until a valid date
     * is entered. If an invalid date format is entered, an error is logged,
     * and the user is asked to provide input again.
     *
     * @param prompt    The message to be displayed to the user before reading the date input
     * @return          The date entered by the user, parsed into a {@code Date} object
     */
    public static Date readDateFromConsole(String prompt) {
        do {
            try {
                String strDate = readLineFromConsole(prompt);

                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

                Date date = df.parse(strDate);

                return date;
            } catch (ParseException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (true);
    }


    /**
     * Prompts the user with a message and requests confirmation by typing "s" for yes
     * or "n" for no. The method will keep prompting until a valid response is entered.
     *
     * @param message   The confirmation message to display to the user.
     * @return          True if the user confirms with "s" (case insensitive), false otherwise.
     */
    static public boolean confirm(String message) {
        String input;
        do {
            input = Utils.readLineFromConsole("\n" + message + "\n");
        } while (!input.equalsIgnoreCase("s") && !input.equalsIgnoreCase("n"));

        return input.equalsIgnoreCase("s");
    }


    /**
     * Displays a list of elements with a header and allows the user to select one of the elements.
     * The method first shows the list using {@code showList} and then allows the user to select
     * an element from the list using {@code selectsObject}. If the user selects "Cancel" (option 0),
     * the method returns null.
     *
     * @param list      The list of elements to display and select from.
     * @param header    The header message to display above the list.
     * @return          The selected object from the list, or null if "Cancel" is chosen.
     */
    static public Object showAndSelectOne(List list, String header) {
        showList(list, header);
        return selectsObject(list);
    }


    /**
     * Displays a list of elements with a header and allows the user to select one of the elements by index.
     * The method first shows the list using {@code showList} and then allows the user to select
     * an index using {@code selectsIndex}. The user can also cancel the selection by choosing option 0.
     *
     * @param list      The list of elements to display and allow selection from.
     * @param header    The header message to display above the list.
     * @return          The index of the selected element (0-based), or -1 if "Cancel" is chosen.
     */
    static public int showAndSelectIndex(List list, String header) {
        showList(list, header);
        return selectsIndex(list);
    }


    /**
     * Displays a list of elements with a header in the console. Each element in the
     * list is displayed as a numbered item starting from 1. An option to cancel the
     * operation is added as "0 - Cancel" at the end of the list.
     *
     * @param list      The list of elements to be displayed. Each element's
     *                  {@code toString()} method is used to generate its string
     *                  representation.
     * @param header    The header message to be displayed above the list.
     */
    static public void showList(List list, String header) {
        System.out.println(header);

        int index = 0;
        for (Object o : list) {
            index++;

            System.out.println("  " + index + " - " + o.toString());
        }
        //System.out.println();
        System.out.println("  0 - Cancel");
    }


    /**
     * Prompts the user to select an object from the given list. The list elements
     * are indexed starting from 1, and the user can choose an object by typing
     * its corresponding number. Option 0 allows the user to cancel, returning null.
     *
     * @param list  The list of objects to select from. Each object in the list should
     *              have a meaningful string representation for display purposes.
     * @return      The selected object from the list, or null if the user cancels the
     *              operation by entering 0.
     */
    static public Object selectsObject(List list) {
        String input;
        int value;
        do {
            input = readLineFromConsole("Type your option: ");
            value = Integer.valueOf(input);
        } while (value < 0 || value > list.size());

        if (value == 0) {
            return null;
        } else {
            return list.get(value - 1);
        }
    }


    /**
     * Prompts the user to select an index from the provided list. The input is
     * taken from the console, and the user is repeatedly asked to provide valid
     * input until a number within the range of the list indices is entered.
     * The returned value is 0-based, and the user can effectively cancel the
     * operation by entering 0, resulting in a return value of -1.
     *
     * @param list    The list of elements for which an index is to be selected.
     * @return        The 0-based index of the selected element, or -1 if the
     *                selection is cancelled by choosing option 0.
     */
    static public int selectsIndex(List list) {
        String input;
        int value;
        do {
            input = readLineFromConsole("Type your option: ");
            try {
                value = Integer.valueOf(input);
            } catch (NumberFormatException ex) {
                value = -1;
            }
        } while (value < 0 || value > list.size());

        return value - 1;
    }
}
