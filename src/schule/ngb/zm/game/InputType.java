package schule.ngb.zm.game;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class InputType {

	static final ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(1);

	public class ActionInput extends InputType {

		private int inputCode;

		private String action;

		private ActionListener al;

		private double delay = 0.0;

		private long lastTriggered = 0L;

		private boolean repeats = false;

		private boolean onPress = true;

		private boolean pressed = false;


		public void press( int inputCode, long time ) {
			pressed = true;
			if( onPress ) {
				trigger(inputCode, time);
			}
		}

		public void release( int inputCode, long time ) {
			pressed = false;
			if( !onPress ) {
				trigger(inputCode, time);
			}
		}

		public void trigger( int inputCode, long time ) {
			if( shouldTrigger(inputCode, time) ) {
				//al.actionTriggered(action);
				lastTriggered = time;
			}
		}

		public boolean shouldTrigger( int inputCode, long time ) {
			return this.inputCode == inputCode && time - lastTriggered > delay;
		}


		public int hashCode() {
			return inputCode;
		}

	}

	public class TriggerInput extends InputType {

	}

	public class RangeInput extends InputType {

	}

}
