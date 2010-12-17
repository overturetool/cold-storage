package org.overture.ide.debug.core;

public interface IDebugConstants
{

	final static String VDMPP_DEBUGGING_ENGINE_ID_KEY = "vdmpp_debugging_engine_id";
	final static String PLUGIN_ID = "org.overture.ide.debug";
	final static String EXTENSION_SOURCEVIEWER_EDITOR = "org.overture.ide.debug.sourceviewerEditor";
	

	// used in the launch configuration
	final static String VDM_LAUNCH_CONFIG_PROJECT = "vdm_launch_config_project";
	final static String VDM_LAUNCH_CONFIG_MODULE = "vdm_launch_config_module";
	final static String VDM_LAUNCH_CONFIG_OPERATION = "vdm_launch_config_method";
	final static String VDM_LAUNCH_CONFIG_STATIC_OPERATION = "vdm_launch_config_static_method";
	//final static String VDM_LAUNCH_CONFIG_EXPRESSION_SEPERATOR = "vdm_launch_config_expression_seperator";
	final static String VDM_LAUNCH_CONFIG_EXPRESSION= "vdm_launch_config_expression";
	
	final static String VDM_LAUNCH_CONFIG_DEFAULT = "vdm_launch_config_default";
	
	final static String VDM_LAUNCH_CONFIG_IS_TRACE = "vdm_launch_config_is_trace";
	
	final static String VDM_LAUNCH_CONFIG_REMOTE_CONTROL = "vdm_launch_config_remote_control_class";
	final static String VDM_LAUNCH_CONFIG_CREATE_COVERAGE = "vdm_launch_config_create_coverage";
	final static String VDM_LAUNCH_CONFIG_REMOTE_DEBUG = "vdm_launch_config_remote_debug";
	final static String VDM_LAUNCH_CONFIG_VM_MEMORY_OPTION = "vdm_launch_config_memory_option";
	final static String VDM_LAUNCH_CONFIG_ENABLE_LOGGING = "vdm_launch_config_enable_logging";
	
	final static String VDM_DEBUG_SESSION_ID = "vdm_debug_session_id";

	public static final String ID_VDM_DEBUG_MODEL = "org.overture.ide.debug.vdm";
	public static final String ATTR_VDM_PROGRAM = ID_VDM_DEBUG_MODEL
			+ ".ATTR_VDM_PROGRAM";
	public static final String ID_VDM_EXECUTABLE = "java";
	
	public static final String BREAKPOINT_MARKER_ID = "org.eclipse.debug.core.lineBreakpointMarker";
	
	//debugger
	public static final String DEBUG_ENGINE_BUNDLE_ID = "org.overture.ide.generated.vdmj";
	public static final String DEBUG_ENGINE_CLASS = "org.overturetool.vdmj.debug.DBGPReaderV2";
	static final String EXTENSION_POINT_VDM_BREAKPOINT_LISTENERS = "breakpointListeners";
	
	/**
	 * Status code indicating an unexpected error.
	 */
	public static final int ERROR = 120;	
	
	
	public static final String CONSOLE_DEBUG_NAME = "Overture Debug";
	public static final String CONSOLE_LOGGING_NAME = "VDM Debug log";
	static final String LogViewId = "org.overture.ide.debug.logging.logview";
	static final String PREF_DBGP_RESPONSE_TIMEOUT = "dbgp_connection_timeout";

	public static final String FILE_SCHEME = "file"; //$NON-NLS-1$
	public static final String DBGP_SCHEME = "dbgp"; //$NON-NLS-1$
	public static final String UNKNOWN_SCHEME = "unknown"; //$NON-NLS-1$
	static final int ERR_DEBUGGER_PROCESS_TERMINATED = 121;
	 
	
}