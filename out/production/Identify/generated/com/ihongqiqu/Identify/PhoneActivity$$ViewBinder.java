// Generated code from Butter Knife. Do not modify!
package com.ihongqiqu.Identify;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class PhoneActivity$$ViewBinder<T extends com.ihongqiqu.Identify.PhoneActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131099650, "field 'etPhone'");
    target.etPhone = finder.castView(view, 2131099650, "field 'etPhone'");
    view = finder.findRequiredView(source, 2131099651, "field 'tvPhoneInvalid'");
    target.tvPhoneInvalid = finder.castView(view, 2131099651, "field 'tvPhoneInvalid'");
    view = finder.findRequiredView(source, 2131099652, "field 'ivPhoneClear'");
    target.ivPhoneClear = finder.castView(view, 2131099652, "field 'ivPhoneClear'");
    view = finder.findRequiredView(source, 2131099653, "field 'btnQuery'");
    target.btnQuery = finder.castView(view, 2131099653, "field 'btnQuery'");
    view = finder.findRequiredView(source, 2131099654, "field 'tvCarrier'");
    target.tvCarrier = finder.castView(view, 2131099654, "field 'tvCarrier'");
    view = finder.findRequiredView(source, 2131099655, "field 'tvProvince'");
    target.tvProvince = finder.castView(view, 2131099655, "field 'tvProvince'");
  }

  @Override public void unbind(T target) {
    target.etPhone = null;
    target.tvPhoneInvalid = null;
    target.ivPhoneClear = null;
    target.btnQuery = null;
    target.tvCarrier = null;
    target.tvProvince = null;
  }
}
