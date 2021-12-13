import java.util.Scanner;

public class Board {
    public int[][] board;
    public int size;
    public int side;
    private Computer aibo;

    public Board(int s){
        size = s;
        side = 1;
        this.board = new int[size][size];
        aibo = new Computer(s);
    }

    public void play(){
        Scanner sc = new Scanner(System.in);
        while(!isGameOver()){
            if(side == 1){
                System.out.println("Your turn:");
                int x = sc.nextInt();
                int y = sc.nextInt();
                setStone(x, y);
            }else{
                System.out.println("Computer's turn:");
                long start = System.currentTimeMillis();
                int[] p = aibo.getMoveFJAB(this);
                setStone(p[0], p[1]);
                long end = System.currentTimeMillis();
                System.out.printf("Time: %.4f s\n", (end - start) / 1000.0);
            }
            displayBoard();
            side = -1*side;
        }
    }

    public void setStone(int x, int y){
        if(x >= size || x < 0 || y >= size || y < 0 || board[x][y] != 0){
            System.out.println(x + " " + y + " " + "Invalid move.");
        }
        else{
            board[x][y] = side;
        }
    }

    public boolean isFull(){
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if(board[i][j] == 0) return false;
            }
        }
        return true;
    }

    public boolean isGameOver() {
        boolean result;
        if(isFull()) return true;

        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if(board[i][j] != 0){
                    if((i > 0 && i < size - 1)
                            && board[i][j] == board[i - 1][j]
                            && board[i][j] == board[i + 1][j]) return true;

                    if((j > 0 && j < size - 1)
                            && board[i][j] == board[i][j - 1]
                            && board[i][j] == board[i][j + 1]) return true;

                    if((i > 0 && i < size - 1 && j > 0 && j < size - 1)
                            && board[i][j] == board[i - 1][j - 1]
                            && board[i][j] == board[i + 1][j + 1]) return true;

                    if((i > 0 && i < size - 1 && j > 0 && j < size - 1)
                            && board[i][j] == board[i - 1][j + 1]
                            && board[i][j] == board[i + 1][j - 1]) return true;
                }
            }
        }
        return false;
    }

    public void displayBoard(){
        System.out.println();
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if(board[i][j] == 0) System.out.print("? ");
                if(board[i][j] == 1) System.out.print("X ");
                if(board[i][j] == -1) System.out.print("O ");
            }
            System.out.println();
        }
    }
}