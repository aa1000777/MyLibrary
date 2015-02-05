package com.cjc.mylibrary.tools;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.cjc.mylibrary.entity.Menu;

/**
 * SAX xml文件解析Menu
 ***************************************************** 
 * <hr>
 * <dt><span class="strong">类功能简介:</span></dt>
 * <dd>SAX xml文件解析</dd>
 * <dt><span class="strong">创建时间:</span></dt>
 * <dd>2014-8-21 下午3:10:44</dd>
 * <dt><span class="strong">公司:</span></dt>
 * <dd>CorpIt</dd>
 * 
 * @author aa1000777 - Email:aa1000777@qq.com
 ***************************************************** 
 */
public class SAXPraserHelperMenu extends DefaultHandler {

	final int ITEM = 0x0005;

	List<Menu> list;
	Menu chann;
	int currentState = 0;

	public List<Menu> getList() {
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
			chann.Value = theString;
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
		if (localName.equals("item"))
			list.add(chann);
	}

	/*
	 * 文档开始通知
	 */
	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		list = new ArrayList<Menu>();
	}

	/*
	 * 标签开始通知
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		chann = new Menu();
		if (localName.equals("item")) {
			for (int i = 0; i < attributes.getLength(); i++) {
				if (attributes.getLocalName(i).equals("name")) {
					chann.Name = attributes.getValue(i);
				}
				if (attributes.getLocalName(i).equals("id")) {
					chann.MenuNO = attributes.getValue(i);
				}
			}
			currentState = ITEM;
			return;
		}
		currentState = 0;
		return;
	}
}
