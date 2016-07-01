package org.xobo.coke.service;

public interface SkinPersister {
  public static final String BEAN_ID = "coke.skinPersister";

  boolean persistSkin(String skinName);

  String getSkinName();
}
