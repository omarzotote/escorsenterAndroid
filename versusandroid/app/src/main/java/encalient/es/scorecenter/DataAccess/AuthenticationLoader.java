/*package encalient.es.scorecenter.DataAccess;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.tiendapago.app.Common.FlowType;
import com.tiendapago.app.Common.SharedSettings;

import java.util.HashMap;

import OuterProto.LoginDTOOuterClass;
import OuterProto.LoginResponseObjectDTOOuterClass;

/**
 * Created by Franky on 12/29/14.
 */
/*public class AuthenticationLoader {
    private static final String PATH = "auth_SessionData";
    public Context context;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    public AuthenticationLoader(Context ctx) {
        context = ctx;
        settings = context.getSharedPreferences(SharedSettings.sharedProperties, 0);
        editor = settings.edit();
    }

    public LoginResponseObjectDTOOuterClass.LoginResponseObjectDTO doLogin(String userName, String password){
        Log.d("Login", userName + "-" + password);

        int loaderMethod = settings.getInt(SharedSettings.loginLoaderMethod, 0);

        if(loaderMethod == FlowType.FORCELOCAL) {
            String u = settings.getString(SharedSettings.username, "");
            String p = settings.getString(SharedSettings.password, "");
            if(!u.equals(userName) || !p.equals(password)) {
                return null;
            }
        }
        LoginResponseObjectDTOOuterClass.LoginResponseObjectDTO response;

        DataLoader<LoginResponseObjectDTOOuterClass.LoginResponseObjectDTO> loginLoader =
                new DataLoader<LoginResponseObjectDTOOuterClass.LoginResponseObjectDTO>(context);
        DataSaver<LoginResponseObjectDTOOuterClass.LoginResponseObjectDTO> saver =
                new DataSaver<LoginResponseObjectDTOOuterClass.LoginResponseObjectDTO>(context);

        LoginDTOOuterClass.LoginDTO credentials = LoginDTOOuterClass.LoginDTO.newBuilder()
                .setUsername(userName)
                .setPassword(password).build();

        DataLoader.DataRequest reqobject = new DataLoader.DataRequest();
        reqobject.controller = "auth";
        reqobject.datauri = new HashMap<String, String>();
        reqobject.datauri.put("p", "1");
        reqobject.body = credentials.toByteArray();
        reqobject.method = "post";
        response = loginLoader.getObject(reqobject, PATH, LoginResponseObjectDTOOuterClass.LoginResponseObjectDTO.PARSER, loaderMethod);
        if(loaderMethod == FlowType.FORCEREMOTE && response != null) {
            if(response.getStatus() == LoginResponseObjectDTOOuterClass.LoginResponseObjectDTO.Status.SUCCESS) {
                editor.putString(SharedSettings.password, password);
                editor.putString(SharedSettings.username, userName);
                editor.commit();
                if(saver.SaveToFile(PATH, response) == 1) {

                } else {

                }
            }
        }

        return response;
    }

}*/
