package CommandName;

import org.junit.Test;
import org.junit.Assert;
import java.util.List;

public class Main {

	public static void main(String[] args) {
 		String cn = CN.cmdNameEquivalent("AttributeCommand");
		Assert.assertEquals("Convert name","attributeprice", cn);
		cn = CN.cmdNameEquivalent("EquipmentCommand");
		Assert.assertEquals("Convert name","equipmentprice", cn);
		cn = CN.cmdNameEquivalent("StatCommand");
		Assert.assertEquals("Convert name","kuudrastats", cn);
		cn = CN.cmdNameEquivalent("UpgradeCommand");
		Assert.assertEquals("Convert name","attributeupgrade", cn);
	}
}
