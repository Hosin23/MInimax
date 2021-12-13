import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Computer {
    private final int range;
    private static final ForkJoinPool FJ_POOL = ForkJoinPool.commonPool();


    public Computer(int r){
        range = r;
    }

    public void playStone(int x, int y, int side, Board playBoard){
        if(x >= range || x < 0 || y >= range || y < 0){
            System.out.println("- -0");
        }
        else{
            playBoard.board[x][y] = side;
        }
    }

    public int minimax(int depth, boolean isMax, Board playBoard){
        int boardValue = evaluate(playBoard.board);
        if(boardValue == 100 || boardValue == -100 || depth == 0 || playBoard.isFull()) return boardValue;
        if(isMax){
            int maxValue = Integer.MIN_VALUE;
            for(int i = 0; i < range; i++) {
                for(int j = 0; j < range; j++){
                    if(playBoard.board[i][j] == 0){
                        playStone(i, j, 1, playBoard);
                        maxValue = Math.max(maxValue, minimax(depth - 1, false, playBoard));
                        playStone(i, j, 0, playBoard);
                    }
                }
            }
            return maxValue;
        }else{
            int minValue = Integer.MAX_VALUE;
            for(int i = 0; i < range; i++) {
                for(int j = 0; j < range; j++){
                    if(playBoard.board[i][j] == 0){
                        playStone(i, j, -1, playBoard);
                        minValue = Math.min(minValue, minimax(depth - 1, true, playBoard));
                        playStone(i, j, 0, playBoard);
                    }
                }
            }
            return minValue;
        }
    }

    public int minimaxAB(int depth, boolean isMax, Board playBoard, int alpha, int beta){
        int boardValue = evaluate2(playBoard.board);
        if(boardValue == 100 || boardValue == -100 || depth == 0 || playBoard.isFull()) return boardValue;
        if(isMax){
            int maxValue = Integer.MIN_VALUE;
            for(int i = 0; i < range; i++) {
                for(int j = 0; j < range; j++){
                    if(playBoard.board[i][j] == 0){
                        playStone(i, j, 1, playBoard);
                        maxValue = Math.max(maxValue, minimaxAB(depth - 1, false, playBoard, alpha, beta));
                        playStone(i, j, 0, playBoard);
                        if(maxValue >= beta)
                            break;
                        alpha = Math.max(alpha, maxValue);
                    }
                    if(maxValue >= beta)
                        break;
                    alpha = Math.max(alpha, maxValue);
                }
            }
            return maxValue;
        }else{
            int minValue = Integer.MAX_VALUE;
            for(int i = 0; i < range; i++) {
                for(int j = 0; j < range; j++){
                    if(playBoard.board[i][j] == 0){
                        playStone(i, j, -1, playBoard);
                        minValue = Math.min(minValue, minimaxAB(depth - 1, true, playBoard, alpha, beta));
                        playStone(i, j, 0, playBoard);
                        if(minValue <= alpha)
                            break;
                        beta = Math.max(beta, minValue);
                    }
                    if(minValue <= alpha)
                        break;
                    beta = Math.max(beta, minValue);
                }
            }
            return minValue;
        }
    }

    public int evaluate(int[][] board){
        int result = 0;
        for(int i = 0; i < range; i++){
            for(int j = 0; j < range; j++){
                if(board[i][j] != 0){
                    if((i > 0 && i < range - 1)
                            && board[i][j] == board[i - 1][j]
                            && board[i][j] == board[i + 1][j]) result = board[i][j] * 100;

                    if((j > 0 && j < range - 1)
                            && board[i][j] == board[i][j - 1]
                            && board[i][j] == board[i][j + 1]) result = board[i][j] * 100;

                    if((i > 0 && i < range - 1 && j > 0 && j < range - 1)
                            && board[i][j] == board[i - 1][j - 1]
                            && board[i][j] == board[i + 1][j + 1]) result = board[i][j] * 100;

                    if((i > 0 && i < range - 1 && j > 0 && j < range - 1)
                            && board[i][j] == board[i - 1][j + 1]
                            && board[i][j] == board[i + 1][j - 1]) result = board[i][j] * 100;
                }
            }
        }
        return result;
    }

    public int evaluate2(int[][] board){
        int result;
        int x1 = 0;
        int x2 = 0;
        int o1 = 0;
        int o2 = 0;
        for(int i = 0; i < range; i++){
            boolean hasX1 = false;
            boolean hasX2 = false;
            boolean hasO1 = false;
            boolean hasO2 = false;
            for(int j = 0; j < range - 2; j++){
                if(board[i][j] != 0){
                    if(board[i][j] == board[i][j + 1] && board[i][j] == board[i][j + 2]) return board[i][j] * 100;
                    if(board[i][j] == 1){
                        if((board[i][j] == board[i][j + 1] && board[i][j + 2] == 0) &&
                                (board[i][j] == board[i][j + 2] && board[i][j + 1] == 0)){
                            hasX1 = true;
                            break;
                        }
                        if(board[i][j + 1] == 0 && board[i][j + 2] == 0)
                            hasX2 = true;
                    }else{
                        if((board[i][j] == board[i][j + 1] && board[i][j + 2] == 0) &&
                                (board[i][j] == board[i][j + 2] && board[i][j + 1] == 0)) {
                            hasO1 = true;
                            break;
                        }
                        if(board[i][j + 1] == 0 && board[i][j + 2] == 0)
                            hasO2 = true;
                    }
                }else{
                    if(board[i][j + 1] == board[i][j + 2] && board[i][j + 1] != 0){
                        if(board[i][j + 1] == 1) hasX1 = true;
                        if(board[i][j + 1] == -1) hasO1 = true;
                        break;
                    }
                    if(board[i][j + 1] == 1 && board[i][j + 2] == 0) hasX2 = true;
                    if(board[i][j + 2] == 1 && board[i][j + 1] == 0) hasX2 = true;
                    if(board[i][j + 1] == -1 && board[i][j + 2] == 0) hasO2 = true;
                    if(board[i][j + 2] == -1 && board[i][j + 1] == 0) hasO2 = true;
                }
            }
            if(hasX1) x1++;
            else if(hasX2) x2++;
            if(hasO1) o1++;
            else if(hasO2) o2++;
        }

        for(int i = 0; i < range - 2; i++){
            boolean hasX1 = false;
            boolean hasX2 = false;
            boolean hasO1 = false;
            boolean hasO2 = false;
            for(int j = 0; j < range; j++){
                if(board[i][j] != 0){
                    if(board[i + 1][j] == board[i][j] && board[i][j] == board[i + 2][j]) return board[i][j] * 100;
                    if(board[i][j] == 1){
                        if((board[i][j] == board[i + 1][j] && board[i + 2][j] == 0) &&
                                (board[i][j] == board[i + 2][j] && board[i + 1][j] == 0)){
                            hasX1 = true;
                            break;
                        }
                        if(board[i + 1][j] == 0 && board[i + 2][j] == 0)
                            hasX2 = true;
                    }else{
                        if((board[i][j] == board[i + 1][j] && board[i + 2][j] == 0) &&
                                (board[i][j] == board[i + 2][j] && board[i + 1][j] == 0)) {
                            hasO1 = true;
                            break;
                        }
                        if(board[i + 1][j] == 0 && board[i + 2][j] == 0)
                            hasO2 = true;
                    }
                }else{
                    if(board[i + 1][j] == board[i + 2][j] && board[i + 1][j] != 0){
                        if(board[i + 1][j] == 1) hasX1 = true;
                        if(board[i + 1][j] == -1) hasO1 = true;
                        break;
                    }
                    if(board[i + 1][j] == 1 && board[i + 2][j] == 0) hasX2 = true;
                    if(board[i + 2][j] == 1 && board[i + 1][j] == 0) hasX2 = true;
                    if(board[i + 1][j] == -1 && board[i + 2][j] == 0) hasO2 = true;
                    if(board[i + 2][j] == -1 && board[i + 1][j] == 0) hasO2 = true;
                }
            }
            if(hasX1) x1++;
            else if(hasX2) x2++;
            if(hasO1) o1++;
            else if(hasO2) o2++;
        }

        for(int i = 0; i < range - 2; i++){
            boolean hasX1 = false;
            boolean hasX2 = false;
            boolean hasO1 = false;
            boolean hasO2 = false;
            for(int j = 0; j < range - 2; j++){
                if(board[i][j] != 0){
                    if(board[i][j] == board[i + 1][j + 1] && board[i][j] == board[i + 2][j + 2])
                        return board[i][j] * 100;
                    if(board[i][j] == 1){
                        if((board[i][j] == board[i + 1][j + 1] && board[i + 2][j + 2] == 0) &&
                                (board[i][j] == board[i + 2][j + 2] && board[i + 1][j + 1] == 0)){
                            hasX1 = true;
                            break;
                        }
                        if(board[i + 1][j + 1] == 0 && board[i + 2][j + 2] == 0)
                            hasX2 = true;
                    }else{
                        if((board[i][j] == board[i + 1][j + 1] && board[i + 2][j + 2] == 0) &&
                                (board[i][j] == board[i + 2][j + 2] && board[i + 1][j + 1] == 0)) {
                            hasO1 = true;
                            break;
                        }
                        if(board[i + 1][j + 1] == 0 && board[i + 2][j + 2] == 0)
                            hasO2 = true;
                    }
                }else{
                    if(board[i + 1][j + 1] == board[i + 2][j + 2] && board[i + 1][j + 1] != 0){
                        if(board[i + 1][j + 1] == 1) hasX1 = true;
                        if(board[i + 1][j + 1] == -1) hasO1 = true;
                        break;
                    }
                    if(board[i + 1][j + 1] == 1 && board[i + 2][j + 2] == 0) hasX2 = true;
                    if(board[i + 1][j + 2] == 1 && board[i + 2][j + 1] == 0) hasX2 = true;
                    if(board[i + 1][j + 1] == -1 && board[i + 2][j + 2] == 0) hasO2 = true;
                    if(board[i + 1][j + 2] == -1 && board[i + 1][j + 1] == 0) hasO2 = true;
                }
            }
            if(hasX1) x1++;
            else if(hasX2) x2++;
            if(hasO1) o1++;
            else if(hasO2) o2++;
        }

        for(int i = 2; i < range; i++){
            boolean hasX1 = false;
            boolean hasX2 = false;
            boolean hasO1 = false;
            boolean hasO2 = false;
            for(int j = 0; j < range - 2; j++){
                if(board[i][j] != 0){
                    if(board[i][j] == board[i - 1][j + 1] && board[i][j] == board[i - 2][j + 2])
                        return board[i][j] * 100;
                    if(board[i][j] == 1){
                        if((board[i][j] == board[i - 1][j + 1] && board[i - 2][j + 2] == 0) &&
                                (board[i][j] == board[i - 2][j + 2] && board[i - 1][j + 1] == 0)){
                            hasX1 = true;
                            break;
                        }
                        if(board[i - 1][j + 1] == 0 && board[i - 2][j + 2] == 0)
                            hasX2 = true;
                    }else{
                        if((board[i][j] == board[i - 1][j + 1] && board[i - 2][j + 2] == 0) &&
                                (board[i][j] == board[i - 2][j + 2] && board[i - 1][j + 1] == 0)) {
                            hasO1 = true;
                            break;
                        }
                        if(board[i - 1][j + 1] == 0 && board[i - 2][j + 2] == 0)
                            hasO2 = true;
                    }
                }else{
                    if(board[i - 1][j + 1] == board[i - 2][j + 2] && board[i - 1][j + 1] != 0){
                        if(board[i - 1][j + 1] == 1) hasX1 = true;
                        if(board[i - 1][j + 1] == -1) hasO1 = true;
                        break;
                    }
                    if(board[i - 1][j + 1] == 1 && board[i - 2][j + 2] == 0) hasX2 = true;
                    if(board[i - 2][j + 2] == 1 && board[i - 1][j + 1] == 0) hasX2 = true;
                    if(board[i - 1][j + 1] == -1 && board[i - 2][j + 2] == 0) hasO2 = true;
                    if(board[i - 2][j + 2] == -1 && board[i - 1][j + 1] == 0) hasO2 = true;
                }
            }
            if(hasX1) x1++;
            else if(hasX2) x2++;
            if(hasO1) o1++;
            else if(hasO2) o2++;
        }
        result = 3 * x1 + x2 - (3 * o1 + o2);
        return result;
    }

    public int[] getMove(Board playBoard){
        int[] result = new int[2];
        int min = Integer.MAX_VALUE;
        for(int i = 0; i < range; i++){
            for(int j = 0; j < range; j++){
                if(playBoard.board[i][j] == 0){
                    playStone(i, j,-1, playBoard);
                    int score = minimax(10, true, playBoard);
                    playStone(i, j, 0, playBoard);
                    if(score < min){
                        min = score;
                        result[0] = i;
                        result[1] = j;
                    }
                }
            }
        }

        return result;
    }

    public int[] getMoveAB(Board playBoard){
        int[] result = new int[2];
        int min = Integer.MAX_VALUE;
        for(int i = 0; i < range; i++){
            for(int j = 0; j < range; j++){
                if(playBoard.board[i][j] == 0){
                    playStone(i, j,-1, playBoard);
                    int score = minimaxAB(10, true, playBoard, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    playStone(i, j, 0, playBoard);
                    if(score < min){
                        min = score;
                        result[0] = i;
                        result[1] = j;
                    }
                }
            }
        }

        return result;
    }

    public int[] getMoveFJ(Board playBoard){
        int[] result = new int[2];
        int min = Integer.MAX_VALUE;
        for(int i = 0; i < range; i++){
            for(int j = 0; j < range; j++){
                if(playBoard.board[i][j] == 0){
                    playStone(i, j,-1, playBoard);
                    int score = FJ_POOL.invoke(new minimaxTask(10, true, playBoard.board));
                    playStone(i, j, 0, playBoard);
                    if(score < min){
                        min = score;
                        result[0] = i;
                        result[1] = j;
                    }
                }
            }
        }

        return result;
    }
    public int[] getMoveFJAB(Board playBoard){
        int[] result = new int[2];
        int min = Integer.MAX_VALUE;
        for(int i = 0; i < range; i++){
            for(int j = 0; j < range; j++){
                if(playBoard.board[i][j] == 0){
                    playStone(i, j,-1, playBoard);
                    int score = FJ_POOL.invoke(new minimaxTaskAB(10, true,
                            Integer.MIN_VALUE, Integer.MAX_VALUE, playBoard.board));
                    playStone(i, j, 0, playBoard);
                    if(score < min){
                        min = score;
                        result[0] = i;
                        result[1] = j;
                    }
                }
            }
        }

        return result;
    }

    private class minimaxTask extends RecursiveTask<Integer>{
        private static final int SEQUENTIAL_CUTOFF = 5;
        private int depth;
        private boolean isMax;
        private int[][] playBoard;
        private int boardValue;

        public minimaxTask(int d, boolean m, int[][] p){
            depth = d;
            isMax = m;
            playBoard = copyBoard(p);
            boardValue = evaluate2(playBoard);
        }

        private int[][] copyBoard(int[][] p){
            int[][] result = new int[p.length][p.length];
            for(int i = 0; i < result.length; i++){
                for(int j = 0; j < result.length; j++){
                    result[i][j] = p[i][j];
                }
            }
            return result;
        }

        private boolean isFull(int[][] b){
            for(int i = 0; i < b.length; i++){
                for(int j = 0; j < b.length; j++){
                    if(b[i][j] == 0) return false;
                }
            }
            return true;
        }

        @Override
        protected Integer compute() {
            if(boardValue == 100 || boardValue == -100 || depth == 0 || isFull(playBoard))
                return boardValue;
            else{
                if(isMax){
                    int maxValue = Integer.MIN_VALUE;
                    List<minimaxTask> threads = new ArrayList<>();
                    for(int i = 0; i < range; i++) {
                        for(int j = 0; j < range; j++){
                            if(playBoard[i][j] == 0){
                                playBoard[i][j] = 1;
                                threads.add(new minimaxTask(depth - 1, false, playBoard));
                                playBoard[i][j] = 0;
                            }
                        }
                    }
                    for(int i = 0; i < threads.size() - 1; i++){
                        threads.get(i).fork();
                    }
                    int last = threads.get(threads.size() - 1).compute();
                    maxValue = Math.max(maxValue, last);

                    for(int i = 0; i < threads.size() - 1; i++){
                        int cur = threads.get(i).join();
                        maxValue = Math.max(maxValue, cur);
                    }
                    return maxValue;
                }else{
                    int minValue = Integer.MAX_VALUE;
                    List<minimaxTask> threads = new ArrayList<>();
                    for(int i = 0; i < range; i++) {
                        for(int j = 0; j < range; j++){
                            if(playBoard[i][j] == 0){
                                playBoard[i][j] = -1;
                                threads.add(new minimaxTask(depth - 1, true, playBoard));
                                playBoard[i][j] = 0;
                            }
                        }
                    }
                    for(int i = 0; i < threads.size() - 1; i++){
                        threads.get(i).fork();
                    }
                    int last = threads.get(threads.size() - 1).compute();
                    minValue = Math.min(minValue, last);

                    for(int i = 0; i < threads.size() - 1; i++){
                        int cur = threads.get(i).join();
                        minValue = Math.min(minValue, cur);

                    }
                    return minValue;
                }
            }
        }
    }

    private class minimaxTaskAB extends RecursiveTask<Integer>{
        private static final int SEQUENTIAL_CUTOFF = 5;
        private int depth;
        private boolean isMax;
        private int alpha, beta;
        private int[][] playBoard;
        private int boardValue;

        public minimaxTaskAB(int d, boolean m, int a, int b, int[][] p){
            depth = d;
            isMax = m;
            alpha = a;
            beta = b;
            playBoard = copyBoard(p);
            boardValue = evaluate2(playBoard);
        }

        private int[][] copyBoard(int[][] p){
            int[][] result = new int[p.length][p.length];
            for(int i = 0; i < result.length; i++){
                for(int j = 0; j < result.length; j++){
                    result[i][j] = p[i][j];
                }
            }
            return result;
        }

        private boolean isFull(int[][] b){
            for(int i = 0; i < b.length; i++){
                for(int j = 0; j < b.length; j++){
                    if(b[i][j] == 0) return false;
                }
            }
            return true;
        }

        @Override
        protected Integer compute() {
            if(boardValue == 100 || boardValue == -100 || depth == 0 || isFull(playBoard))
                return boardValue;
            else{
                if(isMax){
                    int maxValue = Integer.MIN_VALUE;
                    List<minimaxTaskAB> threads = new ArrayList<>();
                    for(int i = 0; i < range; i++) {
                        for(int j = 0; j < range; j++){
                            if(playBoard[i][j] == 0){
                                playBoard[i][j] = 1;
                                threads.add(new minimaxTaskAB(depth - 1, false, alpha, beta, playBoard));
                                playBoard[i][j] = 0;
                            }
                        }
                    }
                    for(int i = 0; i < threads.size() - 1; i++){
                        threads.get(i).fork();
                    }
                    int last = threads.get(threads.size() - 1).compute();
                    maxValue = Math.max(maxValue, last);
                    if(maxValue >= beta){
                        return maxValue;
                    }else{
                        alpha = Math.max(alpha, maxValue);
                        for(int i = 0; i < threads.size() - 1; i++){
                            int cur = threads.get(i).join();
                            maxValue = Math.max(maxValue, cur);
                            if(maxValue >= beta)
                                break;
                            alpha = Math.max(alpha, maxValue);
                        }
                        return maxValue;
                    }
                }else{
                    int minValue = Integer.MAX_VALUE;
                    List<minimaxTaskAB> threads = new ArrayList<>();
                    for(int i = 0; i < range; i++) {
                        for(int j = 0; j < range; j++){
                            if(playBoard[i][j] == 0){
                                playBoard[i][j] = -1;
                                threads.add(new minimaxTaskAB(depth - 1, true, alpha, beta, playBoard));
                                playBoard[i][j] = 0;
                            }
                        }
                    }
                    for(int i = 0; i < threads.size() - 1; i++){
                        threads.get(i).fork();
                    }
                    int last = threads.get(threads.size() - 1).compute();
                    minValue = Math.min(minValue, last);
                    if(minValue <= alpha){
                        return minValue;
                    }else{
                        beta = Math.max(beta, minValue);
                        for(int i = 0; i < threads.size() - 1; i++){
                            int cur = threads.get(i).join();
                            minValue = Math.min(minValue, cur);
                            if(minValue <= alpha)
                                break;
                            beta = Math.max(beta, minValue);
                        }
                        return minValue;
                    }

                }
            }
        }
    }
}
