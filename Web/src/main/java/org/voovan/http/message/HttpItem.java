package org.voovan.http.message;

import org.voovan.Global;
import org.voovan.tools.collection.IntKeyMap;
import org.voovan.tools.security.THash;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 类文字命名
 *
 * @author: helyho
 * Voovan Framework.
 * WebSite: https://github.com/helyho/Voovan
 * Licence: Apache v2 License
 */
public class HttpItem {
	public final static int HTTP_ITEM_MAX_LENGTH = 1024;

	public static final IntKeyMap[]  HTTP_ITEM_LENGTH_LIST = new IntKeyMap[HTTP_ITEM_MAX_LENGTH];
	static {
		for(int i=0;i< HTTP_ITEM_LENGTH_LIST.length; i++){
			HTTP_ITEM_LENGTH_LIST[i] = new IntKeyMap<HttpItem>(4096);
		}
	}

	public static final HttpItem EMPTY = new HttpItem("");

	private final byte[] bytes;
	private final String value;
	private int hashcode = 0;

	public HttpItem(String value) {
		this.value = value;
		this.bytes = value.getBytes();
		this.hashcode = THash.HashFNV1(bytes, 0, bytes.length);

		HTTP_ITEM_LENGTH_LIST[bytes.length].put(hashcode, this);
	}

	public HttpItem(byte[] valueBytes, int offset, int length) {
		byte[] bytes = new byte[length];
		System.arraycopy(valueBytes, offset, bytes, 0, length);
		this.bytes = bytes;
		this.value = new String(bytes, Global.CS_ASCII);

		this.hashcode = THash.HashFNV1(bytes, 0, length);

		HTTP_ITEM_LENGTH_LIST[bytes.length].put(hashcode, this);
	}

	public byte[] getBytes() {
		return bytes;
	}

	public String getValue() {
		return value;
	}

	public int getHashCode(){
		return this.hashcode;
	}

	public static HttpItem getHttpItem(byte[] bytes, int offset, int length){
		int hashcode = THash.HashFNV1(bytes, offset, length);
		HttpItem httpItem = (HttpItem) HTTP_ITEM_LENGTH_LIST[length].get(hashcode);
		if(httpItem == null){
			httpItem = new HttpItem(bytes, offset, length);
		}

		return httpItem;
	}

	@Override
	public String toString() {
		return value;
	}
}
