package gui;

import java.util.StringTokenizer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import main.PicsiSWT;

/**
 * Option pane class (based on SWT MessageBox)
 * http://grepcode.com/file/repo1.maven.org/maven2/org.eclipse.rap/org.eclipse.rap.rwt/1.4.0/org/eclipse/swt/widgets/MessageBox.java
 * 
 * @author Christoph Stamm
 *
 */
public class OptionPane extends Dialog {
	private static final int SPACING = 20;
	private static final int MAX_WIDTH = 640;

	private Shell shell;
	private Image image;
	private String message;
	private String title;
	private Text text;
	private int returnCode;
	private String returnText;

	public OptionPane(Shell parent, int style) {
		super(parent, style);
	}

	public int open(Object[] options, int defOption, boolean textInput) {
		determineImageFromStyle();
		shell = new Shell(getParent(), SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL);
		shell.setText(title);
		createControls(options, defOption, textInput);
		shell.setBounds(computeShellBounds());
		shell.pack();
		shell.open();
		Display display = shell.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return returnCode;
	}

	private void determineImageFromStyle() {
		image = null;
		int style = getStyle();
		int systemImageId = -1;
		if ((style & SWT.ICON_ERROR) != 0) {
			systemImageId = SWT.ICON_ERROR;
		} else if ((style & SWT.ICON_INFORMATION) != 0) {
			systemImageId = SWT.ICON_INFORMATION;
		} else if ((style & SWT.ICON_QUESTION) != 0) {
			systemImageId = SWT.ICON_QUESTION;
		} else if ((style & SWT.ICON_WARNING) != 0) {
			systemImageId = SWT.ICON_WARNING;
		} else if ((style & SWT.ICON_WORKING) != 0) {
			systemImageId = SWT.ICON_WORKING;
		}
		if (systemImageId != -1) {
			image = getParent().getDisplay().getSystemImage(systemImageId);
		}
	}

	private Rectangle computeShellBounds() {
		Rectangle result = new Rectangle(0, 0, 0, 0);
		Point preferredSize = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Rectangle parentSize = getParent().getBounds();
		result.x = (parentSize.width - preferredSize.x) / 2 + parentSize.x;
		result.y = (parentSize.height - preferredSize.y) / 2 + parentSize.y;
		result.width = Math.min(preferredSize.x, MAX_WIDTH);
		result.height = preferredSize.y;
		return result;
	}

	private void createControls(Object[] options, int defOption, boolean textInput) {
		shell.setLayout(new GridLayout(2, false));
		createImage();
		createText();
		if (textInput) createInput();
		createButtons(options, defOption);
	}

	private void createImage() {
		if (image != null) {
			Label label = new Label(shell, SWT.CENTER);
			GridData data = new GridData(SWT.CENTER, SWT.TOP, false, false);
			data.widthHint = image.getBounds().width + SPACING;
			label.setLayoutData(data);
			label.setImage(image);
		}
	}

	private void createText() {
		Label textLabel = new Label(shell, SWT.WRAP);
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		int imageWidth = image == null ? 0 : image.getBounds().width;
		int maxTextWidth = MAX_WIDTH - imageWidth - 2*SPACING;
		int maxLineWidth = getMaxMessageLineWidth();
		if (maxLineWidth > maxTextWidth) {
			data.widthHint = maxTextWidth;
		}
		textLabel.setLayoutData(data);
		textLabel.setText(message);
	}

	private void createInput() {
		text = new Text(shell, SWT.SINGLE | SWT.BORDER);
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		data.horizontalSpan = 2;
		text.setLayoutData(data);
	}

	private void createButtons(Object[] options, int defOption) {
		assert defOption >= 0 && defOption < options.length;

		Composite buttonArea = new Composite(shell, SWT.NONE);
		buttonArea.setLayout(new GridLayout(0, true));
		GridData buttonData = new GridData(SWT.CENTER, SWT.CENTER, true, false);
		buttonData.horizontalSpan = 2;
		buttonArea.setLayoutData(buttonData);
		for(int i=0; i < options.length; i++) {
			createButton(buttonArea, options[i].toString(), i);
		}
		shell.setDefaultButton((Button)buttonArea.getChildren()[defOption]);
	}

	private void createButton(Composite parent, String text, int option) {
		((GridLayout) parent.getLayout()).numColumns++;
		Button result = new Button(parent, SWT.PUSH);
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		int widthHint = convertTextWidthToPixels(text);
		Point minSize = result.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
		data.widthHint = Math.max(widthHint, minSize.x);
		result.setLayoutData(data);
		result.setText(text);
		result.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				OptionPane.this.returnCode = option;
				if (option == 0 && OptionPane.this.text != null) {
					// save text input
					OptionPane.this.returnText = OptionPane.this.text.getText();
				}
				shell.close();
			}
		});
	}

	private int getMaxMessageLineWidth() {
		GC gc = new GC(shell);
		gc.setFont(shell.getFont());
		int result = 0;
		StringTokenizer tokenizer = new StringTokenizer(message, "\n");
		
		while (tokenizer.hasMoreTokens()) {
			String line = tokenizer.nextToken();
			int lineWidth = gc.stringExtent(line).x;
			result = Math.max(result, lineWidth);
		}
		return result;
	}

	private int convertTextWidthToPixels(String text) {
		GC gc = new GC(shell);
		gc.setFont(shell.getFont());
		return gc.stringExtent(text).x + SPACING;
	}

	public static int showOptionDialog(String message, int style, Object[] options, int defOption) {
		OptionPane op = new OptionPane(PicsiSWT.s_shell, style);
		op.title = "Options";
		op.message = message;
		op.returnCode = -1;
		return op.open(options, defOption, false);
	}
	
	public static String showInputDialog(String message) {
		OptionPane op = new OptionPane(PicsiSWT.s_shell, SWT.ICON_QUESTION);
		op.title = "Input";
		op.message = message;
		op.returnCode = -1;
		if (op.open(new Object[]{ SWT.getMessage("SWT_OK"), SWT.getMessage("SWT_Cancel")}, 0, true) == 0) {
			// OK
			return op.returnText;
		} else {
			return null;
		}
	}
}
