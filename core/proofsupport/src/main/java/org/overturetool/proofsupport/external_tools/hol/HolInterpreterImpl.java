package org.overturetool.proofsupport.external_tools.hol;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.overturetool.proofsupport.external_tools.ConsolePipe;
import org.overturetool.proofsupport.external_tools.Utilities;

public class HolInterpreterImpl implements HolInterpreter {

	private static final String SUCCESS = "> val it = 1 : int";
	protected final HolParameters holPram;
	protected final UnquoteConsole unquote;
	protected final MosmlHolConsole mosml;
	protected final Thread pipe;

	public HolInterpreterImpl(HolParameters holParam) throws HolInterpreterException {
		this.holPram = holParam;
		try {
			unquote = new UnquoteConsole(holParam.getUnquoteBinaryPath());
		} catch (IOException e) {
			throw new HolInterpreterException("IO error while connectiong to unquote CLI.", e);
		}
		try {
			mosml = new MosmlHolConsole(holParam.buildMosmlHolCommand());
		} catch (IOException e) {
			throw new HolInterpreterException("IO error while connectiong to Moscow ML CLI.", e);
		}
		pipe = new Thread(new ConsolePipe(unquote, mosml), "UnquoteMosmlPipe");
	}
	
	public void start() {
		pipe.start();
	}

	public String interpretLine(String line) throws HolInterpreterException {
		unquote.writeLine(line);
		try {
			return mosml.readOutputBlock();
		} catch (IOException e) {
			throw new HolInterpreterException("IO error while interpreting line: " + line, e);
		}
	}

	public void quit() throws HolInterpreterException {
		try {
			mosml.quitHol();
			unquote.destroy();
			pipe.join();
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			throw new HolInterpreterException("Interrupted while waiting for clean termination.", e);
		}
	}

	public String[] interpretModel(String holCode) throws HolInterpreterException {
		List<String> output = new LinkedList<String>();
		String[] holLines = Utilities.splitString(holCode, Utilities.NEW_CHARACTER);
		for (String holLine : holLines)
			output.add(interpretLine(holLine));
		return output.toArray(new String[] {});
	}

	// TODO: add timer
	public boolean dischargeProof(String proofCommand) throws HolInterpreterException {
		String output = interpretLine(proofCommand).trim();
		return output.endsWith("val it = true : bool");
	}

}
