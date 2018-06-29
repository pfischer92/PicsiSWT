// http://help.eclipse.org/indigo/index.jsp?topic=%2Forg.eclipse.rap.help%2Fhelp%2Fhtml%2Freference%2Fapi%2Forg%2Feclipse%2Fswt%2Fgraphics%2Fpackage-summary.html

package imageprocessing;

import gui.TwinView;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * Image processing class: contains widely used image processing functions
 * 
 * @author Christoph Stamm
 *
 */
public class ImageProcessing {
	private static class ImageMenuItem {
		private String m_text;
		private int m_accelerator;
		private IImageProcessor m_process;
		
		public ImageMenuItem(String text, int accelerator, IImageProcessor proc) {
			m_text = text;
			m_accelerator = accelerator;
			m_process = proc;
		}
	}
	
	private TwinView m_views;
	private ArrayList<ImageMenuItem> m_menuItems = new ArrayList<ImageMenuItem>();
	
	/**
	 * Registration of image operations
	 * @param views
	 */
	public ImageProcessing(TwinView views) {
		assert views != null : "views are null";
		m_views = views;
		
		m_menuItems.add(new ImageMenuItem("&Invert\tF1", SWT.F1, new Inverter()));
		m_menuItems.add(new ImageMenuItem("&Grauwertbild\tF2", SWT.F2, new Grauwertbild()));
		m_menuItems.add(new ImageMenuItem("&Dithering\tF3", SWT.F3, new Dithering()));
		m_menuItems.add(new ImageMenuItem("&Rotate\tF4", SWT.F4, new Rotate()));
		m_menuItems.add(new ImageMenuItem("&Scale\tF5", SWT.F5, new Scale()));
		m_menuItems.add(new ImageMenuItem("&Filter\tF6", SWT.F6, new Filter()));
		m_menuItems.add(new ImageMenuItem("&Edge Detection\tF7", SWT.F7, new EdgeDetection()));
		m_menuItems.add(new ImageMenuItem("&Convolve Efficient\tF8", SWT.F8, new ConvolveEfficient()));
		m_menuItems.add(new ImageMenuItem("&Filter Master\tF9", SWT.F9, new FilterMaster()));
		m_menuItems.add(new ImageMenuItem("&Flip at axis\tF10", SWT.F10, new FlipAtYAxis()));
		m_menuItems.add(new ImageMenuItem("&Affine\tF11", SWT.F11, new Affine()));
		m_menuItems.add(new ImageMenuItem("&All RGB\tF12", SWT.F12, new AllRGB()));
		// TODO add here further image processing objects (they are inserted into the Image menu)
	}
	
	public void createMenuItems(Menu menu) {
		for(final ImageMenuItem item : m_menuItems) {
			MenuItem mi = new MenuItem(menu, SWT.PUSH);
			mi.setText(item.m_text);
			mi.setAccelerator(item.m_accelerator);
			mi.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					Image output = null;
					try {
						output = item.m_process.run(m_views.getFirstImage(), m_views.getFirstimageType());
					} catch(Throwable e) {
						int last = item.m_text.indexOf('\t');
						if (last == -1) last = item.m_text.length();
						String location = item.m_text.substring(0, last).replace("&", "");
						m_views.m_mainWnd.showErrorDialog("ImageProcessing", location, e);
					}						
					if (output != null) {
						m_views.showImageInSecondView(output);
					}
				}
			});
		}
	}
	
	public boolean isEnabled(int i) {
		return m_menuItems.get(i).m_process.isEnabled(m_views.getFirstimageType());
	}

	// add general image processing class methods here
	public static int clamp8(double d){
		if(d < 0) return 0;
		if(d > 255) return 255;
		return (int)d;
	}
}
