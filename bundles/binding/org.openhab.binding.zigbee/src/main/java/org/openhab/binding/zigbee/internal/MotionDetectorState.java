package org.openhab.binding.zigbee.internal;

import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.Command;
import org.openhab.core.types.Type;

public class MotionDetectorState implements Command {
	
	Type state;
	
	public MotionDetectorState(Type state) {
		this.state = state;
	}
	
	public Command getCommand() {
		return OpenClosedType.valueOf(state.toString());
	}

	@Override
	public String format(String pattern) {
		// TODO Auto-generated method stub
		return null;
	}


}
