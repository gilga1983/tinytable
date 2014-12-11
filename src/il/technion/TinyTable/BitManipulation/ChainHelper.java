package il.technion.TinyTable.BitManipulation;


import java.util.ArrayList;
import java.util.List;


public class ChainHelper {

	public static boolean isFingerprint(long item)
	{
		return ((item&1l) == 1l);
	}
	public static long setFingerprint(long item)
	{
		return item|1l;
	}
	public static int  findFingerprint(long[] chain, long fingerprint)
	{
		if(chain == null || chain.length<1)
			return -1;

		for (int i = 0; i < chain.length; i++) {
			if(isFingerprint(chain[i]))
			{
				if(chain[i]==fingerprint)
					return i;
			}
		}
		return -1;
	}

	public static long howmany(long[] chain, long fingerprint,int itemSize)
	{
		int idx = findFingerprint(chain,fingerprint);
		if(idx<0)
			return 0;
		int offset =0;

		long counter = 0l;
		if(idx>= chain.length-1)
			return 1;
		for (int i = idx+1; i < chain.length; i++) {
			if(isFingerprint(chain[i]))
				break;
			//assemble the counter
			long newPart = ((chain[i]>>>1)<<(offset*itemSize));
			offset++;
			counter|=newPart;
		}
		return 1+counter;
	}
	//
	public static long[]  storeValue(long[] chain, long fingerprint,int itemSize,long newCounter)
	{
		int idx = findFingerprint(chain,fingerprint);
		if(idx<0)
			throw new RuntimeException("cannot find fingerprint");
		ArrayList<Long> newChain = new ArrayList<Long>(chain.length);

		// the first occurance is the fingerprint itself. 
		long value = newCounter-1;
		// all items that are not 
		int i; 
		for(i =0; i<=idx; i++)
		{
			newChain.add(chain[i]);
		}


		// remove old counter. 
		for(;i<chain.length;i++)
		{
			if(isFingerprint(chain[i])){
				break;
			}

		}

		newChain = packNewCounterToChain(itemSize, newChain, value);


		// pack back all other 
		for(;i<chain.length;i++)
		{
			newChain.add(chain[i]);
		}
		return toArray(newChain);
	}
	private static ArrayList<Long> packNewCounterToChain(int itemSize,
			ArrayList<Long> newChain, long value) {
		// pack back the value into the chain. 
		int ctrBitSize = itemSize-1;

		long mask = (1l<<(ctrBitSize)) -1l;
		//System.out.println()
		assert(Long.bitCount(mask)==ctrBitSize);

		while(value!= 0) {
			// mark the value as counter and remove un needed bits. If the LSB bit is zero it is a counter. 
			long item = ((value&mask));
			item= item<<1;

			value = value>>>ctrBitSize;

			// add it to chain. 
			newChain.add(item);
			// shift the remaining value to remove stored bits.
		}
		return newChain;
	}

	public static long[] toArray(List<Long> items)
	{
		int i =0;
		long[] $ = new long[items.size()];
		for (Long long1 : items) {
			$[i++] = long1;
		}
		return $;
	}


}

