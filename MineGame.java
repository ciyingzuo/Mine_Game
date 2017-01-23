import tester.*;
import javalib.impworld.*;
import javalib.worldimages.*;
import java.awt.Color;

class Cell {
    int x;
    int y;
    boolean isMine;
    boolean curser;
    boolean flag;
    boolean checked;

    Cell(int x, int y, boolean isMine, boolean curser, boolean flag) {
        this.x = x;
        this.y = y;
        this.isMine = isMine;
        this.curser = curser;
        this.flag = flag;
    }

    Cell(int x, int y, boolean isMine) {
        this.x = x;
        this.y = y;
        this.isMine = isMine;
        this.curser = false;
        this.flag = false;
    }

    WorldImage draw() {
        if (this.curser) {
            return new RectangleImage(20, 20, "solid", new Color(255, 0, 0));
        } else if (this.flag) {
            return new RectangleImage(20, 20, "solid", new Color(0, 0, 125));
        } else
            return new RectangleImage(20, 20, "solid", new Color(0, 125, 0));
    }

}

class Mine {

    Cell[][] GenMine() {
        Cell[][] c = new Cell[30][30];
        for (int i = 0; i < 30; i++) {
            for (int n = 0; n < 30; n++) {
                double a = Math.random();
                if (a < 0.8) {
                    c[i][n] = new Cell(i, n, false);
                } else {
                    c[i][n] = new Cell(i, n, true);
                }
            }
        }
        return c;
    }
}

public class MineGame extends World {
    Cell[][] cell;
    int state;
    Posn p;

    MineGame() {
        state = 1;
        this.cell = new Mine().GenMine();
        p = new Posn(0, 0);
        cell[0][0].curser = true;
        cell[0][0].isMine = false;
    }

    public WorldScene makeScene() {
        WorldScene background = new WorldScene(600, 600);
        for (int i = 0; i < 30; i++) {
            for (int n = 0; n < 30; n++) {
                background.placeImageXY(this.cell[i][n].draw(), i * 20 + 8, n * 20 + 8);
                int a = 0;
                if (i >= 1 && this.cell[i - 1][n].isMine) {
                    a++;
                }
                if (i <= 28 && this.cell[i + 1][n].isMine) {
                    a++;
                }
                if (n >= 1 && this.cell[i][n - 1].isMine) {
                    a++;
                }
                if (n <= 28 && this.cell[i][n + 1].isMine) {
                    a++;
                }
                if(cell[i][n].checked){
                    background.placeImageXY(new TextImage(Integer.toString(a), new Color(255, 255, 255)), i * 20 + 8,
                        n * 20 + 8);
                }
            }
        }
        if (state == 0) {
            background.placeImageXY(new TextImage("you lose or win, what ever", new Color(255, 255, 255)), 300, 300);
        }
        return background;
    }

    public void onKeyEvent(String i) {
        if (i.equals("n")){
            state = 0;
        }
        if (i.equals("w") && p.y > 0) {
            cell[p.x][p.y].curser = false;
            cell[p.x][p.y - 1].curser = true;
            p.y = p.y - 1;
        }
        if (i.equals("s") && p.y < 29) {
            cell[p.x][p.y].curser = false;
            cell[p.x][p.y + 1].curser = true;
            p.y = p.y + 1;
        }
        if (i.equals("a") && p.x > 0) {
            cell[p.x][p.y].curser = false;
            cell[p.x - 1][p.y].curser = true;
            p.x = p.x - 1;
        }
        if (i.equals("d") && p.x < 29) {
            cell[p.x][p.y].curser = false;
            cell[p.x + 1][p.y].curser = true;
            p.x = p.x + 1;
        }
        if (i.equals("q")) {
            if (cell[p.x][p.y].isMine) {
                state = 0;
            } else
                cell[p.x][p.y].checked = true;
        }
        if (i.equals("e")) {
            cell[p.x][p.y].flag = true;
        }
    }

    public void onTick() {
        int c = 0;
        for (int i = 0; i < 30; i++) {
            for (int n = 0; n < 30; n++) {
                if ((cell[i][n].isMine && cell[i][n].flag)
                        || (cell[i][n].isMine == false && cell[i][n].flag == false)) {
                    c++;
                }
            }
        }
        if (c == 900) {
            state = 0;
        }
    }
}

class example {
    void testGame(Tester t) {
        MineGame w2 = new MineGame();
        w2.bigBang(600, 600, 0.1);
    }
}