package com.logica.smpp.util;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class ComboBoxMultiColumn
    extends JFrame
{
    public ComboBoxMultiColumn()
    {
        String[] tabs =
            {
            "123456\tone", "2\ta really really long description goes here", "3\tthree", "41\tfour"};
        JComboBox comboBox = new JComboBox(tabs);
        comboBox.setRenderer(new TextAreaRenderer());
//        comboBox.setRenderer( new TextPaneRenderer() );
        comboBox.setSelectedIndex(2);
        getContentPane().add(comboBox);
    }

    public static void main(String[] args)
    {
        JFrame frame = new ComboBoxMultiColumn();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /*    **  Tabs are easier to use in a JTextArea, but not very flexible    */

    class TextAreaRenderer
        extends JTextArea
        implements ListCellRenderer
    {
        public TextAreaRenderer()
        {
            setTabSize(10);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
            setText(value.toString());
            setBackground(isSelected ? list.getSelectionBackground() : null);
            setForeground(isSelected ? list.getSelectionForeground() : null);
            return this;
        }
    }
    /*    **  Tabs are harder to use in a JTextPane, but much more flexible    */

    class TextPaneRenderer
        extends JTextPane
        implements ListCellRenderer
    {
        private final int TAB_COLUMN = 10;
        public TextPaneRenderer()
        {
            setMargin(new Insets(0, 0, 0, 0));
            FontMetrics fm = getFontMetrics(getFont());
            int width = fm.charWidth('w') * TAB_COLUMN;
            TabStop[] tabs = new TabStop[1];
            tabs[0] = new TabStop(width, TabStop.ALIGN_LEFT, TabStop.LEAD_NONE);
            TabSet tabSet = new TabSet(tabs);
            SimpleAttributeSet attributes = new SimpleAttributeSet();
            StyleConstants.setTabSet(attributes, tabSet);
            getStyledDocument().setParagraphAttributes(0, 0, attributes, false);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
            setText(value.toString());
            setBackground(isSelected ? list.getSelectionBackground() : null);
            setForeground(isSelected ? list.getSelectionForeground() : null);
            return this;
        }
    }
}
