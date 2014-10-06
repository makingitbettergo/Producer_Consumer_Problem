package assignment;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

/**
 * Buffer class to simulate
 * 
 * @author Yue Li 1251124
 *
 */

public class Buffer {

	public static Random RAN = new Random();
	public static int THREAD_ID = 0;
	public static int[] BUFFER_ITEM = new int[5];

	public static Semaphore prod = new Semaphore(5);
	public static Semaphore con = new Semaphore(0);

	public Buffer() {
	}

	public static void insert_item(int element) {
		// boolean isInserted = false;
		try {
			prod.acquire();
			for (int i = 0; i < BUFFER_ITEM.length; i++) {
				if (BUFFER_ITEM[i] == 0) {
					BUFFER_ITEM[i] = element;
					// isInserted = true;
					System.out.println("Inserted " + element + " at " + i);
					break;
				}
			}
			System.out.print("[");
			for (int i = 0; i < BUFFER_ITEM.length - 1; i++) {
				System.out.print(BUFFER_ITEM[i] + ", ");
			}
			System.out.println(BUFFER_ITEM[4] + "]");
			// if (!isInserted)
			// System.out.println("The queue is full");
			con.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * remove item for consumer threads. consumer class shall not acquire
	 * resource until producer release the resource.
	 */
	public static int remove_item() {
		int item = 0;
		try {
			con.acquire();
			for (int i = 0; i < BUFFER_ITEM.length; i++) {
				if (BUFFER_ITEM[i] != 0) {
					item = BUFFER_ITEM[i];
					BUFFER_ITEM[i] = 0;
					System.out.println("removed " + item + " at " + i);
					System.out.print("[");
					for (int l = 0; l < BUFFER_ITEM.length - 1; l++) {
						System.out.print(BUFFER_ITEM[l] + ", ");
					}
					prod.release();
					System.out.println(BUFFER_ITEM[4] + "]");
					return item;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static void createProducerThread() {
		System.out.println("T " + THREAD_ID++);
		Thread temp = new Thread() {
			int threadNum = THREAD_ID;

			public void run() {
				while (true) {
					try {
						sleep(Buffer.RAN.nextInt(1000));
						System.out.println("Prodcuder thread " + threadNum);
						int item = Buffer.RAN.nextInt(100) + 1;
						Buffer.insert_item(item);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		temp.start();
	}

	public static void createCosumerThread() {
		System.out.println("C " + THREAD_ID++);
		Thread temp = new Thread() {
			int threadNum = THREAD_ID;

			public void run() {
				while (true) {
					try {
						sleep(Buffer.RAN.nextInt(1000));
						System.out.println("Consumer thread " + threadNum);
						Buffer.remove_item();
					} catch (InterruptedException e) {
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