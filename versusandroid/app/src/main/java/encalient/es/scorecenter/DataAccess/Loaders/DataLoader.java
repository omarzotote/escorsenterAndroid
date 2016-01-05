package encalient.es.scorecenter.DataAccess.Loaders;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

//import com.activeandroid.query.Select;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.Parser;
//import com.tiendapago.app.Common.FlowType;
//import com.tiendapago.app.Helpers.ConnectionHelper;
//import com.tiendapago.app.ORMModels.FileORM;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
//import org.apache.http.entity.mime.HttpMultipartMode;
//import org.apache.http.entity.mime.MultipartEntity;
//import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import encalient.es.scorecenter.Common.FlowType;
import es.encalient.Helpers.ConnectionHelper;


public class DataLoader<T extends Message>{

    private HostnameVerifier hostVerifier = new HostnameVerifier()
    {
        @Override
        public boolean verify(String arg0, SSLSession arg1) {
            return true;
        }
    };

    Context context;
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    public DataLoader(Context ctx){
        context = ctx;
        settings = context.getSharedPreferences("TiendaPago", 0);
        editor = settings.edit();
    }

    private boolean FileExist(String filePath) {
        File file = new File(filePath);
        return true;
        /*if(file.exists())
            return true;
        else
            return false;*/
    }

   /* private boolean IsValidFile(String path, String content) {
        String h = DataSaver.getMD5EncryptedString(content);
        FileORM f = new Select().from(FileORM.class).where("Name = ?", path).executeSingle();
        if(f.hash.equals(h)) {
            return true;
        }
        return false;
    }

    private T LoadFromLocal(String filePath, Parser<T> parser) {
        T object = null;
        try {
            InputStream is = context.openFileInput(filePath);
            object = (T) parser.parseFrom(is);
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            object.writeTo(b);
            if(IsValidFile(filePath, b.toString())) {
                return object;
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Error: Could not read file [ " + ex.getMessage() + " ]");
            return null;
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }*/

    private T LoadFromServer(DataRequest request, String savePath, Parser<T> parser) throws IOException {
        T object;
        if(new ConnectionHelper().getConnectionStatus(context)) {
            object = parser.parseFrom(request.ExecuteRequest());
            return object;
        } else {
            //Force connection
            object = null;
        }
        return object;
    }

    public T getObject(DataRequest request, String localPath, Parser<T> parser, int flow){
        T object = null;
        try {
            switch (flow) {
                case FlowType.LOCALFIRST:

                    /*object = LoadFromLocal(localPath, parser);
                    if(object == null) {
                        object = LoadFromServer(request, localPath, parser);
                    }*/
                    break;
              case FlowType.FORCEREMOTE:
              default:
                    object = LoadFromServer(request, localPath, parser);
                    break;
              case FlowType.FORCELOCAL:
                    /*object = LoadFromLocal(localPath, parser);
                    if(object == null) {
                        //Error archivo dañado o inexistente
                    }*/
                    break;
            }
        } catch(Exception ex) {
            System.out.println("Error: [ "+ex.getMessage()+" ]");
        } finally {
            return object;
        }
    }

    public static class DataRequest {
        // public String controller = "customer";
        //public String domain = "encalientes-em.cloudapp.net";
        public String domain = "192.168.0.111";
        public String method = "get";
        public String scheme = "http";
        public ArrayList<String> paths = null;
        public Map<String, String> parameters = null;
        public Map<String, String> datauri = null;
        public Map<String, String> headers = null;
        public byte[] body = null;
        public boolean apicall = true;
        public DataRequest() {

        }
        private String generateRequestApiUri() {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme(scheme)
                    .authority(domain)
                    .appendPath("api");

            if(paths != null) {
                for(String p : paths) {
                    builder.appendPath(p);
                }
            }
            if(parameters != null) {
                for(String key : parameters.keySet()) {
                    builder.appendQueryParameter(key, parameters.get(key));
                }
            }
            if(datauri != null) {
                for (String key : datauri.keySet()) {
                    builder.appendQueryParameter(key, datauri.get(key));
                }
            }
            String myUrl = builder.build().toString();
            return myUrl;
        }
        private String generateRequestUri() {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme(scheme)
                    .authority(domain);
            if(paths != null) {
                for(String p : paths) {
                    builder.appendPath(p);
                }
            }
            if(parameters != null) {
                for(String key : parameters.keySet()) {
                    builder.appendQueryParameter(key, parameters.get(key));
                }
            }
            if(datauri != null) {
                for (String key : datauri.keySet()) {
                    builder.appendQueryParameter(key, datauri.get(key));
                }
            }
            String myUrl = builder.build().toString();
            return myUrl;
        }

       /* public JSONObject ExecuteUploadRequest(File f) {
            HttpClient httpClient = new DefaultHttpClient();
            String url = (apicall) ? generateRequestApiUri() : generateRequestUri();
            HttpPost postRequest = new HttpPost(url);
            InputStream is = null;

            JSONObject finalResult = null;

            MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            multipartEntity.addPart("UploadedFile", new FileBody(f));

            if(headers != null) {
                for (String key : headers.keySet()) {
                    postRequest.setHeader(key, headers.get(key));
                }
            }

            postRequest.setEntity(multipartEntity);
            try {
                HttpResponse response = httpClient.execute(postRequest);

                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));

                StringBuilder builder = new StringBuilder();
                for (String line = null; (line = reader.readLine()) != null;) {
                    builder.append(line).append("\n");
                }
                Log.d("EnvioImagenR", builder.toString());
                finalResult = new JSONObject(builder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return finalResult;
        }*/

        private HttpResponse getHttpResponse() throws IOException {
            DefaultHttpClient client = new DefaultHttpClient();
            String url = (apicall) ? generateRequestApiUri() : generateRequestUri();
            if(method.equals("post")) {
                HttpPost httpm = new HttpPost(url);
                httpm.setHeader("Content-Type", "application/x-protobuf");
                if(headers != null) {
                    for (String key : headers.keySet()) {
                        httpm.setHeader(key, headers.get(key));
                    }
                }
                if(body != null) {
                    httpm.setEntity(new ByteArrayEntity(body));
                }
                HttpResponse response = client.execute(httpm);
                return response;
            } else if(method.equals("get")) {
                HttpGet httpm = new HttpGet(url);
                httpm.setHeader("Content-Type", "application/x-protobuf");
                if (headers != null) {
                    for (String key : headers.keySet()) {
                        httpm.setHeader(key, headers.get(key));
                    }
                }
                HttpResponse response = client.execute(httpm);
                return response;
            }
            return null;
        }

        public InputStream ExecuteRequest() throws IOException {
            InputStream is = getHttpResponse().getEntity().getContent();
            return is;
        }

        public HttpEntity ExecuteEntitytRequest() throws IOException {
            return getHttpResponse().getEntity();
        }
    }


    public T doGetRequest(DataRequest request, Parser<T> parser){
        T object = null;
        request.method = "get";
        try {
            object = parser.parseFrom(request.ExecuteRequest());
            //saveFileContent(offlinePath, arr);
        }
        catch (Exception e) {
            Log.d("Excepcion", e.getMessage());
        }
        finally {
            return object;
        }
    }

    public T doPostRequest(DataRequest request, Parser<T> parser){
        T object = null;
        request.method = "post";
        try {
            object = parser.parseFrom(request.ExecuteRequest());
            //saveFileContent(offlinePath, arr);
        }
        catch (Exception e) {
            Log.d("Excepcion", e.getMessage());
        }
        finally {
            return object;
        }
    }


  /*  private static class DefaultTrustManager implements X509TrustManager
    {
        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }*/
}

