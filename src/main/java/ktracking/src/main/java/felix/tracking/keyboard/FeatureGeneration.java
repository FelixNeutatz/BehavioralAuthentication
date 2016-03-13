package felix.tracking.keyboard;

import org.jnativehook.keyboard.NativeKeyEvent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * Created by felix on 12.03.16.
 */
public class FeatureGeneration {
	
	
	
	public static void main(String[] args) throws IOException {

		BufferedReader br = null;

		String[] line;
		String[] event;

		HashMap<Integer, Float> keysRecord = new HashMap<Integer, Float>();
		HashMap<Integer, Float> keysNextRecord = new HashMap<Integer, Float>(); // this makes sure that really short key events stay in the data

		long timestep = 30000000L; //30 milliseconds

		//read wanted keys
		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader("keys.txt"));

			while ((sCurrentLine = br.readLine()) != null) {

				line = sCurrentLine.split(",");

				keysRecord.put(Integer.parseInt(line[0]), 0.0f);
				keysNextRecord.put(Integer.parseInt(line[0]), 0.0f);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		Long currentTime = null;
		
		int currentX = 0;
		int currentY = 0;


		PrintWriter writer = new PrintWriter("features.txt", "UTF-8");

		int size = keysRecord.size();
		for (Integer k: keysRecord.keySet()){
			if (k < 0) {
				writer.print("mouse_button_" + (k*-1) + ",");
			} else {
				writer.print(NativeKeyEvent.getKeyText(k) + ",");
			}
		}
		writer.print("mouse_X,mouse_Y\n");
		
		long time = 0L;

		//read recording
		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader("test.txt"));

			while ((sCurrentLine = br.readLine()) != null) {

				line = sCurrentLine.split(",");

				if (line.length == 3) { //mouse movement record
					time = Long.parseLong(line[0]);
					
					if (currentTime == null) {
						currentTime = time;
					} 
					
					int x = Integer.parseInt(line[1]);
					int y = Integer.parseInt(line[2]);

					//System.out.println("mouse: " + x + ", " + y);
					currentX = x;
					currentY = y;
				} else { //key or mouse event
					event = line[0].split(":");

					time = Long.parseLong(event[0]);

					if (currentTime == null) {
						currentTime = time;
					} 
					
					int key_ID = Integer.parseInt(event[1]);
					boolean is_pressed = line[1].contains("D");

					if (is_pressed) {
						if (keysRecord.containsKey(key_ID)) {
							keysRecord.put(key_ID, 1.0f);
							keysNextRecord.put(key_ID, 1.0f);
						}
					} else {
						if (keysRecord.containsKey(key_ID)) {
							keysNextRecord.put(key_ID, 0.0f);
						}
					}
				}

				while(time - currentTime >= timestep) {
					for (Integer k: keysRecord.keySet()) {
						writer.print(keysRecord.get(k) + ",");
					}
					writer.print(currentX + "," + currentY + "\n");
					writer.flush();

					//clean
					keysRecord.putAll(keysNextRecord);
					currentTime += timestep;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		writer.close();
	}
}
