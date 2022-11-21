package net.sf.jabref;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Action used to produce a "Browse" button for one of the text fields.
 */
public class BrowseAction extends AbstractAction implements ActionListener {

    private JFrame frame=null;
    //JDialog dialog=null;
    JTextField comp;
    private boolean dir;

    public BrowseAction(JFrame frame, JTextField tc, boolean dir) {
        super(Globals.lang("Browse"));
        this.setFrame(frame);
        this.setDir(dir);
        comp = tc;

    }

    /*public BrowseAction(JDialog dialog, JTextField tc, boolean dir) {
        super(Globals.lang("Browse"));
        this.dialog = dialog;
        this.dir = dir;
        comp = tc;

    } */

    public void actionPerformed(ActionEvent e) {
        String chosen = null;
        if (isDir())
            chosen = Globals.getNewDir(getFrame(), new File(comp.getText()), Globals.NONE,
                    JFileChooser.OPEN_DIALOG, false);
        else
            chosen = Globals.getNewFile(getFrame(), new File(comp.getText()), Globals.NONE,
                    JFileChooser.OPEN_DIALOG, false);
        if (chosen != null) {
            File newFile = new File(chosen);
            comp.setText(newFile.getPath());
        }
    }

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public boolean isDir() {
		return dir;
	}

	public void setDir(boolean dir) {
		this.dir = dir;
	}

}
