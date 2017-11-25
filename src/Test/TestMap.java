import com.aci.student24.api.tanks.objects.Tank;
import com.aci.student24.api.tanks.state.Direction;
import com.aci.student24.tanks.ToBase;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TestMap{
    public static class Cell{
        public static final byte EMPTY = 0;
        public static final byte BRICK = 1;
        public static final byte INDESTRUCTIBLE = 2;
        public static final byte MY_BASE = 3;
        public static final byte ENEMY_BASE = 4;
        public static final byte MY_TANK = 5;
        public static final byte ENEMY_TANK = 6;
    }



    ToBase toBase = new ToBase();
    List<Tank> myTanks;
    static int size_x = 12, size_y = 12;
    int [][] map = {
            {0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0}
    };

    public void setCell(byte cell,int x, int y){
        map[x][y] = cell;
    }

    public void MoveTank(int x, int y, int side){

    }

    @Before
    public void setUp(){
        setCell(ToBase.Cell.MY_BASE, 0, 5);
        setCell(ToBase.Cell.ENEMY_BASE, 12, 5);

        setCell(Cell.MY_TANK, 0, 4);
        setCell(Cell.MY_TANK, 0, 6);
        setCell(Cell.MY_TANK, 1, 5);
        setCell(Cell.MY_TANK, 1, 4);
        setCell(Cell.MY_TANK, 1, 6);
        myTanks = new ArrayList<>();
        myTanks.add(new Tank());
        myTanks.add(new Tank());
        myTanks.add(new Tank());
        myTanks.add(new Tank());
        myTanks.add(new Tank());
        myTanks.get(0).setX(0);
        myTanks.get(0).setY(4);

        myTanks.get(1).setX(0);
        myTanks.get(1).setY(6);

        myTanks.get(2).setX(1);
        myTanks.get(2).setY(5);

        myTanks.get(3).setX(1);
        myTanks.get(3).setY(4);

        myTanks.get(4).setX(1);
        myTanks.get(4).setY(6);


        setCell(Cell.INDESTRUCTIBLE, 5, 4);
        setCell(Cell.INDESTRUCTIBLE, 5, 5);
        setCell(Cell.INDESTRUCTIBLE, 5, 6);
        setCell(Cell.INDESTRUCTIBLE, 5, 7);
        setCell(Cell.INDESTRUCTIBLE, 5, 8);
        setCell(Cell.INDESTRUCTIBLE, 5, 9);

        setCell(Cell.INDESTRUCTIBLE, 8, 0);
        setCell(Cell.INDESTRUCTIBLE, 8, 1);
        setCell(Cell.INDESTRUCTIBLE, 8, 2);

        toBase.matrix = map;
    }

    @Test
    public void tanksShouldMoveRight(){

        for(Tank tank : myTanks) {
            int tank_x = tank.getX();
            int tank_y = tank.getY();
            byte dir = Direction.RIGHT;

            if (toBase.isBlocked(tank_x, tank_y)) {
                final Random rn = new Random();
                int randomDir = rn.nextInt(2);
                dir = randomDir == 1 ? (byte) 1 : (byte) 3;
            }

            boolean shoot = !toBase.isFriendOnALine(tank_x, tank_y, dir);
        }

    }
}