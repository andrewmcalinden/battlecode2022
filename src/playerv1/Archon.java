package playerv1;

import battlecode.common.*;

public class Archon{

    private static int numMiners = 0;
    private static int NO_MORE_MINERS = -1;

    public static void doTurn(RobotController rc) throws GameActionException {
        if (NO_MORE_MINERS == -1){
            NO_MORE_MINERS = (int)(rc.getMapHeight() * rc.getMapWidth() * .01);
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
            if (rc.canBuildRobot(RobotType.MINER, Direction.WEST)){
                rc.buildRobot(RobotType.MINER, Direction.WEST);
                numMiners++;
            }
            if (rc.canBuildRobot(RobotType.MINER, Direction.EAST)){
                rc.buildRobot(RobotType.MINER, Direction.EAST);
                numMiners++;
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
