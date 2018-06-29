package files;
import javax.swing.JTextArea;

import main.PicsiSWT;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Display;

/**
 * Image document class
 * 
 * @author Christoph Stamm
 *
 */
public class Document {
	private String m_fileName;	// image file name
	private IImageFile m_file;	// image file
	private int m_fileType;		// image file type
	
	/**
	 * Loads an image file
	 * @param filename
	 * @param filetype
	 * @param display
	 * @return
	 * @throws Exception
	 */
	public Image load(String filename, int filetype, Display display) throws Exception {
		m_fileType = filetype;
		if (m_fileType >= PicsiSWT.IMAGE_PBM && m_fileType <= PicsiSWT.IMAGE_PPM) {
			m_file = new PNM();
		} else {
			m_file = new BMP();
		}
		if (m_file != null) {
			m_fileName = filename;
			return m_file.read(filename, display);
		}
		return null;
	}
	
	/**
	 * Saves an image file
	 * @param image
	 * @param filename
	 * @param filetype
	 * @throws Exception
	 */
	public void save(Image image, String filename, int filetype) throws Exception {
		if (filename == null) {
			if (m_file != null && m_fileName != null && m_fileType >= 0) {
				// save with existing file name and file type
				filename = m_fileName;
				filetype = m_fileType;
			} else {
				assert filename != null : "filename is null";
				assert filetype >= 0 : "wrong filetype";
				
				if (filetype >= PicsiSWT.IMAGE_PBM && filetype <= PicsiSWT.IMAGE_PPM) {
					m_file = new PNM();
				} else {
					m_file = new BMP();
				}				
			}
		} else {
			// save with new file name or new file type
			assert filetype >= 0 : "wrong filetype";
			if (m_file == null || filetype != m_fileType) {
				if (filetype >= PicsiSWT.IMAGE_PBM && filetype <= PicsiSWT.IMAGE_PPM) {
					m_file = new PNM();
				} else {
					m_file = new BMP();
				}				
			}
		}
		if (m_file != null) {
			m_fileName = filename;
			m_file.save(filename, filetype, image);
		}
	}
	
	/**
	 * Returns true if this image is in binary format
	 * @return
	 */
	public boolean isBinaryFormat() {
		assert m_file != null : "no image file available";
		return m_file.isBinaryFormat();		
	}
	
	/**
	 * Returns the name of this image file
	 * @return
	 */
	public String getFileName() {
		return m_fileName;
	}
	
	/**
	 * Returns the file type of this image file
	 * @return
	 */
	public int getFileType() {
		return m_fileType;
	}
	
	/**
	 * Converts the given image data from binary to ASCII and writes it to the given text area 
	 * @param image image data
	 * @param text text area
	 */
	public void displayTextOfBinaryImage(Image image, JTextArea text) {
		assert m_file != null : "no image file available";
		
		text.removeAll();
		m_file.displayTextOfBinaryImage(image, text);			
	}

}
