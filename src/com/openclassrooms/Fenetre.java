package com.openclassrooms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.*;

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

    //La déclaration pour le menu contextuel
    private JPopupMenu jpm = new JPopupMenu();
    private JMenu background = new JMenu("Couleur de fond");
    private JMenu couleur = new JMenu("Couleur de la forme");

    private JMenuItem launch = new JMenuItem("Lancer l'animation");
    private JMenuItem stop = new JMenuItem("Arrêter l'animation");
    private JMenuItem rouge = new JMenuItem("Rouge"),
            bleu = new JMenuItem("Bleu"),
            vert = new JMenuItem("Vert"),
            blanc =new JMenuItem("Blanc"),
            blancBack =new JMenuItem("Blanc"),
            rougeBack = new JMenuItem("Rouge"),
            bleuBack = new JMenuItem("Bleu"),
            vertBack = new JMenuItem("Vert");

    //On crée des listeners globaux
    private StopAnimationListener stopAnimation = new StopAnimationListener();
    private StartAnimationListener startAnimation = new StartAnimationListener();
    //Avec des listeners pour les couleurs
    private CouleurFondListener bgColor = new CouleurFondListener();
    private CouleurFormeListener frmColor = new CouleurFormeListener();


    public Fenetre() {
        this.setTitle("Animation");
        this.setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        container.setBackground(Color.white);
        container.setLayout(new BorderLayout());

        //On initialise le menu stop
        stop.setEnabled(false);
        //On affecte les écouteurs
        stop.addActionListener(stopAnimation);
        launch.addActionListener(startAnimation);

        //On affecte les écouteurs aux points de menu
        rouge.addActionListener(frmColor);
        bleu.addActionListener(frmColor);
        vert.addActionListener(frmColor);
        blanc.addActionListener(frmColor);

        rougeBack.addActionListener(bgColor);
        bleuBack.addActionListener(bgColor);
        vertBack.addActionListener(bgColor);
        blancBack.addActionListener(bgColor);

        //On crée et on passe l'écouteur pour afficher le menu contextuel
        //Création d'une implémentation de MouseAdapter
        //avec redéfinition de la méthode adéquate
        pan.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent event) {
                //Seulement s'il s'agit d'un clic droit
                //if(event.getButton() == MouseEvent.BUTTON3)
                if (event.isPopupTrigger()) {
                    background.add(blancBack);
                    background.add(rougeBack);
                    background.add(bleuBack);
                    background.add(vertBack);

                    couleur.add(blanc);
                    couleur.add(rouge);
                    couleur.add(bleu);
                    couleur.add(vert);

                    jpm.add(launch);
                    jpm.add(stop);
                    jpm.add(couleur);
                    jpm.add(background);

                    //La méthode qui va afficher le menu
                    jpm.show(pan, event.getX(), event.getY());
                }
            }
        });

        container.add(pan, BorderLayout.CENTER);

        this.setContentPane(container);
        this.initMenu();
        this.setVisible(true);
    }

    private void initMenu() {
        //Ajout du listener pour lancer l'animation
        //ATTENTION, LE LISTENER EST GLOBAL !!!
        lancer.addActionListener(startAnimation);
        //On attribue l'accélerateur c
        lancer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_MASK));
        animation.add(lancer);

        //Ajout du listener pour arrêter l'animation
        //LISTENER A CHANGER ICI AUSSI
        arreter.addActionListener(stopAnimation);
        arreter.setEnabled(false);
        arreter.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK + KeyEvent.SHIFT_DOWN_MASK));
        animation.add(arreter);
        //Menu Forme

        bg.add(carre);
        bg.add(triangle);
        bg.add(rond);
        bg.add(etoile);

        //On crée un nouvel écouteur, inutile de créer 4 instances différentes
        FormeListener fl = new FormeListener();
        carre.addActionListener(fl);
        rond.addActionListener(fl);
        triangle.addActionListener(fl);
        etoile.addActionListener(fl);

        typeForme.add(rond);
        typeForme.add(carre);
        typeForme.add(triangle);
        typeForme.add(etoile);

        rond.setSelected(true);

        forme.add(typeForme);

        //Ajout du listener pour le morphing
        morph.addActionListener(new MorphListener());
        forme.add(morph);

        //Menu À propos

        //Ajout de ce que doit faire le "?"
        aProposItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                JOptionPane jop = new JOptionPane();
                ImageIcon img = new ImageIcon("images/jijel.jpg");
                String mess = "Merci ! \n J'espère que vous vous amusez bien !\n";
                mess += "Je crois qu'il est temps d'ajouter des accélérateurs et des " + " mnémoniques dans tout ça…\n";
                mess += "\n Allez, GO feignant !";
                jop.showMessageDialog(null, mess, "À propos", JOptionPane.INFORMATION_MESSAGE, img);
            }
        });
        aPropos.add(aProposItem);
        //Ajout des menus dans la barre de menus et ajout de mnémoniques !
        animation.setMnemonic('A');
        forme.setMnemonic('F');
        aPropos.setMnemonic('P');

        //Ajout des menus dans la barre de menus
        menuBar.add(animation);
        menuBar.add(forme);
        menuBar.add(aPropos);


        //Ajout de la barre de menus sur la fenêtre
        this.setJMenuBar(menuBar);
    }


    private void go() {
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
    public class StartAnimationListener implements ActionListener {
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
                //On ajoute l'instruction pour le menu contextuel
                launch.setEnabled(false);
                stop.setEnabled(true);

                animated = true;
                t = new Thread(new PlayAnimation());
                t.start();


            }

        }
    }

    class StopAnimationListener implements ActionListener {
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

                //On ajoute l'instruction pour le menu contextuel
                launch.setEnabled(true);
                stop.setEnabled(false);
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
            pan.setForme(((JRadioButtonMenuItem) e.getSource()).getText().toString());
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

    //Écoute le changement de couleur du fond
    class CouleurFondListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {

            if(e.getSource() == vertBack)
                pan.setCouleurFond(Color.green);
            else if (e.getSource() == bleuBack)
                pan.setCouleurFond(Color.blue);
            else if(e.getSource() == rougeBack)
                pan.setCouleurFond(Color.red);
            else
                pan.setCouleurFond(Color.white);
        }
    }

    //Écoute le changement de couleur du fond
    class CouleurFormeListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == vert)
                pan.setCouleurForme(Color.green);
            else if (e.getSource() == bleu)
                pan.setCouleurForme(Color.blue);
            else if(e.getSource() == rouge)
                pan.setCouleurForme(Color.red);
            else
                pan.setCouleurForme(Color.white);
        }
    }

}