package org.overturetool.proofsupport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jp.co.csk.vdm.toolbox.VDM.CGException;

import org.overturetool.ast.itf.IOmlDocument;
import org.overturetool.ast.itf.IOmlExpression;
import org.overturetool.ast.itf.IOmlForAllExpression;
import org.overturetool.proofsupport.external_tools.pog.PoGeneratorException;
import org.overturetool.proofsupport.external_tools.pog.VdmToolsPoProcessor;
import org.overturetool.proofsupport.external_tools.pog.VdmToolsWrapper;
import org.overturetool.proofsupport.test.AutomaticProofSystemTestCase;

public class TranslationPreProcessorTest extends AutomaticProofSystemTestCase {

	public void testPrepareVdmFiles() throws Exception {
		TranslationPreProcessor prep = new TranslationPreProcessor(new VdmToolsWrapper(
				vppdeExecutable), new VdmToolsPoProcessor());
		String modelFile = doSortModel;
		int expectedPoSize = 5;
		int expectedContextSize = 1;
		List<String> contextFiles = new ArrayList<String>(expectedContextSize);
		contextFiles.add(sorterModel);

		PreparationData actual = prep.prepareVdmFiles(modelFile, contextFiles);
		
		assertPreparationDataIsGood(modelFile, expectedContextSize,
				expectedPoSize, actual);
	}

	public void testPrepareVdmFilesNoContext() throws Exception {
		TranslationPreProcessor prep = new TranslationPreProcessor(new VdmToolsWrapper(
				vppdeExecutable), new VdmToolsPoProcessor());
		String modelFile = stackModel;
		int expectedPoSize = 2;
		int expectedContextSize = 0;
		List<String> contextFiles = new ArrayList<String>(expectedContextSize);

		PreparationData actual = prep.prepareVdmFiles(modelFile, contextFiles);

		assertPreparationDataIsGood(modelFile, expectedContextSize,
				expectedPoSize, actual);
	}
	
	public void testPrepareVdmFilesEmptyModel() throws Exception {
		TranslationPreProcessor prep = new TranslationPreProcessor(new VdmToolsWrapper(
				vppdeExecutable), new VdmToolsPoProcessor());
		String modelFile = emptyModel;
		List<String> contextFiles = new ArrayList<String>(0);

		try {
			prep.prepareVdmFiles(modelFile, contextFiles);
			fail("The model was empty and method should have thrown an exception.");
		} catch(PoGeneratorException e) {
			
		}
	}
	
	public void testPrepareVdmFilesCantParse() throws Exception {
		TranslationPreProcessor prep = new TranslationPreProcessor(new VdmToolsWrapper(
				vppdeExecutable), new VdmToolsPoProcessor());
		String modelFile = parseErrorModel;
		List<String> contextFiles = new ArrayList<String>(0);

		try {
			prep.prepareVdmFiles(modelFile, contextFiles);
			fail("The model doesn't parse and method should have thrown an exception.");
		} catch(PoGeneratorException e) {
			
		}
	}
	
	public void testPrepareVdmFilesNullFile() throws Exception {
		TranslationPreProcessor prep = new TranslationPreProcessor(new VdmToolsWrapper(
				vppdeExecutable), new VdmToolsPoProcessor());
		String modelFile = null;
		List<String> contextFiles = new ArrayList<String>(0);

		try {
			prep.prepareVdmFiles(modelFile, contextFiles);
			fail("The model file name was null and method should have thrown an exception.");
		} catch(NullPointerException e) {
			
		}
	}
	
	public void testPrepareVdmExpression() throws Exception {
		TranslationPreProcessor prep = new TranslationPreProcessor(new VdmToolsWrapper(
				vppdeExecutable), new VdmToolsPoProcessor());
		String vdmExpression = 
			  "forall i : int, l : seq of int &"
			+ "not (true = (l = [])) =>"
            + "true = (i <= hd (l)) =>"
            + "l <> []";
		IOmlExpression omlExpression = prep.prepareVdmExpression(vdmExpression);
		
		assertNotNull(omlExpression);
		assertTrue(omlExpression instanceof IOmlForAllExpression);
	}

