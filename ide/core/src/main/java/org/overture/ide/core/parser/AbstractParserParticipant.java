package org.overture.ide.core.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.overture.ide.core.ICoreConstants;
import org.overture.ide.core.VdmCore;

import org.overture.ide.core.resources.IVdmProject;
import org.overture.ide.core.resources.IVdmSourceUnit;
import org.overture.ide.core.utility.FileUtility;
import org.overturetool.vdmj.ast.IAstNode;
import org.overturetool.vdmj.lex.LexLocation;
import org.overturetool.vdmj.messages.VDMError;
import org.overturetool.vdmj.messages.VDMWarning;

public abstract class AbstractParserParticipant implements ISourceParser
{

	protected String natureId;
	protected IVdmProject project;

	public void setNatureId(String natureId)
	{
		this.natureId = natureId;
	}

	public void setProject(IVdmProject project)
	{
		this.project = project;
	}

	/**
	 * Parses a file and updates file markers and the AstManager with the result
	 */
	public void parse(IVdmSourceUnit file)
	{

		ParseResult result;
		try
		{
			LexLocation.getAllLocations().clear();
			result = startParse(file,
					new String(FileUtility.getCharContent(FileUtility.getContent(file.getFile()))),
					file.getFile().getCharset());
			setFileMarkers(file.getFile(), result);
			if (result != null && result.getAst()!=null)
				file.reconcile(
						result.getAst(),
						result.getAllLocation(),
						result.getLocationToAstNodeMap(),
						result.hasParseErrors());

		} catch (CoreException e)
		{
			if (VdmCore.DEBUG)
			{
				VdmCore.log("AbstractParserParticipant:parse IVdmSourceUnit", e);
			}
		}
	}

	/**
	 * Parses a file and updates file markers and the AstManager with the result
	 */
	public void parse(IVdmSourceUnit file, String data)
	{

		ParseResult result;
		try
		{
			LexLocation.getAllLocations().clear();
			result = startParse(file, data, file.getFile().getCharset());
			setFileMarkers(file.getFile(), result);
			if (result != null)

				file.reconcile(
						result.getAst(),
						result.getAllLocation(),
						result.getLocationToAstNodeMap(),
						result.hasParseErrors());
		} catch (CoreException e)
		{
			if (VdmCore.DEBUG)
			{
				VdmCore.log("AbstractParserParticipant:parse IVdmSourceUnit with seperate data", e);
			}
		}

	}

	/**
	 * Adds markers to a file based on the result. if errors occurred the problem markers are first cleared.
	 * 
	 * @param file
	 *            the file where the markers should be set
	 * @param result
	 *            the result indicating if parse errors occurred
	 * @throws CoreException
	 */
	private void setFileMarkers(IFile file, ParseResult result)
			throws CoreException
	{
		if (file != null)
		{
			FileUtility.deleteMarker(file,IMarker.PROBLEM,ICoreConstants.PLUGIN_ID);
			if (result.hasParseErrors())
			{
				file.deleteMarkers(IMarker.PROBLEM,
						true,
						IResource.DEPTH_INFINITE);
				int previousErrorNumber = -1;
				for (VDMError error : result.getErrors())
				{
					if (previousErrorNumber == error.number)// this check is
						// done to avoid
						// error fall
						// through
						continue;
					else
						previousErrorNumber = error.number;
					FileUtility.addMarker(file,
							error.toProblemString(),
							error.location,
							IMarker.SEVERITY_ERROR,
							ICoreConstants.PLUGIN_ID);
				}
			}

			IVdmProject vdmProject = (IVdmProject) project.getAdapter(IVdmProject.class);
			
			if (result.getWarnings().size() > 0 && vdmProject != null
					&& vdmProject.hasSuppressWarnings())
			{
				for (VDMWarning warning : result.getWarnings())
				{
					FileUtility.addMarker(file,
							warning.toProblemString(),
							warning.location,
							IMarker.SEVERITY_WARNING,
							ICoreConstants.PLUGIN_ID);
				}
			}
		}

	}

	/**
	 * Delegate method for parsing, the methods addWarning and addError should be used to report
	 * 
	 * @param file
	 *            the file to be parsed
	 * @param content
	 *            the content of the file
	 * @param charset
	 *            the charset of the content
	 * @return the result of the parse including error report and the AST
	 */
	protected abstract ParseResult startParse(IVdmSourceUnit file, String content,
			String charset);



	protected void addWarning(IFile file, String message, int lineNumber)
	{
		FileUtility.addMarker(file,
				message,
				lineNumber,
				IMarker.SEVERITY_WARNING,
				ICoreConstants.PLUGIN_ID);
	}

	protected void addError(IFile file, String message, int lineNumber)
	{
		FileUtility.addMarker(file, message, lineNumber, IMarker.SEVERITY_ERROR, ICoreConstants.PLUGIN_ID);
	}

	protected void processInternalError(Throwable e)
	{
		System.out.println(e.toString());
	};

	public static class ParseResult
	{
		private List<IAstNode> ast = null;
		private List<VDMError> errors = new ArrayList<VDMError>();
		private List<VDMWarning> warnings = new ArrayList<VDMWarning>();
		private Throwable fatalError;
		private List<LexLocation> allLocation;
		private Map<LexLocation,IAstNode> locationToAstNodeMap;

		public ParseResult() {

		}

		

		public boolean hasParseErrors()
		{
			return errors.size()!=0 || fatalError!=null;
		}

		public void setAst(List<IAstNode> ast)
		{
			Assert.isNotNull(ast,"AST cannot be null");
			Assert.isTrue(ast.size()!=0,"AST cannot be an empty list");
			this.ast = ast;
		}

		public List<IAstNode> getAst()
		{
			return ast;
		}

		public void setWarnings(List<VDMWarning> warnings)
		{
			this.warnings.addAll(warnings);
		}

		public List<VDMWarning> getWarnings()
		{
			return warnings;
		}

		public void setErrors(List<VDMError> errors)
		{
			this.errors.addAll(errors);
		}

		public List<VDMError> getErrors()
		{
			return errors;
		}

		public void setFatalError(Throwable fatalError)
		{
			if (VdmCore.DEBUG)
			{
				//fatalError.printStackTrace();
			}
			this.fatalError = fatalError;
		}

		public Throwable getFatalError()
		{
			return fatalError;
		}



		public void setAllLocation(List<LexLocation> allLocation)
		{
			this.allLocation = allLocation;
		}



		public List<LexLocation> getAllLocation()
		{
			return allLocation;
		}



		public void setLocationToAstNodeMap(Map<LexLocation,IAstNode> locationToAstNodeMap)
		{
			this.locationToAstNodeMap = locationToAstNodeMap;
		}



		public Map<LexLocation,IAstNode> getLocationToAstNodeMap()
		{
			return locationToAstNodeMap;
		};

	}
}