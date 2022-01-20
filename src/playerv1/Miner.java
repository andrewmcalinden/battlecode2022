package playerv1;

import battlecode.common.*;

import java.util.ArrayList;
import java.util.Random;

public class Miner {
    public static final int MAX_MINERS = 5;
    public static final int MAX_SOLDIERS = 10;

    static MapLocation target;

    public static void doTurn(RobotController rc) throws GameActionException {
        MapLocation [] m = rc.senseNearbyLocationsWithLead();
        boolean mined = false;

        for (int i = 0; i < m.length; i++){
            RobotInfo[] robots = rc.senseNearbyRobots(m[i], 10, Communication.myTeam);
            int miners = 0;
            int soldiers = 0;
            for (int j = 0; j < robots.length; j++){
                if (robots[j].type == RobotType.MINER){
                    miners++;
                }
                else if (robots[j].type == RobotType.SOLDIER){
                    soldiers++;
                }
            }
            if (miners < MAX_MINERS){
                Communication.addLeadDeposit(rc, m[i]);
            }
            else{
                Communication.removeLeadDeposit(rc, m[i]);
            }

            if (rc.canMineLead(m[i])) {
                rc.mineLead(m[i]);
                mined = true;
            }
            if (rc.senseLead(m[i]) == 0) {
                Communication.removeLeadDeposit(rc, m[i]);
            }
        }

        if (!mined){ //need to move to a deposit
            ArrayList<MapLocation> options = Communication.getLeadDeposits(rc);
            if (options.size() == 0){
                Movement.moveRandomly(rc);
            }
            else{
                target = options.get(0);
                MapLocation me = rc.getLocation();

                int min = Integer.MAX_VALUE;
                for (MapLocation cur : options){
                    int dist = Math.abs(cur.x - me.x) + Math.abs(cur.y - me.y);
                    if (dist < min){
                        min = dist;
                        target = cur;
                    }
                }

                if (Math.abs(rc.getLocation().x - target.x) > 1 || Math.abs(rc.getLocation().y - target.y) > 1) {
                    Direction dir1 = rc.getLocation().x < target.x ? Direction.EAST : Direction.WEST;
                    Direction dir2 = rc.getLocation().y < target.y ? Direction.NORTH : Direction.SOUTH;
                    int r = RobotPlayer.rng.nextInt(2);
                    if (r == 0) {
                        if (rc.canMove(dir1)) {
                            rc.move(dir1);
                        }
                    } else {
                        if (rc.canMove(dir2)) {
                            rc.move(dir2);
                        }
                    }
                }
            }
        }
//        if (Clock.getBytecodesLeft() > 0){
//            doTurn(rc);
//        }
    }
}
