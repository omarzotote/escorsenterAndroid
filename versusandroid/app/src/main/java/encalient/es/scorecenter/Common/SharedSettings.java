package encalient.es.scorecenter.Common;

/**
 * <p>This class serve as "enum" for the name of shared settings variables.</p>
 * <ul>
 *     <li>sharedProperties: </li>
 *     <li>sharedProperties: </li>
 *     <li>firstLogin: </li>
 *     <li>authExpiration: </li>
 *     <li>lastLogin: </li>
 *     <li>sessionToken: </li>
 *     <li>loginLoaderMethod: </li>
 *     <li>customerLoaderMethod: </li>
 *     <li>customerListLoaderMethod: </li>
 *     <li>lockTime: </li>
 *     <li>username: </li>
 *     <li>password: </li>
 *
 * </ul>
 * @author Sergio Vázquez
 */
public class SharedSettings {
    public static final String sharedProperties = "TiendaPago";
    public static final String firstLogin = "firts_login";
    public static final String authExpiration = "auth_expiration";
    public static final String lastLogin = "last_login";
    public static final String sessionToken = "session_token";
    public static final String loginLoaderMethod = "login_method";
    public static final String customerLoaderMethod = "customer_method";
    public static final String customerListLoaderMethod = "customer_list_method";
    public static final double lockTime = 15.2f;//Minutes
    public static final String username = "user";
    public static final String password = "password";
    public static final int zone_time_add = 5;//Minutes
}