	public void testGenerateOmlAst() throws Exception {
		TranslationPreProcessor prep = new TranslationPreProcessor(new VdmToolsWrapper(
				vppdeExecutable), new VdmToolsPoProcessor());
		String modelFile = doSortModel;
		int expectedPoSize = 5;
		int expectedContextSize = 1;
		List<String> contextFiles = new ArrayList<String>(expectedContextSize);
		contextFiles.add(sorterModel);
		List<String> poExpressions = prep.processPogFile(testPogFileNoNewLine);

		PreparationData actual = prep.generateOmlAst(modelFile, contextFiles, poExpressions);
		
		assertPreparationDataIsGood(modelFile, expectedContextSize,
				expectedPoSize, actual);
	}

	public void testGenerateOmlAstNoContext() throws Exception {
		TranslationPreProcessor prep = new TranslationPreProcessor(new VdmToolsWrapper(
				vppdeExecutable), new VdmToolsPoProcessor());
		String modelFile = stackModel;
		int expectedPoSize = 2;
		int expectedContextSize = 0;
		List<String> contextFiles = new ArrayList<String>(expectedContextSize);
		List<String> poExpressions = prep.processPogFile(stackModelPogFile);

		PreparationData actual = prep.generateOmlAst(modelFile, contextFiles, poExpressions);

		assertPreparationDataIsGood(modelFile, expectedContextSize,
				expectedPoSize, actual);
	}

	public void testParseContext() throws Exception {
		TranslationPreProcessor prep = new TranslationPreProcessor(new VdmToolsWrapper(
				vppdeExecutable), new VdmToolsPoProcessor());
		List<String> contextFiles = new ArrayList<String>(1);
		contextFiles.add(sorterModel);
		contextFiles.add(doSortModel);
		List<IOmlDocument> omlDocs = prep.parseContext(contextFiles);

		assertEquals(2, omlDocs.size());
		for (IOmlDocument doc : omlDocs) {
			assertTrue(doc != null);
			assertTrue(doc.toVdmPpValue() != null);
			assertTrue(doc.toVdmPpValue().length() > 0);
		}
	}

	public void testProcessPogFile() throws Exception {
		TranslationPreProcessor prep = new TranslationPreProcessor(new VdmToolsWrapper(
				vppdeExecutable), new VdmToolsPoProcessor());

		List<String> actual = prep.processPogFile(testPogFileNoNewLine);

		assertEquals(5, actual.size());
		for (String s : actual) {
			assertTrue(s != null);
			assertTrue(s.length() > 0);
		}
	}

	public void testGeneratePogFile() throws Exception {
		String modelFile = doSortModel;
		List<String> contextFiles = new ArrayList<String>(1);
		contextFiles.add(sorterModel);
		TranslationPreProcessor prep = new TranslationPreProcessor(new VdmToolsWrapper(
				vppdeExecutable), new VdmToolsPoProcessor());

		String actual = prep.generatePogFile(modelFile, contextFiles);
		String expected = doSortModel + ".pog";
		File pogFile = new File(expected);

		assertTrue(pogFile.exists());
		assertEquals(expected, actual.trim());
	}

	public void testGeneratePogFileNoContext() throws Exception {
		String modelFile = stackModel;
		List<String> contextFiles = new ArrayList<String>(0);
		TranslationPreProcessor prep = new TranslationPreProcessor(new VdmToolsWrapper(
				vppdeExecutable), new VdmToolsPoProcessor());

		String actual = prep.generatePogFile(modelFile, contextFiles);
		String expected = modelFile + ".pog";
		File pogFile = new File(expected);

		assertTrue(pogFile.exists());
		assertEquals(expected, actual.trim());
	}
	
	private void assertPreparationDataIsGood(String modelFile,
			int expectedContextSize, int expectedPoSize, PreparationData actual)
			throws CGException {
		IOmlDocument omlModel = actual.getOmlModel();
		List<IOmlDocument> omlContextDocuments = actual.getOmlContextDocuments();
		List<IOmlExpression> omlPos = actual.getOmlPos();
		
		assertTrue(omlModel != null);
		assertEquals(modelFile, omlModel.getFilename());
		assertTrue(omlModel.toVdmPpValue() != null);
		assertTrue(actual.omlModel.toVdmPpValue().length() > expectedContextSize);

		assertEquals(expectedContextSize, omlContextDocuments.size());
		if(expectedContextSize > 0)
			for (IOmlDocument doc : omlContextDocuments) {
				assertTrue(doc != null);
				assertTrue(doc.toVdmPpValue() != null);
				assertTrue(doc.toVdmPpValue().length() > 0);
			}
		
		assertEquals(expectedPoSize, omlPos.size());
		if(expectedPoSize > 0)
			for (IOmlExpression s : omlPos)
				assertTrue(s != null);
	}
}
