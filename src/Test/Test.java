/*
import com.aci.student24.api.tanks.state.Direction;
import com.aci.student24.tanks.ToBase;
import org.junit.Assert;
import org.junit.Before;

public class Test {
    ToBase toBase;
    int[][] matrix;

    @Before
    public void setUp(){
        int size_x = 20, size_y = 20;
        matrix = new int[size_x][size_y];
        toBase = new ToBase();
        toBase.size_x = size_x;
        toBase.size_y = size_y;
    }

    @org.junit.Test
    public void isFriendOnALine_shouldWorkCorrectlyWhenUp(){
            matrix[10][10] = ToBase.Cell.MY_TANK;
            matrix[10][15] = ToBase.Cell.MY_TANK;
            //ToBase.matrix = matrix;
            byte dir = Direction.UP;

            Assert.assertTrue(toBase.isFriendOnALine(10, 10, dir));
    }

    @org.junit.Test
    public void isFriendOnALine_shouldWorkCorrectlyWhenDown(){
        matrix[10][10] = ToBase.Cell.MY_TANK;
        matrix[10][15] = ToBase.Cell.MY_TANK;
        //ToBase.matrix = matrix;
        byte dir = Direction.DOWN;

        Assert.assertTrue(toBase.isFriendOnALine(10, 15, dir));
    }

    @org.junit.Test
    public void isFriendOnALine_shouldWorkCorrectlyWhenLeft(){
        matrix[5][10] = ToBase.Cell.MY_TANK;
        matrix[10][10] = ToBase.Cell.MY_TANK;
        ToBase.matrix = matrix;
        byte dir = Direction.LEFT;

        Assert.assertTrue(toBase.isFriendOnALine(5, 10, dir));

    }

    @org.junit.Test
    public void isFriendOnALine_shouldWorkCorrectlyWhenRight(){
        matrix[5][10] = ToBase.Cell.MY_TANK;
        matrix[10][10] = ToBase.Cell.MY_TANK;
        ToBase.matrix = matrix;
        byte dir = Direction.RIGHT;

        Assert.assertTrue(toBase.isFriendOnALine(5, 10, dir));

    }
}
*/
