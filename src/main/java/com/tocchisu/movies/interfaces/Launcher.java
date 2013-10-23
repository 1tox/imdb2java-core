package com.tocchisu.movies.interfaces;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Launcher extends JPanel implements ActionListener {
	private static final Logger logger = LoggerFactory.getLogger(Launcher.class);
	private static final String NEW_LINE = "\n";
	private final JButton openButton;
	private final JEditorPane log;
	private final JFileChooser fc;

	public Launcher() {
		super(new BorderLayout());

		log = createEditorPane();
		log.setMargin(new Insets(5, 5, 5, 5));
		log.setSize(50, 50);
		log.setEditable(false);
		log.setForeground(Color.BLUE);
		JScrollPane logScrollPane = new JScrollPane(log);

		// Create a file chooser
		fc = new JFileChooser();
		fc.setFileFilter(new FileFilter() {

			@Override
			public String getDescription() {
				return "IMDB plain text files or archives directory";
			}

			@Override
			public boolean accept(File f) {
				return f.isDirectory();
			}
		});
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		// Create the open button. We use the image from the JLF
		// Graphics Repository (but we extracted it from the jar).
		openButton = new JButton("Select a directory...", createImageIcon("images/Open16.gif"));
		openButton.addActionListener(this);
		openButton.setFocusPainted(false);

		// For layout purposes, put the buttons in a separate panel
		JPanel buttonPanel = new JPanel(); // use FlowLayout
		buttonPanel.add(new JLabel("Please select the directory containing the IMDB plain text files or archives"));
		buttonPanel.add(openButton);
		buttonPanel.add(getHyperLinkButton());

		// Add the buttons and the log to this panel.
		add(buttonPanel, BorderLayout.PAGE_START);
		add(logScrollPane, BorderLayout.CENTER);
	}

	public void actionPerformed(ActionEvent e) {

		// Handle open button action.
		if (e.getSource() == openButton) {
			int returnVal = fc.showOpenDialog(Launcher.this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				printInfo(file);
			} else {
				log.setText("Selection canceled" + NEW_LINE);
			}
			log.setCaretPosition(log.getDocument().getLength());
		}
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = Launcher.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			logger.error("Couldn't find file: " + path);
			return null;
		}
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event dispatch thread.
	 */
	private static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame("DBMovie");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add content to the window.
		frame.add(new Launcher());

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public void printInfo(File directory) {
		if (directory == null)
			return;
		File[] foundFiles = directory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (!name.matches("^movies\\.list(\\.gz)?$")) {
					return false;
				}
				return true;
			}
		});
		if (foundFiles == null || foundFiles.length == 0) {
			final Color currentForeground = log.getForeground();
			// log.setForeground(Color.RED);
			// log.append("At least one of the following file is missing in the selected directory :"
			// + NEW_LINE);
			// log.append("-> the plain text IMDB movies file (movies.list)" +
			// NEW_LINE);
			// log.append("-> the IMDB archive file (movies.list.gz)" +
			// NEW_LINE);
			// log.append("These files could be downloaded at http://www.imdb.com/interfaces");
			// log.repaint();
			return;
		}
		GZIPInputStream gzipInputStream = null;
		OutputStream out = null;
		try {
			// To Uncompress GZip File Contents we need to open the gzip
			// file.....
			for (File file : foundFiles) {
				String fileName = file.getName();
				if (fileName.matches("^\\.*\\.gz$")) {
					log.setText("Extracting archive " + directory + NEW_LINE);
					gzipInputStream = new GZIPInputStream(new FileInputStream(directory));
					byte[] buf = new byte[1024]; // size can be changed
													// according to
					// programmer's need.
					int len;
					gzipInputStream = new GZIPInputStream(new FileInputStream(directory));
					String outFilename = directory + ".list";
					out = new FileOutputStream(outFilename);
					while ((len = gzipInputStream.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
				} else if (fileName.matches("^\\.*\\.list$")) {

				}
			}
		} catch (IOException e) {
			log.setText("Exception has been thrown" + e + NEW_LINE);
		} finally {
			try {
				if (gzipInputStream != null)
					gzipInputStream.close();
				if (out != null)
					out.close();
			} catch (IOException e) {

			}
		}
	}

	public static void main(String[] args) {
		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				createAndShowGUI();
			}
		});
	}

	private JButton getHyperLinkButton() {
		try {
			final URI uri = new URI("http://java.sun.com");
			class OpenUrlAction implements ActionListener {
				@Override
				public void actionPerformed(ActionEvent event) {
					try {
						Desktop.getDesktop().browse(uri);
					} catch (IOException e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
			JButton button = new JButton();
			button.setText("<HTML>Click the <FONT color=\"#000099\"><U>link</U></FONT>" + " to go to the IMDB interfaces location</HTML>");
			button.setHorizontalAlignment(SwingConstants.LEFT);
			button.setBorderPainted(false);
			button.setOpaque(false);
			button.setBackground(Color.WHITE);
			button.setToolTipText(uri.toString());
			if (Desktop.isDesktopSupported())
				button.addActionListener(new OpenUrlAction());
			button.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseEntered(MouseEvent e) {
					setCursor(new Cursor(Cursor.HAND_CURSOR));
				}

				@Override
				public void mouseExited(MouseEvent e) {
					setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}

			});
			return button;
		} catch (URISyntaxException e) {
			logger.error(e.getMessage(), e);
			return null;
		}

	}

	private JEditorPane createEditorPane() {
		JEditorPane editorPane = new JEditorPane();
		editorPane.setEditable(false);
		URL helpURL;
		try {
			helpURL = new URL("classpath:test.html");
			if (helpURL != null) {
				try {
					editorPane.setPage(helpURL);
				} catch (IOException e) {
					System.err.println("Attempted to read a bad URL: " + helpURL);
				}
			} else {
				System.err.println("Couldn't find file: test.html");
			}
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return editorPane;
	}
}