package Bitalino;

import java.time.LocalDate;
import java.sql.Date;
import java.util.Vector;
import javax.bluetooth.RemoteDevice;
import Objects.Test;

public class BitalinoDemo {

	public static Frame[] frame;
	BITalino bitalino = null;
	Test test = null;
	Date date = null;

	public String bital(String mac, int fre, int channel) {
		String t = null;
		BITalino bitalino = null;
		Test test = null;
		try {
			test = new Test();
			bitalino = new BITalino();
			date = Date.valueOf(LocalDate.now());
			if (channel == 0) {
				test.setSignal("EMG");
			} else {
				test.setSignal("ECG");
			}
			// Code to find Devices
			// Only works on some OS
			Vector<RemoteDevice> devices = bitalino.findDevices();
			System.out.println(devices + " Dispositivo");

			// You need TO CHANGE THE MAC ADDRESS
			// You should have the MAC ADDRESS in a sticker in the Bitalino
			// Sampling rate, should be 10, 100 or 1000
			test.setFrequence(fre);
			bitalino.open(mac, fre);
			// Start acquisition on analog channels A2 and A6
			// For example, If you want A1, A3 and A4 you should use {0,2,3}
			int[] channelsToAcquire = {channel};
			bitalino.start(channelsToAcquire);
			// Read in total 10000000 times
			for (int j = 0; j < 15; j++) {
				// Each time read a block of 10 samples
				int block_size = 10;
				frame = bitalino.read(block_size);
				// System.out.println("size block: " + frame.length);
				// Print the samples
				for (int i = 0; i < frame.length; i++) {
					System.out.println((j * block_size + i) + " seq: " + frame[i].seq + " " + frame[i].analog[channel] + " "
					// + frame[i].analog[2] + " "
					// + frame[i].analog[3] + " "
					// + frame[i].analog[4] + " "
					// + frame[i].analog[5]
					);
					test.sequence.add(j * block_size + i);
					test.value.add(frame[i].analog[channel]);
				}
				System.out.println("Bucle terminado");
			}
			// stop acquisition
			test.printList(test.sequence, test.value);
			t = test.getColumn();
			bitalino.stop();
			System.out.println("Apagamos bitalino");
		} catch (BITalinoException ex) {
			return ex.getCode();
		} catch (Throwable ex) {
			return "Bitalino error";
		} finally {
			try {
				// close bluetooth connection
				if (bitalino != null) {
					bitalino.close();
				}
			} catch (BITalinoException ex) {
				return ex.getCode();
			}
		}

		return t;
	}

}
