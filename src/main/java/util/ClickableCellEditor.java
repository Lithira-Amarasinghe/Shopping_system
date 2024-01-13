package util;


import javax.swing.*;
import java.util.EventObject;

public class ClickableCellEditor extends DefaultCellEditor {
    public ClickableCellEditor(JTextField textField) {
        super(textField);
        textField.setEditable(false);
    }

    @Override
    public boolean isCellEditable(EventObject event) {
        return false; // Always prevent editing
    }
}

