package assignment;

import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

/**
 * Buffer class to simulate Producer and Consumer problem
 * 
 * @author Yue Li 1251124
 *
 */

public class Buffer {

	public static Random RAN = new Random();
	public static int THREAD_ID = 0;
	// determine the length of the buffer
	public static int[] BUFFER_ITEM = new int[5];
	// determine the access of number of producers
	public static Semaphore prod = new Semaphore(5);
	// determine the access of number of consumers
	public static Semaphore con = new Semaphore(0);
	public static int BUFFER_INDEX;

	public Buffer() {
	}

	public static void insert_item(int element, int threadId) {
		try {
			prod.acquire();
			BUFFER_ITEM[BUFFER_INDEX] = element;
			System.out.println("Producer #" + threadId + " inserted " + element
					+ " at index: " + BUFFER_INDEX);
			BUFFER_INDEX++;
			System.out.print("[");
			for (int i = 0; i < BUFFER_ITEM.length - 1; i++) {
				System.out.print(BUFFER_ITEM[i] + ", ");
			}
			System.out.println(BUFFER_ITEM[4] + "]");
			con.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * remove item for consumer threads. consumer class shall not acquire
	 * resource until producer release the resource.
	 */
	public static int remove_item(int threadNum) {
		int item = 0;
		try {
			con.acquire();
			BUFFER_INDEX--;
			item = BUFFER_ITEM[BUFFER_INDEX];
			BUFFER_ITEM[BUFFER_INDEX] = 0;
			System.out.println("Consumer #" + threadNum + " removed " + item
					+ " at " + BUFFER_INDEX);
			System.out.print("[");
			for (int l = 0; l < BUFFER_ITEM.length - 1; l++) {
				System.out.print(BUFFER_ITEM[l] + ", ");
			}
			System.out.println(BUFFER_ITEM[4] + "]");
			prod.release();
			return item;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Create an anonymous producer thread that always insert positive integers
	 * into the buffer array
	 */
	public static void createProducerThread() {
		System.out.println("Producer thread #" + THREAD_ID++ + " created.");
		Thread temp = new Thread() {
			int threadNum = THREAD_ID;

			public void run() {
				while (true) {
					try {
						sleep(Buffer.RAN.nextInt(5000) + 200);
						int item = Buffer.RAN.nextInt(1000) + 1;
						Buffer.insert_item(item, this.threadNum);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		temp.start();
	}

	/**
	 * Create an anonymous consumer thread that always remove positive integers
	 * from the buffer array
	 */
	public static void createCosumerThread() {
		System.out.println("Consumer thread #" + THREAD_ID++ + " created.");
		Thread temp = new Thread() {
			int threadNum = THREAD_ID;

			public void run() {
				while (true) {
					try {
						sleep(Buffer.RAN.nextInt(5000) + 200);
						Buffer.remove_item(threadNum);
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
			System.out.println("Main Thread start under sleeping");
			Thread.sleep(duration * 1000);
			System.out.println("Main Thread is awake."
					+ " The simulation is terminated");
			System.exit(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}