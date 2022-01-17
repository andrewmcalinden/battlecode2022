package playerv1;

import battlecode.common.Clock;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;

public strictfp class RobotPlayer {

    @SuppressWarnings("unused")
    public static void run(RobotController rc) throws GameActionException {
        while(true){
            try{
                switch(rc.getType()){
                    case ARCHON:
                        Archon.doTurn(rc);
                        break;
                    case MINER:
                        Miner.doTurn(rc);
                        break;
                    case SOLDIER:
                        Soldier.doTurn(rc);
                        break;
                }
            }
            catch(GameActionException e){
                System.out.println(rc.getType() + " Exception");
                e.printStackTrace();
            }
            catch (Exception e) {
                System.out.println(rc.getType() + " Exception");
                e.printStackTrace();
            }
            finally {
                Clock.yield();
            }
        }
    }
}
