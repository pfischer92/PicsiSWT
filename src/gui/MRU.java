package gui;

import java.util.prefs.Preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * Most recently used files
 * 
 * @author Christoph Stamm
 *
 */
public class MRU {
	private static int MaxMRUitems = 4;	// number of items in the MRU list
	
	MainWindow m_mainWindow;			// main window
	private Preferences m_prefs;		// system preferences used to store the MRU list

	/**
	 * Creates a MRU wrapper object
	 * @param mw main window
	 */
	public MRU(MainWindow mw) {
		m_mainWindow = mw;
		m_prefs = Preferences.userRoot();
		//m_prefs = Preferences.userNodeForPackage(PicsiSWT.class);
	}
	
	/**
	 * Adds a file name to the MRU list
	 * @param fileName
	 */
	public void addFileName(String fileName) {
		if (m_prefs == null) return;
		assert fileName != null : "filename is null";
		
		// add filename to prefs
		Preferences p = m_prefs.node("/MRU");
		if (p == null) return;

		int top = p.getInt("Top", 0);
		top++;
		if (top > MaxMRUitems) top = 1;
		p.putInt("Top", top);
		
		p.put(String.valueOf(top), fileName);
	}

	/**
	 * Adds MRU list to given menu
	 * @param recent menu
	 */
	public void addRecentFiles(Menu recent) {
		if (m_prefs == null) return;
		assert recent != null : "recent menu is null";
		
		// read filenames from prefs
		Preferences p = m_prefs.node("/MRU");
		if (p == null) return;
		
		int top = p.getInt("Top", 1);
		
		for (int i=0; i < MaxMRUitems; i++) {
			MenuItem item = new MenuItem(recent, SWT.PUSH);
			final String fileName = p.get(String.valueOf(top), "");
			if (!fileName.isEmpty()) {
				item.setText(fileName);
				item.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent event) {
						if (m_mainWindow.updateFile(fileName)) {
							// move fileName to top position in MRU
							moveFileNameToTop(fileName);
						} else {
							// remove fileName from MRU
							removeFileName(fileName);
						}
					}
				});
			}
			top--;
			if (top == 0) top = MaxMRUitems;
		}
	}
	
	/**
	 * Moves a file name to top of list
	 * @param fileName
	 */
	private void moveFileNameToTop(String fileName) {
		if (m_prefs == null) return;
		assert fileName != null : "filename is null";
		
		Preferences p = m_prefs.node("/MRU");
		if (p == null) return;
		
		int top = p.getInt("Top", 1);
		
		// find position of fileName
		for (int i=1; i <= MaxMRUitems; i++) {
			String key = String.valueOf(i);
			String fn = p.get(key, "");
			if (i != top && fileName.equals(fn)) {
				// swap i with top + 1
				top++;
				if (top > MaxMRUitems) top = 1;
				String key2 = String.valueOf(top);
				String fn2 = p.get(key2, "");
				
				p.put(key, fn2);
				p.put(key2, fn);
				p.putInt("Top", top);
			}
		}
	}
	
	/**
	 * Removes a file name from MRU list
	 * @param fileName
	 */
	private void removeFileName(String fileName) {
		if (m_prefs == null) return;
		assert fileName != null : "filename is null";
		
		Preferences p = m_prefs.node("/MRU");
		if (p == null) return;
		
		for (int i=1; i <= MaxMRUitems; i++) {
			String key = String.valueOf(i);
			String fn = p.get(key, "");
			if (fileName.equals(fn)) {
				p.put(key, "");
			}
		}
	}
	
}
