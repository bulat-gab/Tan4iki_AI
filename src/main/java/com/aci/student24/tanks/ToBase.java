package com.aci.student24.tanks;


import com.aci.student24.api.tanks.Algorithm;
import com.aci.student24.api.tanks.objects.Base;
import com.aci.student24.api.tanks.objects.Indestructible;
import com.aci.student24.api.tanks.objects.Position;
import com.aci.student24.api.tanks.objects.Tank;
import com.aci.student24.api.tanks.state.Direction;
import com.aci.student24.api.tanks.state.MapState;
import com.aci.student24.api.tanks.state.TankMove;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ToBase implements Algorithm {
    private int move = 0;
    private int teamId;
    private Base enemyBasePos;
    private int base_x;
    private int base_y;
    public int size_x;
    public int size_y;
    private byte startPos;
    private final int[] tankNumber = {0};
    private final int[] numberOfTanks = {5};
    public static int[][] matrix;

    public static class Cell{
        public static final byte EMPTY = 0;
        public static final byte BRICK = 1;
        public static final byte INDESTRUCTIBLE = 2;
        public static final byte MY_BASE = 3;
        public static final byte ENEMY_BASE = 4;
        public static final byte MY_TANK = 5;
        public static final byte ENEMY_TANK = 6;
    }

    @Override
    public void setMyId(final int id) {
        teamId = id;
    }

    @Override
    public List<TankMove> nextMoves(MapState mapState) {
        move++;
        System.out.println("Move #: " + move);

        enemyBasePos = mapState.getBases().stream().filter(x -> x.getTeamId() != teamId).findFirst().get();
        base_x = enemyBasePos.getX();
        base_y = enemyBasePos.getY();

        // Matrix (map) initialization
        if(move <= 1) {
            size_x = mapState.getSize().getWidth();
            size_y = mapState.getSize().getHeight();

            // Check whether MY_BASE on the left or right side
            startPos = base_x > (size_x / 2) ? Direction.LEFT : Direction.RIGHT;


            matrix = new int[size_x][size_y];
            List<Indestructible> indestructibles = mapState.getIndestructibles();
            for (Indestructible indestructible : indestructibles)
                matrix[indestructible.getX()][indestructible.getY()] = Cell.INDESTRUCTIBLE;
            matrix[base_x][base_y] = Cell.ENEMY_BASE;

            for(Tank t : mapState.getTanks()){
                matrix[t.getX()][t.getY()] = t.getTeamId() == teamId ? Cell.MY_TANK : Cell.ENEMY_TANK;
            }
        }

        // Updating tanks positions
        for(Tank t :mapState.getTanks()){
            matrix[t.getX()][t.getY()] = t.getTeamId() == teamId ? Cell.MY_TANK : Cell.ENEMY_TANK;
        }

        tankNumber[0] = 0;

        List<TankMove> tanks = new LinkedList<>();
        for(Tank tank : mapState.getTanks(teamId)){
            int tank_x = tank.getX();
            int tank_y = tank.getY();
            byte dir = Direction.RIGHT;

            if (isBlocked(tank_x, tank_y)) {
                final Random rn = new Random();
                int randomDir = rn.nextInt(2);
                dir = randomDir == 1 ? (byte)1 : (byte)3;
            }
            boolean shoot = !isFriendOnALine(tank_x, tank_y, dir);

            tankNumber[0]++;

            System.out.println("X: " + "Y: " );

            tanks.add(new TankMove(tank.getId(), dir, shoot));
        }
        numberOfTanks[0] = tankNumber[0];
        return tanks;
    }


    public boolean isBlocked(int x, int y) {
        try {
            if (matrix[x + 1][y] == Cell.INDESTRUCTIBLE)
                return true;
        } catch (Exception e) {
            System.err.println(e);
        }
        return false;
    }

    public boolean isFriendOnALine(int x, int y, byte dir){
        switch (dir){
            case Direction.UP:
                for (int j = y; j > 0; j--) {
                    if(matrix[x][j] == Cell.MY_TANK || matrix[x][j] == Cell.MY_BASE){
                        return true;
                    }
                }
                break;
            case Direction.DOWN:
                for (int j = y; j < size_y; j++) {
                    if(matrix[x][j] == Cell.MY_TANK || matrix[x][j] == Cell.MY_BASE){
                        return true;
                    }
                }
                break;
            case Direction.LEFT:
                for (int j = x; j > 0; j--) {
                    if(matrix[j][y] == Cell.MY_TANK || matrix[j][y] == Cell.MY_BASE){
                        return true;
                    }
                }
                break;
            case Direction.RIGHT:
                for (int j = x; j < size_x; j++) {
                    if(matrix[j][y] == Cell.MY_TANK || matrix[j][y] == Cell.MY_BASE){
                        return true;
                    }
                }
                break;
        }
        return false;
    }
}
