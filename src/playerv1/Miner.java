package playerv1;

import battlecode.common.*;

import java.util.ArrayList;
import java.util.Random;

public class Miner {
    private enum States {
        FIND_LEAD, MINE_LEAD
    }

    private static States currentState;

    public static final int MAX_MINERS = 10;
    public static final int MAX_SOLDIERS = 10;


    public static void doTurn(RobotController rc) throws GameActionException {
        if (Communication.getLeadDeposits(rc).size() != 0) {
            currentState = States.MINE_LEAD;
        } else {
            currentState = States.FIND_LEAD;
        }

        switch(currentState) {
            case FIND_LEAD:
                MapLocation [] m = rc.senseNearbyLocationsWithLead();
                int max = -1;
                int loc = -1;
                for (int i = 0; i < m.length; i++){
                    int cur = rc.senseLead(m[i]);
                    if (cur > max){
                        RobotInfo[] robots = rc.senseNearbyRobots(m[i], 10, Communication.myTeam);
                        if (robots.length == 0){ //only consider this location if there's no friendly miners
                            max = cur;
                            loc = i;
                            Communication.addLeadDeposit(rc, m[loc]);
                            //Communication.addSoldierCall(rc, m[loc]);
                        }
                    }
                }

                if (loc == -1){ //need to move to find a deposit
                    Random r = new Random();
                    int rand = r.nextInt(4);
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

            case MINE_LEAD:
                MapLocation [] lead = rc.senseNearbyLocationsWithLead();
                boolean mined = false;
                for (int i = 0; i < lead.length; i++){
                    while(rc.canMineLead(lead[i])) {
                        rc.mineLead(lead[i]);
                        mined = true;
                    }
                    Communication.addLeadDeposit(rc, lead[i]);
                    if (rc.senseLead(lead[i]) == 0){
                        Communication.removeLeadDeposit(rc, lead[i]);
                    }

                    RobotInfo[] robots = rc.senseNearbyRobots(lead[i], 10, Communication.myTeam);
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
                    if (miners > MAX_MINERS){
                        Communication.removeLeadDeposit(rc, lead[i]);
                        System.out.println("remove location " + lead[i].toString());
                    }
//                    if (soldiers > MAX_SOLDIERS){
//                        Communication.removeSoldierCall(rc, lead[i]);
//                    }
                }

                if (!mined){ //need to go to a deposit to mine
                    ArrayList<MapLocation> options = Communication.getLeadDeposits(rc);
                    if (options.size() == 0){
                        currentState = States.FIND_LEAD;
                        System.out.println("broke: " + rc.getLocation());
                        break;
                    }
                    MapLocation target = options.get(0);
                    MapLocation me = rc.getLocation();

                    int min = Integer.MAX_VALUE;
                    for (MapLocation cur : options){
                        int dist = Math.abs(cur.x - me.x) + Math.abs(cur.y - me.y);
                        if (dist < min){
                            min = dist;
                            target = cur;
                        }
                    }
                    System.out.println("Round: " + rc.getRoundNum());
                    System.out.println("me: " + rc.getLocation());
                    System.out.println("target: " + target);
                    System.out.println();

                    //potential problem: miner won't check if the location is empty until it arrives
                    //it get stuck inside the while loop
                    while (Math.abs(rc.getLocation().x - target.x) > 1){
                        Direction dir = rc.getLocation().x < target.x ? Direction.EAST : Direction.WEST;
                        if (rc.canMove(dir)){
                            rc.move(dir);
                        }
                    }
                    while (Math.abs(rc.getLocation().y - target.y) > 1){
                        Direction dir = rc.getLocation().y < target.y ? Direction.NORTH : Direction.SOUTH;
                        if (rc.canMove(dir)){
                            rc.move(dir);
                        }
                    }
                }
                break;
        }
    }
}
