
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import CONFIG1.*;
import com.vmware.vim25.InvalidProperty;
import com.vmware.vim25.PerfCounterInfo;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.mo.PerformanceManager;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.PerfCounterInfo;
import com.vmware.vim25.PerfEntityMetric;
import com.vmware.vim25.PerfEntityMetricBase;
import com.vmware.vim25.PerfMetricId;
import com.vmware.vim25.PerfMetricIntSeries;
import com.vmware.vim25.PerfMetricSeries;
import com.vmware.vim25.PerfProviderSummary;
import com.vmware.vim25.PerfQuerySpec;
import com.vmware.vim25.PerfSampleInfo;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.PerformanceManager;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

public class VMstats {

	static final String SERVER_NAME = "130.65.133.50";
	static final String USER_NAME = "administrator";
	static final String PASSWORD = "12!@qwQW";
	private String vmname;
	
	public VMstats(String vm){
		
		this.vmname = vm;
		
		String url = "https://" + SERVER_NAME + "/sdk";
		
		try {
			ServiceInstance si = new ServiceInstance(new URL(url), USER_NAME, PASSWORD, true);
			
			VirtualMachine host = (VirtualMachine) new InventoryNavigator(
					si.getRootFolder()).searchManagedEntity(
					"VirtualMachine", this.vmname);
			PerformanceManager perfMgr = si.getPerformanceManager();
			
			PerfProviderSummary summary = perfMgr
					.queryPerfProviderSummary(host);
			
			System.out.println("perfMgr : " + perfMgr.toString());
			
			int perfInterval = summary.getRefreshRate();
			
			System.out.println("perf interval : " + perfInterval);

			PerfCounterInfo[] perfCounters = perfMgr.getPerfCounter();
			
			System.out.println("perfcounter length" + perfCounters.length );

			for (PerfCounterInfo perfCounterInfo : perfCounters){
				String perfCounterString = perfCounterInfo.getNameInfo().getLabel() + " (" + perfCounterInfo.getGroupInfo().getKey() + ") in "
						+ perfCounterInfo.getUnitInfo().getLabel() + " (" + perfCounterInfo.getStatsType().toString() + ")";
				System.out.println(perfCounterInfo.getKey() + " : " + perfCounterString ); //perfCounterString
			}

			
//			for (int i = 0; i < perfCounters.length; i++){
//				PerfCounterInfo perfCounterInfo = perfCounters[i];
//				String perfCounterString = perfCounterInfo.getNameInfo().getLabel() + " (" + perfCounterInfo.getGroupInfo().getKey() + ") in "
//						+ perfCounterInfo.getUnitInfo().getLabel() + " (" + perfCounterInfo.getStatsType().toString() + ")";
//				System.out.println(perfCounterInfo.getKey() + " : " + perfCounterString ); //perfCounterString
//			
//			}
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
		
		
	}

	public static void main(String[] args) {
		
		//VMstats vm = new VMstats("T17-VM02-Ubuntu32");
		VMstats vm = new VMstats("T17-VM01-Ubuntu32");
		
	}

}
