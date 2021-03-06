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
package org.overture.ide.vdmrt.ui.editor.contentAssist;

import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.overture.ide.ui.templates.VdmContentAssistProcessor;
import org.overture.ide.vdmrt.ui.IVdmRtUiConstants;

public class VdmRtContentAssistProcessor extends VdmContentAssistProcessor
		implements IContentAssistProcessor {

	@Override
	protected String getTempleteContextType() {
		return IVdmRtUiConstants.TEMPLATE_CONTENT_TYPE;
	}

}
