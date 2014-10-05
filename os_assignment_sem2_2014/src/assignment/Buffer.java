package assignment;

import java.util.Random;
import java.util.Scanner;

public class Buffer {

	public static Random RAN = new Random();
	public static int THREAD_ID = 0;
	public static boolean[] BUFFER_ITEM = new boolean[10];
	public static int INDEX;

	public Buffer() {
		INDEX = 0;
	}

	public static void createProducerThread() {
		System.out.println("T" + THREAD_ID++);
		Thread temp = new Thread() {
			int threadNum = THREAD_ID;

			public void run() {
				while (true) {
					System.out.println("Prodcuder thread " + threadNum
							+ "is running");
					try {
						sleep(Buffer.RAN.nextInt(10000));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		temp.start();
	}

	public static void createCosumerThread() {
		System.out.println("C" + THREAD_ID++);
		Thread temp = new Thread() {
			public void run() {
				int threadNum = THREAD_ID;
				while (true) {
					System.out.println("Consumer thread " + threadNum
							+ " is running");
					try {
						sleep(Buffer.RAN.nextInt(10000));
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		temp.start();
	}

	public static void main(String[] args) {
		Scanner reader = new Scanner(System.in);
		String usrIn = null;
		int duration = 0, numOfProThread = 0, numOfConThread = 0;
		boolean isValid = true;

		// user input & validation
		do {
			isValid = true;
			System.out
					.println("Please enter THREE integer values:\n"
							+ "#1 Main Thread Sleeping time, in seconds\n"
							+ "#2 Number of producer threads\n"
							+ "#3 Number of consumer threads\n"
							+ "Example: \"10 1 1\"");
			usrIn = reader.nextLine();
			try {
				duration = Integer.parseInt(usrIn.split(" ")[0]);
				numOfProThread = Integer.parseInt(usrIn.split(" ")[1]);
				numOfConThread = Integer.parseInt(usrIn.split(" ")[2]);
			} catch (Exception e) {
				isValid = false;
				System.out.println("your input is invalid!\n"
						+ "Press Enter to try again");
				reader.nextLine();
			}
		} while (!isValid);
		reader.close();
		// user input & validation ends

		// confirm user input
		System.out.println("The program will last " + duration
				+ " with Producer Number " + numOfProThread
				+ " and Consumer Number" + numOfConThread);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Simulation start");
		// confirm end

		// init threads start
		if (numOfConThread < numOfProThread) {
			int count = 0;
			while (count++ < numOfConThread) {
				createProducerThread();
				createCosumerThread();
			}
			while (count++ < numOfProThread)
				createProducerThread();
		} else if (numOfConThread > numOfProThread) {
			int count = 0;
			while (count++ < numOfProThread) {
				createProducerThread();
				createCosumerThread();
			}
			while (count++ < numOfConThread)
				createProducerThread();
		} else {
			int count = 0;
			while (count++ < numOfConThread) {
				createProducerThread();
				createCosumerThread();
			}
		}
		// init threads end
		try {
			Thread.sleep(duration * 1000);
			System.exit(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}