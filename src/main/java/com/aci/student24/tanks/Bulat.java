package com.aci.student24.tanks;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import com.aci.student24.api.tanks.Algorithm;
import com.aci.student24.api.tanks.objects.Base;
import com.aci.student24.api.tanks.objects.Indestructible;
import com.aci.student24.api.tanks.objects.Tank;
import com.aci.student24.api.tanks.state.Direction;
import com.aci.student24.api.tanks.state.MapState;
import com.aci.student24.api.tanks.state.TankMove;

public class Bulat implements Algorithm {
    List<Tank> myTanks;
    int[][] matrix;

    private int teamId;
    private int i = 0;

    private Base enemyBasePos;
    private int enemyBaseX;
    private int enemyBaseY;
    public int heigth;
    public int width;
    private byte startPos;
    private int tankNumber = 0;
    private int numberOfTanks = 5;
    int move = 0;


    @Override
    public void setMyId(final int id) {
        teamId = id;
    }

    @Override
    public List<TankMove> nextMoves(MapState mapState) {
        move++;
        System.out.println("BULAT_GAB Move)))00))0 #: " + move);

        enemyBasePos = mapState.getBases().stream().filter(x -> x.getTeamId() != teamId).findFirst().get();
        enemyBaseX = enemyBasePos.getX();
        enemyBaseY = enemyBasePos.getY();

        // Matrix (map) initialization
        if(move <= 1) {
            width = mapState.getSize().getWidth();
            heigth = mapState.getSize().getHeight();

            // Check whether MY_BASE on the left or right side
            startPos = enemyBaseX > (width / 2) ? Direction.LEFT : Direction.RIGHT;

            matrix = new int[heigth][width];
            List<Indestructible> indestructibles = mapState.getIndestructibles();
            Objects.requireNonNull(indestructibles);
            for (Indestructible indestructible : indestructibles)
                matrix[indestructible.getY()][indestructible.getX()] = Cell.INDESTRUCTIBLE;
            matrix[enemyBaseY][enemyBaseX] = Cell.ENEMY_BASE;
        }

        // Updating tanks positions
        for(Tank t : mapState.getTanks()){
            matrix[t.getY()][t.getX()] = t.getTeamId() == teamId ? Cell.MY_TANK : Cell.ENEMY_TANK;
        }

        tankNumber = 0;

        List<TankMove> tankMoves = new LinkedList<>();
        for(Tank tank : mapState.getTanks(teamId)) {
            int tank_x = tank.getX();
            int tank_y = tank.getY();
            byte dir = startPos == Direction.LEFT ? Direction.RIGHT : Direction.LEFT;

            List<Point> shortestPathToBase = new MazePathFinder().findPath(
                    new Maze(matrix),
                    new Point(tank_x, tank_y),
                    new Point(enemyBaseX, enemyBaseY));
            try {
                Point nextCell = shortestPathToBase.get(1);
                if(nextCell.x > tank_x)
                    dir = Direction.RIGHT;
                else if(nextCell.x < tank_x)
                    dir = Direction.LEFT;
                else if(nextCell.y > tank_y)
                    dir = Direction.DOWN;
                else if(nextCell.y < tank_y)
                    dir = Direction.UP;
            }
            catch (Exception e){
                System.err.println(e);
            }
            boolean shoot = !isFriendOnALine(tank_x, tank_y, dir)  &&
                    !isFriendOnALine(tank_x, tank_y, tank.getDir());

            tankMoves.add(new TankMove(tank.getId(), dir, shoot));
        }

        return tankMoves;
    }

