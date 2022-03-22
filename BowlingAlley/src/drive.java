import java.util.Vector;
import java.io.*;
import controlDesk.*;
import misc.Alley;
import misc.ReadConfig;

public class drive {

	public static void main(String[] args) {
		
		try {
			int numLanes = Integer.parseInt(ReadConfig.GetPropValues("no_of_lanes"));
			int maxPatronsPerParty=Integer.parseInt(ReadConfig.GetPropValues("max_party_size"));

			Alley a = new Alley( numLanes );
			ControlDesk controlDesk = a.getControlDesk();

			ControlDeskView cdv = new ControlDeskView( numLanes, controlDesk, maxPatronsPerParty);
			controlDesk.subscribe( cdv );
		} catch (Exception e) {
			System.err.println("Exception occured while reading config properties");
		}
		

	}
}
