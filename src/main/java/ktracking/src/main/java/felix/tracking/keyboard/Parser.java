package felix.tracking.keyboard;

import org.jnativehook.keyboard.NativeKeyEvent;

import java.io.*;
import java.util.HashSet;

public class Parser {

	public static void main(String[] args) throws IOException {

		BufferedReader br = null;
		
		String [] line;
		String [] event;

		HashSet<Integer> keys = new HashSet<Integer>();

		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader("test.txt"));

			while ((sCurrentLine = br.readLine()) != null) {
				
				line = sCurrentLine.split(",");
				
				if (line.length == 3) { //mouse movement record
					long time = Long.parseLong(line[0]);
					int x = Integer.parseInt(line[1]);
					int y = Integer.parseInt(line[2]);
					
					System.out.println("mouse: " + x + ", " + y);
				} else { //key or mouse event
					event = line[0].split(":");

					long time = Long.parseLong(event[0]);
					int key_ID = Integer.parseInt(event[1]);
					boolean is_pressed = line[1].contains("D");

					if (key_ID >= 0) {
						if (is_pressed) {
							System.out.println("Keyboard: press " + NativeKeyEvent.getKeyText(key_ID));
							keys.add(key_ID);
						} else {
							System.out.println("Keyboard: release " + NativeKeyEvent.getKeyText(key_ID));
						}
					} else { // mouse click
						if (is_pressed) {
							System.out.println("Mouse: press Button " + (key_ID * -1));
							keys.add(key_ID);
						} else {
							System.out.println("Mouse: release Button " + (key_ID * -1));
						}
					}
				}
				
				//System.out.println(sCurrentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		
		System.out.println("number of occured keys: " + keys.size());

		PrintWriter writer = new PrintWriter("keys.txt", "UTF-8");
		for (Integer i : keys) {
			if (i >= 0) {
				writer.println(i + "," + NativeKeyEvent.getKeyText(i));
			} else {
				writer.println(i + ", mouse button " + (i * -1));
			}
		}
		writer.close();
		
	}
}
