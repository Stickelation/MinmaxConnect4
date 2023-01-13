import java.util.List;
import java.util.ArrayList;
public class Node
{
        List children = null;
        Node parent = null;
        char[][]board;
        double score=0.00; // Boards start with a score of 0 until evaluated, or until minimax assigns a score based off of its children
        int depth=0;
        int lastMove=0;
        boolean isLeaf = false;
        boolean updated = false;
        Node (char[][]passedBoard) // Nodes each contain a connect-4 board (All methods are self-explanatory)
        {
            board=passedBoard;
            children = new ArrayList();
            parent=null;

        }
        public boolean getUpdated()
        {
            return updated;
        }
        public void setUpdated(boolean state)
        {
            updated=state;
        }
        public void setLeaf()
        {
            isLeaf=true;
        }
        public int getLastMove()
        {
            return lastMove;
        }
        public void setLastMove(int temp)
        {
            lastMove=temp;
        }
        public char[][] getBoard()
        {
            return board;
        }

        public void setDepth(int passedDepth)
        {
            depth = passedDepth;
        }

        public int getDepth()
        {
            return depth;
        }

        public void addChild(Node child)
        {
            child.parent=this;
            children.add(child);
        }

        public List getChildren()
        {
            return children;
        }
        public double getScore()
        {
        return score;
        }

        public Node getParent()
        {
            return parent;
        }

        public void setParent(Node passed)
        {
            parent=passed;
        }

        public void setScore(double temp)
    {
        score = temp;
    }

        public void displayNode(int nRows, int nCols) // This was added ONLY FOR TESTING PURPOSES
        {
            for (int i = 0; i < nRows; i++)
            {
                for (int j = 0; j < nCols; j++)
                {
                    System.out.print('|');
                    System.out.print(this.board[i][j]);

                }
                System.out.print('|');
                System.out.println();
            }
        }

}
