package files;

import javax.swing.JTextArea;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public interface IImageFile {
	/**
	 * Loads an image file from the file system
	 * @param fileName
	 * @param display
	 * @return
	 * @throws Exception
	 */
	public Image read(String fileName, Display display) throws Exception;
	
	/**
	 * Saves an image file to the file system
	 * @param fileName
	 * @param fileType
	 * @param image
	 * @throws Exception
	 */
	public void save(String fileName, int fileType, Image image) throws Exception;
	
	/**
	 * Converts the given image data from binary to ASCII and writes it to the given text area 
	 * @param image
	 * @param text
	 */
	public void displayTextOfBinaryImage(Image image, JTextArea text);
	
	/**
	 * Returns true if this image is in binary format
	 * @return
	 */
	public boolean isBinaryFormat();
}
