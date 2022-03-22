/* BowlerFile.java
 *
 *  Version:
 *  		$Id$
 * 
 *  Revisions:
 * 		$Log: BowlerFile.java,v $
 * 		Revision 1.5  2003/02/02 17:36:45  ???
 * 		Updated comments to match javadoc format.
 * 		
 * 		Revision 1.4  2003/02/02 16:29:52  ???
 * 		Added ControlDeskEvent and ControlDeskObserver. Updated Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of ControlDesk.
 * 		
 * 
 */

/**
 * Class for interfacing with Bowler database
 *
 */
package bowler;

import java.util.*;

import misc.ReadConfig;

import java.io.*;

public class BowlerFile {

	/** The location of the bowelr database */

    /**
     * Retrieves bowler information from the database and returns a Bowler objects with populated fields.
     *
     * @param nickName	the nickName of the bolwer to retrieve
     *
     * @return a Bowler object
     * 
     */

	public static Bowler getBowlerInfo(String nickName)
		throws IOException, FileNotFoundException {

		BufferedReader in = new BufferedReader(new FileReader(
							ReadConfig.GetPropValues("bowler_db_filepath")));
		String data;
		while ((data = in.readLine()) != null) {
			// File format is nick\tfname\te-mail
			String[] bowler = data.split("\t");
			if (nickName.equals(bowler[0])) {
				StringBuilder out = new StringBuilder("Nick: ");
				out.append(bowler[0]);
				out.append(" Full: ");
				out.append(bowler[1]);
				out.append(" email: ");
				out.append(bowler[2]);
				System.out.println(out.toString());
				return (new Bowler(bowler[0], bowler[1], bowler[2]));
			}
		}
		System.out.println("Nick not found...");
		return null;
	}

    /**
     * Stores a Bowler in the database
     *
     * @param nickName	the NickName of the Bowler
     * @param fullName	the FullName of the Bowler
     * @param email	the E-mail Address of the Bowler
     *
     */

	public static void putBowlerInfo(
		String nickName,
		String fullName,
		String email)
		throws IOException, FileNotFoundException {

		try {

			StringBuilder data = new StringBuilder(nickName).append("\t").append(fullName);
			data.append("\t").append(email).append("\n");

			RandomAccessFile out = new RandomAccessFile(
									ReadConfig.GetPropValues("bowler_db_filepath"), "rw");
			out.skipBytes((int) out.length());
			out.writeBytes(data.toString());
			out.close();
		} catch (Exception e) {
			System.err.println("exception in storing bowler info " + e );
		}
	}

    /**
     * Retrieves a list of nicknames in the bowler database
     *
     * @return a Vector of Strings
     * 
     */

	public static Vector getBowlers()
		throws IOException, FileNotFoundException {
		
		Vector allBowlers = new Vector();

		try {
			BufferedReader in = new BufferedReader(new FileReader(
								ReadConfig.GetPropValues("bowler_db_filepath")));
			String data;
			while ((data = in.readLine()) != null) {
				// File format is nick\tfname\te-mail
				String[] bowler = data.split("\t");
				//"Nick: bowler[0] Full: bowler[1] email: bowler[2]
				allBowlers.add(bowler[0]);
			}
		} catch (Exception e) {
			System.err.println("exception in retrieving bowler info " + e );
		}
		return allBowlers;
	}

}