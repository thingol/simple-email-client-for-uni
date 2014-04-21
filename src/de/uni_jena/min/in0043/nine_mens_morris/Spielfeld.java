package de.uni_jena.min.in0043.nine_mens_morris;

import java.awt.*;

public class Spielfeld{
	
	public int[][] placement;
	public boolean[] placed;
	
	public Spielfeld()
	{
		placement = new int[24][2];
		placed = new boolean[24];
	}
	
	public void places(Frame mFra)
	{
		int height = mFra.getSize().height;
		int width = mFra.getSize().width;
		int middleX = width / 2;
		int middleY = height / 2;
		int addX = middleX - middleX / 4;
		int addY = middleY - middleY / 4;
		
		//Outer top
		placement[0][0] = middleX / 4;
		placement[0][1] = middleY / 4;
		placement[1][0] = middleX / 4 + addX;
		placement[1][1] = middleY / 4;
		placement[2][0] = middleX / 4 + (addX) * 2;
		placement[2][1] = middleY / 4;
		//Middle top
		placement[3][0] = middleX / 4 + addX / 3;
		placement[3][1] = middleY / 4 + addY / 3;
		placement[4][0] = middleX / 4 + (addX) / 3 + (middleX - (middleX / 4 + addX / 3));
		placement[4][1] = middleY / 4 + addY / 3;
		placement[5][0] = middleX / 4 + (addX) / 3 + (middleX - (middleX / 4 + addX / 3)) * 2;
		placement[5][1] = middleY / 4 + addY / 3;
		//Inner top
		placement[6][0] = middleX / 4 + 2* addX / 3;
		placement[6][1] = middleY / 4 + 2* addY / 3;
		placement[7][0] = middleX / 4 + 2*addX / 3 + (middleX - (middleX / 4 + 2 * addX / 3));
		placement[7][1] = middleY / 4 + 2* addY / 3;
		placement[8][0] = middleX / 4 + 2*(addX)/3 + (middleX - (middleX / 4 + 2 * addX / 3)) * 2;
		placement[8][1] = middleY / 4 + 2* addY / 3;
		//Middle of Field
		
		placement[9][0] = middleX / 4;
		placement[9][1] = middleY;
		placement[10][0] = middleX / 4 + addX / 3;
		placement[10][1] = middleY;
		placement[11][0] = middleX / 4 + 2* addX / 3;
		placement[11][1] = middleY;
		
		placement[12][0] = middleX / 4 + 2*(addX)/3 + (middleX - (middleX / 4 + 2 * addX / 3)) * 2;
		placement[12][1] = middleY;
		placement[13][0] = middleX / 4 + (addX) / 3 + (middleX - (middleX / 4 + addX / 3)) * 2;
		placement[13][1] = middleY;
		placement[14][0] = middleX / 4 + (addX) * 2;
		placement[14][1] = middleY;
		
		//Inner bottom
		placement[15][0] = middleX / 4 + 2* addX / 3;
		placement[15][1] = middleY / 4 + 2* addY / 3 + (middleY - (middleY / 4 + 2 * addY / 3)) * 2;
		placement[16][0] = middleX / 4 + 2*addX / 3 + (middleX - (middleX / 4 + 2 * addX / 3));
		placement[16][1] = middleY / 4 + 2* addY / 3 + (middleY - (middleY / 4 + 2 * addY / 3)) * 2;
		placement[17][0] = middleX / 4 + 2*(addX)/3 + (middleX - (middleX / 4 + 2 * addX / 3)) * 2;
		placement[17][1] = middleY / 4 + 2* addY / 3 + (middleY - (middleY / 4 + 2 * addY / 3)) * 2;
		//Middle bottom
		placement[18][0] = middleX / 4 + addX / 3;
		placement[18][1] = middleY / 4 + addY / 3 + (middleY - (middleY / 4 + addY / 3)) * 2;
		placement[19][0] = middleX / 4 + (addX) / 3 + (middleX - (middleX / 4 + addX / 3));
		placement[19][1] = middleY / 4 + addY / 3 + (middleY - (middleY / 4 + addY / 3)) * 2;
		placement[20][0] = middleX / 4 + (addX) / 3 + (middleX - (middleX / 4 + addX / 3)) * 2;
		placement[20][1] = middleY / 4 + addY / 3 + (middleY - (middleY / 4 + addY / 3)) * 2;
		//Outer bottom
		placement[21][0] = middleX / 4;
		placement[21][1] = middleY / 4 + (addY) * 2;
		placement[22][0] = middleX / 4 + addX;
		placement[22][1] = middleY / 4 + (addY) * 2;
		placement[23][0] = middleX / 4 + (addX) * 2;
		placement[23][1] = middleY / 4 + (addY) * 2;
	}

}