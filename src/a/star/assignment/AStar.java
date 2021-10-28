/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a.star.assignment;

import java.util.*;

import javax.sound.sampled.SourceDataLine;
        
/**
 *
 * @author noahburkhimer
 */
public class AStar {

    // variable used to represent width and height of board
    final static int N = 15;
    
    // varibable used to determine if a node is the solution or not
    public static boolean solution;
    
    // variables to track default horizontal, vertical, and diagonal cost of a move
    final static int Default_HV_Cost = 10;
    final static int Default_Diag_Cost = 14;
    
    // 2D array of nodes to represent the grid
    private static Node[][] grid = new Node[N][N];

    
    // a PriorityQueue to track the open list
    private static PriorityQueue<Node> openList = new PriorityQueue<Node>((Node n1, Node n2) ->{
            return n1.getF() < n2.getF() ? -1 : n1.getF() > n2.getF() ? 1 : 0;});
    
    // an ArrayList to track the closed list
    private static boolean[][] closedList = new boolean[N][N];
    
    // variables that track the row & column for the start & end nodes
    private static int startRow = 0;
    private static int startCol = 0;
    private static int endRow = 0;
    private static int endCol = 0;
   
    // variables for formatting output
    private static String StartColor = "\033[1;32m";
    private static String GoalColor = "\033[1m\033[1;34m";
    private static String PathColor = "\033[1;35m";
    private static String BlockColor = "\033[1m\033[1;31m";
    private static String OpenColor = "\033[0;30m";
    private static String ClearColor = "\033[0m";

    private static Node Start, Goal;
    
