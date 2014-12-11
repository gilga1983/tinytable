package il.technion.TinyTable.BitManipulation;


public class AuxilaryBitSet {
	public long[] words;
	public AuxilaryBitSet(final int l) {
		
		this.words = new long[l];
	}
	/**
	 * Changes the bits in the range from - to to the bits specified by the
	 * value starting from the LSB bits. Notice that the maximal value to be set
	 * is 64 bits.
	 * 
	 * @param from
	 *            - from bit to change
	 * @param to
	 *            - to bit to change
	 * @param value
	 *            - new bits to write.
	 */
	public void setBits(final int from, final int to, final long value) {
		 int fromBitIdx = from & 63;
			   int fromWordIdx = from >>> 6;
		 int toBitIdx = to & 63;
		 int toWordIdx = to >>> 6;
		
		fromWordIdx = fromWordIdx%this.words.length;
		toWordIdx = toWordIdx%this.words.length;
		if (fromWordIdx == toWordIdx) {
			this.words[fromWordIdx] = BitHelper.ReplaceBitsInWord(this.words[fromWordIdx], fromBitIdx, toBitIdx, value);
			// System.out.println(Long.toBinaryString(this.words[fromWordIdx]));
			return;
		}
		if(toBitIdx ==0)
		{
			this.words[fromWordIdx] = BitHelper.ReplaceBitsInWord(this.words[fromWordIdx], fromBitIdx, 63, value,true);
			return;
		}
		final int part1length = 64 - fromBitIdx;

		final long part1Value = value & BitHelper.generateMask(0, part1length);
		final long part2Value = (value >>> part1length);

//		 System.out.println("SetBits: Part 1 length is: " + part1length +
//		 " Part1 value is: " + Long.toBinaryString(part1Value) + " Part 2 value " + part2Value  );

		this.words[fromWordIdx] = BitHelper.ReplaceBitsInWord(this.words[fromWordIdx], fromBitIdx, 63, part1Value,true);
		this.words[toWordIdx] = BitHelper.ReplaceBitsInWord(this.words[toWordIdx], 0, toBitIdx, part2Value);

		// assert readValue1 == part1Value : "part1Value: " + part1Value +
		// " ValueRead: " + readValue1;
//		final long readValue1 = BitHelper.getValueFromWord(this.words[fromWordIdx], fromBitIdx, 63, true);
//		final long readValue2 = BitHelper.getValueFromWord(this.words[toWordIdx], 0, toBitIdx, false);
//		
//		
//		Assert.assertTrue(readValue1==part1Value);
//
//		Assert.assertTrue(readValue2==part2Value);

		
	}
	
	
	public long replaceBits(final int from, final int to,long value) {
		
		final int fromBitIdx = from & 63;
		
		int fromWordIdx = from >>> 6;
		fromWordIdx = fromWordIdx%this.words.length;
		final int toBitIdx = to & 63;
		 int toWordIdx = to >>> 6;
		 toWordIdx = toWordIdx%this.words.length;

		if (fromWordIdx == toWordIdx){
			// this is the common path so lets avoid all the functions. 
			long mask =  ((-1l << (fromBitIdx)) ^ (-1l << (toBitIdx)));
			long $ = (this.words[fromWordIdx]&(mask))>>>fromBitIdx;
			this.words[fromWordIdx] &= (~mask);
			// put the item in the mask.
			//mask &= (value << (fromBitIdx));
			this.words[fromWordIdx] |=  (value << (fromBitIdx));
			 return $;
		}
		if(toBitIdx== 0)
		{
			
			
			long $ =0l;
//			fromWordIdx%=this.words.length;
			$ =  BitHelper.getValueFromWord(this.words[fromWordIdx], fromBitIdx, 63, true);
			this.words[fromWordIdx] = BitHelper.ReplaceBitsInWord(this.words[fromWordIdx], fromBitIdx, 63, value,true);
			return $;
			
		}
		
		final long lowBits = BitHelper.getValueFromWord(this.words[fromWordIdx], fromBitIdx, 63,true);
		// System.out.println("LowBits: " + lowBits);
		long highBits = BitHelper.getValueFromWord(this.words[toWordIdx], 0, toBitIdx);
		final int part1length = 64 - fromBitIdx;

		final long part1Value = value & BitHelper.generateMask(0, part1length);
		final long part2Value = (value >>> part1length);

//		 System.out.println("SetBits: Part 1 length is: " + part1length +
//		 " Part1 value is: " + Long.toBinaryString(part1Value) + " Part 2 value " + part2Value  );

		this.words[fromWordIdx] = BitHelper.ReplaceBitsInWord(this.words[fromWordIdx], fromBitIdx, 63, part1Value,true);
		this.words[toWordIdx] = BitHelper.ReplaceBitsInWord(this.words[toWordIdx], 0, toBitIdx, part2Value);

		
		
		return lowBits | (highBits << (64 - fromBitIdx));
	}
	
