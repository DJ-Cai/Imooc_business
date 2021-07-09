package net.dongjian.lib_network.okhttp.listener;

/**
 * @author vision
 *包装类
 */
public class DisposeDataHandle
{
	public DisposeDataListener mListener = null; //业务层的具体回调
	public Class<?> mClass = null; //要解析成为的对象
	public String mSource = null; //文件保存路径

	public DisposeDataHandle(DisposeDataListener listener)
	{
		this.mListener = listener;
	}

	/**
	 * 业务层有具体的想要json数据转化为的对象
	 * @param listener
	 * @param clazz
	 */
	public DisposeDataHandle(DisposeDataListener listener, Class<?> clazz)
	{
		this.mListener = listener;
		this.mClass = clazz;
	}

	/**
	 * 业务层只想拿到最直接的底层数据
	 * @param listener
	 * @param source
	 */
	public DisposeDataHandle(DisposeDataListener listener, String source)
	{
		this.mListener = listener;
		this.mSource = source;
	}
}