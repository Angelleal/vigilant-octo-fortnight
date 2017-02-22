import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Created by USUARIO on 13/02/2017.
 */
public class Percolation {
    private int gridSize = 0, openSiteCount = 0, virtualTop = 0, virtualBottom = 0;
    private boolean[] openSites;
    private WeightedQuickUnionUF sites, sitesViz;

    /**
     * create n-by-n grid, with all sites blocked
     *
     * @param n grid size
     */
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("N must be greater than 0");
        }
        gridSize = n;
        int siteNumber = n * n;
        virtualBottom = siteNumber + 1;
        openSites = new boolean[siteNumber + 2];
        sites = new WeightedQuickUnionUF(siteNumber + 2);
        sitesViz = new WeightedQuickUnionUF(siteNumber + 1);
        for (int i = 1; i <= gridSize; i++) {
            sites.union(virtualTop, i);
            sitesViz.union(virtualTop, i);
            int bottomRowStart = virtualBottom - gridSize;
            sites.union(virtualBottom, bottomRowStart + i);
        }
    }

    /**
     * open site (row, col) if it is not open already
     *
     * @param row row of site
     * @param col column of site
     */
    public void open(int row, int col) {
        validateIndices(row, col);
        if (!isOpen(row, col)) {
            connectTopSite(row, col);
            connectRightSite(row, col);
            connectBottomSite(row, col);
            connectLeftSite(row, col);
            openSites[rowColTo1D(row, col)] = true;
            openSiteCount++;
        }

    }

    /**
     * is site (row, col) open?
     *
     * @param row
     * @param col
     * @return
     */
    public boolean isOpen(int row, int col) {
        validateIndices(row, col);
        return openSites[rowColTo1D(row, col)];
    }

    /**
     * is site (row, col) full?
     *
     * @param row
     * @param col
     * @return
     */
    public boolean isFull(int row, int col) {
        validateIndices(row, col);
        return isOpen(row, col) && sitesViz.connected(virtualTop, rowColTo1D(row, col));
    }

    /**
     * number of open sites
     *
     * @return
     */
    public int numberOfOpenSites() {
        return openSiteCount;
    }

    /**
     * does the system percolate?
     *
     * @return
     */
    public boolean percolates() {
        return sites.connected(virtualTop, virtualBottom);
    }

    private int rowColTo1D(int row, int col) {
        int ret = ((row * gridSize) + col) - gridSize;
        if (ret < 1) {
            return 0;
        } else if (ret > gridSize * gridSize) {
            return ((gridSize * gridSize) + 1);
        } else {
            return ret;
        }
    }

    /**
     * Validates that indices are contained between 1 and N
     *
     * @param row row index to validate
     * @param col column index to validate
     */
    private void validateIndices(int row, int col) {
        if (row <= 0 || row > gridSize) {
            throw new IndexOutOfBoundsException("row index i out of bounds");
        }
        if (col <= 0 || col > gridSize) {
            throw new IndexOutOfBoundsException("column index j out of bounds");
        }
    }

    private void connectLeftSite(int row, int col) {
        if (col > 1 && isOpen(row, col - 1)) {
            sites.union(rowColTo1D(row, col), rowColTo1D(row, col - 1));
            sitesViz.union(rowColTo1D(row, col), rowColTo1D(row, col - 1));
        }

    }

    private void connectRightSite(int row, int col) {
        if (col < gridSize && isOpen(row, col + 1)) {
            sites.union(rowColTo1D(row, col), rowColTo1D(row, col + 1));
            sitesViz.union(rowColTo1D(row, col), rowColTo1D(row, col + 1));
        }

    }

    private void connectTopSite(int row, int col) {
        if (row > 1 && isOpen(row - 1, col)) {
            sites.union(rowColTo1D(row, col), rowColTo1D(row - 1, col));
            sitesViz.union(rowColTo1D(row, col), rowColTo1D(row - 1, col));
        }

    }

    private void connectBottomSite(int row, int col) {
        if (row < gridSize && isOpen(row + 1, col)) {
            sites.union(rowColTo1D(row, col), rowColTo1D(row + 1, col));
            sitesViz.union(rowColTo1D(row, col), rowColTo1D(row + 1, col));
        }

    }
}