    public boolean isFriendOnALine(int y, int x, byte dir){
        switch (dir){
            case Direction.UP:
                for (int j = y; j > 0; j--) {
                    if(y != j && (matrix[x][j] == Cell.MY_TANK || matrix[x][j] == Cell.MY_BASE)){
                        System.out.println("isFriendOnALine<< ");

                        return true;
                    }
                }
                break;
            case Direction.DOWN:
                for (int j = y; j < heigth; j++) {
                    if(j != y && (matrix[x][j] == Cell.MY_TANK || matrix[x][j] == Cell.MY_BASE)){
                        System.out.println("isFriendOnALine: " + x + " " + j);
                        return true;
                    }
                }
                break;
            case Direction.LEFT:
                for (int j = x; j > 0; j--) {
                    if(j != x && (matrix[j][y] == Cell.MY_TANK || matrix[j][y] == Cell.MY_BASE)){
                        return true;
                    }
                }
                break;
            case Direction.RIGHT:
                for (int j = x; j < width; j++) {
                    if(j != x && (matrix[j][y] == Cell.MY_TANK || matrix[j][y] == Cell.MY_BASE)){
                        return true;
                    }
                }
                break;
        }
        return false;
    }

    public static class Cell{
        public static final byte EMPTY = 0;
        public static final byte BRICK = 1;
        public static final byte INDESTRUCTIBLE = 2;
        public static final byte MY_BASE = 3;
        public static final byte ENEMY_BASE = 4;
        public static final byte MY_TANK = 5;
        public static final byte ENEMY_TANK = 6;
    }

    public static class Maze {

        private static final boolean CELL_OCCUPIED = true;

        private final int[][] maze;

        public Maze(final int[][] maze) {
            Objects.requireNonNull(maze, "The input maze is null.");

            final int numberOfRows = maze.length;

            if (numberOfRows == 0) {
                throw new IllegalArgumentException("The input maze is empty.");
            }

            int numberOfColumns = 0;

            for (int row = 0; row < maze.length; ++row) {
                numberOfColumns = Math.max(numberOfColumns, maze[row].length);
            }

            this.maze = new int[numberOfRows][numberOfColumns];

            for (int row = 0; row < numberOfRows; ++row) {
                for (int column = 0;
                     column < Math.min(numberOfColumns, maze[row].length);
                     column++) {
                    this.maze[row][column] = maze[row][column];
                }
            }
        }

        public int getWidth() {
            return maze[0].length;
        }

        public int getHeight() {
            return maze.length;
        }

        public boolean cellIsFree(final Point p) {
            return cellIsFree(p.x, p.y);
        }

        public boolean cellIsWithinMaze(final Point p) {
            return p.x >= 0 && p.x < getWidth() && p.y >= 0 && p.y < getHeight();
        }

        public boolean cellIsTraversible(final Point p) {
            return cellIsWithinMaze(p) && cellIsFree(p);
        }

        public boolean cellIsFree(final int x, final int y) {
            checkXCoordinate(x);
            checkYCoordinate(y);
            return maze[y][x] != Cell.INDESTRUCTIBLE;
        }

        public String withPath(final List<Point> path) {
            final char[][] matrix = new char[getWidth()][getHeight()];

            for (int i = 0; i < matrix.length; ++i) {
                for (int j = 0; j < matrix[0].length; ++j) {
                    matrix[i][j] = maze[i][j] == Cell.INDESTRUCTIBLE? 'x' : '.';
                }
            }

            for (final Point p : path) {
                matrix[p.y][p.x] = 'o';
            }

            final StringBuilder sb = new StringBuilder();

            sb.append(new String(matrix[0]));

            for (int i = 1; i < matrix.length; ++i) {
                sb.append('\n');
                sb.append(new String(matrix[i]));
            }

            return sb.toString();
        }


        private void checkXCoordinate(final int x) {
            if (x < 0) {
                throw new IndexOutOfBoundsException(
                        "The x-coordinate is negative: " + x + ".");
            }

            if (x >= maze[0].length) {
                throw new IndexOutOfBoundsException(
                        "The x-coordinate is too large (" + x +
                                "). The amount of columns in this maze is " +
                                maze[0].length + ".");
            }
        }
        private void checkYCoordinate(final int y) {
            if (y < 0) {
                throw new IndexOutOfBoundsException(
                        "The y-coordinate is negative: " + y + ".");
            }

            if (y >= maze.length) {
                throw new IndexOutOfBoundsException(
                        "The y-coordinate is too large (" + y +
                                "). The amount of rows in this maze is " +
                                maze.length + ".");
            }
        }
    }

