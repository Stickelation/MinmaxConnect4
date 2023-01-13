import java.util.*;

public class GeneralTree // This class creates and manipulates a tree full of nodes
{
    static int MAXDEPTH;
    int maxColumns;
    char[][]tempBoard;
    boolean playerTurn = true; // Becomes false after the player takes their turn
    String playerWin = "XXXX";
    String computerWin= "OOOO";
    char playerPiece = 'X';
    char currentPiece = playerPiece;
    char computerPiece = 'O';
    int maxRows;
    boolean isPlayGoal = false;
    boolean isCompGoal = false;
    boolean boardFull=false;
    Node root;
    int depthCounter=1;
    List<Node> tempList = new Stack();


    public void createTree(Node start, int cols, int rows, int maxDepth) // Creates tree with the current node as the root
    {
        MAXDEPTH = maxDepth;
        root = start;
        maxColumns=cols;
        maxRows=rows;
        List<Node> rootList = new Stack();
        rootList.add(root); // stores root node in list
        populateTree(rootList,0,0);
    }

    void populateTree(List<Node>nodeList,int counter,int listNum)
    {
        if (depthCounter%2!=0) // Swaps the current piece as depth increases
            currentPiece=computerPiece;
        else
            currentPiece=playerPiece;

        while (depthCounter<=MAXDEPTH) // Runs until the desired depth is reached
        {
            while(nodeList.isEmpty()==false) // Runs until the end of the nodelist (all passed children have found their new children)
            {
                Node temp = nodeList.get(0);
          /*      while (temp.isLeaf)
                {
                    nodeList.remove(0);
                    temp=nodeList.get(listCounter);
                }

           */
                counter = 0; // Counter resets to 0 as new row of children are being tested
                while (legalMoves(temp).length > counter)
                {
                    copyBoard(temp.getBoard()); // copies board
                    updateTempBoard(legalMoves(temp)[counter]); //updates copied board
                    Node temp2 = new Node(tempBoard); // stores copied board in a new node
                    temp.addChild(temp2); // assigns the new node as a child of the current parent
                    temp2.setParent(temp); // assigns the current node as the parent of the new node
                    temp2.setDepth(depthCounter); // sets current depth of the new node
                    temp2.setLastMove(legalMoves(temp)[counter]); // sets last legal move that was performed
                    evaluate(legalMoves(temp)[counter]);
                    if (depthCounter == MAXDEPTH || legalMoves(temp).length == 0 || isCompGoal || isPlayGoal) // Only runs on Leaf Nodes
                    {
                        double holder = evaluate(legalMoves(temp)[counter]);
                        temp2.setScore(holder); // Evaluates the current board position (using the last x and y point to narrow search)
                    }
                    if (isCompGoal || isPlayGoal || boardFull) // If an 'end-state' is reached, no further children are created for that node
                    {
                        temp2.setLeaf();
                        boardFull = false;
                        isCompGoal = false;
                        isPlayGoal = false;
                        break;
                    }
                    else
                        tempList.add(temp2); // adds new non-leaf child to the new list, so that its children may be found
                   // temp2.displayNode(maxRows, maxColumns); // uncomment if you want to visually see the creation of child nodes (possible board states of depth n)
                    counter++;
                }
                nodeList.remove(0);
            }
            depthCounter++; // Increases depth by 1
            nodeList.clear();
            for (int i = 0; i < tempList.size(); i++) // transfers the child nodes in tempList to nodeList, which will be used for the next depth
            {
                nodeList.add(tempList.get(i));
            }
            tempList.clear();
            if (depthCounter%2!=0) // Swaps the current piece as depth increases
                currentPiece=computerPiece;
            else
                currentPiece=playerPiece;
        }
    }

