import javax.swing.*;

/**
 * Utility dialog boxes, separated into different class for readability.
 */
public class DialogUtils {

    /**
    * Prompts the user to enter an integer value using a dialog box. If the user enters a valid integer, it is returned as an Integer object.
    * If the user cancels the input or enters an invalid number, an error message is displayed and null is returned.
    *
    * @param frame the parent frame for the dialog box to appear in
    * @param message the message to display in the input dialog
    * @return the integer entered by the user, or null if input is cancelled or invalid
    */
    public static Integer promptForInt(JFrame frame, String message){
        try{
            String input =JOptionPane.showInputDialog(frame, message);

            if(input == null){
                return null;
            }
            return Integer.parseInt(input);

        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(frame, "Please enter a valid number.");
            return null;
        }
    }

    /**
     * Prompts the user to enter a string value using a dialog box. If the user enters a non-empty string, 
     * it is returned. If the user cancels the input or enters an empty string, null is returned.
     * @param frame the parent frame for the dialog box to appear in
     * @param message message to be passed into the dialog
     * @return the string entered or null if cancelled or empty string is enteted 
     */
    public static String promptForString(JFrame frame, String message){
        String input = JOptionPane.showInputDialog(frame, message);
        if(input == null || input.isBlank()){
            return null;
        }
        return input;
    }

    /**
     * Displays a dropdown dialog with the given title and choices, allowing the user to select one of the options.
     * If the user selects an option and confirms, the selected option is returned as a string. If the user cancels the dialog, null is returned.
     * @param frame the parent fram for the dialog box to appear in
     * @param title Title of the dialog box
     * @param choices The passed in options to select in the dropdown
     * @return The selected option, or null if nothing is selected or dialog is cancelled 
     */
    public static String dropdown(JFrame frame, String title, String[] choices){
        JComboBox<String> comboBox = new JComboBox<>(choices);
        int result = JOptionPane.showConfirmDialog(frame, comboBox, title, JOptionPane.OK_CANCEL_OPTION);
        if(result != JOptionPane.OK_OPTION){
            return null;
        }
        return (String) comboBox.getSelectedItem();
    }
}