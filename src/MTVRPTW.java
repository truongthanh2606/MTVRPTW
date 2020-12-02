import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.logging.Logger;

public class MTVRPTW {

    static final Logger logger = Logger.getLogger(MTVRPTW.class.getName());

    /**
     * Algorithm: a multi-start strategy where we consider different threshold for the maximum # clusters.
     * For each number of cluster, we find the best solution by running initial construction and improvement phase,
     * then select the best result (solution) in all solutions.
     * The algorithm for each # cluster is as follow:
     *      1. Initial construction:
     *          1.1. Cluster demand nodes by time window
     *          1.2. Parallel construct a solution for each cluster with Solomon's sequential insertion heuristic
     *          1.3. Merge these solutions iteratively
     *      2. Improvement phase
     *
     */
    public void runClusterRouteMergeAlgorithm(DataModel dataModel) {
        // Step 1: try different # of clusters
        List<List<Route>> solutions = new ArrayList<>();

        // Step 2-9 (currently 2-7): Initial solution construction
        for (int numClusters = 1; numClusters <= dataModel.numClustersThreshold; numClusters++) {
            logger.info("Try " + numClusters + " clusters");
            ClusterRouting clusterRouting = new ClusterRouting(dataModel);  // also pass dataModel
            // Do 3 steps: cluster, parallel construction, merge
            List<Route> routes = clusterRouting.initialConstruction(numClusters);
            solutions.add(routes);
        }
    }

    /**
     * Read algorithm parameters:
     *      threshold of # clusters
     *      vehicle capacity
     */
    public static void readInputParameters(DataModel dataModel, String inputDirectory) {
        try {
            File inputParametersFile = new File( inputDirectory + "/parameters.txt");
            Scanner inputParameter = new Scanner(inputParametersFile);
            dataModel.setInputTestFolder(inputDirectory + "/" + inputParameter.nextLine());
            dataModel.setNumClustersThreshold(inputParameter.nextInt());
            dataModel.setVehicleCapacity(inputParameter.nextInt());
            dataModel.setAlphaParameters(inputParameter.nextDouble(), inputParameter.nextDouble());
            dataModel.setPNeighbourhoodSize(inputParameter.nextInt());
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MTVRPTW mtvrptw = new MTVRPTW();
        String inputDirectory = System.getProperty("user.dir") + "/input/";
        logger.info("Reading input from " + inputDirectory);
        DataModel dataModel = new DataModel();  // read from input (add parameters later)
        readInputParameters(dataModel, inputDirectory);
        dataModel.readInputAndPopulateData();
        mtvrptw.runClusterRouteMergeAlgorithm(dataModel);
    }
}
