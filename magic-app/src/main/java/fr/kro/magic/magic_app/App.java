package fr.kro.magic.magic_app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import io.magicthegathering.javasdk.api.CardAPI;
import io.magicthegathering.javasdk.api.SetAPI;
import io.magicthegathering.javasdk.resource.Card;
import io.magicthegathering.javasdk.resource.MtgSet;

public class App extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JComboBox<String> combo = new JComboBox<String>();

	static JTextField testField1;
	JButton bouton1;
	static String url = "http://gatherer.wizards.com/Handlers/Image.ashx?multiverseid=439601&type=card";
	static String imgurl;
	static String code = "UST";
	static int setssize;
	private JFrame frame = new JFrame("Magic APP");

	public static void main(String[] args) throws Exception {
		App app = new App();
		app.init();
	}

	public void init() throws Exception {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1280, 720);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setPreferredSize(new Dimension(1280, 720));
		JLabel background = new JLabel(new ImageIcon(new URL("https://wolvmc.eu/perso/back.jpg")));
		setLayout(new BorderLayout());
		background.setPreferredSize(new Dimension(1280, 720));
		testField1 = new JTextField("");
		background.add(testField1);
		background.setLayout(new FlowLayout());

		bouton1 = new JButton("Rechercher");
		bouton1.setPreferredSize(new Dimension(250, 30));
		bouton1.addActionListener(this);

		testField1.setPreferredSize(new Dimension(250, 30));
		combo.setPreferredSize(new Dimension(250, 30));
		combo.addItem("Aucune");
		List<MtgSet> sets = SetAPI.getAllSets();
		for (int a = 0; a < sets.size(); a++) {
			combo.addItem(sets.get(a).getName());
		}
		background.add(combo);
		background.add(bouton1);
		frame.pack();
		frame.getContentPane().add(background);
		frame.setVisible(true);

	}

	public void contenue() throws Exception {
		Card card = getCardSet(String.valueOf(testField1.getText()));
		url = card.getImageUrl().toString();
		initurl(url);
	}

	public static Card getCardSet(String cardname) {
		String setCode = code;
		MtgSet set = SetAPI.getSet(setCode);
		List<Card> list = set.getCards();
		String newlist = list.toString().replaceAll("},", "//");
		String newlist2 = newlist.replaceAll("null,", "//");
		String[] cardstring = newlist2.split("//");

		Card card = null;

		setssize = cardstring.length;
		for (int a = 0; a < setssize; a++) {
			if (cardstring[a].contains(cardname)) {
				card = list.get(a);
			}
		}
		return card;
	}

	public static Card getCardAll(String cardname) {
		List<Card> cards = CardAPI.getAllCards();
		String newlist = cards.toString().replaceAll("},", "//");
		String newlist2 = newlist.replaceAll("null,", "//");
		String[] cardstring = newlist2.split("//");
		Card card = null;

		for (int a = 0; a < cardstring.length; a++) {
			System.out.println(cardstring[a] + (a + 1) + " / " + cardstring.length);
			if (cardstring[a].contains(cardname)) {
				card = cards.get(a);
			}
		}
		return card;
	}

	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e) {
		if ((JButton) e.getSource() == bouton1) {
			int indexcode = (combo.getSelectedIndex() - 1);
			List<MtgSet> sets = SetAPI.getAllSets();
			if (combo.getSelectedItem().equals("Aucune")) {

			} else {
				for (int a = 0; a < combo.countComponents(); a++) {
					code = sets.get(indexcode).getCode();
				}
			}
			String name = String.valueOf(testField1.getText());
			try {
				if (name.isEmpty()) {
					initIMGALL();
				} else {
					if (combo.getSelectedItem().equals("Aucune")) {
						initurl(getCardAll(name).getImageUrl());
					} else {
						contenue();
					}
				}

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	public void initurl(String urli) throws Exception {
		JFrame frame1 = new JFrame(String.valueOf(testField1.getText()));
		JLabel label = new JLabel(new ImageIcon(new URL(urli)));
		frame1.getContentPane().add(label);
		frame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame1.pack();
		frame1.setVisible(true);

	}

	public JLabel createJLabel() {
		JLabel jl = new JLabel();

		return jl;
	}

	public void initIMGALL() throws Exception {
		MtgSet set = SetAPI.getSet(code);
		List<Card> list = set.getCards();
		JLabel[] tab = new JLabel[list.size()];// instance du tableau
		JPanel pan = new JPanel();// instance du panneau
		JScrollPane scroll = new JScrollPane(pan);
		scroll.setPreferredSize(new Dimension(1280, 350));
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).getName() + " " + (i + 1) + "/" + list.size());

			tab[i] = createJLabel();// on crée les JLabel et on met dans tab
		}

		for (int i = 0; i < list.size(); i++) {
			ImageIcon iiurl = new ImageIcon(new URL(list.get(i).getImageUrl()));
			tab[i].setIcon(iiurl);
			pan.add(tab[i]);
			System.out.println("Ok " + list.get(i).getName() + " " + (i + 1) + "/" + list.size());
		}

		JFrame frame2 = new JFrame(set.getName());
		frame2.getContentPane().add(scroll);
		frame2.setSize(1280, 350);
		frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame2.pack();
		frame2.setVisible(true);
	}
}