    void copyBoard(char[][]board) // Copies the current board to 'tempBoard' which will be changed for each possibility
    {
        tempBoard= new char[maxRows][maxColumns];
        for (int i = 0; i <= maxRows-1; i++)  // Copies the contents of the current board
        {
            for (int j = 0; j <= maxColumns-1; j++)
            {
                tempBoard[i][j]=board[i][j];
            }
        }

    }
    void updateTempBoard(int input) // adds the last legal move to the current board
    {
        int temp = maxRows-1;
        while (true)
        {
            if (tempBoard[temp][input]== ' ')
            {
                tempBoard[temp][input]= currentPiece;
                break;
            }
            else
                temp--;
        }
    }
    public int minimax (Node tempRoot) // postOrder list traversal that updates nodes' values (THIS IS THE MINIMAX FUNCTION) To make it more efficient, this function compares the children with the parent, instead of the parent with its children
    {
        List newTempList = tempRoot.getChildren();
        for (Object o : newTempList) // Parses through all children
        {
            Node holder = (Node) o;
            minimax(holder); // recursive call to repeat search for every child of the node
        }
        if (tempRoot.getDepth() % 2 != 0) // If we're looking for the max of the children, Updates parent with max value
        {
            doMax(tempRoot);
   //         tempRoot.displayNode(maxRows,maxColumns); // These three lines are put in for personal testing purposes (hence why they are commented)
   //         System.out.print("Last move was:");
   //         System.out.println(tempRoot.getLastMove());
   //         System.out.print("Last score was:");
   //         System.out.println(tempRoot.getScore());
        }
        else
        {
            doMin(tempRoot);
        }
        if (tempRoot.getParent()!=null)
        {
    //        System.out.println(tempRoot.getScore());
    //        System.out.print("This node is depth: ");
    //        System.out.println(tempRoot.getDepth());
    //        System.out.print("This Node has this many children: ");//       System.out.println(tempRoot.getChildren().size());
    //        System.out.println("This is the parent:");
    //        tempRoot.getParent().displayNode(maxRows,maxColumns);
        }

            return tempRoot.getLastMove(); // Returns the updated root move
    }

 void doMax(Node tempRoot) // If we're looking for the max of the children, checks if the child is greater than the parent, otherwise it does the opposite
 {
         if (tempRoot.getParent()==null) // stops at root node
             return;
         double parentScoreHolder=0.00;
         double childScoreHolder=0.00;
         if (tempRoot.getUpdated()==false) // Will only run evaluation on leaves
         {
             tempRoot.setUpdated(true);
             childScoreHolder = evaluate2(tempRoot);
             tempRoot.setScore(childScoreHolder);
         }
         else
             childScoreHolder=tempRoot.getScore();

         if (tempRoot.getParent().getUpdated()==false)
         {
             tempRoot.getParent().setUpdated(true);
             tempRoot.getParent().setScore(childScoreHolder);
             if (tempRoot.getParent().getParent()==null) // If this node's parent is the root node, and if it meets the criteria to be updated, it's 'lastmove' is also changed
             {
                 tempRoot.getParent().setLastMove(tempRoot.getLastMove());
             }
         }
     else
         {
             parentScoreHolder = tempRoot.getParent().getScore();

             if (parentScoreHolder <= childScoreHolder) // If the parent's score is NOT already higher than the child's, the parent's score and last move are updated
             {
                 if (tempRoot.getParent().getParent()==null) // If this node's parent is the root node, and if it meets the criteria to be updated, it's 'lastmove' is also changed
                 {
                     tempRoot.getParent().setLastMove(tempRoot.getLastMove());
                 }
                 tempRoot.getParent().setScore(childScoreHolder);
             }
         }
 }

