import java.util.Vector;
import java.io.*;
import controlDesk.*;
import misc.Alley;

public class drive {

	public static void main(String[] args) {

		int numLanes = 3;
		int maxPatronsPerParty=5;

		Alley a = new Alley( numLanes );
		ControlDesk controlDesk = a.getControlDesk();

		ControlDeskView cdv = new ControlDeskView( numLanes, controlDesk, maxPatronsPerParty);
		controlDesk.subscribe( cdv );

	}
}
