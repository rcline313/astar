/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a.star.assignment;

import java.util.*;

/**
 *
 * @author noahburkhimer
 */
public class Pathfinder {
    //Set the cost of the matrix

    private static int DEFAULT_HV_COST = 10; // Horizontal - Vertical Cost
    private static int DEFAULT_DIAGONAL_COST = 14;
    private int hvCost;
    private int diagonalCost;
    private Node[][] searchArea;
    private PriorityQueue<Node> openList;
    private Set<Node> closedSet;
    private Node start;
    private Node end;
    //constructor

    public Pathfinder(int rows, int cols, Node initialNode, Node finalNode,
            int hvCost, int diagonalCost) {
        this.hvCost = hvCost;
        this.diagonalCost = diagonalCost;
        setInitialNode(initialNode);
        setFinalNode(finalNode);
        this.searchArea = new Node[rows][cols];
        this.openList = new PriorityQueue<Node>(new Comparator<Node>() {
            @Override
            public int compare(Node node0, Node node1) {
                return Integer.compare(node0.getF(), node1.getF());
            }
        });
        setNodes();
        this.closedSet = new HashSet<>();
    }
    //default constructor

    public Pathfinder(int rows, int cols, Node initialNode, Node finalNode) {
        this(rows, cols, initialNode, finalNode, DEFAULT_HV_COST, DEFAULT_DIAGONAL_COST);
    }

    private void setNodes() {
        for (int i = 0; i < searchArea.length; i++) {
            for (int j = 0; j < searchArea[0].length; j++) {
                Node node = new Node(i, j);
                node.calculateHeuristic(getFinalNode());
                this.searchArea[i][j] = node;
            }
        }
    }

    public void setBlocks(int[][] blocksArray) {
        for (int i = 0; i < blocksArray.length; i++) {
            int row = blocksArray[i][0];
            int col = blocksArray[i][1];
            setBlock(row, col);
        }
    }

    public List<Node> findPath() {
        openList.add(start);
        while (!isEmpty(openList)) {
            Node currentNode = openList.poll();
            closedSet.add(currentNode);
            if (isFinalNode(currentNode)) {
                return getPath(currentNode);
            } else {
                addAdjacentNodes(currentNode);
            }
        }
        return new ArrayList<Node>();
    }

    private List<Node> getPath(Node currentNode) {
        List<Node> path = new ArrayList<Node>();
        path.add(currentNode);
        Node parent;
        while ((parent = currentNode.getParent()) != null) {
            path.add(0, parent);
            currentNode = parent;
        }
        return path;
    }

    private void addAdjacentNodes(Node currentNode) {
        addAdjacentUpperRow(currentNode);
        addAdjacentMiddleRow(currentNode);
        addAdjacentLowerRow(currentNode);
    }

    private void addAdjacentLowerRow(Node currentNode) {
        int row = currentNode.getRow();
        int col = currentNode.getCol();
        int lowerRow = row + 1;
        if (lowerRow < getSearchArea().length) {
            if (col - 1 >= 0) {
                checkNode(currentNode, col - 1, lowerRow, getDiagonalCost());
            }
            if (col + 1 < getSearchArea()[0].length) {
                checkNode(currentNode, col + 1, lowerRow, getDiagonalCost());
            }
            checkNode(currentNode, col, lowerRow, getHvCost());
        }
    }

    private void addAdjacentMiddleRow(Node currentNode) {
        int row = currentNode.getRow();
        int col = currentNode.getCol();
        int middleRow = row;
        if (col - 1 >= 0) {
            checkNode(currentNode, col - 1, middleRow, getHvCost());
        }
        if (col + 1 < getSearchArea()[0].length) {
            checkNode(currentNode, col + 1, middleRow, getHvCost());
        }
    }

    private void addAdjacentUpperRow(Node currentNode) {
        int row = currentNode.getRow();
        int col = currentNode.getCol();
        int upperRow = row - 1;
        if (upperRow >= 0) {
            if (col - 1 >= 0) {
                checkNode(currentNode, col - 1, upperRow, getDiagonalCost());
            }
            if (col + 1 < getSearchArea()[0].length) {
                checkNode(currentNode, col + 1, upperRow, getDiagonalCost());
            }
            checkNode(currentNode, col, upperRow, getHvCost());
        }
    }

