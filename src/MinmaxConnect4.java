// THIS PROGRAM WAS MADE IN JAVA USING INTELLIJ
// NOTE: Please ignore any commented out lines - they were included ONLY for debugging/testing purposes whilst coding
import java.util.Scanner; // Only used for user input

// Justin Stickel
// Date: 2021/10/17

// This program will create a 7x6 game of Connect 4 that utilises a Minimax algorithm to allow for an AI opponent
public class MinmaxConnect4
{
    static int MAXDEPTH = 4; // Note: Increasing depth also increases AI difficulty, but consequently increases process time
    int maxColumns = 7; // Length of the Connect-4 board
    int maxRows = 6; // Height of the Connect-4 board
    char [][] gameBoard = new char[maxRows][maxColumns];
    char [][] currentBoard;
    int compInput;
    boolean playerTurn = true; // Becomes false after the player takes their turn
    boolean gameFinished = false; // Becomes true once the game has a winner
    char playerPiece = 'X';
    char computerPiece = 'O';
    char currentPiece;
    String playerWin = "XXXX";
    String computerWin= "OOOO";
    MinmaxConnect4()
    {
        loadBoard();
        printBoard(gameBoard);
        currentBoard = gameBoard; // Sets the current board as the base(empty) board
        currentPiece = playerPiece; // User is set to play first
        while (!gameFinished)
        {
            if (playerTurn)
            {
                updateBoard(getUserInput());
                currentPiece=computerPiece;
                playerTurn=false;
            }
            else
            {
                updateBoard(getCompInput());
                //updateBoard(getUserInput()); //Uncomment this line and comment the above line to play as both players
                currentPiece=playerPiece;
                playerTurn=true;
            }
        }

    }

    void loadBoard() // fills the board with 'empty' tiles
    {
        for (int i = 0; i <= maxRows-1; i++)
        {
            for (int j = 0; j <= maxColumns-1; j++)
            {
                gameBoard[i][j]= ' ';
            }
        }
    }

    void printBoard(char [][] outBoard) // Prints the current state of the board
    {
        System.out.print(" 0 1 2 3 4 5 6");
        System.out.println();
        for (int i = 0; i <= maxRows-1; i++)  // These embedded loops will create a representation of the game for the user to see
        {
            for (int j = 0; j <= maxColumns-1; j++)
            {
                System.out.print('|');
                System.out.print(outBoard[i][j]);
            }
            System.out.print('|');
            System.out.println();
        }
    }

    int getUserInput() // Collects user input through console
    {
        int input;
        while (true)
        {
            System.out.println("Enter a column: ");
            Scanner in = new Scanner(System.in);
            input = in.nextInt();
            if (input<0 || input >= maxColumns||gameBoard[0][input] != ' ')
            {
                System.out.println("You cannot place a piece there!");
            }
            else
                break;
        }
        return input;
    }

    int getCompInput() // Using MINIMAX, determines the AI's move and returns it
    {
        Node currentNode = new Node(currentBoard);
        GeneralTree tree = new GeneralTree();
        tree.createTree(currentNode,maxColumns,maxRows,MAXDEPTH);
        compInput = tree.minimax(currentNode); // This is the MINIMAX call - using a postorder generaltree search, the leaf children are found and through them, the parents are updated (their scores and last moves)
    //    System.out.println(currentNode.getScore());
        return compInput;
    }

    void updateBoard(int input) // adds the last legal move to the current board
    {
        int temp = maxRows-1;
        while (true)
        {
            if (currentBoard[temp][input]== ' ')
            {
                currentBoard[temp][input]= currentPiece;
                printBoard(currentBoard);
                checkGameOver(temp, input,true);
                break;
            }
            else
                temp--;
        }
    }

    void checkGameOver(int yVal, int xVal, boolean realCheck) // Checks to see if someone has won from the last move, or if there is a tie
    {
        String rowCheck= "";
        String colCheck= "";
        String diagCheck= "";
        String reverseDiagCheck= "";
        boolean boardFull=true;
        String currentString;
        if (playerTurn)
            currentString=playerWin;
        else
            currentString=computerWin;

        for (int i = 0; i <= maxColumns-1; i++) // Checks row
        {
            rowCheck+=currentBoard[yVal][i];
        }
        for (int i = 0; i <= maxRows-1; i++) // Check column
        {
            colCheck+=currentBoard[i][xVal];
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
            diagCheck+=currentBoard[tempY][tempX];
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
            reverseDiagCheck+=currentBoard[tempY2][tempX2];
            tempY2++;
            tempX2--;
        }
        if (rowCheck.contains(currentString)||diagCheck.contains(currentString)||colCheck.contains(currentString)||reverseDiagCheck.contains(currentString))
        {
            if (realCheck) // Only runs/ends game if this is method is not being called by the miniMax algorithm
            {
                gameFinished = true;
                if (playerTurn)
                    System.out.println("Congrats! You have defeated the AI and won!");
                else
                    System.out.println("You lose! The AI has won! Better luck next time!");
            }

        }

        if (realCheck)
        {
            for (int i = 0; i <= maxRows - 1; i++)  // Searches the array (board) to see if any empty spaces are left
            {
                for (int j = 0; j <= maxColumns - 1; j++) {
                    if (currentBoard[i][j] == ' ') {
                        boardFull = false;
                        break;
                    }
                }
            }
            if (boardFull)
            {
                gameFinished = true;
                System.out.println("Tie game! There are no possible moves left!");
            }
        }
    }
/*
   public int [] legalMoves()
    {
        int[]tempList = new int[maxColumns-1];
        int j=0;

        for (int i = 0; i <= maxColumns-1; i++)
        {
            if (currentBoard[0][i]==0)
            {
                tempList[j]=i;
                j++;
            }
        }
        int[]moves=new int[j]; // Creating an array of perfect size to store all possible moves
        if (j + 1 >= 0) System.arraycopy(tempList, 0, moves, 0, j + 1);
        return moves;
    }
*/
    public static void main(String[] args){ new MinmaxConnect4(); }
}
