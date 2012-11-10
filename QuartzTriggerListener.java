import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;

// A sample Quartz Listener showing a proto for preventing concurrent instances in clusters
public class QuartzTriggerListener implements TriggerListener {

	private static final String TRIGGER_LISTENER_NAME = "TriggerListener";

	public String getName() {
		return TRIGGER_LISTENER_NAME;
	}

	public void triggerComplete(Trigger trigger, JobExecutionContext context, int triggerInstructionCode) {
		// Code Here
	}

	public void triggerFired(Trigger trigger, JobExecutionContext context) {
		// Code Here
	}

	public void triggerMisfired(Trigger trigger) {
		// Code Here
	}

	public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
		if (isQuartzJobAlreadyRunning(trigger.getJobName(), trigger.getJobGroup())) {
			System.out.println(trigger.getJobName()+ " Already running....");
			return true;
		} else {
			return false;
		}
	}

	/**
	* Checks whether the quartz job already running for the given jobName or not. 
	* If the job already running for the given jobName 
	* returns true otherwise returns false
	*/
	public static boolean isQuartzJobAlreadyRunning(String jobName, String jobGroup) {
		String selectFiredTrigger = "SELECT JOB_NAME,ENTRY_ID FROM QRTZ_FIRED_TRIGGERS WHERE JOB_NAME ='"
		+ jobName + "' AND JOB_GROUP ='" + jobGroup+"'";
		String jdbcDriver = "<Your JDBC Driver>";
		String url = "<jdbc_url>";
		String userName = "<username>";
		String password = "<password>";
		int recordCount = 0;
		try {
			Class.forName(jdbcDriver);
			Connection con = DriverManager.getConnection(url, userName,
			password);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(selectFiredTrigger);
			while (rs.next()) {
			recordCount++;
			}
			rs.close();
			stmt.close();
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// If recordCount two means already one trigger running a job and 
		//the current trigger ready to run another job
		// So, we will terminate or return back the current trigger.
		return recordCount >= 2 ? true : false;
	}
}