    void doMin(Node tempRoot)
    {
        if (tempRoot.getParent()==null) // stops at root node
            return;
        double parentScoreHolder=tempRoot.getParent().getScore();
        double childScoreHolder=tempRoot.getScore();
        if (tempRoot.getUpdated()==false) // Will only run on leaves
        {
            tempRoot.setUpdated(true);
            childScoreHolder = evaluate2(tempRoot);
            tempRoot.setScore(childScoreHolder);
        }
        else
            childScoreHolder=tempRoot.getScore();
        if (tempRoot.getParent().getUpdated()==false)
        {
            tempRoot.getParent().setUpdated(true);
            tempRoot.getParent().setScore(childScoreHolder);
        }
        //tempRoot.displayNode(maxRows, maxColumns);
        if (parentScoreHolder >= childScoreHolder)
        {
            tempRoot.getParent().setScore(childScoreHolder);
        }
    }
    public int [] legalMoves(Node passedNode)
    {
        int[]tempList = new int[maxColumns];
        int j=0;

        for (int i = 0; i <= maxColumns-1; i++)
        {
            if (passedNode.getBoard()[0][i]==' ')
            {
                tempList[j]=i;
                j++;
            }
        }
        int[]moves=new int[j]; // Creating an array of perfect size to store all possible moves
        if (j >= 0) System.arraycopy(tempList, 0, moves, 0, j );
        return moves;
    }

    public double evaluate2(Node passedNode) // Evaluates the entire scored and returns a score based on heuristics (FOR MARKERS: Terminal points give +/-10000.00, all other heuristics give +/- 1-5)
    {
        double newScore = 0.00;
        int tempX=0;
        int tempY=0;
        int half = (int)(maxColumns/2);
        char temp = ' ';
        String hori = ""; // Holds all horizontal combinations in a string
        String vert = ""; // Holds all vertical combinations in a string
        String diag = ""; // Top left to bottom right
        String reverseDiag = ""; // Bottom left to top Right
        for (int i = 0; i < maxRows; i++)
        {
            for (int j = 0; j < maxColumns; j++)
            {
                temp = passedNode.getBoard()[i][j];
                if (temp==' ')
                hori+=('-');
                else
                    hori+=temp;
                if(j==half)
                {
                    if (passedNode.getBoard()[i][j] == 'X') // Decreases score by 1 if a computer piece was placed in the center (since it is more optimal)
                    {
                        newScore--;
                    }
                    else if (passedNode.getBoard()[i][j] == 'O') // Increases score by 1 if a computer piece was placed in the center (since it is more optimal)
                    {
                        newScore++;
                    }
                }
            }
            hori+="N"; // Separates different horizontals
        }
        for (int i = 0; i < maxColumns; i++)
        {
            for (int j = 0; j < maxRows; j++)
            {
                temp = passedNode.getBoard()[j][i];
                if (temp==' ')
                    vert+=('-');
                else
                    vert+=temp;
            }
            vert+="N"; // Separates different verticals
        }

        for (int i = 0; i < maxRows; i++)
        {
            for (int j = 0; j < maxColumns; j++)
            {
                tempY=i;
                tempX=j;

                while (tempY < maxRows && tempX < maxColumns)// Checks top left to bottom right diagonal
                {
                    temp = passedNode.getBoard()[tempY][tempX];
                    if (temp==' ')
                        diag+=('-');
                    else
                        diag+=temp;
                    tempY++;
                    tempX++;
                }
                diag+="N"; // Separates different diagonals
            }
        }
        for (int i = 0; i < maxRows; i++)
        {
            for (int j = 0; j < maxColumns; j++)
            {
                tempY=i;
                tempX=j;

                while (tempY<maxRows && tempX>0) // Checks bottom left to top right diagonal
                {
                    temp = passedNode.getBoard()[tempY][tempX];
                    if (temp==' ')
                        reverseDiag+=('-');
                    else
                        reverseDiag+=temp;
                    tempY++;
                    tempX--;
                }
                reverseDiag+="N"; // Separates different diagonals
            }
        }
        if (hori.contains(computerWin)||vert.contains(computerWin)||diag.contains(computerWin)||reverseDiag.contains(computerWin)) // If terminal state is found, returns maximum value
        {
            newScore=10000.00;
            return newScore;
        }
        else if (hori.contains(playerWin)||vert.contains(playerWin)||diag.contains(playerWin)||reverseDiag.contains(playerWin)) // If terminal state is found, returns minimum value
        {
            newScore=(-10000.00);
            return newScore;
        }
        else  // Turns the 4 strings into a charArray, then parses through it adding/subtracting from 'newScore' based off of doubles/triples/placing towards the center
        {
            hori+="N";
            vert+="N";
            diag+="N";
            hori=hori.concat(vert);
            hori=hori.concat(diag);
            hori=hori.concat(reverseDiag);
            char[] charHori = hori.toCharArray();
            int max = charHori.length-2;
            for (int i = 0; i < max; i++)
            {
                if (charHori[i]=='O')
                {
                   if (charHori[i+1]=='O') // Increases board's score by 2 if two computer pieces are together (in any direction)
                    {
                        newScore+=2;
                        if (charHori[i+2]=='O') // Increases board's score by 6 (total), if 3 computer pieces are together (in any direction)
                        {
                            newScore+=4;
                        }
                    }
                }
                else if (charHori[i]=='X')
                {
                    if (charHori[i+1]=='X') // Decreases board's score by 2 if two player pieces are together (in any direction)
                    {
                        newScore-=2;
                        if (charHori[i+2]=='X') // Decreases board's score by 6 (total), if 3 player pieces are together (in any direction)
                        {
                            newScore-=4;
                        }
                    }
                }
            }
        }

        return newScore;
    }

