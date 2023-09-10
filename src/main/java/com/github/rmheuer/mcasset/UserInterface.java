package com.github.rmheuer.mcasset;

import java.awt.*;
import java.awt.FileDialog;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.filechooser.FileView;

public class UserInterface extends Frame {
    public Choice versionList;
    public static UserInterface ui;
    public static File dir;
    public String path = "";
    public JProgressBar loadingBar = new JProgressBar();
    public Label pathLabel;

    UserInterface() {
	ui = this;
	this.setTitle("MCAssetExtractor-GUI");
	this.setSize(400, 400);
	this.setLayout(new BorderLayout());
	this.setVisible(true);
	this.addWindowListener(new WindowAdapter() {
	    public void windowClosing(WindowEvent we) {
		dispose();
		System.exit(0);
	    }
	});
	Panel bottomPanel = new Panel();
	bottomPanel.setLayout(new GridLayout(3, 0));
	this.add(loadingBar);
	loadingBar.setMinimum(0);
	loadingBar.setMaximum(100);
	loadingBar.setValue(100);
	loadingBar.updateUI();
	this.add(bottomPanel, BorderLayout.PAGE_END);
	Panel versionListPanel = new Panel();
	versionListPanel.setSize(250, 50);
	versionListPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
	bottomPanel.add(versionListPanel, BorderLayout.PAGE_END);
	Panel downloadPanel = new Panel();
	downloadPanel.setSize(250, 50);
	downloadPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
	bottomPanel.add(downloadPanel, BorderLayout.PAGE_END);
	Panel pathPanel = new Panel();
	pathPanel.setSize(250, 50);
	pathPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
	path = System.getProperty("user.home") + "/MCExtractedAssets-" + McAssetExtractor.getLatestRelease();
	dir = new java.io.File(System.getProperty("user.home") + "/MCExtractedAssets-" + McAssetExtractor.getLatestRelease());
	pathLabel = new Label("Current Path: " + path);
	pathPanel.add(pathLabel);
	bottomPanel.add(pathPanel, BorderLayout.PAGE_END);
	Label chooseVerText = new Label("Loading versions...");
	reload();
	versionListPanel.add(chooseVerText);
	versionList = new Choice();
	versionList.add("Latest Release " + "(" + McAssetExtractor.getLatestRelease() + ")");
	versionList.add("Latest Snapshot " + "(" + McAssetExtractor.getLatestSnapshot() + ")");
	for (String item : McAssetExtractor.getVersions())
	{
	    versionList.add(item);
	}
	versionListPanel.add(versionList);
	chooseVerText.setText("Version:");
	Button getPackButton = new Button();
	getPackButton.setLabel("Download");
	getPackButton.addMouseListener(new MouseAdapter() {
	    public void mouseClicked(MouseEvent me) {
		new Runnable() {
		    public void run() {
			new McAssetExtractor().run(versionList.getSelectedItem(), dir);
		    }
		};
		do
		{
		    pathLabel.setText(McAssetExtractor.status);
		    try
		    {
			Thread.sleep(33);
		    }
		    catch (InterruptedException e)
		    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		    reload();
		} while (true);
	    }
	});
	downloadPanel.add(getPackButton);
	Button setLocationButton = new Button();
	setLocationButton.setLabel("Change");
	setLocationButton.addMouseListener(new MouseAdapter() {
	    public void mouseClicked(MouseEvent me) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		int result = fileChooser.showOpenDialog(ui);
		if (result == JFileChooser.APPROVE_OPTION)
		{
		    dir = fileChooser.getCurrentDirectory();
		    ui.path = dir.getAbsolutePath();
		    pathLabel.setText("Current Path: " + ui.path);
		}
	    }
	});
	pathPanel.add(setLocationButton);
	reload();
	
    }

    public void reload() {
	this.update(this.getGraphics());
	this.print(this.getGraphics());
	this.setSize(this.getWidth() - 1, this.getHeight());
	this.setSize(this.getWidth() + 1, this.getHeight());
    }
}
