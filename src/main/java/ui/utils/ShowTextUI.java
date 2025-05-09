package ui.utils;

import org.apache.commons.lang3.StringUtils;


/**
 * The ShowTextUI class is responsible for displaying a given text in the
 * console. It provides functionality to print the provided text with padding
 * for better visibility.
 *
 * This class ensures that the text passed during its instantiation is not null
 * or empty. An exception is thrown if the provided text is invalid.
 */
public class ShowTextUI {
    private String text;


    /**
     * Constructs a ShowTextUI instance with the specified text to be displayed.
     * Validates that the provided text is not null or empty; otherwise, an
     * IllegalArgumentException is thrown.
     *
     * @param text    Text to be displayed; must not be null or empty
     * @throws        IllegalArgumentException if the provided text is null or empty
     */
    public ShowTextUI(String text) {
        if (StringUtils.isBlank(text))
            throw new IllegalArgumentException("ShowTextUI does not support null or empty text");

        this.text = text;
    }


    /**
     * Executes the main functionality of the ShowTextUI class.
     *
     * Displays the text provided during the instantiation of the ShowTextUI
     * object. The method outputs the text to the console, surrounded by blank
     * lines for enhanced readability.
     */
    public void run() {
        System.out.println("\n");
        System.out.println(this.text);
        System.out.println("\n");
    }
}
