package com.ice.bike.test.util;

import java.util.Date;

import org.junit.Test;

import com.ice.bike.contants.BCDConverter;

import misc.Dateu;

public class BikeTest {
	@Test
	public void BCDTest() {
		byte[] str2Bcd = BCDConverter.str2Bcd("20170902012020");
		String bcd2Str = BCDConverter.bcd2Str(str2Bcd);
		System.out.println(bcd2Str);
		Date date = Dateu.parseDateyyyyMMddHHmmss(bcd2Str);
		System.out.println(Dateu.parseDateyyyyMMddHHmmss(date));
	}
}