    public double evaluate(int xVal)
    {
        checkGameOver(xVal);
        if (isPlayGoal) // terminal state
        {
            return -10000.00;
        }
        if (isCompGoal) // terminal state
        {
            return 10000.00;
        }

        return 0;
    }
    void checkGameOver(int xVal) // Checks to see if someone has won from the last move, or if there is a tie
    {
        int yVal=0;
        for (int i = maxRows-1; i >=0; i--)
        {
            if (tempBoard[i][xVal]==' ')
                yVal++;
        }
        String rowCheck= "";
        String colCheck= "";
        String diagCheck= "";
        String reverseDiagCheck= "";
        boardFull=true;
        String currentString;
        if (playerTurn)
            currentString=playerWin;
        else
            currentString=computerWin;

        for (int i = 0; i <= maxColumns-1; i++) // Checks row
        {
            rowCheck+=tempBoard[yVal][i];
        }
        for (int i = 0; i <= maxRows-1; i++) // Check column
        {
            colCheck+=tempBoard[i][xVal];
        }
        int tempX = xVal;
        int tempY= yVal;
        while (tempY>0&&tempX>0)// Checks top left to bottom right diagonal
        {
            tempY--;
            tempX--;
        }
        while (tempY<=maxRows-1&&tempX<=maxColumns-1)
        {
            diagCheck+=tempBoard[tempY][tempX];
            tempY++;
            tempX++;
        }
        int tempX2 = xVal;
        int tempY2= yVal;
        while (tempY2>0 && tempX2<maxColumns-1) // Check top right to bottom left diagonal
        {
            tempY2--;
            tempX2++;
        }
        while (tempY2<=maxRows-1 &&tempX2>=0)
        {
            reverseDiagCheck+=tempBoard[tempY2][tempX2];
            tempY2++;
            tempX2--;
        }
        if (rowCheck.contains(currentString)||diagCheck.contains(currentString)||colCheck.contains(currentString)||reverseDiagCheck.contains(currentString))
        {
                if (playerTurn)
                {
                    //System.out.println("FOUND");
                    isPlayGoal=true;
                }
                else
                    isCompGoal=true;
        }

            for (int i = 0; i <= maxRows - 1; i++)  // Searches the array (board) to see if any empty spaces are left
            {
                for (int j = 0; j <= maxColumns - 1; j++)
                {
                    if (tempBoard[i][j] == ' ')
                    {
                        boardFull = false;
                        break;
                    }
                }
            }
        }
    }