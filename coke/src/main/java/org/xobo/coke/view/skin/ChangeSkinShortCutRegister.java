package org.xobo.coke.view.skin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bstek.bdf2.core.view.frame.IFrameShortcutActionRegister;
import com.bstek.dorado.view.widget.Container;
import com.bstek.dorado.view.widget.SubViewHolder;

@Service
public class ChangeSkinShortCutRegister implements IFrameShortcutActionRegister {
  @Value("${coke.disableChangeSkinShortCutRegister}")
  private boolean disable;

  @Override
  public void registerToFrameTop(Container container) {
    SubViewHolder subviewHolder = new SubViewHolder();
    subviewHolder.setSubView("org.xobo.coke.view.skin.ChangeSkins");
    container.addChild(subviewHolder);
  }

  @Override
  public void registerToStatusBar(Container container) {

  }

  @Override
  public boolean isDisabled() {
    return disable;
  }

  @Override
  public int order() {
    return 0;
  }

}
