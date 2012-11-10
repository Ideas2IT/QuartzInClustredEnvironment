
Quartz In Clustered Environment
===============================

There is no built-in mechanism in Quartz to prevent concurrent instances of quartz job running at a time in a cluster environment. 

So we directly use the quartz table to solve this problem.
 
Create a Listener class QuartzTriggerListener for your quartz jobs and implement a interface TriggerListener.

TriggerListener has five methods. Namely,

getName
triggerFired
vetoJobExecution
triggerMisfired
triggerComplete

The vetoJobExecution is called by the Scheduler when a trigger has fired, and it's associated JobDetail is about to be executed. If the implementation vetos the execution (via returning true, the job's execute method will not be called. By doing so we can block the jobs execution.

In the custom isQuartzJobAlreadyRunning method we check the table QRTZ_FIRED_TRIGGERS whether the job in the job group is present. If the record is present twice for the job in the job group, the method returns true otherwise returns false.


