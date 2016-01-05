package encalient.es.scorecenter.Common;

/**
 * <p>This class is used to define a "enum" of the different types for the data load method.</p>
 * <ul>
 *     <li>LOCALFIRST: The application will try to find the data on the local storage, else, the system
 *     will try to download the information from the server.</li>
 *     <li>FORCEREMOTE: The data will be downloaded from the server.</li>
 *     <li>FORCELOCAL: The data will be loaded from the local storage.</li>
 * </ul>
 * @author Nacho Arreola
 */
public class FlowType {
    public static final int LOCALFIRST = 1;
    public static final int FORCEREMOTE = 2;
    public static final int FORCELOCAL = 3;
}
