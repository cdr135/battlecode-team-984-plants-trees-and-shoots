package testplayer;

import java.util.*;
import battlecode.common.*;
import testplayer.res.*;
import static testplayer.res.Utils.*;

@SuppressWarnings("unused")
public class ArchonBrain implements Brain {

	private Routine last;
	private Routine current;
	private RobotController rc;
	private MapLocation lastLoc;
	private double radius;
	private int radiusInc;
	private boolean space;
	private Direction[] dirs = Directions.d6();
	// private List<MapLocation> knownEnemyArchons;

	private Map<Integer, RobotInfo> robots;

	private boolean isLeader;

	private void runTurn() throws GameActionException {
		float numPoints = rc.getTeamBullets();
		TreeInfo[] treeinfo = rc.senseNearbyTrees();
		RobotInfo[] nejworld =  rc.senseNearbyRobots();
		double numGardeners = 0;
		double numTrees = 0;
		for (int i = 0; i < nejworld.length; i++){
			if (nejworld[i].getType().equals(RobotType.GARDENER)){
				numGardeners++;
			}
		}
		for (int i = 0 ; i < treeinfo.length; i++){
			if (treeinfo[i].team.equals(rc.getTeam())){
				numTrees++;
			}
		}
		
		//if (numTrees/numGardeners >= 4 || numGardeners < 3){ //placeholder
		if (numGardeners < 4 && rc.getTeamBullets() > 150)
			for (Direction d : shuffle(dirs))
				if (rc.canBuildRobot(RobotType.GARDENER, d)){
					rc.buildRobot(RobotType.GARDENER, d);
					break;
				}
				
		//}
		// if we can instantly win the game, win
		if (rc.getTeamBullets() >= (1000 - rc.getTeamVictoryPoints()) * rc.getVictoryPointCost())
			rc.donate((float) ((1000 - rc.getTeamVictoryPoints()) * rc.getVictoryPointCost() + 0.000000001));
		if (rc.getTeamBullets() > 250){
			rc.donate((float) (Math.round(rc.getTeamBullets()/(20*rc.getVictoryPointCost()))*rc.getVictoryPointCost()+.000000001));
		}
		//cash in when game ends
		if(rc.getRoundNum() == rc.getRoundLimit() - 1){
			rc.donate(rc.getTeamBullets());
		}
		//cash in when archon almost dead
		if(rc.getHealth()<30){
			rc.donate(rc.getTeamBullets());
		}
		BulletInfo[] bleh = rc.senseNearbyBullets();
		//insert dodge script here/movement
		}
	
	private void move() throws GameActionException{
		
	}
	private void initialize() throws GameActionException {
		current = Routine.GROUP;
		robots = new TreeMap<Integer, RobotInfo>();
		radius = 8;
		radiusInc = 7;
		space = true;
	}

	@Override
	public void run(RobotController rcI) {
		rc = rcI;
		try {
			initialize();
		} catch (GameActionException e1) {
			e1.printStackTrace();
		}

		while (true) {
			Clock.yield();
			try {
				runTurn();
			} catch (GameActionException e) {
				e.printStackTrace();
			}
		}
	}

}