    private void checkNode(Node currentNode, int col, int row, int cost) {
        Node adjacentNode = getSearchArea()[row][col];
        if (!adjacentNode.isBlock() && !getClosedSet().contains(adjacentNode)) {
            if (!getOpenList().contains(adjacentNode)) {
                adjacentNode.setNodeData(currentNode, cost);
                getOpenList().add(adjacentNode);
            } else {
                boolean changed = adjacentNode.selectivePath(currentNode, cost);
                if (changed) {
                    // Remove and Add the changed node, so that the PriorityQueue can sort again its
                    // contents with the modified "finalCost" value of the modified node
                    getOpenList().remove(adjacentNode);
                    getOpenList().add(adjacentNode);
                }
            }
        }
    }

    private boolean isFinalNode(Node currentNode) {
        return currentNode.equals(end);
    }

    private boolean isEmpty(PriorityQueue<Node> openList) {
        return openList.size() == 0;
    }

    private void setBlock(int row, int col) {
        this.searchArea[row][col].setBlock(true);
    }

    public Node getInitialNode() {
        return start;
    }

    public void setInitialNode(Node initialNode) {
        this.start = initialNode;
    }

    public Node getFinalNode() {
        return end;
    }

    public void setFinalNode(Node finalNode) {
        this.end = finalNode;
    }

    public Node[][] getSearchArea() {
        return searchArea;
    }

    public void setSearchArea(Node[][] searchArea) {
        this.searchArea = searchArea;
    }

    public PriorityQueue<Node> getOpenList() {
        return openList;
    }

    public void setOpenList(PriorityQueue<Node> openList) {
        this.openList = openList;
    }

    public Set<Node> getClosedSet() {
        return closedSet;
    }

    public void setClosedSet(Set<Node> closedSet) {
        this.closedSet = closedSet;
    }

    public int getHvCost() {
        return hvCost;
    }

    public void setHvCost(int hvCost) {
        this.hvCost = hvCost;
    }

    private int getDiagonalCost() {
        return diagonalCost;
    }

    @SuppressWarnings("unused")
    private void setDiagonalCost(int diagonalCost) {
        this.diagonalCost = diagonalCost;
    }

    public static void main(String[] args) {
        //declare local variables
        int row = 0;
        int col = 0;
        //prompt and read rhe row and column values to find the cost
        Scanner in = new Scanner(System.in);
        //prompt for source node
        System.out.print("Enter the Source Node [row column] : ");
        row = in.nextInt();
        col = in.nextInt();
        //prompt for Target node
        Node initialNode = new Node(row, col);
        System.out.print("Enter the Target Node [row column] : ");
        row = in.nextInt();
        col = in.nextInt();
        Node finalNode = new Node(row, col);
        int rows = 12;
        int cols = 12;
        //create a class object
        Pathfinder path = new Pathfinder(rows, cols, initialNode, finalNode);
        int[][] blocksArray = new int[][]{{(int) (Math.random() * 10),
            (int) (Math.random() * 10)}, {(int) (Math.random() * 10),
            (int) (Math.random() * 10)}, {(int) (Math.random() * 10),
            (int) (Math.random() * 10)}, {(int) (Math.random() * 10),
            (int) (Math.random() * 10)}, {(int) (Math.random() * 10),
            (int) (Math.random() * 10)}, {(int) (Math.random() * 10),
            (int) (Math.random() * 10)}};
        row = 0;
        col = 0;
        /* PRINT PATH */
        System.out.println("###### GRID ######\n");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int block = 0;
                for (int b = 0; b < blocksArray.length; b++) {
                    row = blocksArray[b][0];
                    col = blocksArray[b][1];
                    if (row == i && col == j) {
                        System.out.print("B" + " ");
                        block = 1;
                    }
                }
                if (block == 0) {
                    System.out.print("-" + " ");
                }
            }
            System.out.println("\n");
        }

        path.setBlocks(blocksArray);
        List<Node> optimalPath = path.findPath();
        for (Node node : optimalPath) {
            System.out.print("[" + node.getRow() + "," + node.getCol() + "] ");
        }
    }
}
