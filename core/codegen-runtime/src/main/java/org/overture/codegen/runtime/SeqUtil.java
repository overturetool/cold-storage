package org.overture.codegen.runtime;

import java.util.Collections;


public class SeqUtil
{
	public static VDMSeq seq()
	{
		return new VDMSeq();
	}
	
	@SuppressWarnings("unchecked")
	public static VDMSeq seq(Object... elements)
	{
		if(elements == null)
			throw new IllegalArgumentException("Cannot instantiate sequence from null");
		
		VDMSeq seq = seq();
		for(Object element : elements)
			seq.add(element);

		return seq;
	}
	
	@SuppressWarnings("unchecked")
	public static VDMSeq mod(VDMSeq seq, Maplet... maplets)
	{
		if(maplets == null)
			throw new IllegalArgumentException("Cannot modify sequence from null");
		
		if(seq == null)
			throw new IllegalArgumentException("Cannot modify null");
		
		for(Maplet maplet : maplets)
		{
			Object left = maplet.getLeft();
			Object right = maplet.getRight();
			
			if(!(left instanceof Long))
				throw new IllegalArgumentException("Domain values of maplets in a sequence modification must be of type nat1");
			
			Long key = (Long) left;
			seq.set(Utils.index(key), right);
		}
		
		return seq;
	}
	
	public static String mod(String string, Maplet... maplets)
	{
		if(maplets == null)
			throw new IllegalArgumentException("Cannot modify sequence from null");

		if(string == null)
			throw new IllegalArgumentException("Cannot modify null");
		
		StringBuilder builder = new StringBuilder(string);
		
		for(Maplet maplet : maplets)
		{
			Object left = maplet.getLeft();
			Object right = maplet.getRight();
			
			if(!(left instanceof Long))
				throw new IllegalArgumentException("Domain values of maplets in a sequence modification must be of type nat1");

			if(!(right instanceof Character))
				throw new IllegalArgumentException("Range values must be characters when modifying a character sequence");
			
			char rightChar = (Character) right;
			
			Long key = (Long) left;
			builder.setCharAt(Utils.index(key), rightChar);
		}
		
		return builder.toString();
	}
	
	@SuppressWarnings("unchecked")
	public static VDMSet elems(VDMSeq seq)
	{
		if(seq == null)
			throw new IllegalArgumentException("Cannot get elems of null");

		VDMSet elems = SetUtil.set();
		elems.addAll(seq);
		
		return elems;
	}
	
	@SuppressWarnings("unchecked")
	public static VDMSet elems(String string)
	{
		if(string == null)
			throw new IllegalArgumentException("Cannot get elems of null");

		VDMSet elems = SetUtil.set();
		
		for(int i = 0; i < string.length(); i++)
		{
			elems.add(string.charAt(i));
		}
		
		return elems;
	}
	
	@SuppressWarnings("unchecked")
	public static VDMSeq reverse(VDMSeq seq)
	{
		if(seq == null)
			throw new IllegalArgumentException("Cannot reverse null");
		
		VDMSeq result = seq();
		
		result.addAll(seq);
		Collections.reverse(result);
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static VDMSeq tail(VDMSeq seq)
	{
		if(seq == null)
			throw new IllegalArgumentException("Cannot take tail of null");
		
		if(seq.isEmpty())
			throw new IllegalArgumentException("Cannot take tail of empty sequence");
		
		VDMSeq tail = new VDMSeq();
		
		for(int i = 1; i < seq.size(); i++)
		{
			Object element = seq.get(i);
			tail.add(element);
		}
		
		return tail;
	}
	
	public static String tail(String seq)
	{
		if(seq == null)
			throw new IllegalArgumentException("Cannot take tail of null");
		
		if(seq.isEmpty())
			throw new IllegalArgumentException("Cannot take tail of empty string");

		return seq.substring(1, seq.length());
	}
	
	@SuppressWarnings("unchecked")
	public static VDMSet inds(VDMSeq seq)
	{
		if(seq == null)
			throw new IllegalArgumentException("Cannot get indices of null");
		
		VDMSet indices = SetUtil.set();
		
		for(long i = 1; i <= seq.size(); i++)
		{
			indices.add(i);
		}
		
		return indices;
	}
	
	@SuppressWarnings("unchecked")
	public static VDMSet inds(String seq)
	{
		if(seq == null)
			throw new IllegalArgumentException("Cannot get indices of null");
		
		VDMSet indices = SetUtil.set();
		
		for(long i = 1; i <= seq.length(); i++)
		{
			indices.add(i);
		}
		
		return indices;
	}
	
	public static boolean equals(VDMSeq left, VDMSeq right)
	{
		if(left == null || right == null)
			throw new IllegalArgumentException("A sequences cannot be compared to null");
		
		return left.equals(right);
	}
	
	@SuppressWarnings("unchecked")
	public static VDMSeq conc(VDMSeq left, VDMSeq right)
	{
		if(left == null || right == null)
			throw new IllegalArgumentException("Cannot concatenate null");

		VDMSeq result = seq();

		result.addAll(left);
		result.addAll(right);
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static VDMSeq distConc(VDMSeq sequences)
	{
		if(sequences == null)
			throw new IllegalArgumentException("Distributed concatenation of null is undefined");
		
		VDMSeq result = seq();
		
		for(Object seq : sequences)
		{
			if(!(seq instanceof VDMSeq))
				throw new IllegalArgumentException("Distributed concatenation only supports sequences");
			
			VDMSeq vdmSeq = (VDMSeq) seq;
			result.addAll(vdmSeq);
		}
		
		return result;
	}
	
	public static String distConcStrings(VDMSeq stringSeq)
	{
		if(stringSeq == null)
			throw new IllegalArgumentException("Distributed string concatenation of null is undefined");
	
		String result = "";
		
		for(Object str : stringSeq)
		{
			if(!(str instanceof String))
				throw new IllegalArgumentException("Distributed string concatenation only supports strings");
			
			result += str;
		}
		
		return result;
	}	
}