	/**
	 * \ Packs the bits in the specified range inside a long.
	 * 
	 * @param from
	 *            - from bit
	 * @param to
	 *            - to bit 
	 * @return - a long whose to-from first bits are set according tot eh
	 *         bitArray.
	 */
	public long getBits(final int from, final int to) {
		final int fromBitIdx = from & 63;
		
		int fromWordIdx = from >>> 6;
		fromWordIdx= fromWordIdx%this.words.length;

		final int toBitIdx = to & 63;
		 int toWordIdx = to >>> 6;
		toWordIdx= toWordIdx%this.words.length;

		if (fromWordIdx == toWordIdx){
			//common path avoid the function. 
			return ((this.words[fromWordIdx] & ((-1l << (fromBitIdx)) ^ (-1l << (toBitIdx)))) >>> (fromBitIdx));

			
			//return (BitHelper.getValueFromWord(this.words[fromWordIdx], fromBitIdx, toBitIdx));
					
		}
		if(toBitIdx== 0)
		{
//			return ((this.words[fromWordIdx] & ((-1l << (fromBitIdx)) ^ (-1l ))) >>> (fromBitIdx));

			return BitHelper.getValueFromWord(this.words[fromWordIdx], fromBitIdx, 63, true);
		}
		
		final long lowBits = BitHelper.getValueFromWord(this.words[fromWordIdx], fromBitIdx, 63, true);
		// System.out.println("LowBits: " + lowBits);
		long highBits = BitHelper.getValueFromWord(this.words[toWordIdx], 0, toBitIdx);

		return lowBits | (highBits << (64 - fromBitIdx));
	}
	
	public long testBits(final int from, final int to,long fingerprint) {
		final int fromBitIdx = from & 63;
		
		int fromWordIdx = from >>> 6;
		fromWordIdx= fromWordIdx%this.words.length;

		final int toBitIdx = to & 63;
		 int toWordIdx = to >>> 6;
		toWordIdx= toWordIdx%this.words.length;

		if (fromWordIdx == toWordIdx){
			//common path avoid the function. 
			return ((this.words[fromWordIdx] & ((-1l << (fromBitIdx)) ^ (-1l << (toBitIdx)))) >>> (fromBitIdx));

			
			//return (BitHelper.getValueFromWord(this.words[fromWordIdx], fromBitIdx, toBitIdx));
					
		}
		if(toBitIdx== 0)
		{
			return ((this.words[fromWordIdx] & ((-1l << (fromBitIdx)) ^ (-1l ))) >>> (fromBitIdx));

//			return BitHelper.getValueFromWord(this.words[fromWordIdx], fromBitIdx, 63, true);
		}
		
		final long lowBits = BitHelper.getValueFromWord(this.words[fromWordIdx], fromBitIdx, 63, true);
		// System.out.println("LowBits: " + lowBits);
		long highBits = BitHelper.getValueFromWord(this.words[toWordIdx], 0, toBitIdx);

		return lowBits | (highBits << (64 - fromBitIdx));
	}

}
