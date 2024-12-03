package Helpers;

import java.awt.Container;

import javax.swing.JOptionPane;

public class ErrorHandler {

    public ErrorHandler(String errMessage, Container parent) {
        JOptionPane.showMessageDialog(parent, "Error message: " + errMessage, "Error Found", JOptionPane.ERROR_MESSAGE);
    }
}
