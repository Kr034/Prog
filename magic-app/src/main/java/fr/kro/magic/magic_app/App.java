package fr.kro.magic.magic_app;

import java.awt.Dimension;
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

import io.magicthegathering.javasdk.api.SetAPI;
import io.magicthegathering.javasdk.resource.Card;
import io.magicthegathering.javasdk.resource.MtgSet;

public class App extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JComboBox<String> combo = new JComboBox<String>();

	static JTextField testField1;
	JButton bouton1;
	static String url;
	static String imgurl;
	static String code = "UST";
	static int setssize;

	public static void main(String[] args) {
		App app = new App();
		app.init();
	}

	public void init() {
		JFrame frame = new JFrame("Magic APP");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setSize(1080, 720);
		frame.setLocationRelativeTo(null);
		JPanel panel = new JPanel();

		testField1 = new JTextField("");
		panel.add(testField1);

		bouton1 = new JButton("Rechercher");
		bouton1.addActionListener(this);

		testField1.setPreferredSize(new Dimension(100, 20));
		combo.setPreferredSize(new Dimension(100, 20));
		List<MtgSet> sets = SetAPI.getAllSets();
		for (int a = 0; a < sets.size(); a++) {
			combo.addItem(sets.get(a).getName());
		}
		panel.add(combo);
		panel.add(bouton1);
		frame.getContentPane().add(panel);
		frame.setVisible(true);

	}

	public void contenue() throws Exception {
		Card card = getCardSet(String.valueOf(testField1.getText()));
		url = card.getImageUrl().toString();
		System.out.println(url);
		initurl();
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

	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e) {
		if ((JButton) e.getSource() == bouton1) {
			int indexcode = combo.getSelectedIndex();
			List<MtgSet> sets = SetAPI.getAllSets();
			for (int a = 0; a < combo.countComponents(); a++) {
				code = sets.get(indexcode).getCode();
			}
			System.out.println(code);
			String name = String.valueOf(testField1.getText());
			System.out.println(getCardSet(name));
			try {
				if (name.isEmpty()) {
					initIMGALL();
				} else {
					contenue();
				}

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public void initurl() throws Exception {
		JFrame frame = new JFrame(String.valueOf(testField1.getText()));
		JLabel label = new JLabel(new ImageIcon(new URL(url)));
		frame.getContentPane().add(label);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

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
		scroll.setPreferredSize(new Dimension(1280, 720));
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

		JFrame frame = new JFrame(set.getName());
		frame.getContentPane().add(scroll);
		frame.setSize(1280, 720);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
