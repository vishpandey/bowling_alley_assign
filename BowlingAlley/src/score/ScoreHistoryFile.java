/**
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
package score;
import java.util.*;
import java.io.*;

public class ScoreHistoryFile {

	private static String SCOREHISTORY_DAT = "SCOREHISTORY.DAT";

	public static void addScore(String nick, String score)
		throws IOException, FileNotFoundException {

		Date date = new Date();
		String dateString = "" + date.getHours() + ":" + date.getMinutes() + " " + date.getMonth() + "/" + date.getDay() + "/" + (date.getYear() + 1900);
		String data = nick + "\t" + dateString + "\t" + score + "\n";

		RandomAccessFile out = new RandomAccessFile(SCOREHISTORY_DAT, "rw");
		out.skipBytes((int) out.length());
		out.writeBytes(data);
		out.close();
	}

	public static Vector getScores(String nick)
		throws IOException, FileNotFoundException {
		Vector scores = new Vector();

		BufferedReader in =
			new BufferedReader(new FileReader(SCOREHISTORY_DAT));
		String data;
		while ((data = in.readLine()) != null) {
			// File format is nick\tfname\te-mail
			String[] scoredata = data.split("\t");
			//"Nick: scoredata[0] Date: scoredata[1] Score: scoredata[2]
			if (nick.equals(scoredata[0])) {
				scores.add(new Score(scoredata[0], scoredata[1], scoredata[2]));
			}
		}
		return scores;
	}

	public static Vector getScores()
		throws IOException, FileNotFoundException {
		Vector scores = new Vector();

		BufferedReader in =
			new BufferedReader(new FileReader(SCOREHISTORY_DAT));
		String data;
		while ((data = in.readLine()) != null) {
			String[] scoredata = data.split("\t");
			scores.add(new Score(scoredata[0], scoredata[1], scoredata[2]));
		}
		return scores;
	}

	public static int getHighestScore(String nick) throws FileNotFoundException, IOException {
		Vector scores = getScores();

		Iterator scoreIt = scores.iterator();
		int max = 0;
		while (scoreIt.hasNext()) {
			Score score = (Score) scoreIt.next();
			int temp_score = Integer.parseInt(score.getScore());
			if(temp_score > max) {
				max = temp_score;
			}
		}

		return max;
	}

	public static int getLowestScore(String nick) throws FileNotFoundException, IOException {
		Vector scores = getScores();

		Iterator scoreIt = scores.iterator();
		int min = Integer.MAX_VALUE;
		while (scoreIt.hasNext()) {
			Score score = (Score) scoreIt.next();
			int temp_score = Integer.parseInt(score.getScore());
			if(temp_score < min) {
				min = temp_score;
			}
		}

		if(min == Integer.MAX_VALUE)
			return 0;

		return min;
	}

	public static int getAverageScore(String nick) throws FileNotFoundException, IOException {
		Vector scores = getScores();

		Iterator scoreIt = scores.iterator();
		int sum = 0;
		while (scoreIt.hasNext()) {
			Score score = (Score) scoreIt.next();
			sum += Integer.parseInt(score.getScore());
		}

		return (sum / scores.size());
	}

	public static Vector getOverallHigh() throws IOException, FileNotFoundException {
		Vector overall_scores = getScores();

		String bestPlayer = "";
		int max = 0;
		Iterator scoreIt = overall_scores.iterator();
		while (scoreIt.hasNext()) {
			Score score = (Score) scoreIt.next();
			int temp_score = Integer.parseInt(score.getScore());
			if(temp_score > max) {
				max = temp_score;
				bestPlayer = score.getNickName();
			}
		}

		Vector<String> out = new Vector<String>();

		out.add(bestPlayer);
		out.add(Integer.toString(max));

		return out;
	}

	public static Vector getOverallLow() throws IOException, FileNotFoundException {
		Vector overall_scores = getScores();

		String worstPlayer = "";
		int min = Integer.MAX_VALUE;
		Iterator scoreIt = overall_scores.iterator();
		while (scoreIt.hasNext()) {
			Score score = (Score) scoreIt.next();
			int temp_score = Integer.parseInt(score.getScore());
			if(temp_score < min) {
				min = temp_score;
				worstPlayer = score.getNickName();
			}
		}

		Vector<String> out = new Vector<String>();

		if(min == Integer.MAX_VALUE)
			min = 0;
		
		out.add(worstPlayer);
		out.add(Integer.toString(min));

		return out;
	}

}
