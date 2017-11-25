package com.aci.student24.tanks;


import com.aci.student24.api.tanks.Algorithm;
import com.aci.student24.api.tanks.objects.Base;
import com.aci.student24.api.tanks.state.Direction;
import com.aci.student24.api.tanks.state.MapState;
import com.aci.student24.api.tanks.state.TankMove;

import java.util.List;
import java.util.stream.Collectors;

public class ToBase implements Algorithm {
    private int move = 0;
    private int teamId;
    private Base enemyBasePos;
    private int i = 0;
    private final int[] tankNumber = {0};
    private final int[] numberOfTanks = {5};

    @Override
    public void setMyId(final int id) {
        teamId = id;
    }

    @Override
    public List<TankMove> nextMoves(MapState mapState) {
        move++;
        enemyBasePos = mapState.getBases().stream().filter(x -> x.getTeamId() != teamId).findAny().get();
        tankNumber[0] = 0;

        List<TankMove> tanks =  mapState.getTanks(teamId).stream().map(tank -> {
            int x = tank.getX();
            int y = tank.getY();
            byte dir = Direction.NO;
            boolean shoot = false;


            if(tankNumber[0] == 0) {
                if (y > enemyBasePos.getY()) {
                    dir = Direction.DOWN;
                } else if (y < enemyBasePos.getY()) {
                    dir = Direction.UP;
                } else
                    shoot = true;
            }


            tankNumber[0]++;

            System.out.println("X: " + "Y: " + y);

            return new TankMove(tank.getId(), dir, shoot);
        }).collect(Collectors.toList());

        numberOfTanks[0] = tankNumber[0];

        return tanks;
    }
}