    // A utility function that fills the 2D chessboard array 
    // with values of a specified type
    public static void populateMap(Node[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                
                Node n = new Node(i,j);
                map[i][j] = n;
                map[i][j].h = Math.abs(i - endRow) + Math.abs(j - endCol);
                // map[i][j].solution = false;
                
                Random rnd = new Random();
                double chance = rnd.nextDouble();
                
                if (chance < 0.1){ 
                    n.setSymbol("#");
                    n.setType(1);
                    n.isBlock = true;
                }
                else{
                    n.setSymbol("-");
                    n.setType(0);
                }
            }
        }// end of for-loop
    }// end of method
    
    
    
    public static void displayMap(Node[][] map){
        System.out.println("************GRID***************");
        
        for (int i = 0; i < N; i++){
            System.out.print(" ");
            for (int j = 0; j < N; j++){
                System.out.print(map[i][j].getSymbol() + " ");
            }
            System.out.print("\n");
        }// end of for-loop
    }// end of method
    
    
    
    public static void displayScores(){
        System.out.println("Scores for cells");
        
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
            
                if (grid[i][j] != null){
                    // System.out.println(grid[i][j]);
                    System.out.printf("%-3d ", grid[i][j].getF());
                }
                else{
                    System.out.print("BL ");
                }
            }
            System.out.println();
        }
        System.out.println();      
    }// end of method

    public static void displaySolution(){

        if (closedList[endRow][endCol]){

            System.out.println("Path to Goal: ");

            Node curr = grid[endRow][endCol];

            grid[curr.row][curr.col].solution = true;
            // int index = 1;
            while (curr.parent != null){
                // System.out.println(index + ": " + curr.parent);
                grid[curr.parent.row][curr.parent.col].solution = true;
                curr = curr.parent;
                // index++;
            }
            System.out.println("\n");
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    if (i == startRow && j == startCol){
                        System.out.print(StartColor + "S" + ClearColor); // start node
                    }
                    else if (i == endRow && j == endCol){
                        System.out.print(GoalColor + "G" + ClearColor); // goal node
                    }
                    else if (!grid[i][j].isBlock){
                        System.out.print(grid[i][j].solution ? PathColor + "X" + ClearColor : OpenColor + "0" + ClearColor);
                    }
                    else{
                        System.out.print(BlockColor + "#" + ClearColor); // block
                    }
                    System.out.print(" ");
                }
                System.out.println();
            } // end of for-loop 
        } else{
            System.out.println("No possible path");
        }// end of primary if-else condition
        
    }// end of method
    
    
    
    public static void setStart(){   
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the row number for the starting node: ");
        startRow = sc.nextInt();
        
        System.out.println("Please enter the column number for the starting node: ");
        startCol = sc.nextInt();
        
        Start = new Node(startRow, startCol);
        Start.setSymbol("S");
        grid[startRow][startCol] = Start;
    }// end method
    
    
    public static void setEnd(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the row number for the goal node: ");
        endRow = sc.nextInt();
        
        System.out.println("Please enter the column number for the goal node: ");
        endCol = sc.nextInt();
        
        Goal = new Node(endRow, endCol);
        Goal.setSymbol("G");
        grid[endRow][endCol] = Goal;
    }// end of method
   
   
   
    /*private static boolean isEmpty(PriorityQueue<Node> list) {
        list = openList;
        return list.size() == 0;
    }*/
    
    
    
    public static void updateCost(Node current, Node temp, int cost){
        if (temp == null || closedList[temp.row][temp.col]){
            return;
        }
        
        int tempFinalCost = temp.getH() + cost;
        boolean isOpen = openList.contains(temp);
        
        if (!isOpen || tempFinalCost < temp.getF()){
            temp.setF(tempFinalCost);
            temp.setParent(current);
            
            if (!isOpen){
                openList.add(temp);
            }
            
        } // end of if-statement
        
    } // end of method
    
    
    
    /*private static List<Node> getPath(Node current) {
        
        List<Node> path = new ArrayList<Node>();
        path.add(current);
        Node parent;
        
        while ((parent = current.getParent()) != null) {
            path.add(0, parent);
            current = parent;

        }// end of while-loop

        return path;

    }// end of method*/
    
    
    
    
    public static void a_star_search(){

        openList.add(Start);
        Node curr;

        while (true) {
            curr = openList.poll();
            if (curr == null)
                break;
            
            closedList[curr.getRow()][curr.getCol()] = true;
            
            if (curr.equals(Goal))
                return;
            
            Node temp;
            
            if (curr.row - 1 >= 0){
                temp = grid[curr.row - 1][curr.col];
                updateCost(curr, temp, curr.f + Default_HV_Cost);
                
                if (curr.col - 1 >= 0){
                    temp = grid[curr.row - 1][curr.col - 1];
                    updateCost(curr, temp, curr.f + Default_Diag_Cost);
                }
                
                if (curr.col + 1 < grid[0].length){
                    temp = grid[curr.row - 1][curr.col + 1];
                    updateCost(curr, temp, curr.f + Default_Diag_Cost);
                }
                
            }
            
            if (curr.col - 1 >= 0){
                temp = grid[curr.row][curr.col - 1];
                updateCost(curr, temp, curr.f + Default_HV_Cost);
            }
            
            if (curr.col + 1 < grid[0].length){
                temp = grid[curr.row][curr.col + 1];
                updateCost(curr, temp, curr.f + Default_Diag_Cost);
            }
            
            if (curr.row + 1 < grid.length){
                temp = grid[curr.row + 1][curr.col];
                updateCost(curr, temp, curr.f + Default_HV_Cost);
            
                if (curr.col - 1 >= 0){
                    temp = grid[curr.row + 1][curr.col - 1];
                    updateCost(curr, temp, curr.f + Default_HV_Cost);
                }
                
                if (curr.col + 1 < grid[0].length){
                    temp = grid[curr.row + 1][curr.col + 1];
                    updateCost(curr, temp, curr.f + Default_Diag_Cost);
                }
            }
            
        }// end of while-loop
        
        
        
        /*Node startNode = grid[startRow][startCol];
        openList.add(startNode);
        System.out.println(openList);
                
        while (!isEmpty(openList)) {
            Node curr = openList.poll();
            System.out.println("current node is: " + curr);
            closedList.add(curr);
            if (curr.equals(grid[endRow][endCol])) {
                getPath(curr);
                break;
            } 
            else {
                addAdjacentNodes(curr);
            }

        }// end of while-loop

        return new ArrayList<Node>();*/

    }// end of method
  
    

    /*private static void addAdjacentNodes(Node currentNode) {
        addAdjacentUpperRow(currentNode);
        addAdjacentMiddleRow(currentNode);
        addAdjacentLowerRow(currentNode);
    }// end of method

    
    
    private static void addAdjacentLowerRow(Node currentNode) {
        int row = currentNode.getRow();
        int col = currentNode.getCol();
        int lowerRow = row + 1;

        if (lowerRow < grid.length) {

            if (col - 1 >= 0) {
                checkNode(currentNode, col - 1, lowerRow, getDiagonalCost()); // Comment this line if diagonal movements are not allowed
            }

            if (col + 1 < grid[0].length) {
                checkNode(currentNode, col + 1, lowerRow, getDiagonalCost()); // Comment this line if diagonal movements are not allowed
            }

            checkNode(currentNode, col, lowerRow, getHvCost());
        }
    }

    
    
    private static void addAdjacentMiddleRow(Node currentNode) {
        int row = currentNode.getRow();
        int col = currentNode.getCol();
        int middleRow = row;

        if (col - 1 >= 0) {
            checkNode(currentNode, col - 1, middleRow, getHvCost());
        }

        if (col + 1 < grid[0].length) {
            checkNode(currentNode, col + 1, middleRow, getHvCost());
        }

    }

    
    
    private static void addAdjacentUpperRow(Node currentNode) {
        int row = currentNode.getRow();
        int col = currentNode.getCol();
        int upperRow = row - 1;

        if (upperRow >= 0) {

            if (col - 1 >= 0) {
            checkNode(currentNode, col - 1, upperRow, getDiagonalCost()); // Comment this if diagonal movements are not allowed
            }

            if (col + 1 < grid[0].length) {
                checkNode(currentNode, col + 1, upperRow, getDiagonalCost()); // Comment this if diagonal movements are not allowed
            }
            
            checkNode(currentNode, col, upperRow, getHvCost());
        }
    }*/

    
    
    /*private static void checkNode(Node currentNode, int col, int row, int cost) {
        Node adjacentNode = grid[row][col];
        if (!adjacentNode.isBlock() && !closedList.contains(adjacentNode)) {
            if (!openList.contains(adjacentNode)) {
                adjacentNode.setNodeData(currentNode, cost);
                openList.add(adjacentNode);
            } 
            else {
                boolean changed = adjacentNode.checkBetterPath(currentNode, cost);
                if (changed) {
                    // Remove and Add the changed node, so that the PriorityQueue can sort again its
                    // contents with the modified "finalCost" value of the modified node
                    openList.remove(adjacentNode);
                    openList.add(adjacentNode);
                }
            }
        }
    }// end of method*/
    
    
    
    /*public static void displayPathMap(ArrayList<Node> path, Node[][] map){
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                for(int x = 0; x <= path.size()-1; x++){
                    Node n = path.get(x);
                    if(n.equals(map[i][j])){
                        map[i][j].setSymbol("X");
                    }
                }
            }
        }
    }*/
    
    
    public static void getHeuristic(Node [][]b, Node g){
         //get heuristic
         for (int i=0;i<b.length; i++){
            for(int j=0;j<b[0].length; j++){
                b[i][j].setH((10*(Math.abs(i-g.getRow())))+(10*(Math.abs(j-g.getCol()))));
            }
        }
     }


    public void displayHeuristic(Node [][]d) {

        System.out.println("\nThis is a grid of the heuristics");
        for(int i=0;i<d.length;i++) {
            System.out.print("\n");
            //this sides checks the spacing and add an extra space if it is single digit
            if (i<10){System.out.print(i + "  | ");}
            else System.out.print(i+" | ");

            //traverses and gets the value of type for the node
            for(int j=0;j<d[0].length;j++) {
                if ((d[i][j].getH()>=10)&&(d[i][j].getH()<=90)){
                System.out.print(d[i][j].getH()+ "  ");}

                else if (d[i][j].getH()<10){
                    System.out.print(d[i][j].getH()+ "   ");
                }
                else
                    System.out.print(d[i][j].getH()+ " ");

                System.out.print(" ");
            }}System.out.println("\n");
    }
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        populateMap(grid);
        setStart();
        setEnd();
        
        // displayMap(grid);
        
        a_star_search();
        // displayScores();
        System.out.println("Start: " + Start.toString());
        System.out.println("End: " + Goal.toString());
        System.out.println();

        displayMap(grid);
        System.out.println();

        displaySolution();
        System.out.println();
    }
    
}
