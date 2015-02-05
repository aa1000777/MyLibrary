package com.cjc.mylibrary.tools;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.cjc.mylibrary.entity.Xml;

/**
 * SAX xml文件解析列表
 ***************************************************** 
 * <hr>
 * <dt><span class="strong">类功能简介:</span></dt>
 * <dd>SAX xml文件解析</dd>
 * <dt><span class="strong">创建时间:</span></dt>
 * <dd>2014-8-21 下午3:10:11</dd>
 * <dt><span class="strong">公司:</span></dt>
 * <dd>CorpIt</dd>
 * 
 * @author aa1000777 - Email:aa1000777@qq.com
 ***************************************************** 
 */
public class SAXPraserHelperContent extends DefaultHandler {
	final int ITEM = 0x0005;
	List<Xml> list;
	Xml chann;
	int currentState = 0;

	public List<Xml> getList() {
		return list;
	}

	/*
	 * 接口字符块通知
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		// TODO Auto-generated method stub
		String theString = String.valueOf(ch, start, length);
		if (currentState != 0) {
			chann.valeu = theString;
			currentState = 0;
		}
		return;
	}

	/*
	 * 接收文档结束通知
	 */
	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
	}

	/*
	 * 接收标签结束通知
	 */
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		// TODO Auto-generated method stub
		if (localName.equals("item")) {
			list.add(chann);
		}
	}

	/*
	 * 文档开始通知
	 */
	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		list = new ArrayList<Xml>();
	}

	/*
	 * 标签开始通知
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		chann = new Xml();
		if (localName.equals("item")) {
			for (int i = 0; i < attributes.getLength(); i++) {
				if (attributes.getLocalName(i).equals("name")) {
					chann.name = attributes.getValue(i);
				}
			}
			currentState = ITEM;
			return;
		}
		currentState = 0;
		return;
	}
}
