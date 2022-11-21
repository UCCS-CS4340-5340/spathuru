/*
Copyright (C) 2003 Morten O. Alver

All programs in this directory and
subdirectories are published under the GNU General Public License as
described below.

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or (at
your option) any later version.

This program is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
USA

Further information about the GNU GPL is available at:
http://www.gnu.org/copyleft/gpl.ja.html

*/

package net.sf.jabref;

import java.util.*;

public class EntrySorter implements DatabaseChangeListener {

    //TreeSet set;
    private final ArrayList set;
    private Comparator comp;
    private String[] idArray;
    private BibtexEntry[] entryArray;
    //static BibtexEntry[] DUMMY = new BibtexEntry[0];
    private boolean outdated = false;
    private boolean changed = false;

    public EntrySorter(Map entries, Comparator comp) {
	    //set = new TreeSet(comp);
        set = new ArrayList();
        this.setComp(comp);
        Set keySet = entries.keySet();
	    if (keySet != null) {
    	    Iterator i = keySet.iterator();
    	    while (i.hasNext()) {
    		    getSet().add(entries.get(i.next()));
            }
            //Collections.sort(set, comp);
            changed = true;
            index();
	    }
    }

    public void index() {

        /*  Old version, from when set was a TreeSet.

        // The boolean "changing" is true in the situation that an entry is about to change,
        // and has temporarily been removed from the entry set in this sorter. So, if we index
        // now, we will cause exceptions other places because one entry has been left out of
        // the indexed array. Simply waiting foth this to change can lead to deadlocks,
        // so we have no other choice than to return without indexing.
        if (changing)
            return;
        */


        synchronized(getSet()) {

            // Resort if necessary:
            if (changed) {
                Collections.sort(getSet(), getComp());
                changed = false;
            }

            // Create an array of IDs for quick access, since getIdAt() is called by
            // getValueAt() in EntryTableModel, which *has* to be efficient.

	        int count = getSet().size();
            setIdArray(new String[count]);
            setEntryArray(new BibtexEntry[count]);
	        int piv = 0;
	        for (Iterator i=getSet().iterator(); i.hasNext();) {
	            //        for (int i=0; i<idArray.length; i++) {
    	        BibtexEntry entry = (BibtexEntry)i.next();
    	        getIdArray()[piv] = entry.getId();
    	        getEntryArray()[piv] = entry;
    	        piv++;
            }
        }
    }

    public boolean isOutdated() {
	return outdated;
    }

    public String getIdAt(int pos) {
        synchronized(getSet()) {
            return getIdArray()[pos];
        }
	//return ((BibtexEntry)(entryArray[pos])).getId();
    }

    public BibtexEntry getEntryAt(int pos) {
        synchronized(getSet()) {
            return getEntryArray()[pos];
        }
    }

    public int getEntryCount() {
        synchronized(getSet()) {
	        if (getEntryArray() != null)
	            return getEntryArray().length;
	        else
	        return 0;
        }
    }

    public void databaseChanged(DatabaseChangeEvent e) {
        synchronized(getSet()) {
	        if (e.getType() == DatabaseChangeEvent.ADDED_ENTRY) {
                int pos = -Collections.binarySearch(getSet(), e.getEntry(), getComp()) - 1;
                getSet().add(pos, e.getEntry());
                //addEntry(e.getEntry());
                //set.add(e.getEntry());
                //changed = true;
                //Collections.sort(set, comp);
            }
	        else if (e.getType() == DatabaseChangeEvent.REMOVED_ENTRY) {
	            getSet().remove(e.getEntry());
                changed = true;
            }
	        else if (e.getType() == DatabaseChangeEvent.CHANGED_ENTRY) {
                // Entry changed. Resort list:
                //Collections.sort(set, comp);
                int pos = Collections.binarySearch(getSet(), e.getEntry(), getComp());
                int posOld = getSet().indexOf(e.getEntry());
                if (pos < 0) {
                    getSet().remove(posOld);
                    getSet().add(-pos-1, e.getEntry());
                }
                //changed = true;
            }

    	}

    }

    /** Add an entry to the sorted set, making sure it is put in the right position
     *
     * @param entry The entry to add
     */
    private void addEntry(BibtexEntry entry) {
        int pos = -Collections.binarySearch(getSet(), entry, getComp()) - 1;
        getSet().add(pos, entry);
    }

	public ArrayList getSet() {
		return set;
	}

	public String[] getIdArray() {
		return idArray;
	}

	public void setIdArray(String[] idArray) {
		this.idArray = idArray;
	}

	public BibtexEntry[] getEntryArray() {
		return entryArray;
	}

	public void setEntryArray(BibtexEntry[] entryArray) {
		this.entryArray = entryArray;
	}

	public Comparator getComp() {
		return comp;
	}

	public void setComp(Comparator comp) {
		this.comp = comp;
	}

/* Old version, from when set was a TreeSet.

    public void databaseChanged(DatabaseChangeEvent e) {
    synchronized(set) {
        if (e.getType() == DatabaseChangeEvent.ADDED_ENTRY) {
            set.add(e.getEntry());
        }
        else if (e.getType() == DatabaseChangeEvent.REMOVED_ENTRY) {
            set.remove(e.getEntry());
        }
        else if (e.getType() == DatabaseChangeEvent.CHANGING_ENTRY) {
            set.remove(e.getEntry());
            changing = true;
        }
        else if (e.getType() == DatabaseChangeEvent.CHANGED_ENTRY) {
            // Remove and re-add the entry, to make sure it is in the
            // correct sort position.
            set.add(e.getEntry());
            changing = false;
        }

    }

}
*/
}
