package lane;

import java.util.HashMap;
import java.util.Iterator;

import bowler.Bowler;
import party.Party;
import score.ScoreHistoryFile;

class LaneScore {
	private int[] curScores;
	private int[][] cumulScores;
	private int[][] finalScores;
	private HashMap scores;

	LaneScore() {
		scores = new HashMap();
	}

	public int[] getScoreFromKey(Bowler Cur) {
		return (int[])scores.get(Cur);
	}

	public void setScoresInHashMap(Bowler Cur, int[] curScore) {
		scores.put(Cur, curScore);
	}

	public void setCumulScore(int row, int col, int value) {
		this.cumulScores[row][col] = value;
	}

	private void incrementCumulScore(int row, int col, int incValue) {
		setCumulScore(row, col, getCumulScore(row, col) + incValue);
	}

	public int[][] getFinalScores() {
		return finalScores;
	}

	public void setFinalScores(int row, int col, int value) {
		getFinalScores()[row][col] = value;
	}

	public int getCumulScore(int row, int col) {
		return this.getCumulScores()[row][col];
	}

	public int[][] getCumulScores() {
		return this.cumulScores;
	}

	public HashMap getScoresHashMap() {
		return scores;
	}

	public void initScoresArrays(int size) {
		curScores = new int[size];
		cumulScores = new int[size][10];
		finalScores = new int[size][128];//Hardcoding a max of 128 games
	}


	/** markScore()
	 *
	 * Method that marks a bowlers score on the board.
	 * 
	 * @param Cur		The current bowler
	 * @param frame	The frame that bowler is on
	 * @param ball		The ball the bowler is on
	 * @param score	The bowler's score 
	 */
	public void markScore( Bowler Cur, int frame, int throwNumber, int score, 
							int bowlIndex, int ball){
		int[] curScore;
		int index =  ( (frame - 1) * 2 + throwNumber);

		curScore = (int[]) getScoreFromKey(Cur);

	
		curScore[ index - 1] = score;
		setScoresInHashMap(Cur, curScore);
		getScore( Cur, frame, bowlIndex, ball);
	}


	/** getScore()
	 *
	 * Method that calculates a bowlers score
	 * 
	 * @param Cur		The bowler that is currently up
	 * @param frame	The frame the current bowler is on
	 * 
	 * @return			The bowlers total score
	 */
	private int getScore( Bowler Cur, int frame, int bowlIndex, int ball) {
		int[] curScore;
		int strikeballs = 0;
		int totalScore = 0;
		curScore = getScoreFromKey(Cur);
		for (int i = 0; i != 10; i++){
			setCumulScore(bowlIndex, i, 0);
		}
		int current = 2*(frame - 1)+ball-1;
		//Iterate through each ball until the current one.
		for (int i = 0; i != current+2; i++){
			//Spare:
			if( i%2 == 1 && curScore[i - 1] + curScore[i] == 10 && i < current - 1 && i < 19){
				//This ball was a the second of a spare.  
				//Also, we're not on the current ball.
				//Add the next ball to the ith one in cumul.
				incrementCumulScore(bowlIndex, (i/2), curScore[i+1] + curScore[i]); 
			} else if( i < current && i%2 == 0 && curScore[i] == 10  && i < 18){
				strikeballs = 0;
				//This ball is the first ball, and was a strike.
				//If we can get 2 balls after it, good add them to cumul.
				if (curScore[i+2] != -1) {
					strikeballs = 1;
					if(curScore[i+3] != -1) {
						//Still got em.
						strikeballs = 2;
					} else if(curScore[i+4] != -1) {
						//Ok, got it.
						strikeballs = 2;
					}
				}
				if (strikeballs == 2){
					//Add up the strike.
					//Add the next two balls to the current cumulscore.
					incrementCumulScore(bowlIndex, i/2, 10);
					if(curScore[i+1] != -1) {
						incrementCumulScore(bowlIndex, i/2, curScore[i+1] + getCumulScore(bowlIndex, (i/2)-1));
						if (curScore[i+2] != -1){
							if( curScore[i+2] != -2){
								incrementCumulScore(bowlIndex, (i/2), curScore[i+2]);
							}
						} else {
							if( curScore[i+3] != -2){
								incrementCumulScore(bowlIndex, (i/2), curScore[i+3]);
							}
						}
					} else {
						if ( i/2 > 0 ){
							incrementCumulScore(bowlIndex, i/2, curScore[i+2] + getCumulScore(bowlIndex, (i/2)-1));
						} else {
							incrementCumulScore(bowlIndex, i/2, curScore[i+2]);
						}
						if (curScore[i+3] != -1){
							if( curScore[i+3] != -2){
								incrementCumulScore(bowlIndex, (i/2), curScore[i+3]);
							}
						} else {
							incrementCumulScore(bowlIndex, (i/2), curScore[i+4]);
						}
					}
				} else {
					break;
				}
			}else { 
				//We're dealing with a normal throw, add it and be on our way.
				if( i%2 == 0 && i < 18){
					if ( i/2 == 0 ) {
						//First frame, first ball.  Set his cumul score to the first ball
						if(curScore[i] != -2){	
							incrementCumulScore(bowlIndex, i/2, curScore[i]);
						}
					} else if (i/2 != 9){
						//add his last frame's cumul to this ball, make it this frame's cumul.
						if(curScore[i] != -2){
							incrementCumulScore(bowlIndex, i/2 , getCumulScore(bowlIndex, i/2 - 1) + curScore[i]);
						} else {
							incrementCumulScore(bowlIndex, i/2, getCumulScore(bowlIndex, i/2 - 1));
						}	
					}
				} else if (i < 18){ 
					if(curScore[i] != -1 && i > 2){
						if(curScore[i] != -2){
							incrementCumulScore(bowlIndex, i/2, curScore[i]);
						}
					}
				}
				if (i/2 == 9){
					if (i == 18){
						incrementCumulScore(bowlIndex, 9, getCumulScore(bowlIndex, 8));
					}
					if(curScore[i] != -2){
						incrementCumulScore(bowlIndex, 9, curScore[i]);
					}
				} else if (i/2 == 10) {
					if(curScore[i] != -2){
						incrementCumulScore(bowlIndex, 9, curScore[i]);
					}
				}
			}
		}
		return totalScore;
	}
}