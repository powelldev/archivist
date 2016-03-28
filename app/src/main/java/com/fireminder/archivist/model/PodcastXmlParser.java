package com.fireminder.archivist.model;

import android.content.ContentValues;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class PodcastXmlParser {
  public ContentValues parse(String response) throws DocumentException {
    Element channel = DocumentHelper.parseText(response).getRootElement().element("channel");
    final String title = channel.elementText("title");
    final String description = channel.elementText("description");
    final String imgPath = getImgPath(channel);

    ContentValues cv = new ContentValues();
    cv.put(PodcastTable.Contract.TITLE, title);
    cv.put(PodcastTable.Contract.DESCRIPTION, description);
    cv.put(PodcastTable.Contract.IMG_URL, imgPath);

    return cv;
  }

  private String getImgPath(Element channel){
    for (Element image : channel.elements("image")) {
      if ("itunes".equalsIgnoreCase(image.getNamespacePrefix())) {
        return image.attributeValue("href");
      }
    }
    return channel.element("thumbnail").attributeValue("url");
  }
}
