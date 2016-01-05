/*package encalient.es.scorecenter.DataAccess;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.tiendapago.app.Common.FlowType;
import com.tiendapago.app.Common.SharedSettings;
import com.tiendapago.app.ORMModels.CustomerORM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import OuterProto.CustomerDTOOuterClass;
import OuterProto.CustomerListDTOOuterClass;
import OuterProto.CustomerRouteDTOOuterClass;

/**
 * Created by Franky on 12/24/14.
 */
/*public class CustomerLoader {

    private final String CONTROLLER = "customer";

    public Context context;
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    public CustomerLoader(Context ctx) {
        context = ctx;
        settings = context.getSharedPreferences(SharedSettings.sharedProperties, 0);
        editor = settings.edit();
    }

    public CustomerDTOOuterClass.CustomerDTO getCustomer(long id) {
        String offlinePath = CONTROLLER + "_" + id;
        DataLoader<CustomerDTOOuterClass.CustomerDTO> customerLoader = new DataLoader<CustomerDTOOuterClass.CustomerDTO>(context);
        CustomerDTOOuterClass.CustomerDTO customer = customerLoader.getObject(null, offlinePath, CustomerDTOOuterClass.CustomerDTO.PARSER, FlowType.FORCELOCAL);
        return customer;
    }

    public ArrayList<CustomerDTOOuterClass.CustomerDTO> getCustomers() {
        Log.d("PruebaLoader", "Descargando...");
        int loader_method = settings.getInt(SharedSettings.customerListLoaderMethod, FlowType.FORCEREMOTE);
        ArrayList<CustomerDTOOuterClass.CustomerDTO> customers = new ArrayList<CustomerDTOOuterClass.CustomerDTO>();

        if(loader_method == FlowType.FORCELOCAL) {
            List<CustomerORM> l = new Select().all().from(CustomerORM.class).execute();
            customers = getCustomers(l);
        } else if(loader_method == FlowType.FORCEREMOTE) {
            int current = 1;
            int total = 0;
            String offlinePath = CONTROLLER + "s_page_";
            CustomerListDTOOuterClass.CustomerListDTO customerList;
            DataLoader<CustomerListDTOOuterClass.CustomerListDTO> customersLoader = new DataLoader<CustomerListDTOOuterClass.CustomerListDTO>(context);
            DataSaver<CustomerListDTOOuterClass.CustomerListDTO> saver =
                    new DataSaver<CustomerListDTOOuterClass.CustomerListDTO>(context);
            DataSaver<CustomerDTOOuterClass.CustomerDTO> cSaver = new DataSaver<CustomerDTOOuterClass.CustomerDTO>(context);
            DataLoader.DataRequest req = new DataLoader.DataRequest();
            req.controller = CONTROLLER;
            req.method = "get";
            req.datauri = new HashMap<String, String>();
            req.datauri.put("getcustomerswithlayout", "1");
            req.datauri.put("page", Integer.toString(current));

            String token = settings.getString(SharedSettings.sessionToken, "");
            req.headers = new HashMap<String, String>();
            req.headers.put("Cookie", "stoken=" + token);

            new Delete().from(CustomerORM.class).execute();
            Log.d("PageGetter", "1");
            customerList = customersLoader.getObject(req, (offlinePath + current), CustomerListDTOOuterClass.CustomerListDTO.PARSER, loader_method);
            Log.d("PageGetter", "2");
            if (customerList != null && customerList.getItemsList().size() > 0) {
                total = customerList.getPageTotal();
            }
            Log.d("PageGetter", ""+total);
            List<CustomerDTOOuterClass.CustomerDTO> cList = null;
            while (current <= total) {
                cList = customerList.getItemsList();
                for (CustomerDTOOuterClass.CustomerDTO c : cList) {
                    if (cSaver.SaveToFile("customer_" + c.getId(), c) == 1) {
                        String name = (!c.getFirstName().equals("")) ? c.getFirstName() : "";
                        name = (!c.getMiddleName().equals("")) ? (name + " " + c.getMiddleName()) : name;
                        name = (!c.getLastName().equals("")) ? (name + " " + c.getLastName()) : name;

                        String address = (!c.getAddressStreet().equals("")) ? c.getAddressStreet() : "";
                        address = (!c.getAddressIntNumber().equals("")) ? (address + " " + c.getAddressIntNumber()) : address;
                        address = (!c.getAddressExtNumber().equals("")) ? (address + " int. " + c.getAddressExtNumber()) : address;
                        address = (!c.getRegion().equals("")) ? (address + ", " + c.getRegion()) : address;
                        address = (!c.getCity().equals("")) ? (address + ", " + c.getCity()) : address;
                        long id = c.getId();
                        CustomerORM cust = new Select().from(CustomerORM.class).where("CustomerId = ?", id).executeSingle();
                        if (cust == null) {
                            cust = new CustomerORM();
                        }

                        String routesCode = "";
                        String routesSupplier = "";

                        for(CustomerRouteDTOOuterClass.CustomerRouteDTO route : c.getRoutesList()) {
                            if(routesCode.equals("")) {
                                routesCode = route.getCode();
                                routesSupplier = route.getSupplier();
                            } else {
                                routesCode = routesCode + "," + route.getCode();
                                routesSupplier = routesCode + "," + route.getSupplier();
                            }
                        }

                        cust.cid = id;
                        Log.d("Filter-Name-Loader1", name.toUpperCase());
                        cust.name = name.toUpperCase().replace("Ñ", "N");
                        cust.address = address;
                        cust.storename = c.getStoreName();
                        cust.nextvisit = c.getNextVisitScheduled();
                        cust.routes = routesCode + "|" + routesSupplier;
                        cust.save();
                        CustomerORM cust2 = new Select().from(CustomerORM.class).where("CustomerId = ?", id).executeSingle();
                        Log.d("Filter-Name-Loaders2", cust2.name);
                        List<CustomerRouteDTOOuterClass.CustomerRouteDTO> rl = c.getRoutesList();
                    }
                }
                if(cList != null) {
                    customers.addAll(cList);
                }
                current++;
                if (current <= total) {
                    req.datauri.put("page", Integer.toString(current));
                    customerList = customersLoader.getObject(req, (offlinePath + current), CustomerListDTOOuterClass.CustomerListDTO.PARSER, loader_method);
                }
            }
            Log.d("PruebaLoader", "Total: "+customers.size());
        }
        Log.d("PageGetter", "Y luego?");
        if(customers != null) {
            if(customers.size()==0) {
                Log.d("PageGetter", ":(");
                customers = null;
            }
        }

        return customers;
    }

    public ArrayList<CustomerDTOOuterClass.CustomerDTO> getCustomers(List<CustomerORM> list) {
        ArrayList<CustomerDTOOuterClass.CustomerDTO> customers = new ArrayList<CustomerDTOOuterClass.CustomerDTO>();
        for(CustomerORM c : list) {
            CustomerDTOOuterClass.CustomerDTO cus = getCustomer(c.cid);
            if(cus != null) {
                customers.add(cus);
            } else {
                String p1 = "";
                String p2 = "";
            }

        }
        if(customers.size() == 0) return null;
        return customers;
    }

    public int editCustomer(CustomerDTOOuterClass.CustomerDTO customer){
        DataLoader<CustomerDTOOuterClass.CustomerDTO> protoLoader = new DataLoader<CustomerDTOOuterClass.CustomerDTO>(context);

        return 0;
    }

}
*/