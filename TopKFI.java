// This file is a template for the project of Dati e Algoritmi AA 2022-23

// You are free to modify this code, but do not use any library outside of
// java.io and java.util. Check carefully the input and output format,
// use the one described in the project specifications otherwise we may not
// be able to test and evaluate your submission!

// The following code reads the input dataset line by line, parsing the items in each transaction.
// Before the submission, disable any debug output (for example, setting the variable
// DEBUG to false) and only output the results as described in the project specifications.

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;

public class TopKFI{

	public static boolean DEBUG = false;

	public static void main(String args[]){

		// parse input arguments
		if(args.length != 3){
			System.out.println("The arguments are not correct!");
			System.out.println("Please use \njava TopKFI datasetpath K M");
			return;
		}

		String db_path = args[0];
		int K = Integer.parseInt(args[1]);
		int M = Integer.parseInt(args[2]);

		if(K < 0 || M < 0){
			System.out.println("K and M should be positive!");
			return;
		}

		if(DEBUG){
			System.out.println("Path to dataset: "+db_path);
			System.out.println("K: "+K);
			System.out.println("M: "+M);
		}
		// read the input file

		//
		int j = 0;
		ArrayList<Integer> flag = new ArrayList<>();
		ArrayList<Integer> item_set = new ArrayList<Integer>();
		//
		HashSet<Integer> unique_items = new HashSet<Integer>();
		//
		PriorityQueue priority_items = new PriorityQueue();

		try {
      File file_db = new File(db_path);
      Scanner db_reader = new Scanner(file_db);
			int transaction_id = 0;
      while (db_reader.hasNextLine()) {
				transaction_id++;
        String transaction = db_reader.nextLine();
				if(DEBUG){
					System.out.println("transaction "+transaction_id+" is "+transaction);
				}
				String[] items_str = transaction.split("\\s+");
				//
		  		Arrays.sort(items_str, new Comparator<String>() {
					@Override
					public int compare(String o1, String o2) {
					  return Integer.valueOf(o1).compareTo(Integer.valueOf(o2));
					}
		  		});
		  			/* read the transaction "items_str" into the array "items" */
					for(int i=0; i<items_str.length; i++){
						try{
							if(items_str[i].equals(items_str[items_str.length -1])) {
								flag.add(Integer.parseInt(items_str[j]));
								j++;
							}

							item_set.add(Integer.parseInt(items_str[i]));

							unique_items.add(Integer.parseInt(items_str[i]));

							if(DEBUG){
								System.out.println("  item "+items_str[i]);
							}
						} catch (NumberFormatException e) {
							System.out.println("Input format of transaction is wrong!");
							System.out.println("transaction "+transaction_id+" is "+transaction);
							e.printStackTrace();
							return;
						}
					}
				}
			db_reader.close();
	    } catch (FileNotFoundException e) {
	      System.out.println("The file "+db_path+" does not exist!");
	      e.printStackTrace();
				return;
	    }
		//unique items or the supports of all singleton itemsets
	}
}

class PriorityQueue{

	private int length;	//The length of queue

	private ArrayList<QueueElement> qList;  //Priority queue

	protected int indexCount = 0;  //For counting the time stamp of inserted elements

	//The order of this priority queue is descending

	public PriorityQueue() {
		//Constructor of Priority Queue,
		//the bigger number stand for higher priority in my priority queue.
		qList = new ArrayList<QueueElement>();
		length = 0;
	}

	public boolean insert(int p) {
		//Insert an element with the binary search for its position, whose running time is logn
		//And the priority number should be integer and at least  0
		if(p >= 0) {
			int mid = 0;
			int start = 0;
			int end = qList.size();  //The pair of start and end are the two delimiters for binary search
			QueueElement le = new QueueElement(p);
			if(qList.size() == 0) {
				qList.add(le);
			}
			else if(qList.size() == 1) {
				if(p == qList.get(0).prio) {
					qList.get(0).equivList.add(0, le);
				}
				else if(p > qList.get(0).prio) {
					qList.add(le);
				}
				else {
					qList.add(0, le);
				}
			}
			else {
				if(p < qList.get(0).prio) {
					qList.add(0, le);
				}
				else if(p > qList.get(qList.size() - 1).prio) {
					qList.add(le);
				}
				else if(p == qList.get(0).prio) {
					qList.get(0).equivList.add(le);
				}
				else if(p == qList.get(qList.size() - 1).prio) {
					qList.get(qList.size() - 1).equivList.add(le);
				}
				else {
					while(true) {
						mid = (start + end) / 2;
						if(p > qList.get(mid).prio) {
							start = mid;
						}
						else if(p < qList.get(mid).prio) {
							end = mid;
						}
						else {
							qList.get(mid).equivList.add(le);
							break;
						}

						if(start == end - 1) {
							qList.add(end, le);
							break;
						}
					}
				}

			}
			length++;
			return true;
		}
		else {
			System.out.println("Please use positive number to represent priority, thanks.");
			return false;
		}
	}

	public int extractMax() {
		//Remove and return the largest key from the queue
		int max = -1;
		if(qList.size() > 0) {
			max = qList.get(qList.size() - 1).prio;
			if(qList.get(qList.size() - 1).equivList.size() > 0) {
				qList.get(qList.size() - 1).timeStamp = qList.get(qList.size() - 1).equivList.get(0).timeStamp;
				qList.get(qList.size() - 1).equivList.remove(0);
			}
			else {
				qList.remove(qList.size() - 1);
			}
			length--;
			return max;
		}
		else {
			return max;
		}
	}

	public int extractMin() {
		//Remove and return the the smallest key from the queue
		int min = -1;
		if(qList.size() > 0) {
			min = qList.get(0).prio;
			if(qList.get(0).equivList.size() > 0) {
				qList.get(0).timeStamp = qList.get(0).equivList.get(0).timeStamp;
				qList.get(0).equivList.remove(0);
			}
			else {
				qList.remove(0);
			}
			length--;
			return min;
		}
		else {
			return min;
		}
	}

	public int returnMin() {
		//Remove and return the the smallest key from the queue
		int min = -1;
		if(qList.size() > 0) {
			min = qList.get(0).prio;
			return min;
		}
		else {
			return min;
		}
	}

	public int returnMax() {
		//Remove and return the largest key from the queue
		int max = -1;
		if(qList.size() > 0) {
			max = qList.get(qList.size() - 1).prio;
			return max;
		}
		else {
			return max;
		}
	}

	public int size() {
		//Return the length of the queue
		return length;
	}

	//Class for queue element, the element has an other arraylist for the elements has equivalent key
	public class QueueElement {

		int prio;	//The bigger number stands for higher priority
		int timeStamp;  //The index represents the time when this element was inserted into the queue
		ArrayList<QueueElement> equivList;  //Saving the equal element in order to make queue stable

		public QueueElement(int p) {
			//Element in the queue
			equivList = new ArrayList<QueueElement>();
			prio = p;
			indexCount++;
			timeStamp = indexCount;
		}
	}

}