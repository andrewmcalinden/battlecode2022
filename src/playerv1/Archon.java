package playerv1;

import battlecode.common.*;

public class Archon{

    private static int numMiners = 0;
    private static int NO_MORE_MINERS = -1;

    public static void doTurn(RobotController rc) throws GameActionException {
        if (NO_MORE_MINERS == -1){
            NO_MORE_MINERS = (int)(rc.getMapHeight() * rc.getMapWidth() * .003);
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

            RobotInfo r = rc.senseRobotAtLocation(enemies[i].getLocation());
            if (r == null || r.team == Communication.myTeam){
                Communication.removeHuntingLocation(rc, enemies[i].getLocation());
            }
        }

        MapLocation [] m = rc.senseNearbyLocationsWithLead();

        for (int i = 0; i < m.length; i++) {
            RobotInfo[] robots = rc.senseNearbyRobots(m[i], 10, Communication.myTeam);
            int miners = 0;
            for (int j = 0; j < robots.length; j++) {
                if (robots[j].type == RobotType.MINER) {
                    miners++;
                }
            }

            if (miners < Communication.MAX_MINERS) {
                Communication.addLeadDeposit(rc, m[i]);
            } else {
                Communication.removeLeadDeposit(rc, m[i]);
            }
            if (rc.senseLead(m[i]) == 0) {
                Communication.removeLeadDeposit(rc, m[i]);
            }
        }

        if (numMiners >= NO_MORE_MINERS ){
            if (rc.canBuildRobot(RobotType.SOLDIER, Direction.NORTH)){
                rc.buildRobot(RobotType.SOLDIER, Direction.NORTH);
            }
            if (rc.canBuildRobot(RobotType.SOLDIER, Direction.SOUTH)){
                rc.buildRobot(RobotType.SOLDIER, Direction.SOUTH);
            }
            if (rc.canBuildRobot(RobotType.SOLDIER, Direction.WEST)){
                rc.buildRobot(RobotType.SOLDIER, Direction.WEST);
            }
            if (rc.canBuildRobot(RobotType.SOLDIER, Direction.EAST)){
                rc.buildRobot(RobotType.SOLDIER, Direction.EAST);
            }
        }
        else{
            if (rc.canBuildRobot(RobotType.MINER, Direction.NORTH)){
                rc.buildRobot(RobotType.MINER, Direction.NORTH);
                numMiners++;
            }
            if (rc.canBuildRobot(RobotType.MINER, Direction.SOUTH)){
                rc.buildRobot(RobotType.MINER, Direction.SOUTH);
                numMiners++;
            }
            if (rc.canBuildRobot(RobotType.MINER, Direction.NORTHEAST)){
                rc.buildRobot(RobotType.MINER, Direction.NORTHEAST);
                numMiners++;
            }
            if (rc.canBuildRobot(RobotType.SOLDIER, Direction.WEST)){
                rc.buildRobot(RobotType.SOLDIER, Direction.WEST);
            }
            if (rc.canBuildRobot(RobotType.SOLDIER, Direction.EAST)){
                rc.buildRobot(RobotType.SOLDIER, Direction.EAST);
            }
        }

        RobotInfo [] friendlyBots = rc.senseNearbyRobots();
        for (int i = 0; i < friendlyBots.length; i++){
            if (rc.canRepair(friendlyBots[i].getLocation())){
                rc.repair(friendlyBots[i].getLocation());
            }
        }
    }
}
