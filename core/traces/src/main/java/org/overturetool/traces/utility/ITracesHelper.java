package org.overturetool.traces.utility;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.overturetool.vdmj.definitions.NamedTraceDefinition;
import org.overturetool.vdmj.traces.TraceReductionType;
import org.xml.sax.SAXException;

public interface ITracesHelper
{
	public abstract boolean initialized();

	public abstract List<String> getClassNamesWithTraces() throws IOException,
			TraceHelperNotInitializedException;

	public abstract List<NamedTraceDefinition> getTraceDefinitions(
			String className) throws IOException, SAXException,
			ClassNotFoundException, TraceHelperNotInitializedException;

	public abstract List<TraceTestResult> getTraceTests(String className,
			String trace) throws SAXException, IOException,
			ClassNotFoundException, TraceHelperNotInitializedException;

	public abstract List<TraceTestResult> getTraceTests(String className,
			String trace, Integer startNumber, Integer stopNumber)
			throws SAXException, IOException, ClassNotFoundException,
			TraceHelperNotInitializedException;

	public abstract Integer getTraceTestCount(String className, String trace)
			throws SAXException, IOException, ClassNotFoundException,
			TraceHelperNotInitializedException;

	public abstract void processSingleTrace(String className, String traceName,
			Object monitor) throws ClassNotFoundException,
			TraceHelperNotInitializedException;

	public abstract void processClassTraces(String className, Object monitor)
			throws ClassNotFoundException, TraceHelperNotInitializedException,IOException,Exception;
	
	public abstract void processClassTraces(String className,float subset, TraceReductionType traceReductionType,long seed, Object monitor)
	throws ClassNotFoundException, TraceHelperNotInitializedException,IOException,Exception;
	

	public abstract TraceTestStatus getStatus(String className, String trace,
			Integer num) throws SAXException, IOException,
			ClassNotFoundException;

	public abstract TraceTestResult getResult(String className, String trace,
			Integer num) throws SAXException, IOException,
			ClassNotFoundException;

	public abstract File getFile(String className) throws IOException,
			ClassNotFoundException, TraceHelperNotInitializedException;

	public abstract int getSkippedCount(String className, String traceName)
			throws SAXException, IOException, ClassNotFoundException;
	
	public abstract String getProjectName();

}
