import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.*;
import java.io.PrintStream;

public class FlammiaM_Project7_Java{
	public static void main(String[] args) throws FileNotFoundException{
		//makes sure arguments are present
		if(args.length != 3){
			System.out.println("To run please include these arguments: inputData MSTFile debugFile");
			return;
		}
		//opens inputfile with error handling
		File inFile = new File(args[0]);
		if(!inFile.isFile() && !inFile.canRead()){
			System.out.println("Failed to open file (Was filename typed correctly?)");
			return;
		}
		//output streams
		PrintStream debug = new PrintStream(new FileOutputStream(args[2],true));
		PrintStream output = new PrintStream(new FileOutputStream(args[1],true));
		//start of step 0
		kruskalMST mst = new kruskalMST();
		Scanner scan = new Scanner(inFile);
		mst.numNodes = scan.nextInt();
		mst.numSets = mst.numNodes;
		mst.inWhichSet = new int[mst.numNodes + 1];
		for(int i=0;i<=mst.numNodes;i++){
			mst.inWhichSet[i] = i;
		}
		mst.listHeadEdge = new edgeNode();
		mst.listHeadMST = new edgeNode();
		mst.totalMSTCost = 0;
		mst.printAry(debug);
		//step 4 loop
		while(scan.hasNextInt()){
			//step 1 -> 3
			int Ni = scan.nextInt();
			int Nj = scan.nextInt();
			int cost = scan.nextInt();
			edgeNode node  = new edgeNode(Ni,Nj,cost);
			mst.insert(node, mst.listHeadEdge);
			mst.printList(debug,mst.listHeadEdge);
		}
		//step 10 loop
		while(mst.numSets > 1){
			//step 5
			edgeNode nextEdge = mst.removeEdge(mst.listHeadEdge);
			while(mst.inWhichSet[nextEdge.Ni] == mst.inWhichSet[nextEdge.Nj]){
				nextEdge = mst.removeEdge(mst.listHeadEdge);
			}
			
			mst.insert(nextEdge, mst.listHeadMST);
			mst.totalMSTCost += nextEdge.cost;
			mst.merge2Sets(nextEdge.Ni, nextEdge.Nj);
			mst.numSets--;
			
			mst.printAry(debug);
			System.out.println("Printing Edge List");
			mst.printList(debug,mst.listHeadEdge);
			System.out.println("Printing MST List");
			mst.printList(debug,mst.listHeadMST);
		}
		mst.printList(output,mst.listHeadMST);
		System.out.println("Total MST Cost is:"+mst.totalMSTCost);
	}
}

class edgeNode{
	//variables
	int Ni;
	int Nj;
	int cost;
	edgeNode next;
	
	edgeNode(){
		this.Ni = 0;
		this.Nj = 0;
		this.cost = 0;
		this.next = null;
	}
	edgeNode(int Ni, int Nj, int cost){
		this.Ni = Ni;
		this.Nj = Nj;
		this.cost = cost;
		this.next = null;
	}
	
	//methods
	void printNode(){
		System.out.println("<" + this.Ni + " " + this.Nj + " " + this.cost + ">");
	}
}

class kruskalMST{
	//variables
	int numNodes;
	int[] inWhichSet;
	int numSets;
	int totalMSTCost;
	edgeNode listHeadEdge;
	edgeNode listHeadMST;
	
	kruskalMST(){
		this.numNodes = 0;
		this.numSets = 0;
		this.totalMSTCost = 0;
		listHeadEdge = null;
		listHeadMST = null;
	}
	
	//methods
	void insert(edgeNode node, edgeNode head){
		edgeNode temp = head;
		while(temp.next != null){
			if(temp.next.cost > node.cost){
				node.next = temp.next;
				temp.next = node;
				break;
			}
			else{
				temp = temp.next;
			}
		}
		if(temp.next == null){
			temp.next = node;
		}
	}
	edgeNode removeEdge(edgeNode head){
		edgeNode temp = head.next;
		head.next = head.next.next;
		temp.next = null;
		return temp;
	}
	void merge2Sets(int node1, int node2){
		if(node1 < node2){
			inWhichSet[node2] = node1;
		}
		else{
			inWhichSet[node1] = node2;
		}
	}
	void printAry(PrintStream fileOut){
		System.setOut(fileOut);
		System.out.println("Printing inWhichSet array....");
		for(int i=1;i<=this.numNodes;i++){
			System.out.print(this.inWhichSet[i] + " ");
		}
		System.out.println();
	}
	void printList(PrintStream fileOut, edgeNode head){
		System.setOut(fileOut);
		edgeNode temp = head;
		while(temp.next != null){
			temp.printNode();
			temp = temp.next;
		}
		temp.printNode();
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}
}