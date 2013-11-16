import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

public class MainFrm extends JFrame implements MouseListener {
   private final ImageIcon bombIcon = new ImageIcon("mine.png");
   private final ImageIcon flagIcon = new ImageIcon("flag.png");

   private Minesweeper map;
   private JButton button[][];
   private boolean flag[][];

   private boolean gameStatus = true;
   private int openedMines = 0;

   public MainFrm() {
      setTitle("Minesweeper");
      setSize(500, 500);
      setLocationRelativeTo(null);
      setDefaultCloseOperation(EXIT_ON_CLOSE);

      map = new Minesweeper();
      button = new JButton[map.size()][map.size()];
      flag = new boolean[map.size()][map.size()];

      createFrm();

      setVisible(true);
   }

   private void createFrm() {
      JPanel panel = new JPanel(new GridLayout(map.size(), map.size()));
      for (int i=0; i<map.size(); i++)
         for (int j=0; j<map.size(); j++) {
            button[i][j] = new JButton();
            button[i][j].addMouseListener(this);
            panel.add(button[i][j]);
         }
      add(panel);
   }

   private int[] getButtonPressed(Object src) {
      for (int i=0; i<map.size(); i++)
         for (int j=0; j<map.size(); j++)
            if (button[i][j] == src) {
               int retval[] = {i, j};
               return retval;
            }
      return null;
   }

   public void mouseReleased(MouseEvent e) {
      if (gameStatus) {
         int idx[] = getButtonPressed(e.getSource());
         int x = idx[0];
         int y = idx[1];

         if (e.getModifiers() == InputEvent.BUTTON3_MASK) { // right click
            if (button[x][y].isEnabled()) {
               if (!flag[x][y])
                  button[x][y].setIcon(flagIcon);
               else
                  button[x][y].setIcon(null);
               flag[x][y] = !flag[x][y];
            }
         }
         else if (e.getModifiers() == InputEvent.BUTTON1_MASK) { // left click
            if (map.value(x, y) == 0) { // mines
               button[x][y].setEnabled(false);
               openedMines++;
               showMines(x, y);
            }
            else if (map.value(x, y) < 0) { // bombs
               showAllBomb();
               gameStatus = false;
            }
            else if (map.value(x, y) > 0) { // mines
               button[x][y].setText("" + map.value(x, y));
               button[x][y].setEnabled(false);
               openedMines++;
            }
            checkWinner();
         }
      }
   }

   private void checkWinner() {
      if (gameStatus) {
         if (openedMines >= map.mines()) {
            gameStatus = false;
            showAllBomb();
            JOptionPane.showMessageDialog(this, "You win!");
         }
      }
      else {
         JOptionPane.showMessageDialog(this, "Game over!", "Game over!", JOptionPane.ERROR_MESSAGE);
      }
   }

   private void showAllBomb() {
      for (int i=0; i<map.size(); i++)
         for (int j=0; j<map.size(); j++)
            if (map.value(i, j) < 0)
               button[i][j].setIcon(bombIcon);
   }

   private void showMines(int x, int y) {
      LinkedList<Integer> Q = new LinkedList<Integer>();
      
      Q.add(x);
      Q.add(y);
      
      while (Q.size() > 0) {
         x = Q.remove();
         y = Q.remove();

         for (int i=-1; i<=1; i++) {
            for (int j=-1; j<=1; j++) {
               if (x+i >= 0 && x+i < map.size() && y+j >= 0 && y+j < map.size()) {
                  if ((map.value(x+i, y+j) >= 0) && (button[x+i][y+j].isEnabled())) {
                     button[x+i][y+j].setEnabled(false);

                     if (map.value(x+i, y+j) > 0) {
                        button[x+i][y+j].setText("" + map.value(x+i, y+j));
                     }
                     else if (map.value(x+i, y+j) == 0) {
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

   public void mouseExited(MouseEvent e) {}
   public void mouseEntered(MouseEvent e) {}
   public void mousePressed(MouseEvent e) {}
   public void mouseClicked(MouseEvent e) {}

   public static void main(String[] args) {
      MainFrm frm = new MainFrm();
   }
}
