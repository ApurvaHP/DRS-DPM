
import java.net.URL;

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

import CONFIG1.*;

public class GetStatistics {
	public String ip;
	private ServiceInstance si;
	private VirtualMachine vm;
	private ManagedEntity[] mes;
	private String vmname ;

	
	public GetStatistics(String vm){
		
		this.vmname = vm;
						
		try {
			
			ServiceInstance si = new ServiceInstance(new URL(
					"https://130.65.133.50/sdk"), "administrator", "12!@qwQW",
					true);
			System.out.println("connected");
			Folder rootFolder = si.getRootFolder();
			
			VirtualMachine host = (VirtualMachine) new InventoryNavigator(
					si.getRootFolder()).searchManagedEntity(
					"VirtualMachine", this.vmname);
			
			PerformanceManager perfMgr = si.getPerformanceManager();
								
			PerfProviderSummary summary = perfMgr
					.queryPerfProviderSummary(host);
			
			System.out.println("summary : " +summary);
			
			int perfInterval = summary.getRefreshRate();
			
			System.out.println("perf interval : " + perfInterval);
			
			System.out.println("avail perf metric : " + perfMgr.queryAvailablePerfMetric(host, null, null, perfInterval) );

			System.out.println("perfMgr: " + perfMgr.getPerfCounter().toString());
			
			PerfMetricId[] queryAvailablePerfMetric = perfMgr
					.queryAvailablePerfMetric(host, null, null,
							perfInterval);
			PerfQuerySpec qSpec = new PerfQuerySpec();
			qSpec.setEntity(host.getMOR());
			qSpec.setMetricId(queryAvailablePerfMetric);
			qSpec.intervalId = perfInterval;
			PerfEntityMetricBase[] pembs = perfMgr
					.queryPerf(new PerfQuerySpec[] { qSpec });
			for (int i = 0; pembs != null && i < pembs.length; i++) {

				PerfEntityMetricBase val = pembs[i];
				PerfEntityMetric pem = (PerfEntityMetric) val;
				PerfMetricSeries[] vals = pem.getValue();
				PerfSampleInfo[] infos = pem.getSampleInfo();
System.out.println("Val0 " + vals);
				
//				for (int j = 0; vals != null && j < vals.length; ++j) {
//					PerfMetricIntSeries val1 = (PerfMetricIntSeries) vals[j];
//					long[] longs = val1.getValue();
//
//					for (int k : a) {
//						infoList.put(aName[counter],
//								String.valueOf(longs[k]));
//						counter++;
//					}
//					counter = 0;
//
//				}
			}

			System.out.println("Avail perf metric:"+queryAvailablePerfMetric[5]);
			
			PerfCounterInfo[] pci = perfMgr.getPerfCounter();
			
			System.out.println("perfcounterinfo" + pci[5]);
		
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
	}
	
	public static void main(String[] args) {
		
		GetStatistics gs = new GetStatistics("T17-VM02-Ubuntu32");
		
		
		
	}

}
