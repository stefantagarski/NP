You are asked to develop a system for log processing. For each log you need to keep the message from the log (String), the type of the log (String) and a timestamp (long). For that purpose complete the missing parts of the interface ILog.

The generic interface LogProcessor should be used for log processing. This interface has only one method with signature ArrayList processLogs (ArrayList logs) The methods has one arguments - the logs that should to be processed and the results is a list of processed logs. You can find this interface in the starter code and you only need to define the generic parameters for it.

The class LogSystem is also given in the start code. You need to keep a list of logs in this class. For the class you need to define the generic parameters, to implement the constructor LogSystem(ArrayList elements) and to complete the method printResults().

In this method you need to create three concrete log processor (with lambda expressions):

Log processor that will return only the logs which are from type INFO
Log processor that will return only the logs whose messages are with length smaller that 100 characters.
Log processor that will return the logs sorted by their timestamp in ascending order.
