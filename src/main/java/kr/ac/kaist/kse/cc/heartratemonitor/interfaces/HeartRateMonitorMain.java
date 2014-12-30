package kr.ac.kaist.kse.cc.heartratemonitor.interfaces;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import kr.ac.kaist.kse.cc.heartratemonitor.application.HeartRateMeasuringServiceImpl;
import kr.ac.kaist.kse.cc.heartratemonitor.domain.service.HeartRateMeasuringService;

public class HeartRateMonitorMain extends JPanel {

	private static final long serialVersionUID = 4580362826280644573L;

	private JLabel infoJLabel;
	private JButton startJButton;
	
	private HeartRateMeasuringService heartRateMeasureService;
	
	public HeartRateMonitorMain() {
		heartRateMeasureService = new HeartRateMeasuringServiceImpl();
		infoJLabel = new JLabel("Not Running");
		startJButton = new JButton("Start/End");
		startJButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (!heartRateMeasureService.isRunning())
					heartRateMeasureService.start();
				else
					heartRateMeasureService.stop();

				if (heartRateMeasureService.isRunning())
					infoJLabel.setText("Running");
				else
					infoJLabel.setText("Not Running");
				updateUI();
			}
		});

		add(infoJLabel);
		add(startJButton);
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new HeartRateMonitorMain());
		frame.pack();
		frame.setVisible(true);
	}

}
