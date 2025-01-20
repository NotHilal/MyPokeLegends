package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Heart extends Entity{

	public OBJ_Heart(GamePanel gp) {
		super(gp);
		name = "Heart";
		image = setup("heartFull","object");
		image2 = setup("heartHalf","object");
		image3 = setup("heartBlank","object");
	}

}
