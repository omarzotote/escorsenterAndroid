package encalient.es.scorecenter.DataAccess.DataSources;

import android.content.Context;

//import com.activeandroid.query.Select;
import com.google.protobuf.Message;
//import com.tiendapago.app.ORMModels.FileORM;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by josemiguelarreola on 1/2/15.
 */
public class DataSaver <T extends Message>{
    Context context;
    public DataSaver(Context ctx) {
        context = ctx;
    }
    /*public int SaveToFile(String path, T object) {
        FileOutputStream fos = null;
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        try {
            fos = context.openFileOutput(path, Context.MODE_PRIVATE);
            object.writeTo(fos);
            object.writeTo(b);
            FileORM fd = new Select().from(FileORM.class).where("Name = '"+path+"'").executeSingle();
            if(fd == null) {
                fd = new FileORM();
            }

            fd.name = path;
            fd.hash  = getMD5EncryptedString(b.toString());
            fd.save();
        }
        catch(Exception ex){
            System.out.println("Error: Could not save file [ "+ex.getMessage()+" ]");
            return 0;
        }
        finally
        {
            if (fos != null) {
                try {
                    fos.close();
                }
                catch (Exception ex)
                {

                }
            }

            if (b != null) {
                try {
                    b.close();
                }
                catch (Exception ex)
                {

                }
            }

        }

        return 1;
    }

    public static String getMD5EncryptedString(String encTarget){
        MessageDigest mdEnc = null;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception while encrypting to md5");
            e.printStackTrace();
        } // Encryption algorithm
        mdEnc.update(encTarget.getBytes(), 0, encTarget.length());
        String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
        while ( md5.length() < 32 ) {
            md5 = "0"+md5;
        }
        return md5;
    }*/
}
