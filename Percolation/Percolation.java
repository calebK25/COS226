import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
    private final WeightedQuickUnionUF uf; // Union-find instance variable
    private boolean[][] grid; // 2D grid boolean instance variable
    private int openSites; // amount of sites open instance variable
    private int n; // grid size instance variable

    // backwash testing
    private final WeightedQuickUnionUF backwash; // backwash union-find
    private int vtop; // virtual top
    private int vbot; // virtual bottom

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("n cannot be less than or equal to 0");
        this.n = n;
        int size = n * n;
        openSites = 0;
        grid = new boolean[n][n];
        uf = new WeightedQuickUnionUF(size + 2); // +2 to consider virtual top and bot

        // backwash implementation
        backwash = new WeightedQuickUnionUF(size + 1); // + 1 considers virtual top
        vtop = size;
        vbot = size + 1; // adds and extra row to the bottom

        // connect the pseudo top to top
        for (int i = 0; i < n; i++) {
            uf.union(vtop, i);
            backwash.union(vtop, i);
        }
    }

    // private method for throwing exceptions
    private void exception(int row, int col) {
        if (row >= grid.length || col >= grid.length || row < 0 || col < 0)
            throw new IllegalArgumentException("Out of bounds");
    }

    // private method for converting from 2D to 1D index
    private int index(int row, int col) {
        return row * n + col;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        // checks to throw exception if necessary
        exception(row, col);
        if (grid[row][col]) return;

        // marks site as open
        grid[row][col] = true;
        openSites++;
        // set initial point and checks around the point
        int initial = index(row, col);
        // checks top
        if (row < n - 1 && isOpen(row + 1, col)) {
            uf.union(initial, index(row + 1, col));
            backwash.union(initial, index(row + 1, col));
        }
        // checks the bottom
        if (row > 0 && isOpen(row - 1, col)) {
            uf.union(initial, index(row - 1, col));
            backwash.union(initial, index(row - 1, col));
        }
        // checks left
        if (col > 0 && isOpen(row, col - 1)) {
            uf.union(initial, index(row, col - 1));
            backwash.union(initial, index(row, col - 1));
        }
        // checks right
        if (col < n - 1 && isOpen(row, col + 1)) {
            uf.union(initial, index(row, col + 1));
            backwash.union(initial, index(row, col + 1));
        }
        // Connect bottom row sites to virtual bottom
        if (row == n - 1) {
            uf.union(initial, vbot);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        exception(row, col);
        return grid[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        // compares leader value at each box to check for fullness.
        exception(row, col);
        return isOpen(row, col) &&
                backwash.find(index(row, col)) == backwash.find(vtop);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        // ensures the top is filled and bottom is also filled
        return uf.find(vtop) == uf.find(vbot);

    }

    // unit testing
    public static void main(String[] args) {
        Percolation perc = new Percolation(5);
        perc.open(0, 1);
        perc.open(1, 1);
        System.out.println(perc.isFull(0, 0));
        System.out.println(perc.percolates());
    }
}
