package playerv1;

import battlecode.common.*;

import java.util.ArrayList;

public class Soldier {
    static MapLocation target;

    public static void doTurn(RobotController rc) throws GameActionException {
        ArrayList<BetterLocation> options = Communication.getHuntingLocations(rc);
        for (BetterLocation cur : options){
            MapLocation loc = cur.loc;
            if (rc.getLocation().distanceSquaredTo(loc) < rc.getType().visionRadiusSquared){
                RobotInfo r = rc.senseRobotAtLocation(loc);
                if (r == null || r.team == Communication.myTeam){
                    Communication.removeHuntingLocation(rc, loc);
                }
            }
        }

        RobotInfo[] enemies = rc.senseNearbyRobots(20, Communication.enemyTeam);
        boolean attacked = false;

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

            if (rc.canAttack(enemies[i].getLocation())) {
                rc.attack(enemies[i].getLocation());
                attacked = true;
            }

            RobotInfo r = rc.senseRobotAtLocation(enemies[i].getLocation());
            if (r == null || r.team == Communication.myTeam){
                Communication.removeHuntingLocation(rc, enemies[i].getLocation());
            }
        }

        MapLocation [] m = rc.senseNearbyLocationsWithLead();
        for (int i = 0; i < m.length; i++){
            RobotInfo[] robots = rc.senseNearbyRobots(m[i], 10, Communication.myTeam);
            int miners = 0;
            for (int j = 0; j < robots.length; j++){
                if (robots[j].type == RobotType.MINER){
                    miners++;
                }
            }
            if (miners < Communication.MAX_MINERS){
                Communication.addLeadDeposit(rc, m[i]);
            }
            else{
                Communication.removeLeadDeposit(rc, m[i]);
            }

            if (rc.senseLead(m[i]) == 0) {
                Communication.removeLeadDeposit(rc, m[i]);
            }
        }

        if (!attacked){ //need to move to an enemy
            if (options.size() == 0){
                Movement.moveRandomly(rc);
            }
            else{
                MapLocation lastTarget = target;
                MapLocation me = rc.getLocation();

                int min = Integer.MAX_VALUE;
                for (BetterLocation cur : options){
                    int num = cur.num;
                    MapLocation loc = cur.loc;

                    if (num < Communication.MAX_SOLDIERS){
                        int dist = me.distanceSquaredTo(loc);
                        if (dist < min){
                            min = dist;
                            target = loc;
                        }
                    }
                }

                if (target == null){
                    Movement.moveRandomly(rc);
                    return;
                }

                if (!target.equals(lastTarget)){
                    if (Communication.goingToHuntingLocation(rc, target)){
                        Communication.notGoingToHuntingLocation(rc, lastTarget);
                    }
                    else{
                        target = null;
                        Movement.moveRandomly(rc);
                        return;
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
        else{
            Communication.notGoingToHuntingLocation(rc, target);
            target = null;
        }
    }
}
