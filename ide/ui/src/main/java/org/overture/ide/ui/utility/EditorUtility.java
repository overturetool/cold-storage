/*******************************************************************************
 * Copyright (c) 2009, 2011 Overture Team and others.
 *
 * Overture is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Overture is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Overture.  If not, see <http://www.gnu.org/licenses/>.
 * 	
 * The Overture Tool web-site: http://overturetool.org/
 *******************************************************************************/
package org.overture.ide.ui.utility;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.overture.ide.core.utility.FileUtility;
import org.overture.ide.core.utility.SourceLocationConverter;
import org.overturetool.vdmj.lex.LexLocation;

public class EditorUtility
{
	public static void gotoLocation(IFile file, LexLocation location,
			String message) {
		try {

			IWorkbench wb = PlatformUI.getWorkbench();
			IWorkbenchWindow win = wb.getActiveWorkbenchWindow();

			IEditorPart editor = IDE.openEditor(win.getActivePage(), file, true);

			gotoLocaion(file, location, message, editor);

		} catch (CoreException e) {

			e.printStackTrace();
		}
	}

	private static void gotoLocaion(IFile file, LexLocation location,
			String message, IEditorPart editor) throws CoreException
	{
		IMarker marker = file.createMarker(IMarker.MARKER);
		marker.setAttribute(IMarker.MESSAGE, message);
		marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO);

		SourceLocationConverter converter = new SourceLocationConverter(FileUtility.getContent(file));
		marker.setAttribute(IMarker.CHAR_START,converter.getStartPos( location));
		marker.setAttribute(IMarker.CHAR_END,converter.getEndPos(location));
		IDE.gotoMarker(editor, marker);

		marker.delete();
	}

	public static void gotoLocation(String editorId, IFile file, LexLocation location,
			String message) {
		try {

			IWorkbench wb = PlatformUI.getWorkbench();
			IWorkbenchWindow win = wb.getActiveWorkbenchWindow();

			IEditorPart editor = IDE.openEditor(win.getActivePage(), file, editorId,true);

			gotoLocaion(file, location, message, editor);

		} catch (CoreException e) {

			e.printStackTrace();
		}
	}
	
//	public static List<Character> getContent(IFile file) {
//		
//		InputStream inStream;
//		InputStreamReader in = null;
//		List<Character> content = new Vector<Character>();
//		try {
//			inStream = file.getContents();
//			in = new InputStreamReader(inStream, file.getCharset());
//
//			int c = -1;
//			while ((c = in.read()) != -1)
//				content.add((char) c);
//
//		} catch (CoreException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			if (in != null)
//				try {
//					in.close();
//				} catch (IOException e) {
//				}
//		}
//
//		return content;
//
//	}
//	public static char[] getCharContent(List<Character> content) {
//		char[] source = new char[content.size()];
//		for (int i = 0; i < content.size(); i++) {
//			source[i] = content.get(i);
//		}
//		return source;
//	}
}
