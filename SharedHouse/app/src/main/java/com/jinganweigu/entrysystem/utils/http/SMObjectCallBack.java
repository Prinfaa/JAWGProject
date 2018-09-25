package com.jinganweigu.entrysystem.utils.http;

import java.lang.reflect.ParameterizedType;

public abstract class SMObjectCallBack<T> {
	private Class<T> clazz;

	@SuppressWarnings("unchecked")
	public SMObjectCallBack() {
		ParameterizedType type = (ParameterizedType) this.getClass()
				.getGenericSuperclass();
		this.clazz = (Class<T>) type.getActualTypeArguments()[0];
	}

	public abstract void onSuccess(T t);

	public abstract void onError(int error, String msg);

	public  void onLoading(long total, long current, boolean isUploading){

	}

	public Class<T> getClazz() {
		return clazz;
	}

}
