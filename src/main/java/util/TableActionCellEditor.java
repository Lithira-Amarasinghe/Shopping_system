package util;

import javax.swing.*;
import java.util.EventObject;

public class TableActionCellEditor extends DefaultCellEditor {

    public TableActionCellEditor() {
        super(new JCheckBox());
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return false; // Prevent editing even when clicked
    }
}
