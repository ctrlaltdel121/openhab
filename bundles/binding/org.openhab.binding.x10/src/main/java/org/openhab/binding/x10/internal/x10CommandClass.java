package org.openhab.binding.x10.internal;

/**
 * Contains all the supported commands for x10 modules
 * 
 * @author Benji
 *
 */
public class x10CommandClass {
	
	// The prefix for every bash command
	final String COMMAND_PREFIX = "./x10cmd rf ";
	
	private enum x10Command {
		ON("on"), 
		OFF("off"),
		INCREASE("bright"),
		DECREASE("dim");
		
		private String bash_command;
		
		private x10Command(String bash_command) {
			this.bash_command = bash_command;
		}
		
		public String getCommand() {
			return bash_command;
		}
	}
	
	public String getCommand(String commandClass, String itemCode) throws IllegalArgumentException {
		String bash_command = "";
		
		x10Command command = x10Command.valueOf(commandClass);
		
		bash_command = COMMAND_PREFIX + itemCode + " " + command.getCommand();
		
		return bash_command;
	}
	
}
