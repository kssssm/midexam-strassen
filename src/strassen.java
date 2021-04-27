import java.util.*;

public class strassen {
    public int[][] MultiArray(int[][] A, int[][] B) {
        int n = A.length;
        int[][] result = new int[n][n];
        if (n == 1)
            result[0][0] = A[0][0] * B[0][0];
        else {
            int temp=n/2;
            int[][] A11 = new int[n / 2][n / 2];
            int[][] A12 = new int[n / 2][n / 2];
            int[][] A21 = new int[n / 2][n / 2];
            int[][] A22 = new int[n / 2][n / 2];

            SplitArray(A, A11,0, 0);
            SplitArray(A, A12,0, n/2);
            SplitArray(A, A21, n/2, 0);
            SplitArray(A, A22, n/2, n/2);
            /**A행렬 4분할해서 새로운 배열로 정의**/

            int[][] B12 = new int[n / 2][n / 2];
            int[][] B11 = new int[n / 2][n / 2];
            int[][] B21 = new int[n / 2][n / 2];
            int[][] B22 = new int[n / 2][n / 2];
            SplitArray(B, B11, 0, 0);
            SplitArray(B, B12, 0, n / 2);
            SplitArray(B, B21, n / 2, 0);
            SplitArray(B, B22, n / 2, n / 2);
            /**A행렬 4분할해서 새로운 배열로 정의**/


            int[][] M1 = MultiArray(AddArray(A11, A22), AddArray(B11, B22));
            int[][] M2 = MultiArray(AddArray(A21, A22), B11);
            int[][] M3 = MultiArray(A11, SubArray(B12, B22));
            int[][] M4 = MultiArray(A22, SubArray(B21, B11));
            int[][] M5 = MultiArray(AddArray(A11, A12), B22);
            int[][] M6 = MultiArray(SubArray(A21, A11), AddArray(B11, B12));
            int[][] M7 = MultiArray(SubArray(A12, A22), AddArray(B21, B22));

            int[][] C11 = AddArray(SubArray(AddArray(M1, M4), M5), M7);
            int[][] C12 = AddArray(M3, M5);
            int[][] C21 = AddArray(M2, M4);
            int[][] C22 = AddArray(SubArray(AddArray(M1, M3), M2), M6);

            /**
             스트라센의 연산행렬 정의
             M1 = (A11 + A22) (B11 + B22)
             M2 = (A21 + A22) B11
             M3 = A11 (B12 - B22)
             M4 = A22 (B21 - B11)
             M5 = (A11 + A12) B22
             M6 = (A21 - A11) (B11 + B12)
             M7 = (A12 - A22) (B21 + B22)

             C11 = M1 + M4 - M5 + M7
             C12 = M3 + M5
             C21 = M2 + M4
             C22 = M1 - M2 + M3 + M6
             **/


            MergeArray(C11, result, 0, 0);
            MergeArray(C12, result, 0, n/2);
            MergeArray(C21, result, n/2, 0);
            MergeArray(C22, result, n/2, n/2);
            /** 연산된 C를 result 배열에 병합해서 저장 **/
        }
        return result;
    }

    public int[][] SubArray(int[][] A, int[][] B) {
        int n = A.length;
        int[][] C = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = A[i][j] - B[i][j];
        return C;
    }

    public int[][] AddArray(int[][] A, int[][] B) {
        int n = A.length;
        int[][] C = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = A[i][j] + B[i][j];
        return C;
    }

    public void SplitArray(int[][] Parent, int[][] Child, int row, int column) {

        for (int i = 0, r=row; i < Child.length; i++, r++)
            for (int j = 0, c=column; j < Child.length; j++, c++)
                Child[i][j] = Parent[r][c];
    }

    /**
     * 배열 분할
     **/
    public void MergeArray(int[][] Child, int[][] Parent, int row, int column) {

        for (int i = 0, r=row; i < Child.length; i++, r++)
            for (int j = 0, c=column; j < Child.length; j++, c++)
                Parent[r][c] = Child[i][j];
    }

    /**
     * 배열 병합
     **/
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        strassen s = new strassen();

        System.out.println("n :");
        int N = scan.nextInt();
        System.out.println("A \n");
        int[][] A = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                A[i][j] = scan.nextInt();

        System.out.println("B  \n");
        int[][] B = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                B[i][j] = scan.nextInt();

        int[][] result = s.MultiArray(A, B);

        System.out.println("A*B : ");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++)
                System.out.print(result[i][j] + " ");
            System.out.println();
        }

    }
}