    public static class MazePathFinder {

        private Maze maze;
        private Point source;
        private Point target;
        private boolean[][] visited;
        private Map<Point, Point> parents;

        public MazePathFinder() {}

        private MazePathFinder(final Maze maze,
                               final Point source,
                               final Point target) {
            Objects.requireNonNull(maze, "The input maze is null.");
            Objects.requireNonNull(source, "The source node is null.");
            Objects.requireNonNull(target, "The target node is null.");

            this.maze = maze;
            this.source = source;
            this.target = target;

            checkSourceNode();
            checkTargetNode();

            this.visited = new boolean[maze.getHeight()][maze.getWidth()];
            this.parents = new HashMap<>();
            this.parents.put(source, null);
        }

        public List<Point> findPath(final Maze maze,
                                    final Point source,
                                    final Point target) {
            return new MazePathFinder(maze, source, target).compute();
        }

        private List<Point> compute() {
            final Queue<Point> queue = new ArrayDeque<>();
            final Map<Point, Integer> distances = new HashMap<>();

            queue.add(source);
            distances.put(source, 0);

            while (!queue.isEmpty()) {
                // Removes the head of the queue.
                final Point current = queue.remove();

                if (current.equals(target)) {
                    return constructPath();
                }

                for (final Point child : generateChildren(current)) {
                    if (!parents.containsKey(child)) {
                        parents.put(child, current);
                        // Appends 'child' to the end of this queue.
                        queue.add(child);
                    }
                }
            }

            // null means that the target node is not reachable
            // from the source node.
            return null;
        }

        private List<Point> constructPath() {
            Point current = target;
            final List<Point> path = new ArrayList<>();

            while (current != null) {
                path.add(current);
                current = parents.get(current);
            }

            Collections.<Point>reverse(path);
            return path;
        }

        private Iterable<Point> generateChildren(final Point current) {
            final Point north = new Point(current.x, current.y - 1);
            final Point south = new Point(current.x, current.y + 1);
            final Point west = new Point(current.x - 1, current.y);
            final Point east = new Point(current.x + 1, current.y);

            final List<Point> childList = new ArrayList<>(4);

            if (maze.cellIsTraversible(north)) {
                childList.add(north);
            }

            if (maze.cellIsTraversible(south)) {
                childList.add(south);
            }

            if (maze.cellIsTraversible(west)) {
                childList.add(west);
            }

            if (maze.cellIsTraversible(east)) {
                childList.add(east);
            }

            return childList;
        }

        private void checkSourceNode() {
            checkNode(source,
                    "The source node (" + source + ") is outside the maze. " +
                            "The width of the maze is " + maze.getWidth() + " and " +
                            "the height of the maze is " + maze.getHeight() + ".");

            if (!maze.cellIsFree(source.x, source.y)) {
                throw new IllegalArgumentException(
                        "The source node (" + source + ") is at a occupied cell.");
            }
        }

        private void checkTargetNode() {
            checkNode(target,
                    "The target node (" + target + ") is outside the maze. " +
                            "The width of the maze is " + maze.getWidth() + " and " +
                            "the height of the maze is " + maze.getHeight() + ".");

            if (!maze.cellIsFree(target.x, target.y)) {
                throw new IllegalArgumentException(
                        "The target node (" + target + ") is at a occupied cell.");
            }
        }

        private void checkNode(final Point node, final String errorMessage) {
            if (node.x < 0
                    || node.x >= maze.getWidth()
                    || node.y < 0
                    || node.y >= maze.getHeight()) {
                throw new IllegalArgumentException(errorMessage);
            }
        }
    }
}
