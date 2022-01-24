package playerv1;

import battlecode.common.*;

import java.util.ArrayList;
import java.util.Random;

public class Miner {
    static MapLocation target;

    public static void doTurn(RobotController rc) throws GameActionException {
        MapLocation [] m = rc.senseNearbyLocationsWithLead();
        boolean mined = false;

        for (int i = 0; i < m.length; i++) {
            RobotInfo[] robots = rc.senseNearbyRobots(m[i], 10, Communication.myTeam);
            int miners = 0;
            int soldiers = 0;
            for (int j = 0; j < robots.length; j++) {
                if (robots[j].type == RobotType.MINER) {
                    miners++;
                } else if (robots[j].type == RobotType.SOLDIER) {
                    soldiers++;
                }
            }
            if (miners < Communication.MAX_MINERS) {
                Communication.addLeadDeposit(rc, m[i]);
//                System.out.println("me: " + rc.getLocation());
//                System.out.println("round: " + rc.getRoundNum());
//                System.out.println("adding location: " + m[i]);
            } else {
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

        RobotInfo[] enemies = rc.senseNearbyRobots(20, Communication.enemyTeam);
        for (int i = 0; i < enemies.length; i++){
            RobotInfo[] friendlyRobots = rc.senseNearbyRobots(enemies[i].getLocation(), 10, Communication.myTeam);
            int soldiers = 0;
            for (int j = 0; j < friendlyRobots.length; j++){
                if (friendlyRobots[j].type == RobotType.SOLDIER){
                    soldiers++;
                }
            }
            if (soldiers < Communication.MAX_SOLDIERS){
                Communication.addHuntingLocation(rc, enemies[i].getLocation());
            }

            RobotInfo r = rc.senseRobotAtLocation(enemies[i].getLocation());
            if (r == null || r.team == Communication.myTeam){
                Communication.removeHuntingLocation(rc, enemies[i].getLocation());
            }
        }

        if (!mined) { //need to move to a deposit
            ArrayList<BetterLocation> options = Communication.getLeadDeposits(rc);
            if (options.size() == 0) {
                Movement.moveRandomly(rc);
            } else {
                target = options.get(0).loc;
                MapLocation me = rc.getLocation();

                int min = Integer.MAX_VALUE;
                for (BetterLocation cur : options) {
                    int dist = me.distanceSquaredTo(cur.loc);
                    if (dist < min) {
                        min = dist;
                        target = cur.loc;
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
    }
}
