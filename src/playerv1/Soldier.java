package playerv1;

import battlecode.common.*;

import java.util.ArrayList;

public class Soldier {
    public static final int MAX_MINERS = 5;
    public static final int MAX_SOLDIERS = 10;

    static MapLocation target;

    public static void doTurn(RobotController rc) throws GameActionException {
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
            if (soldiers < MAX_SOLDIERS){
                Communication.addHuntingLocation(rc, enemies[i].getLocation());
            }

            if (rc.canAttack(enemies[i].getLocation())) {
                rc.attack(enemies[i].getLocation());
                attacked = true;
            }

            if (rc.canSenseRobotAtLocation(enemies[i].getLocation())){
                RobotInfo r = rc.senseRobotAtLocation(enemies[i].getLocation());
                if (r != null && r.team == Communication.myTeam){
                    Communication.removeHuntingLocation(rc, enemies[i].getLocation());
                }
            }
            else{
                Communication.removeHuntingLocation(rc, enemies[i].getLocation());
            }

            RobotInfo[] nearbyEnemies = rc.senseNearbyRobots(20, Communication.enemyTeam);
            if (nearbyEnemies.length == 0){
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
            if (miners < MAX_MINERS){
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
            ArrayList<MapLocation> options = Communication.getHuntingLocations(rc);
            if (options.size() == 0){
                Movement.moveRandomly(rc);
            }
            else{
                for (MapLocation cur : options){
                    //remove it if we sense anything other than an enemy robot
                    if (rc.canSenseRobotAtLocation(cur)){
                        RobotInfo r = rc.senseRobotAtLocation(cur);
                        if (r != null && r.team == Communication.myTeam){
                            Communication.removeHuntingLocation(rc, cur);
                        }
                    }
//                    else{
//                        Communication.removeHuntingLocation(rc, cur);
//                    }

                    target = options.get(0);
                    MapLocation me = rc.getLocation();

                    int min = Integer.MAX_VALUE;

                    int dist = me.distanceSquaredTo(cur);
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
    }
}
