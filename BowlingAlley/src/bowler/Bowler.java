package bowler;
/*
 * Bowler.java
 *
 * Version:
 *     $Id$
 *
 * Revisions:
 *     $Log: Bowler.java,v $
 *     Revision 1.3  2003/01/15 02:57:40  ???
 *     Added accessors and and equals() method
 *
 *     Revision 1.2  2003/01/12 22:23:32  ???
 *     *** empty log message ***
 *
 *     Revision 1.1  2003/01/12 19:09:12  ???
 *     Adding Party, Lane, Bowler, and Alley.
 *
 */
import java.io.Serializable;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *  Class that holds all bowler info
 *
 */

public class Bowler implements Serializable {

    private String fullName;
    private String nickName;
    private String email;
    public int zeroes;
    public boolean block;

    public Bowler( String nick, String full, String mail ) {
		nickName = nick;
		fullName = full;
		email = mail;
		block = false;
		zeroes = 0;
    }


    public String getNickName() {

        return nickName;  

    }

	public String getFullName ( ) {
			return fullName;
	}
	
	public String getNick ( ) {
		return nickName;
	}

	public String getEmail ( ) {
		return email;	
	}
	
	public boolean equals ( Bowler b) {
		boolean retval = true;
		if ( !(nickName.equals(b.getNickName())) ) {
				retval = false;
		}
		if ( !(fullName.equals(b.getFullName())) ) {
				retval = false;
		}	
		if ( !(email.equals(b.getEmail())) ) {
				retval = false;
		}
		return retval;
	}

	/**
     * Retrieves a matching Bowler from the bowler database.
     *
     * @param nickName	The NickName of the Bowler
     *
     * @return a Bowler object.
     *
     */

	public static Bowler registerPatron(String nickName) {
		Bowler patron = null;
		StringBuilder error = new StringBuilder();
		error.append("Error...");
		try {
			// only one patron / nick.... no dupes, no checks

			patron = BowlerFile.getBowlerInfo(nickName);

		} catch (FileNotFoundException e) {
			error.append(e);
			System.err.println(error.toString());
		} catch (IOException e) {
			error.append(e);
			System.err.println(error.toString());
		}

		return patron;
	}
}