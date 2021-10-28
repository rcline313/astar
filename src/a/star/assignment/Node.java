package a.star.assignment;

// import java.util.*;

/**
 *
 * @author noahburkhimer
 */
public class Node {

    public int row, col, f, g, h, type;
    public Node parent;
    public String smb;
    public boolean isBlock, solution;

    // default constructor for a node
    public Node(int r, int c) {
        this.row = r;
        this.col = c;
        this.type = type;
        this.parent = null;
        this.smb = "";
        //type 0 is traverseable, 1 is not 
    }

    //mutator methods to set values
    public void setF(int value) {
        this.f = value;
    }

    public void setG(int value) {
        this.g = value;
    }

    public void setH(int value) {
        this.h = value;
    }

    public void setParent(Node n) {
        this.parent = n;
    }

    public void setRow(int r) {
        this.row = r;
    }

    public void setCol(int c) {
        this.col = c;
    }

    public void setType(int t) {
        this.type = t;
    }

    public void setSymbol(String s) {
        this.smb = s;
    }

    //accessor methods to get values
    public int getF() {
        return f;
    }

    public int getG() {
        return g;
    }

    public int getH() {
        return h;
    }

    public Node getParent() {
        return parent;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getType() {
        return type;
    }

    public String getSymbol() {
        return smb;
    }

    public void calculateF() {
        this.f = this.getG() + this.getH();
    }

    public void setNodeData(Node currentNode, int cost) {
        int gCost = currentNode.getG() + cost;
        this.setParent(currentNode);
        this.setG(gCost);
        this.calculateF();
    }

    public boolean checkBetterPath(Node currentNode, int cost) {
        int gCost = currentNode.getG() + cost;
        if (gCost < getG()) {
            setNodeData(currentNode, cost);
            return true;
        }
        return false;
    }

    //compute path
    public void calculateHeuristic(Node finalNode) {
        this.h = Math.abs(finalNode.getRow() - getRow()) + Math.abs(finalNode.getCol() - getCol());
    }

    //Implement the method to find the best path
    public boolean selectivePath(Node currentNode, int cost) {
        int gCost = currentNode.getG() + cost;
        if (gCost < getG()) {
            setNodeData(currentNode, cost);
            return true;
        }
        return false;
    }

    //Implement the method to compute the final cost
    // private void calculateFinalCost() {
    //     int finalCost = getG() + getH();
    //     setF(finalCost);
    // }

    public boolean isBlock() {
        return isBlock;
    }

    public boolean setBlock(boolean isBlock) {
        return this.isBlock = isBlock;
    }

    public boolean equals(Object in) {
        //typecast to Node
        Node n = (Node) in;

        return this.row == n.getRow() && this.col == n.getCol();
    }

    public String toString() {
        return "Node: {" + this.row + ", " + this.col + "}";
    }

} // end of class
