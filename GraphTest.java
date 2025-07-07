import java.util.*;

// 邻接矩阵存储结构
class GraphMatrix {
    private int[][] matrix;
    private int vertexCount;
    private boolean directed;
    private List<String> vertices;

    public GraphMatrix(int n, boolean directed) {
        this.vertexCount = n;
        this.directed = directed;
        this.matrix = new int[n][n];
        this.vertices = new ArrayList<>();
        for (int i = 0; i < n; i++) vertices.add("V" + i);
    }

    public void addVertex(String label) {
        vertexCount++;
        vertices.add(label);
        int[][] newMatrix = new int[vertexCount][vertexCount];
        for (int i = 0; i < vertexCount - 1; i++)
            for (int j = 0; j < vertexCount - 1; j++)
                newMatrix[i][j] = matrix[i][j];
        matrix = newMatrix;
    }

    public void removeVertex(int idx) {
        if (idx < 0 || idx >= vertexCount) return;
        vertexCount--;
        vertices.remove(idx);
        int[][] newMatrix = new int[vertexCount][vertexCount];
        for (int i = 0, ni = 0; i < matrix.length; i++) {
            if (i == idx) continue;
            for (int j = 0, nj = 0; j < matrix.length; j++) {
                if (j == idx) continue;
                newMatrix[ni][nj++] = matrix[i][j];
            }
            ni++;
        }
        matrix = newMatrix;
    }

    public void addEdge(int u, int v) {
        matrix[u][v] = 1;
        if (!directed) matrix[v][u] = 1;
    }

    public void removeEdge(int u, int v) {
        matrix[u][v] = 0;
        if (!directed) matrix[v][u] = 0;
    }

    public int inDegree(int v) {
        int deg = 0;
        for (int i = 0; i < vertexCount; i++) deg += matrix[i][v];
        return deg;
    }

    public int outDegree(int v) {
        int deg = 0;
        for (int i = 0; i < vertexCount; i++) deg += matrix[v][i];
        return deg;
    }

    public int degree(int v) {
        if (directed) return inDegree(v) + outDegree(v);
        return outDegree(v);
    }

    public List<String> getVertices() {
        return vertices;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    // 转换为邻接表
    public GraphList toGraphList() {
        GraphList g = new GraphList(vertexCount, directed);
        for (int i = 0; i < vertexCount; i++) g.setVertexLabel(i, vertices.get(i));
        for (int i = 0; i < vertexCount; i++)
            for (int j = 0; j < vertexCount; j++)
                if (matrix[i][j] != 0) g.addEdge(i, j);
        return g;
    }

    // 连通性与连通分量
    public int connectedComponents() {
        boolean[] visited = new boolean[vertexCount];
        int count = 0;
        for (int i = 0; i < vertexCount; i++) {
            if (!visited[i]) {
                dfs(i, visited);
                count++;
            }
        }
        return count;
    }

    private void dfs(int u, boolean[] visited) {
        visited[u] = true;
        for (int v = 0; v < vertexCount; v++)
            if (matrix[u][v] != 0 && !visited[v]) dfs(v, visited);
    }

    public boolean hasPath(int u, int v) {
        boolean[] visited = new boolean[vertexCount];
        return dfsPath(u, v, visited);
    }

    private boolean dfsPath(int u, int v, boolean[] visited) {
        if (u == v) return true;
        visited[u] = true;
        for (int i = 0; i < vertexCount; i++) {
            if (matrix[u][i] != 0 && !visited[i]) {
                if (dfsPath(i, v, visited)) return true;
            }
        }
        return false;
    }

    public List<Integer> getSimplePath(int u, int v) {
        List<Integer> path = new ArrayList<>();
        boolean[] visited = new boolean[vertexCount];
        if (dfsSimplePath(u, v, visited, path)) return path;
        return null;
    }

    private boolean dfsSimplePath(int u, int v, boolean[] visited, List<Integer> path) {
        path.add(u);
        if (u == v) return true;
        visited[u] = true;
        for (int i = 0; i < vertexCount; i++) {
            if (matrix[u][i] != 0 && !visited[i]) {
                if (dfsSimplePath(i, v, visited, path)) return true;
            }
        }
        path.remove(path.size() - 1);
        return false;
    }

    public void print() {
        System.out.println("邻接矩阵:");
        for (int i = 0; i < vertexCount; i++) {
            for (int j = 0; j < vertexCount; j++)
                System.out.print(matrix[i][j] + " ");
            System.out.println();
        }
    }
}

// 邻接表存储结构
class GraphList {
    private List<List<Integer>> adj;
    private List<String> vertices;
    private int vertexCount;
    private boolean directed;

    public GraphList(int n, boolean directed) {
        this.vertexCount = n;
        this.directed = directed;
        adj = new ArrayList<>();
        vertices = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
            vertices.add("V" + i);
        }
    }

    public void setVertexLabel(int idx, String label) {
        vertices.set(idx, label);
    }

    public void addVertex(String label) {
        vertexCount++;
        vertices.add(label);
        adj.add(new ArrayList<>());
    }

