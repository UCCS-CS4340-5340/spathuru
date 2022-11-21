package net.sf.jabref;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;


public class CheckBoxMessage extends JPanel {
  BorderLayout borderLayout1 = new BorderLayout();
  private GridBagLayout gbl = new GridBagLayout();
  private GridBagConstraints con = new GridBagConstraints();
  JCheckBox cb;

  public CheckBoxMessage(String message, String cbText, boolean defaultValue) {
    cb = new JCheckBox(cbText, defaultValue);
    setLayout(getGbl());
    getCon().gridwidth = GridBagConstraints.REMAINDER;

    JLabel lab = new JLabel(message+"\n"), empt = new JLabel("");
    cb.setHorizontalAlignment(JLabel.LEFT);
    getGbl().setConstraints(lab, getCon());
    add(lab);
    getCon().anchor = GridBagConstraints.WEST;
    getCon().insets = new Insets(10, 0, 0, 0);
    getGbl().setConstraints(cb, getCon());
    add(cb);
  }

  public boolean isSelected() {
    return cb.isSelected();
  }

public GridBagConstraints getCon() {
	return con;
}

public void setCon(GridBagConstraints con) {
	this.con = con;
}

public GridBagLayout getGbl() {
	return gbl;
}

public void setGbl(GridBagLayout gbl) {
	this.gbl = gbl;
}
}
