package playerv1;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;

public class Movement {
    public static void moveRandomly(RobotController rc) throws GameActionException {
        int rand = RobotPlayer.rng.nextInt(4);
        switch (rand) {
            case 0:
                if (rc.canMove(Direction.NORTH)) {
                    rc.move(Direction.NORTH);
                    break;
                }
            case 1:
                if (rc.canMove(Direction.SOUTH)) {
                    rc.move(Direction.SOUTH);
                    break;
                }
            case 2:
                if (rc.canMove(Direction.EAST)) {
                    rc.move(Direction.EAST);
                    break;
                }
            case 3:
                if (rc.canMove(Direction.WEST)) {
                    rc.move(Direction.WEST);
                    break;
                }
        }
    }
}
