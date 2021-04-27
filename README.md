 					Greedy2-11 : 김선민, 이예린, 정윤아

# strassen

------

## 1. 행렬의 곱
###일반적인 행렬의 곱

```java
MatrixMulti(int [][]A,int [][]B){
n = A.lengh;

for(int i=0; i<A.length; i++){
  for(int j=0; j<A.length; j++){
  C[i][j] = 0;
    for(int k=0; j<A.length; k++){
    C[i][j] = A[i][k] * B[k][j];
    }
  }
}
return C;

```
-A와 B가 n*n 행렬일때, C=A*B로 정의한다.
-3중 for문 이용
-C의 원소 하나를 구하려면 곱셈 n번과 덧셈 (n-1)번이 필요하고 C 전체를 구하기 위해서는
 n^3번의 곱셈과 n^2(n-1)번의 덧셈의 연산을 하게된다
 따라서 시간 복잡도는 O(n^3) 이다.

### 분할정복을 이용한 행렬의 곱

```java
MatrixMulti(int [][]A,int [][]B){
n = A.lengh;

if(n==1) C[1][1]=A[1][1]*B[1][1];
  else{
       C11=MatrixMulti(A11,B11)+MatrixMulti(A12,B21);
       C12=MatrixMulti(A11,B12)+MatrixMulti(A12,B22);
       C21=MatrixMulti(A21,B11)+MatrixMulti(A22,B21);
       C22=MatrixMulti(A21,B12)+MatrixMulti(A22,B22);

```
-A와 B가 n*n 행렬이고 n이 2의 거듭제곱일떄, 각 행렬을 4개의 행렬(n/2*n/2)로 분할한다.
  A=(A11,A12,A21,A22).  B=(B11,B12,B21,B22).  C=(C11,C12,C21,C22)
- C11 = (A11 * B11) + (A12 * B21)
  C12 = (A11 * B12) + (A12 * B22)
  C21 = (A21 * B11) + (A22 * B21)
  C22 = (A21 * B12) + (A22 * B22)  
 이 등식들을 이용해서 재귀적인 알고리즘을 이용한다.

 - 8번의 재귀호출이 발생하므로 8*T(n/2) 각 분할된 행렬들이 (n^2/4)개의 원소를 갖고 있고 4번의 덧셈이 이루어지기 때문에 O(n^2)시간 이 걸린다.
 따라서 시간복잡도는 O(n^3)이다.
---

## 2. strassen algorithm
  1. n*n 행렬을 n/2*n/2 행렬 4개로 분할한다.
   A=(A11,A12,A21,A22).  B=(B11,B12,B21,B22).  C=(C11,C12,C21,C22)
  2. 분할된 행렬로 7개의 행렬식(M)을 만든다
    M1 = (A11 + A22)*(B11 + B22)
    M2 = (A21 + A22)*B11
    M3 = A11*(B12 - B22)
    M4 = A22*(B21 - B11)
    M5 = (A11 + A12)*B22
    M6 = (A21 - A11)*(B11 + B12)
    M7 = (A12 - A22)*(B21 + B22)
  3. M을 이용해 C의 분할된 행렬을 계산한다
    C11 = M1 + M4 - M5 + M7
    C12 = M3 + M5
    C21 = M2 + M4
    C22 = M1 - M2 + M3 + M6
-7번의 재귀호출이 발생하므로 7T(n/2) n/2*n/2 행렬을 더하거나 빼므로 O(N^2)시간이 걸린다.
 따라서 시간 복잡도는 O(n^2.807)이다.









---

## 3. 자바 코드

```java
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
```

---

## 4.  코드 결과

#### 입력



#### 출력




---

## 5.  성능 평가

- n개의 물건 각각의 단위 무게 당 가치를 계산하는 데는 O(n) 시간이 걸린다. : Item내의 cost  계산
- 물건의 단위 무게 당 가치에 대해서 내림차순으로 정렬하기 위해 O(n^2) 시간이 걸린다. (이중 for문을 이용했기 때문에) : SortItem
- while 루프의 수행은 n번을 넘지 않으며, 루프 내부의 수행은 O(1) 시간이 걸린다. : FractionalKnapsack내의 while 루프
- if문의 코드는 각각 O(1) 시간이 걸리고 return도 O(1) 시간이 걸린다. :FractionalKnapsack내의 if문과 return

따라서 부분 배낭 그리디 알고리즘의 시간 복잡도는 O(n)+O(n^2)+nxO(1)+O(1) = O(n^2) 이며 이를 그래프로 나타내면 이와 같다.
![n^2](https://user-images.githubusercontent.com/81538527/114697087-6f01bd80-9d58-11eb-9309-f4ee90e55381.png)
