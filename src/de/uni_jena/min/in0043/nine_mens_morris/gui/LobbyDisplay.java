package de.uni_jena.min.in0043.nine_mens_morris.gui;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.uni_jena.min.in0043.nine_mens_morris.net.ProtocolOperators;

public class LobbyDisplay extends JFrame {

	private static Logger log = LogManager.getLogger();
	private static final long serialVersionUID = 3402979622585680695L;
	private static final int width = 600;
	private static final int height = 600;

	private JButton challenge;
	private JList<String> players;
	private DefaultListModel<String> lm;
	private int[] userIDs;
	private JScrollPane sp;
	private byte[] cmdBuf;
	private boolean cmdSent = false;

	public LobbyDisplay() {
		this("The Lobby");
	}

	public LobbyDisplay(String title) {
		super(title);

		final JFrame me = this;
		challenge = new JButton("Issue challenge");
		challenge.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selection = players.getSelectedIndex();
				if(selection != -1) {
					log.debug("you selected " + players.getSelectedValue() + " (" + userIDs[selection] + ")");
					setCmdBuf(new byte[]{ProtocolOperators.CHALLENGE[0], (byte) userIDs[selection], 0});
				} else {
					log.debug("no opponent selected");
				}
			}
		});

		lm = new DefaultListModel<String>();
		players = new JList<String>();
		sp = new JScrollPane(players, VERTICAL_SCROLLBAR_AS_NEEDED,HORIZONTAL_SCROLLBAR_NEVER);

		add(sp);
		add(challenge);

		setSize(width, height);
		setResizable(false);
		setLayout(new GridLayout(2, 1));

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				int n = JOptionPane.showConfirmDialog(
						me,
						"Would you like to close the application?",
						null,
						JOptionPane.YES_NO_OPTION);
				if(n == 0) {
					setCmdBuf(ProtocolOperators.BYE);
					me.setVisible(false);
				}

			}
		});
	}

	public synchronized byte[] getCmdBuf() {
		if(!cmdSent) {
			cmdSent = true;
			return cmdBuf;
		}
		return null;
	}

	public synchronized void setChallenge() {
		log.debug("disabling challenge button");
		challenge.setEnabled(false);
	}
	
	public synchronized void setCmdBuf(byte[] cmdBuf) {
		log.entry();
		log.debug("cmdSent = " + cmdSent);
		if(cmdSent) {
			this.cmdBuf = cmdBuf;
			cmdSent = false;
		}
	}

	public synchronized void setNormal() {
		log.debug("enabling challenge button");
		challenge.setEnabled(true);
	}
	
	public synchronized void setPlayerList(String[] userNames, int[] userIDs) {		
		if(userNames != null) {
			players.setListData(userNames);
		} else {
			log.debug("I'm alone, clearing list");
			lm.clear();
			players.setModel(lm); // why is this necessary?
		}

		this.userIDs = userIDs;
	}

	public synchronized void challenged(int challenger) {

		Object[] opt = {"Yes", "No"};
		int n2 = JOptionPane.showOptionDialog(this,
				"You have been challenged by " + challenger,
				"Duel Dialog",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				opt,
				opt[1]);

		if(n2 == 0) {
			log.debug("Accepted the duel");
			setCmdBuf(ProtocolOperators.ACK);
		} else if (n2 == 1) {
			log.debug("Declined the duel");
			setCmdBuf(ProtocolOperators.NACK);
		}
	}
}

