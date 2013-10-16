import java.util.*;

public class Minesweeper {
   private final int ROWS = 8;
   private final int BOMB = 10;
   private int map[][] = new int[ROWS][ROWS];

   public Minesweeper() {
      // initialize map
      for (int i=0; i<ROWS; i++)
         for (int j=0; j<ROWS; j++)
            map[i][j] = 0;

      // random bomb and calculate cell values
      randomBomb();
      calculateCells();
   }

   private void randomBomb() {
      Random rnd = new Random();
      for (int i=0; i<BOMB; i++) {
         int x = rnd.nextInt(ROWS), y = rnd.nextInt(ROWS);
         while (map[x][y] < 0) {
            // if randomized location is already a bomb, re-random
            x = rnd.nextInt(ROWS);
            y = rnd.nextInt(ROWS);
         }
         map[x][y] = -9999;
      }
   }

   private void calculateCells() {
      for (int i=0; i<ROWS; i++)
         for (int j=0; j<ROWS; j++)
            if (map[i][j] < 0) {
               // if is bomb, increase adjacent cell values by 1
               if (i+1 < ROWS)
                  map[i+1][j] += 1;
               if (i-1 >= 0)
                  map[i-1][j] += 1;
               if (j+1 < ROWS)
                  map[i][j+1] += 1;
               if (j-1 >= 0)
                  map[i][j-1] += 1;
               if ((i+1 < ROWS) && (j+1 < ROWS))
                  map[i+1][j+1] += 1;
               if ((i+1 < ROWS) && (j-1 >= 0))
                  map[i+1][j-1] += 1;
               if ((i-1 >= 0) && (j+1 < ROWS))
                  map[i-1][j+1] += 1;
               if ((i-1 >= 0) && (j-1 >= 0))
                  map[i-1][j-1] += 1;
            }
   }

   public int getMapValue(int x, int y) {
      return map[x][y];
   }

   public int getMapSize() {
      return ROWS;
   }

   public int getBombCount() {
      return BOMB;
   }

   public int getMinesCount() {
      return ROWS * ROWS - BOMB;
   }
}
