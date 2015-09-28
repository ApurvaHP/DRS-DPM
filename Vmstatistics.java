import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.vmware.vim25.InvalidProperty;
import com.vmware.vim25.PerfCounterInfo;
import com.vmware.vim25.PerfEntityMetric;
import com.vmware.vim25.PerfEntityMetricBase;
import com.vmware.vim25.PerfMetricId;
import com.vmware.vim25.PerfMetricIntSeries;
import com.vmware.vim25.PerfMetricSeries;
import com.vmware.vim25.PerfProviderSummary;
import com.vmware.vim25.PerfQuerySpec;
import com.vmware.vim25.PerfSampleInfo;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.PerformanceManager;
import com.vmware.vim25.mo.ServiceInstance;
import com.google.gson.Gson;


public class Vmstatistics {

	static final String SERVER_NAME = "130.65.133.50";
	static final String USER_NAME = "administrator";
	static final String PASSWORD = "12!@qwQW";
	private static final String HOSTNAME = "130.65.133.52";
	
	private static final int SELECTED_COUNTER_ID = 6; // Active (mem) in KB (absolute)
	
	static Integer[] a = { 5, 23, 124, 142, 156 };
	static String[] aName = { "cpu", "mem", "disk", "net", "sys" };
	private static HashMap<String, String> infoList = new HashMap<String, String>();
	static int counter = 0;
	static writetofile writer;
	
	

	public static void main(String[] args) {
		String url = "https://" + SERVER_NAME + "/sdk/vimService";
		//Gson gson = new Gson();
		try {
			writer = writetofile.getInstance();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			System.out.println("vcenter connected");
			ServiceInstance si = new ServiceInstance(new URL(url), USER_NAME, PASSWORD, true);
			HostSystem host = (HostSystem) new InventoryNavigator(si.getRootFolder()).searchManagedEntity("HostSystem", HOSTNAME);
			System.out.println("host connected");
			PerformanceManager perfMgr = si.getPerformanceManager();
			System.out.println("Counter ID = " + SELECTED_COUNTER_ID);
			
			PerfCounterInfo[] pcis = perfMgr.getPerfCounter();
			
			for(int i=0; i<pcis.length; i++){
				
				System.out.println(pcis[i].getGroupInfo().getKey() + "." + pcis[i].getNameInfo().getKey() + "." + pcis[i].getRollupType() + "," + pcis[i].getKey());
			}
			
			PerfProviderSummary summary = perfMgr.queryPerfProviderSummary(host); //
			int perfInterval = summary.getRefreshRate();
			System.out.println(summary.summarySupported);
			System.out.println("Refresh rate = " + perfInterval);

			PerfMetricId[] queryAvailablePerfMetric = perfMgr.queryAvailablePerfMetric(host, null, null, perfInterval);
			ArrayList<PerfMetricId> list = new ArrayList<PerfMetricId>();
			for (int i2 = 0; i2 < queryAvailablePerfMetric.length; i2++) {
				PerfMetricId perfMetricId = queryAvailablePerfMetric[i2];
				if (SELECTED_COUNTER_ID == perfMetricId.getCounterId()) {
					list.add(perfMetricId);
				}
			}
			PerfMetricId[] pmis = list.toArray(new PerfMetricId[list.size()]);

			PerfQuerySpec qSpec = new PerfQuerySpec();
			qSpec.setEntity(host.getMOR());
			qSpec.setMetricId(pmis);
			qSpec.intervalId = perfInterval;
			qSpec.maxSample = 1;

			PerfEntityMetricBase[] pembs = perfMgr.queryPerf(new PerfQuerySpec[] { qSpec });
			for (int i = 0; pembs != null && i < pembs.length; i++) {
				PerfEntityMetricBase val = pembs[i];
				PerfEntityMetric pem = (PerfEntityMetric) val;
				PerfMetricSeries[] vals = pem.getValue();
				PerfSampleInfo[] infos = pem.getSampleInfo();
				//PerfMetricIntSeries val1 = (PerfMetricIntSeries) vals[0];
				//long[] longs = val1.getValue();
				//System.out.println();
				//System.out.println(longs[0]);
				
				for (int j = 0; vals != null && j < vals.length; ++j) {
					PerfMetricIntSeries val1 = (PerfMetricIntSeries) vals[j];
					long[] longs = val1.getValue();
					
					
////					for (int k : a) {
////						infoList.put(aName[counter],
////								String.valueOf(longs[k]));
////						counter++;
////					}
////					counter = 0;
					
					//System.out.println("stats: " + );
					
					for (int k = 0; k < longs.length; k++) {
						System.out.println(infos[k].getTimestamp().getTime() + " : " + longs[k]);
						//System.out.println(String.valueOf(longs[k]));
					}
					System.out.println();
			}
			}
			si.getServerConnection().logout();
		} catch (InvalidProperty e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RuntimeFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		infoList.put("vmIP", HOSTNAME);
//		SimpleDateFormat sd = new SimpleDateFormat(
//				"yyyy-MM-dd hh:mm:ss");
//		String dateFormat = sd.format(new Date());
//		infoList.put("datetime", dateFormat);
//		counter = 0;
//
//		try {
//			//Mongo mongo = new Mongo("130.65.133.178", 27017);
//			//DB db = mongo.getDB("project2");
//			//DBCollection collection1 = db.getCollection("vmtest");
//			DBObject basicdbObject = (DBObject) JSON.parse(gson.toJson(infoList));
//			StringBuilder sb = new StringBuilder();
//			sb.append(dateFormat + ",");
//			sb.append(HOSTNAME+",");
//			for (String str : aName) {
//				sb.append(infoList.get(str).toString()+",");
//			}
//			String out = sb.toString();
//			out = out.substring(0,out.length()-1);
//			
//			writer.write(out+"\n");
//			System.out.println(out);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
	
}
