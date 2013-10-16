import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.accessibility.*;

import java.util.*;

public class Main extends JFrame implements MouseListener {
   private Minesweeper mines;
   
   private final ImageIcon bombIcon = new ImageIcon("mine.png");
   private final ImageIcon flagIcon = new ImageIcon("flag.png");

   private JButton button[][];
   private boolean flag[][];

   private boolean gameStatus = true;
   private int openedMines = 0;
   
   public Main() {
      super("Minesweeper");
      setBounds(100, 100, 500, 500);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      mines = new Minesweeper();
      button = new JButton[mines.getMapSize()][mines.getMapSize()];
      flag = new boolean[mines.getMapSize()][mines.getMapSize()];
      
      createPanel();
      setVisible(true);
   }

   private void createPanel() {
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(mines.getMapSize(), mines.getMapSize()));

      Container container = this.getContentPane();
      container.add(panel);

      for (int i=0; i<mines.getMapSize(); i++)
         for (int j=0; j<mines.getMapSize(); j++) {
            flag[i][j] = false;
            
            button[i][j] = new JButton();
            button[i][j].addMouseListener(this);

            AccessibleContext info = button[i][j].getAccessibleContext();
            info.setAccessibleDescription(i + "," + j);
            
            panel.add(button[i][j]);
         }
   }

   public void mouseReleased(MouseEvent e) {
      if (gameStatus) {
         int[] loc = getButtonLocation((JButton)e.getSource());
         int x = loc[0], y = loc[1];
         
         if (e.getModifiers() == InputEvent.BUTTON3_MASK) { // right-click
            if (button[x][y].isEnabled()) {
               if (!flag[x][y])
                  button[x][y].setIcon(flagIcon);
               else
                  button[x][y].setIcon(null);
                  
               flag[x][y] = !flag[x][y];
            }
         }
         else if (e.getModifiers() == InputEvent.BUTTON1_MASK) { // left-click
            int value = mines.getMapValue(x, y);
            if (value == 0) { // safe
               button[x][y].setEnabled(false);
               openedMines++;
               showAllSafeMines(x, y);
            }
            else if (value < 0) { // bomb!
               showAllBomb();
               gameStatus = false;
            }
            else if (value > 0) { // safe
               button[x][y].setText("" + mines.getMapValue(x, y));
               button[x][y].setEnabled(false);
               openedMines++;
            }
         }
         checkWin();
      }
   }

   public void mouseExited(MouseEvent e) {
   }

   public void mouseEntered(MouseEvent e) {
   }

   public void mousePressed(MouseEvent e) {
   }

   public void mouseClicked(MouseEvent e) {
   }

   private void checkWin() {
      if (gameStatus) {
         if (openedMines >= mines.getMinesCount()) {
            gameStatus = false;
            showAllBomb();
            JOptionPane.showMessageDialog(this, "Congratulation! You win!");
         }
      }
      else {
         JOptionPane.showMessageDialog(this, "Game over!");
      }
   }

   private void showAllBomb() {
      for (int i=0; i<mines.getMapSize(); i++)
         for (int j=0; j<mines.getMapSize(); j++)
            if (mines.getMapValue(i, j) < 0)
               button[i][j].setIcon(bombIcon);
   }

   private void showAllSafeMines(int x, int y) {
      LinkedList<Integer> Q = new LinkedList<Integer>();
      
      Q.add(x);
      Q.add(y);
      
      while (Q.size() > 0) {
         x = Q.remove();
         y = Q.remove();

         for (int i=-1; i<=1; i++) {
            for (int j=-1; j<=1; j++) {
               if (x+i >= 0 && x+i < mines.getMapSize() && y+j >= 0 && y+j < mines.getMapSize()) {
                  if ((mines.getMapValue(x+i, y+j) >= 0) && (button[x+i][y+j].isEnabled())) {
                     button[x+i][y+j].setEnabled(false);
                     if (mines.getMapValue(x+i, y+j) > 0) {
                        button[x+i][y+j].setText("" + mines.getMapValue(x+i, y+j));
                     }
                     else if (mines.getMapValue(x+i, y+j) == 0) {
                        Q.add(x+i);
                        Q.add(y+j);
                     }
                     openedMines++;
                  }
               }
            }
         }
      }
   }

   private int[] getButtonLocation(JButton btn) {
      String info = btn.getAccessibleContext().getAccessibleDescription();
      String[] split = info.split(",");
      int[] retval = {Integer.parseInt(split[0]), Integer.parseInt(split[1])};
      return retval;
   }

   public static void main(String[] args) {
      Main frm = new Main();
   }
}
