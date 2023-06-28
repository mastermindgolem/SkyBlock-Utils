package Help;

import Help.Type.Berserker;
import Help.Type.Help;
import Help.Type.Mage;
import Help.Type.Warrior;

import java.lang.reflect.InvocationTargetException;

public class Main {
	public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

		// Create an instance of the Warrior class
		Warrior warrior = new Warrior();
		// Add help to cache
		HelpCache.addHelpProvider(warrior.getClass().getName(), new Warrior());


		Mage mage = new Mage();
		HelpManager helpManager = HelpCache.getHelpManager(mage.getClass().getName(), new Mage());

		Help warriorHelp = HelpCache.getHelpManager("Warrior").getHelp();
		Help mageHelp = HelpCache.getHelpManager("Mage").getHelp();

		Berserker bers = new Berserker();
		HelpCache.addHelpProvider(bers.getClass().getName(), new Berserker());

		HelpInvocation.addHelp();
		HelpInvocation.displayHelp();
	}
}