    public void removeVertex(int idx) {
        if (idx < 0 || idx >= vertexCount) return;
        vertexCount--;
        vertices.remove(idx);
        adj.remove(idx);
        for (List<Integer> list : adj) {
            list.removeIf(e -> e == idx);
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) > idx) list.set(i, list.get(i) - 1);
            }
        }
    }

    public void addEdge(int u, int v) {
        adj.get(u).add(v);
        if (!directed) adj.get(v).add(u);
    }

    public void removeEdge(int u, int v) {
        adj.get(u).remove((Integer) v);
        if (!directed) adj.get(v).remove((Integer) u);
    }

    public int inDegree(int v) {
        int deg = 0;
        for (List<Integer> list : adj)
            for (int u : list) if (u == v) deg++;
        return deg;
    }

    public int outDegree(int v) {
        return adj.get(v).size();
    }

    public int degree(int v) {
        if (directed) return inDegree(v) + outDegree(v);
        return outDegree(v);
    }

    public List<String> getVertices() {
        return vertices;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    // 转换为邻接矩阵
    public GraphMatrix toGraphMatrix() {
        GraphMatrix g = new GraphMatrix(vertexCount, directed);
        for (int i = 0; i < vertexCount; i++) g.getVertices().set(i, vertices.get(i));
        for (int u = 0; u < vertexCount; u++)
            for (int v : adj.get(u)) g.addEdge(u, v);
        return g;
    }

    // 连通性与连通分量
    public int connectedComponents() {
        boolean[] visited = new boolean[vertexCount];
        int count = 0;
        for (int i = 0; i < vertexCount; i++) {
            if (!visited[i]) {
                dfs(i, visited);
                count++;
            }
        }
        return count;
    }

    private void dfs(int u, boolean[] visited) {
        visited[u] = true;
        for (int v : adj.get(u))
            if (!visited[v]) dfs(v, visited);
    }

    public boolean hasPath(int u, int v) {
        boolean[] visited = new boolean[vertexCount];
        return dfsPath(u, v, visited);
    }

    private boolean dfsPath(int u, int v, boolean[] visited) {
        if (u == v) return true;
        visited[u] = true;
        for (int i : adj.get(u)) {
            if (!visited[i]) {
                if (dfsPath(i, v, visited)) return true;
            }
        }
        return false;
    }

    public List<Integer> getSimplePath(int u, int v) {
        List<Integer> path = new ArrayList<>();
        boolean[] visited = new boolean[vertexCount];
        if (dfsSimplePath(u, v, visited, path)) return path;
        return null;
    }

    private boolean dfsSimplePath(int u, int v, boolean[] visited, List<Integer> path) {
        path.add(u);
        if (u == v) return true;
        visited[u] = true;
        for (int i : adj.get(u)) {
            if (!visited[i]) {
                if (dfsSimplePath(i, v, visited, path)) return true;
            }
        }
        path.remove(path.size() - 1);
        return false;
    }

    public void print() {
        System.out.println("邻接表:");
        for (int i = 0; i < vertexCount; i++) {
            System.out.print(vertices.get(i) + ": ");
            for (int v : adj.get(i)) System.out.print(vertices.get(v) + " ");
            System.out.println();
        }
    }
}

// 测试用例
public class GraphTest {
    public static void main(String[] args) {
        System.out.println("==== 无向图（邻接矩阵） ====");
        GraphMatrix g = new GraphMatrix(3, false);
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.print();
        System.out.println("度: V0=" + g.degree(0) + ", V1=" + g.degree(1) + ", V2=" + g.degree(2));
        g.addVertex("V3");
        g.addEdge(2, 3);
        g.print();
        g.removeEdge(1, 2);
        g.print();
        g.removeVertex(1);
        g.print();
        System.out.println("连通分量个数: " + g.connectedComponents());
        System.out.println("0到2有路径? " + g.hasPath(0, 2));
        System.out.println("0到2简单路径: " + g.getSimplePath(0, 2));

        System.out.println("==== 有向图（邻接表） ====");
        GraphList gl = new GraphList(4, true);
        gl.addEdge(0, 1);
        gl.addEdge(1, 2);
        gl.addEdge(2, 3);
        gl.print();
        System.out.println("V0出度: " + gl.outDegree(0) + ", 入度: " + gl.inDegree(0));
        System.out.println("V2出度: " + gl.outDegree(2) + ", 入度: " + gl.inDegree(2));
        gl.addVertex("V4");
        gl.addEdge(3, 4);
        gl.print();
        gl.removeEdge(1, 2);
        gl.print();
        gl.removeVertex(1);
        gl.print();
        System.out.println("连通分量个数: " + gl.connectedComponents());
        System.out.println("0到3有路径? " + gl.hasPath(0, 3));
        System.out.println("0到3简单路径: " + gl.getSimplePath(0, 3));

        System.out.println("==== 结构转换 ====");
        GraphList gl2 = g.toGraphList();
        gl2.print();
        GraphMatrix gm2 = gl.toGraphMatrix();
        gm2.print();
    }
}
