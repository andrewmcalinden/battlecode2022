package playerv1;

import battlecode.common.*;

public class Archon {
    private enum States{
        FIND_LEAD, MINE_LEAD
    }

    private static States currentState;

    public static void doTurn(RobotController rc) throws GameActionException {
        if (Communication.getLeadDeposits(rc).size() != 0){
            currentState = States.MINE_LEAD;
        }
        else{
            currentState = States.FIND_LEAD;
        }

        switch(currentState){
            case FIND_LEAD:
                if (rc.canBuildRobot(RobotType.MINER, Direction.NORTH)){
                    rc.buildRobot(RobotType.MINER, Direction.NORTH);
                }
                if (rc.canBuildRobot(RobotType.MINER, Direction.SOUTH)){
                    rc.buildRobot(RobotType.MINER, Direction.SOUTH);
                }
                if (rc.canBuildRobot(RobotType.MINER, Direction.WEST)){
                    rc.buildRobot(RobotType.MINER, Direction.WEST);
                }
                if (rc.canBuildRobot(RobotType.MINER, Direction.EAST)){
                    rc.buildRobot(RobotType.MINER, Direction.EAST);
                }
                break;
            case MINE_LEAD:
                if (rc.canBuildRobot(RobotType.MINER, Direction.NORTH)){
                    rc.buildRobot(RobotType.MINER, Direction.NORTH);
                }
                if (rc.canBuildRobot(RobotType.MINER, Direction.SOUTH)){
                    rc.buildRobot(RobotType.MINER, Direction.SOUTH);
                }
                if (rc.canBuildRobot(RobotType.MINER, Direction.WEST)){
                    rc.buildRobot(RobotType.MINER, Direction.WEST);
                }
                if (rc.canBuildRobot(RobotType.MINER, Direction.EAST)){
                    rc.buildRobot(RobotType.MINER, Direction.EAST);
                }

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
                break;
        }

        RobotInfo [] friendlyBots = rc.senseNearbyRobots();
        for (int i = 0; i < friendlyBots.length; i++){
            if (rc.canRepair(friendlyBots[i].getLocation())){
                rc.repair(friendlyBots[i].getLocation());
            }
        }
    }
}
