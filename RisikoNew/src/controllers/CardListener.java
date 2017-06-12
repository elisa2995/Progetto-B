/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import gui.CardAnimation;
import gui.CardPanel;
import gui.GUI;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.event.MouseInputAdapter;
import static risiko.Phase.PLAY_CARDS;
import risiko.game.GameProxy;

/**
 *
 * @author feded
 */
public class CardListener extends MouseInputAdapter {

    private CardPanel cardPanel;
    private GameProxy game;
    private static final int CHOSEN_OFFSET = 670;
    private static final int LOW_Y = 20;
    private static final int HIGH_Y = 15;

    public CardListener(CardPanel cardPanel, GameProxy game) {
        this.cardPanel = cardPanel;
        this.game = game;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (game.getPhase() == PLAY_CARDS) {
            JLabel label = (JLabel) e.getComponent();
            cardPanel.getCardsPane().setLayer(label, 1);
            String card = (String) label.getClientProperty("name");
            label.setIcon(new ImageIcon("images/" + card + "Black.png"));
            Point p = label.getLocation();
            Point p1 = new Point(p.x, HIGH_Y);
            label.setLocation(p1);
            cardPanel.updateUI();
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (game.getPhase() == PLAY_CARDS) {
            JLabel label = (JLabel) e.getComponent();
            cardPanel.getCardsPane().setLayer(label, cardPanel.getLabelLayer(label));
            String card = (String) label.getClientProperty("name");
            label.setIcon(new ImageIcon("images/" + card + ".png"));
            Point p = label.getLocation();
            Point p1 = new Point(p.x, LOW_Y);
            label.setLocation(p1);
            cardPanel.updateUI();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (game.getPhase() == PLAY_CARDS) {
            JLabel label = (JLabel) e.getComponent();
            if (!(boolean) label.getClientProperty("chosen")) {
                if (cardPanel.getNrChosenCards() < 3) {
                    String card = (String) label.getClientProperty("name");
                    label.setIcon(new ImageIcon("images/" + card + "Green.png"));
                    CardAnimation animation = new CardAnimation(cardPanel, label.getX(), HIGH_Y, getToX(label), label);
                    animation.start();
                    cardPanel.updateUI();
                } else {
                    return;
                }
            } else {
                String card = (String) label.getClientProperty("name");
                label.setIcon(new ImageIcon("images/" + card + "Green.png"));
                CardAnimation animation = new CardAnimation(cardPanel, label.getX(), HIGH_Y, getToX(label), label);
                animation.start();
                cardPanel.updateUI();
            }
        }
    }

    private int getToX(JLabel label) {
        if ((boolean) label.getClientProperty("chosen")) {
            return 10 + 50 * cardPanel.getNrCards();
        }
        return CHOSEN_OFFSET + cardPanel.getNrChosenCards() * 50;

    }
}
