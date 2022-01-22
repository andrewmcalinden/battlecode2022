package playerv1;

import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.Team;

import java.util.ArrayList;

public class Communication {
    public static final int MINER_START = 0;
    public static final int MINER_STOP = 29;

    public static final int SOLDIER_START = 30;
    public static final int SOLDIER_STOP = 59;

    public static Team myTeam = (Team.A).isPlayer() ? Team.A : Team.B;
    public static Team enemyTeam = myTeam.opponent();

    public static ArrayList<MapLocation> getLeadDeposits(RobotController rc) throws GameActionException {
        ArrayList<MapLocation> ans = new ArrayList<>();
        for (int i = MINER_START; i <= MINER_STOP; i++){
            int read = rc.readSharedArray(i);
            if (read != 0){
                ans.add(intToMapLocation(read));
            }
        }
        return ans;
    }

    public static ArrayList<MapLocation> getHuntingLocations(RobotController rc) throws GameActionException {
        ArrayList<MapLocation> ans = new ArrayList<>();
        for (int i = SOLDIER_START; i <= SOLDIER_STOP; i++){
            int read = rc.readSharedArray(i);
            if (read != 0){
                ans.add(intToMapLocation(read));
            }
        }
        return ans;
    }

    public static void addLeadDeposit(RobotController rc, MapLocation loc) throws GameActionException {
        int firstBlank = -1;
        for (int i = MINER_START; i <= MINER_STOP; i++){
            int val = rc.readSharedArray(i);
            if (val != 0 && intToMapLocation(val).equals(loc)){
                return; //this deposit is already in the array
            }
            if (val == 0) {
                firstBlank = i;
                break;
            }
        }
        if (firstBlank != -1){
            rc.writeSharedArray(firstBlank, mapLocationToInt(loc));
        }
    }

    public static void addHuntingLocation(RobotController rc, MapLocation loc) throws GameActionException {
        int firstBlank = -1;
        for (int i = SOLDIER_START; i <= SOLDIER_STOP; i++){
            int val = rc.readSharedArray(i);
            if (val != 0 && intToMapLocation(val).equals(loc)){
                return; //this deposit is already in the array
            }
            if (val == 0) {
                firstBlank = i;
                break;
            }
        }
        if (firstBlank != -1){
            rc.writeSharedArray(firstBlank, mapLocationToInt(loc));
        }
    }

    public static void removeLeadDeposit(RobotController rc, MapLocation loc) throws GameActionException{
        for (int i = MINER_START; i <= MINER_STOP; i++){
            int read = rc.readSharedArray(i);
            if (read != 0){
                if (intToMapLocation(read).equals(loc)){
                    rc.writeSharedArray(i, 0);
                }

            }
        }
    }

    public static void removeHuntingLocation(RobotController rc, MapLocation loc) throws GameActionException{
        for (int i = SOLDIER_START; i <= SOLDIER_STOP; i++){
            int read = rc.readSharedArray(i);
            if (read != 0){
                if (intToMapLocation(read).equals(loc)){
                    rc.writeSharedArray(i, 0);
                }
            }
        }
    }

    //use this to count how many miners are going to a deposit
    //if the number is too high, don't send any more miners there
    public static void goingToLeadDeposit(RobotController rc, MapLocation loc) throws GameActionException{
        for (int i = MINER_START; i <= MINER_STOP; i++){
            int read = rc.readSharedArray(i);
            if (read != 0){
                if (intToMapLocation(read).equals(loc)){
                    rc.writeSharedArray(i, read + 10000);
                }

            }
        }
    }

    public static void goingToHuntingLocation(RobotController rc, MapLocation loc) throws GameActionException{
        for (int i = SOLDIER_START; i <= SOLDIER_STOP; i++){
            int read = rc.readSharedArray(i);
            if (read != 0){
                if (intToMapLocation(read).equals(loc)){
                    rc.writeSharedArray(i, read + 10000);
                }

            }
        }
    }

    public static MapLocation intToMapLocation(int i){
        String s = "" + i;
        int x = Integer.parseInt(s.substring(1, 3));
        int y = Integer.parseInt(s.substring(3));
        return new MapLocation(x, y);
    }

    public static int mapLocationToInt(MapLocation m){
        String x = "" + m.x;
        if (m.x < 10){
            x = "0" + m.x;
        }

        String y = "" + m.y;
        if (m.y < 10){
            y = "0" + m.y;
        }
        return Integer.parseInt("1" + x + y);
    }

}
