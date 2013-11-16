import java.util.*;

public class Minesweeper {
   private final int SIZE = 8;
   private final int BOMB = 10;

   private int map[][] = new int[SIZE][SIZE];

   public Minesweeper() {
      // initialize
      for (int i=0; i<SIZE; i++) Arrays.fill(map[i], 0);
      randomBomb();
      calculate();
   }

   private void randomBomb() {
      Random rnd = new Random();
      for (int i=0; i<BOMB; i++) {
         int x, y;
         do {
            x = rnd.nextInt(SIZE);
            y = rnd.nextInt(SIZE);
         } while (map[x][y] < 0);
         map[x][y] = -9999; // set coordinate (x, y) as bomb!
      }
   }

   private void calculate() {
      for (int i=0; i<SIZE; i++)
         for (int j=0; j<SIZE; j++)
            if (map[i][j] < 0) {
               if (i+1 < SIZE) map[i+1][j] += 1;
               if (i-1 >= 0)   map[i-1][j] += 1;
               if (j+1 < SIZE) map[i][j+1] += 1;
               if (j-1 >= 0)   map[i][j-1] += 1;

               if ((i+1 < SIZE) && (j+1 < SIZE)) map[i+1][j+1] += 1;
               if ((i+1 < SIZE) && (j-1 >= 0))   map[i+1][j-1] += 1;
               if ((i-1 >= 0) && (j+1 < SIZE))   map[i-1][j+1] += 1;
               if ((i-1 >= 0) && (j-1 >= 0))     map[i-1][j-1] += 1;
            }
   }

   public int value(int x, int y) {
      return map[x][y];
   }

   public int size() {
      return SIZE;
   }

   public int bomb() {
      return BOMB;
   }

   public int mines() {
      return (SIZE * SIZE - BOMB);
   }
}
