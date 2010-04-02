/* Copyright, 2009 Bani (http://baniverso.com) & Chester (http://chester.blog.br)
 * 
 * This file is part of MemeThis.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.memethis.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Bani
 * @author Chester
 */	
public class HashHelper {

	/**
	 * This must contain a javascript function named "hash" that receives a
	 * parameter and returns its hash.
	 * <p>
	 * We've used this one: http://bit.ly/15Pzsx [SHA-1 implementation in
	 * JavaScript (c) Chris Veness 2002-2009]
	 */
	public static final String JS_HASH_FUNCTION = "/* SHA-1 (c) Chris Veness 2002-2009 http://bit.ly/15Pzsx */function sha1Hash(k){var o=[1518500249,1859775393,2400959708,3395469782];k+=String.fromCharCode(128);var y=k.length/4+2;var m=Math.ceil(y/16);var n=new Array(m);for(var A=0;A<m;A++){n[A]=new Array(16);for(var z=0;z<16;z++){n[A][z]=(k.charCodeAt(A*64+z*4)<<24)|(k.charCodeAt(A*64+z*4+1)<<16)|(k.charCodeAt(A*64+z*4+2)<<8)|(k.charCodeAt(A*64+z*4+3))}}n[m-1][14]=((k.length-1)*8)/Math.pow(2,32);n[m-1][14]=Math.floor(n[m-1][14]);n[m-1][15]=((k.length-1)*8)&4294967295;var v=1732584193;var u=4023233417;var r=2562383102;var q=271733878;var p=3285377520;var g=new Array(80);var F,E,D,C,B;for(var A=0;A<m;A++){for(var w=0;w<16;w++){g[w]=n[A][w]}for(var w=16;w<80;w++){g[w]=ROTL(g[w-3]^g[w-8]^g[w-14]^g[w-16],1)}F=v;E=u;D=r;C=q;B=p;for(var w=0;w<80;w++){var x=Math.floor(w/20);var h=(ROTL(F,5)+f(x,E,D,C)+B+o[x]+g[w])&4294967295;B=C;C=D;D=ROTL(E,30);E=F;F=h}v=(v+F)&4294967295;u=(u+E)&4294967295;r=(r+D)&4294967295;q=(q+C)&4294967295;p=(p+B)&4294967295}return v.toHexStr()+u.toHexStr()+r.toHexStr()+q.toHexStr()+p.toHexStr()}function f(b,a,d,c){switch(b){case 0:return(a&d)^(~a&c);case 1:return a^d^c;case 2:return(a&d)^(a&c)^(d&c);case 3:return a^d^c}}function ROTL(a,b){return(a<<b)|(a>>>(32-b))}Number.prototype.toHexStr=function(){var c='',a;for(var b=7;b>=0;b--){a=(this>>>(b*4))&15;c+=a.toString(16)}return c};function hash(k){return sha1Hash(k)}";

	/**
	 * Does the same hash that the <code>JS_HASH_FUNCTION</code>'s
	 * <code>hash</code> function does in JavaScript
	 * 
	 * @param s
	 *            String to hash
	 * @return hash
	 */
	public static String hash(String s) {
		// Use a SHA-1 hash, returned as an hex string
		return sha1(s);
	}

	/**
	 * Hashes a string using the sha-1 algorythm
	 * 
	 * @param s
	 *            string data to be hashed
	 * @return Hex string representation of a sha-1 hash of <code>s</code>
	 */
	public static String sha1(String s) {
		MessageDigest sha1;
		try {
			sha1 = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			// Shouldn't happen, but this will provide enough info
			throw new RuntimeException(e);
		}
		sha1.reset();
		byte[] b = sha1.digest(s.getBytes());
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			int j = ((int) b[i]) & 0xFF;
			buf.append(hex.charAt(j / 16));
			buf.append(hex.charAt(j % 16));
		}
		return buf.toString();
	}

	/**
	 * Used by <code>sha1()</code>
	 */
	private static final String hex = "0123456789abcdef";

}
