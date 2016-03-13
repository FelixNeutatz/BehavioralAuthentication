package felix.tracking.keyboard;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.jnativehook.mouse.NativeMouseWheelEvent;
import org.jnativehook.mouse.NativeMouseWheelListener;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class GlobalKeyListener implements NativeKeyListener, NativeMouseInputListener, NativeMouseWheelListener {

	static PrintWriter writer;

	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		//Time, Button number, DOWN
		writer.println(System.nanoTime() + ":" + e.getKeyCode() + ",D");
		writer.flush();
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		//Time, Button number, UP
		writer.println(System.nanoTime() + ":" + e.getKeyCode() + ",U" );
		writer.flush();
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {
		//not necessary
	}

	@Override
	public void nativeMouseClicked(NativeMouseEvent nativeMouseEvent) {
		//not necessary
	}

	@Override
	public void nativeMousePressed(NativeMouseEvent nativeMouseEvent) {
		//Time, Button number, DOWN
		writer.println(System.nanoTime() + ":" + (nativeMouseEvent.getButton() * -1) + ",D");
		writer.flush();
	}

	@Override
	public void nativeMouseReleased(NativeMouseEvent nativeMouseEvent) {
		//Time, Button number, UP
		writer.println(System.nanoTime() + ":" + (nativeMouseEvent.getButton() * -1) + ",U" );
		writer.flush();
	}

	@Override
	public void nativeMouseMoved(NativeMouseEvent nativeMouseEvent) {
		//Time, x-Coordinate, y-Coordinate
		writer.println(System.nanoTime() + "," + nativeMouseEvent.getX() + "," + nativeMouseEvent.getY() );
		writer.flush();
	}

	@Override
	public void nativeMouseDragged(NativeMouseEvent nativeMouseEvent) {
		//Time, x-Coordinate, y-Coordinate
		writer.println(System.nanoTime() + "," + nativeMouseEvent.getX() + "," + nativeMouseEvent.getY() );
		writer.flush();
	}

	@Override
	public void nativeMouseWheelMoved(NativeMouseWheelEvent nativeMouseWheelEvent) {
		//not necessary
	}

	public static void main(String[] args) {
		try {
			// Clear previous logging configurations.
			//LogManager.getLogManager().reset();

			// Get the logger for "org.jnativehook" and set the level to off.
			//Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
			//logger.setLevel(Level.OFF);
			
			writer = new PrintWriter("tracking" + System.nanoTime() + ".txt", "UTF-8");
			
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());

			System.exit(1);
		} catch (FileNotFoundException e){
			System.exit(1);
		} catch (UnsupportedEncodingException e1){
			System.exit(1);
		}
		
		//Construct the example object and initialze native hook.
		GlobalScreen.addNativeKeyListener(new GlobalKeyListener());
		GlobalScreen.addNativeMouseListener(new GlobalKeyListener());
		GlobalScreen.addNativeMouseMotionListener(new GlobalKeyListener());
		GlobalScreen.addNativeMouseWheelListener(new GlobalKeyListener()); //not needed
	}
}
