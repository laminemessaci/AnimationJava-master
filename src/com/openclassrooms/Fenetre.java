package com.openclassrooms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;

public class Fenetre extends JFrame {

    private Panneau pan = new Panneau();
    private JPanel container = new JPanel();

    // private JButton bouton = new JButton("Go");
    // private JButton bouton2 = new JButton("Stop");

    // private JLabel label = new JLabel("Choix de la forme");
    // private JComboBox combo = new JComboBox();
    //  private JCheckBox morph = new JCheckBox("Morphing");

    private int compteur = 0;
    private boolean animated = true;
    private boolean backX, backY;
    private int x, y;
    private Thread t;

    private JMenuBar menuBar = new JMenuBar();

    private JMenu animation = new JMenu("Animation"),
            forme = new JMenu("Forme"),
            typeForme = new JMenu("Type de forme"),
            aPropos = new JMenu("À propos");

    private JMenuItem lancer = new JMenuItem("Lancer l'animation"),
            arreter = new JMenuItem("Arrêter l'animation"),
            quitter = new JMenuItem("Quitter"),
            aProposItem = new JMenuItem("?");

    private JCheckBoxMenuItem morph = new JCheckBoxMenuItem("Morphing");
    private JRadioButtonMenuItem carre = new JRadioButtonMenuItem("Carré"),
            rond = new JRadioButtonMenuItem("Rond"),
            triangle = new JRadioButtonMenuItem("Triangle"),
            etoile = new JRadioButtonMenuItem("Etoile");

    private ButtonGroup bg = new ButtonGroup();


    public Fenetre() {
        this.setTitle("Animation");
        this.setSize(300, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        container.setBackground(Color.white);
        container.setLayout(new BorderLayout());
        container.add(pan, BorderLayout.CENTER);

        //   bouton.addActionListener(new BoutonListener());
        //   bouton2.addActionListener(new Bouton2Listener());
        //   bouton2.setEnabled(false);
        //   JPanel south = new JPanel();
        //   south.add(bouton);
        //   south.add(bouton2);
        //   container.add(south, BorderLayout.SOUTH);

        //   combo.addItem("ROND");
        //   combo.addItem("CARRE");
        //   combo.addItem("TRIANGLE");
        //   combo.addItem("ETOILE");
        //   combo.addActionListener(new FormeListener());
        //   morph.addActionListener(new MorphListener());

        //   JPanel top = new JPanel();
        //   top.add(label);
        //   top.add(combo);
        //   top.add(morph);
        //   container.add(top, BorderLayout.NORTH);
        this.setContentPane(container);
        this.initMenu();
        this.setVisible(true);
    }

    private void initMenu() {
        //Menu animation
        animation.add(lancer);
        arreter.setEnabled(false);
        animation.add(arreter);
        animation.addSeparator();
        //Pour quitter l'application
        quitter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });
        animation.add(quitter);

        //Menu forme
        bg.add(carre);
        bg.add(triangle);
        bg.add(rond);
        bg.add(etoile);

        typeForme.add(rond);
        typeForme.add(carre);
        typeForme.add(triangle);
        typeForme.add(etoile);

        rond.setSelected(true);

        forme.add(typeForme);
        forme.add(morph);

        //Menu À propos
        aPropos.add(aProposItem);

        //Ajout des menus dans la barre de menus
        menuBar.add(animation);
        menuBar.add(forme);
        menuBar.add(aPropos);

        //Ajout de la barre de menus sur la fenêtre
        this.setJMenuBar(menuBar);
    }

    private  void go() {
        x = pan.getPosX();
        y = pan.getPosY();
        while (this.animated) {

            //Si le mode morphing est activé, on utilise la taille actuelle de la forme
            if (pan.isMorph()) {
                if (x < 1) backX = false;
                if (x > pan.getWidth() - pan.getDrawSize()) backX = true;
                if (y < 1) backY = false;
                if (y > pan.getHeight() - pan.getDrawSize()) backY = true;
            }
            //Sinon, on fait comme d'habitude
            else {
                if (x < 1) backX = false;
                if (x > pan.getWidth() - 50) backX = true;
                if (y < 1) backY = false;
                if (y > pan.getHeight() - 50) backY = true;
            }

            if (!backX) pan.setPosX(++x);
            else pan.setPosX(--x);
            if (!backY) pan.setPosY(++y);
            else pan.setPosY(--y);
            pan.repaint();
            try {
                Thread.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //Classe écoutant notre bouton
    public class BoutonListener implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            //  animated = true;
            //  t = new Thread(new PlayAnimation());
            //  t.start();
            //  bouton.setEnabled(false);
            //  bouton2.setEnabled(true);

            JOptionPane jop = new JOptionPane();
            int option = jop.showConfirmDialog(null,
                    "Voulez-vous lancer l'animation ?",
                    "Lancement de l'animation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (option == JOptionPane.OK_OPTION) {
                lancer.setEnabled(false);
                arreter.setEnabled(true);
                animated = true;
                t = new Thread(new PlayAnimation());
                t.start();

            }
        }
    }

        class Bouton2Listener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                //   animated = false;
                //   bouton.setEnabled(true);
                //   bouton2.setEnabled(false);

                JOptionPane jop = new JOptionPane();
                int option = jop.showConfirmDialog(null,
                        "Voulez-vous arrêter l'animation ?",
                        "Arrêt de l'animation",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (option != JOptionPane.NO_OPTION && option != JOptionPane.CANCEL_OPTION && option != JOptionPane.CLOSED_OPTION) {
                    animated = false;
                    //On remplace nos boutons par nos JMenuItem
                    lancer.setEnabled(true);
                    arreter.setEnabled(false);
                }
            }
        }

            class PlayAnimation implements Runnable {
                public void run() {
                    go();
                }
            }

            class FormeListener implements ActionListener {
                public void actionPerformed(ActionEvent e) {
                    //La méthode retourne un Object puisque nous passons des Object dans une liste
                    //Il faut donc utiliser la méthode toString() pour retourner un String (ou utiliser un cast)
                    // pan.setForme(combo.getSelectedItem().toString());
                }
            }

            class MorphListener implements ActionListener {
                public void actionPerformed(ActionEvent e) {
                    //Si la case est cochée, on active le mode morphing
                    if (morph.isSelected()) pan.setMorph(true);
                        //Sinon, on ne fait rien
                    else pan.setMorph(false);
                }
            }
}

